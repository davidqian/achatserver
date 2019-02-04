package com.achatserver.user.service;

import com.achatserver.user.dao.UserDao;
import com.achatserver.user.pojo.User;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.transaction.Transactional;
import java.util.Map;
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

    public int login(User user, String clientIp, User dbUser){
        dbUser = getByMobile(user.getMobile());
        if(dbUser != null){
            if(encoder.matches(user.getPassword(), dbUser.getPassword())){
                dbUser.setLastLoginIp(clientIp);
                dbUser.setLastLoginTime(System.currentTimeMillis());
                dbUser = userDao.save(dbUser);
                return 0;
            }else{
                return 1;
            }
        }
        return 2;
    }

    public int register(User user, String clientIp){
        User dbUser = getByMobile(user.getMobile());
        if(dbUser != null){
            return 1;
        }
        long now = System.currentTimeMillis();
        user.setUid(idWorker.nextId() + "");
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRegisterTime(now);
        user.setLastLoginIp(clientIp);
        user.setLastLoginTime(now);
        user.setLastLoginIp(clientIp);
        userDao.save(user);
        return 0;
    }

    public int sendIdentityCode(String mobile){
        String loginCheckCodeKey = loginCheckCode(mobile);
        String cacheCode = (String)redisTemplate.opsForValue().get(loginCheckCodeKey);
        if(cacheCode.isEmpty()){
               return 1;
        }else{
           String code = RandomStringUtils.randomNumeric(6);
           redisTemplate.opsForValue().set(loginCheckCodeKey, code, 1, TimeUnit.MINUTES);
           System.out.println("login check code is:" + code);
           //TODO:异步发送到用户手机
           return 0;
        }
    }

    public boolean checkCode(String mobile, String code){
        String loginCheckCodeKey = loginCheckCode(mobile);
        String cacheCode = (String)redisTemplate.opsForValue().get(loginCheckCodeKey);
        if(!cacheCode.isEmpty() && cacheCode.equals(code)){
            return true;
        }else{
            return false;
        }
    }

    private String loginCheckCode(String mobile){
       return "login_check_code_" + mobile;
    }

    private User getByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }
}
