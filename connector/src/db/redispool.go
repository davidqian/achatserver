package db

import (
	"fmt"
	"github.com/garyburd/redigo/redis"
	"sync"
	"time"
)

var m *redis.Pool
var lock *sync.Mutex = &sync.Mutex {}

func RedisPool() *redis.Pool {
	lock.Lock()
	defer lock.Unlock()
	if m == nil {
		m = &redis.Pool{
			MaxIdle:     3,
			MaxActive:   5,
			IdleTimeout: 240 * time.Second,
			Dial: func() (redis.Conn, error) {
				c, err := redis.DialURL("redis://127.0.0.1:6379")
				if err != nil {
					return nil, err
				}
				return c, err
			},
			TestOnBorrow: func(c redis.Conn, t time.Time) error {
				if time.Since(t) < time.Minute {
					return nil
				}
				_, err := c.Do("PING")
				return err
			},
		}
	}
	return m
}

func loginKey(uid string)(key string){
	return fmt.Sprintf("loginlock_%s", uid)
}

func LoginLock(uid string, value string)(locked bool, err error){
	c := RedisPool().Get()
	_, err = redis.String(c.Do("SET", loginKey(uid), value, "EX", 5, "NX"))
	if err == redis.ErrNil{
		return false, nil
	}
	if err != nil{
		return false, err
	}
	c.Close()
	return true, nil
}

func LoginUnlock(uid string)(err error){
	c := RedisPool().Get()
	defer c.Close()
	_, err = c.Do("del", loginKey(uid))
	return
}

