package com.achatserver.seqid.request;

import entity.Result;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;

public class DeferRequest {
    private DeferredResult<Result> defer;
    private BigDecimal uid;
}
