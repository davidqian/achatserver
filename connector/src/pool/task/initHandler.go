package pool

import (
	"db"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"net/http"
	"protocol"
	"time"
)

type initRequest struct {
	Token string
}

type checkTokenBack struct {
	Flag bool
	Code int
	Message string
	Data uidSet
}

type uidSet struct {
	Uid string
	LoginFlag string
}

type ret struct {
	Code int
}

func InitHandler(httpClient *http.Client, task *Task){
	if task.Conn.GetConnectionStatus() || task.Conn.Inited() {
		return
	}
	var initData initRequest
	if err := json.Unmarshal([]byte(task.Protocol.Body), &initData); err == nil {
		if uidSet, err := checkCode(httpClient, initData.Token); err == nil {
			locked, err := db.LoginLock(uidSet.Uid, string(time.Now().Unix()))
			defer db.LoginUnlock(uidSet.Uid)
			if(locked){
				//TODO: 检查并踢掉其它地方的链接
				task.Conn.SetUid(uidSet.Uid)
				retData := ret{
					Code: INITED,
				}

				if retBuff, err := json.Marshal(retData);err == nil {
					retProto := protocol.MakeProtocol(1, task.Protocol.SeqId, string(retBuff))

					if buf, err := json.Marshal(retProto); err == nil {
						task.Conn.WriteMessage(buf)
					} else {
						task.Conn.Close()
					}
				}
			}else{
				//TODO: 加入 log
				if(err != nil){
					fmt.Println(err)
				}
				task.Conn.Close()
			}
		} else {
			task.Conn.Close()
		}
	}else{
		task.Conn.Close()
	}
	return
}

func checkCode(httpClient *http.Client, token string)(uidSet uidSet, err error){
	//TODO: 提取配置
	if response, err := httpClient.Get("http://127.0.0.1:9000/user/checkToken/" + token); err == nil {
		defer response.Body.Close()
		var body []byte
		if body,err = ioutil.ReadAll(response.Body); err == nil{
			var b checkTokenBack
			err = json.Unmarshal(body, &b)
			if(err == nil){
				if(b.Flag) {
					uidSet = b.Data
				}else{
					err = errors.New(b.Message)
				}
			}
		}
	}
	return
}

