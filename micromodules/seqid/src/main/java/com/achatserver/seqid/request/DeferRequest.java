package com.achatserver.seqid.request;

import entity.Result;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;

public class DeferRequest {
    private DeferredResult<Result> defer;
    private BigDecimal uid;

    public DeferRequest(DeferredResult<Result> defer, BigDecimal uid){
        this.defer = defer;
        this.uid = uid;
    }

    public BigDecimal getUid() {
        return uid;
    }

    public void setUid(BigDecimal uid) {
        this.uid = uid;
    }

    public DeferredResult<Result> getDefer() {
        return defer;
    }

    public void setDefer(DeferredResult<Result> defer) {
        this.defer = defer;
    }
}