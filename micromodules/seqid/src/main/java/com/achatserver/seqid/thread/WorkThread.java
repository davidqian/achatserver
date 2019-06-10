package com.achatserver.seqid.thread;

import com.achatserver.seqid.request.DeferRequest;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class WorkThread extends Thread{

    private int threadId;

    private Map<Long, Map<BigDecimal, BigDecimal>> uidSeqIdMap = new HashMap<>();

    public WorkThread(int threadId){
        this.threadId = threadId;
    }

    @Override
    public void run(){
        LinkedBlockingDeque<DeferRequest> linkedQueue = WorkThreadMap.getThreadQueue(threadId);
        while(true){
            DeferRequest request = linkedQueue.poll();
            if(request != null){
                BigDecimal uid = request.getUid();
                BigDecimal curSeqId = null;
                long belong = WorkThreadMap.getUidBelong(uid);
                if(belong > 0) {
                    Map<BigDecimal, BigDecimal> map = uidSeqIdMap.get(belong);
                    if (map != null) {
                        curSeqId = map.get(uid);
                    }
                    curSeqId = WorkThreadMap.getSeqId(uid, belong, curSeqId);
                    map.put(uid, curSeqId);
                    Map<String, BigDecimal> data = new HashMap();
                    data.put("seqId", curSeqId);
                    request.getDefer().setResult(new Result(true, StatusCode.OK, "获取成功", data));
                }else{
                    request.getDefer().setResult(new Result(false, StatusCode.ERROR, "获取失败"));
                }
            }
        }
    }
}
