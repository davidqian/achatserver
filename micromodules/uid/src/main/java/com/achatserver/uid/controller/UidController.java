package com.achatserver.uid.controller;

import com.achatserver.uid.service.UidService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.function.Consumer;

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
                deferredResult.setErrorResult(new Result(false, StatusCode.ERROR, "请求超时"));
            }
        });

        deferredResult.onError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                throwable.printStackTrace();
                deferredResult.setErrorResult(new Result(false, StatusCode.ERROR, "请求错误"));
            }
        });

        deferredResult.onCompletion(new Runnable(){
            @Override
            public void run() {
            }
        });

        return deferredResult;
    }
}
