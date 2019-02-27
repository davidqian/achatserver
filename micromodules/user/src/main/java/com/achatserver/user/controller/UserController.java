package com.achatserver.user.controller;

import com.achatserver.user.pojo.User;
import com.achatserver.user.service.UidService;
import com.achatserver.user.service.UserService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import util.AchatUtil;
import util.IdWorker;
import util.Jwt;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UidService uidService;

    @Autowired
    private AchatUtil achatUtil;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private Jwt jwt;

    @Autowired
    private HttpServletRequest request;

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
                    User checkUser = userService.getByMobile(user.getMobile());
                    if(checkUser == null) {
                        Result uidRes = uidService.getUid();
                        if(uidRes.isFlag()) {
                            Map<String, BigDecimal> uidData = (Map<String, BigDecimal>) uidRes.getData();
                            BigDecimal uid = uidData.get("uid");
                            user.setUid(uid);
                            userService.register(user, clientIp);
                            String jwtData = jwt.createJWT(uid.toString(), "login", "user");
                            Map data = new HashMap();
                            data.put("token", jwtData);
                            return new Result(true, StatusCode.OK, "注册成功", data);
                        }else{
                            return new Result(false, StatusCode.ERROR, "调用uid服务失败");
                        }
                    }else{
                        return new Result(false, StatusCode.REGISTERERROR, "重复注册");
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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody User user) {
        String clientIp = achatUtil.getIpAddr(request);
        User dbUser = userService.login(user, clientIp);
        if(dbUser != null){
            String jwtData = jwt.createJWT(dbUser.getUid().toString() , dbUser.getLastLoginFlag(), "user");
            Map data = new HashMap();
            data.put("token", jwtData);
            return new Result(true, StatusCode.OK, "登录成功", data);
        }else {
            return new Result(false, StatusCode.LOGINERROR, "用户名或密码不正确");
        }
    }

    @RequestMapping(value = "/checkToken/{token}")
    public Result checkToken(@PathVariable String token) {
        Claims claims = jwt.parseJWT(token);
        String id = claims.getId();
        if(id == null || "".equals(id)){
            return new Result(false, StatusCode.ERROR, "token错误");
        }else {
            Map map = new HashMap();

            map.put("uid", claims.getId());
            map.put("loginFlag", claims.getSubject());
            return new Result(true, StatusCode.OK, "检查成功", map);
        }
    }
}
