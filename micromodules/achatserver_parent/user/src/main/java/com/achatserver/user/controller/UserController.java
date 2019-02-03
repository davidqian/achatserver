package com.achatserver.user.controller;

import com.achatserver.user.pojo.User;
import com.achatserver.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.GetClientIp;

import javax.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GetClientIp getClientIp;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user){
        String clientIp = getClientIp.getIpAddr(request);
        int res = userService.login(user, clientIp);
        //TODO:返回给长链接的链接字符
        if(res == 0){
            return new Result(true, StatusCode.OK, "login success");
        }else if(res == 1){
            return new Result(false, StatusCode.LOGINERROR, "用户名或者密码错误");
        }else{
            return new Result(false, StatusCode.LOGINERROR, "用户不存在");
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody User user){
       //TODO:验证用户名称的合法性
       //TODO:验证密码的合法性
       //TODO:是否开启短信验证
       String clientIp = getClientIp.getIpAddr(request);
       int res = userService.register(user, clientIp);
       if(res == 0){
           //TODO: 返回给长链接的链接字符
           return new Result(true, StatusCode.OK, "注册成功");
       }else{
           return new Result(false, StatusCode.REGISTERERROR, "注册失败");
       }
    }
}
