package com.achatserver.seqid.controller;

import com.achatserver.seqid.request.DeferRequest;
import com.achatserver.seqid.service.SeqIdService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.util.function.Consumer;

@RestController
@CrossOrigin
@RequestMapping("/seqid")
public class SeqIdController {

    @Autowired
    SeqIdService seqIdService;

    @RequestMapping("/{uid}")
    public DeferredResult<Result> getSeqId(@PathVariable BigDecimal uid){
        DeferredResult<Result> deferredResult = new DeferredResult<>();

        DeferRequest deferRequest = new DeferRequest(deferredResult, uid);
        seqIdService.execute(deferRequest);
        deferredResult.onTimeout(new Runnable(){
            @Override
            public void run() {
                deferredResult.setErrorResult(new Result(false, StatusCode.ERROR, "请求超时"));
            }
        });

        deferredResult.onError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
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
