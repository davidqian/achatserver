package com.achatserver.user.controller;

import com.achatserver.user.pojo.User;
import com.achatserver.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.AchatUtil;
import util.Jwt;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;


    @Autowired
    private AchatUtil achatUtil;

    @Autowired
    private Jwt jwt;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        String clientIp = achatUtil.getIpAddr(request);
        User dbUser = new User();
        int res = userService.login(user, clientIp, dbUser);
        if (res == 0) {
            String token = jwt.createJWT(dbUser.getUid(), dbUser.getMobile(), "user");
            Map map = new HashMap<String, String>();
            map.put("token", token);
            return new Result(true, StatusCode.OK, "login success", map);
        } else if (res == 1) {
            return new Result(false, StatusCode.LOGINERROR, "用户名或者密码错误");
        } else {
            return new Result(false, StatusCode.LOGINERROR, "用户不存在");
        }
    }

    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    public Result checkToken(@RequestBody Map map){
        String token = (String)map.get("token");
        if(!token.isEmpty()){
            Claims claims = jwt.parseJWT(token);
            if(claims.getExpiration().getTime() > System.currentTimeMillis()){
                Map ret = new HashMap();
                ret.put("uid", claims.getId());
                return new Result(true, StatusCode.OK, "验证成功", ret);
            }else{
                return new Result(false, StatusCode.ERROR, "token过期");
            }
        }
        return new Result(false, StatusCode.ERROR, "token错误");
    }

    @RequestMapping(value = "/identityCode", method = RequestMethod.POST)
    public Result identityCode(@RequestBody User user) {
        String mobile = user.getMobile();
        if(achatUtil.isMobileNO(mobile)) {
            int ret = userService.sendIdentityCode(mobile);
            if (ret == 1) {
                return new Result(false, StatusCode.REPEATERROR, "验证码已经发送");
            } else {
                return new Result(true, StatusCode.OK, "验证码已发送");
            }
        }else{
            return new Result(false, StatusCode.ERROR, "手机号错误");
        }
    }

    @RequestMapping(value = "/register/{code}", method = RequestMethod.POST)
    public Result register(@RequestBody User user, @PathVariable String code) {
        String mobile = user.getMobile();
        if(achatUtil.isMobileNO(mobile)) {
            String password = user.getPassword();
            int passwordLen = password.length();
            if(passwordLen >= 6 && passwordLen <= 20) {
                if (userService.checkCode(user.getMobile(), code)) {
                    String clientIp = achatUtil.getIpAddr(request);
                    int res = userService.register(user, clientIp);
                    if (res == 0) {
                        return new Result(true, StatusCode.OK, "注册成功");
                    } else {
                        return new Result(false, StatusCode.REGISTERERROR, "注册失败");
                    }
                } else {
                    return new Result(false, StatusCode.REGISTERERROR, "验证码不正确");
                }
            }else{
                return new Result(false, StatusCode.ERROR, "密码长度错误");
            }
        }else{
            return new Result(false, StatusCode.ERROR, "手机号错误");
        }
    }
}
