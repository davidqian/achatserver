package com.achatserver.user.service;

import com.achatserver.user.dao.UserDao;
import com.achatserver.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    public User login(User user, String clientIp){
        User dbUser = getByMobile(user.getMobile());
        if(dbUser != null){
            if(encoder.matches(user.getPassword(), dbUser.getPassword())){
                dbUser.setLastLoginIp(clientIp);

                long lastLoginTime = dbUser.getLastLoginTime().getTime();
                long now = System.currentTimeMillis();
                if((now - lastLoginTime) > 5 * 60 * 1000){
                    dbUser.setLastLoginFlag(now + "");
                }

                dbUser.setLastLoginTime(new Timestamp(new Date().getTime()));
                userDao.save(dbUser);
                return dbUser;
            }
        }
        return null;
    }

    public void register(User user, String clientIp){
        Timestamp now = new Timestamp(new Date().getTime());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRegisterIp(clientIp);
        user.setRegisterTime(now);
        user.setLastLoginIp(clientIp);
        user.setLastLoginTime(now);
        user.setLastLoginIp(clientIp);
        user.setLastLoginFlag(System.currentTimeMillis() + "");
        userDao.save(user);
    }

    public int sendIdentityCode(String mobile){
        String loginCheckCodeKey = loginCheckCode(mobile);
        String cacheCode = (String)redisTemplate.opsForValue().get(loginCheckCodeKey);
        if(cacheCode != null){
            return 1;
        }else{
            String code = RandomStringUtils.randomNumeric(6);
            redisTemplate.opsForValue().set(loginCheckCodeKey, code, 10, TimeUnit.MINUTES);
            System.out.println("login check code is:" + code);
            //TODO:异步发送到用户手机
            return 0;
        }
    }

    public boolean checkCode(String mobile, String code){
        String loginCheckCodeKey = loginCheckCode(mobile);
        String cacheCode = (String)redisTemplate.opsForValue().get(loginCheckCodeKey);
        System.out.println("cached code:" + cacheCode);
        System.out.println("pass code:" + code);
        if(cacheCode != null && cacheCode.equals(code)){
            return true;
        }else{
            return false;
        }
    }

    private String loginCheckCode(String mobile){
        return "login_check_code_" + mobile;
    }

    public User getByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }
}
