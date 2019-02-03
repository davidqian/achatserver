package com.achatserver.user.service;

import com.achatserver.user.dao.UserDao;
import com.achatserver.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private IdWorker idWorker;

    public int login(User user, String clientIp){
        User dbUser = getByMobile(user.getMobile());
        if(dbUser != null){
            if(encoder.matches(user.getPassword(), dbUser.getPassword())){
                dbUser.setLastLoginIp(clientIp);
                dbUser.setLastLoginTime(System.currentTimeMillis());
                userDao.save(dbUser);
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

    private User getByMobile(String mobile){
        return userDao.findByMobile(mobile);
    }
}
