package com.achatserver.uid.controller;

import com.achatserver.uid.service.UidService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@CrossOrigin
@RequestMapping("/uid")
public class UidController {

    @Autowired
    private UidService uidService;

    @RequestMapping("/getUid")
    public DeferredResult<Result> getUid(){
        DeferredResult<Result> deferredResult = new DeferredResult<>();
        uidService.execute(deferredResult);
        deferredResult.onTimeout(new Runnable(){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " onTimeout");
                // 返回超时信息
                deferredResult.setErrorResult(new Result(false, StatusCode.ERROR, "请求超时"));
            }
        });

        // 处理完成的回调方法，无论是超时还是处理成功，都会进入这个回调方法
        deferredResult.onCompletion(new Runnable(){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " onCompletion");
            }
        });

        return deferredResult;
    }
}
