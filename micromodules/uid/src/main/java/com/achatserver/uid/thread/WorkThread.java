package com.achatserver.uid.thread;

import com.achatserver.uid.dao.IdSegmentDao;
import com.achatserver.uid.pojo.IdSegment;
import com.achatserver.uid.service.UidService;
import com.achatserver.uid.util.SpringContextUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class WorkThread extends Thread{

    private UidService uidService;

    private int threadId;

    private int maxUid;

    private int step;

    private int curUid;


    public WorkThread(int threadId){
        this.threadId = threadId;
        this.setFromDb();
    }

    private UidService getUidService(){
        if(uidService == null){
            uidService = (UidService)SpringContextUtil.getBean(UidService.class);
        }
        return uidService;
    }

    private boolean setFromDb(){

        getUidService();

        IdSegment idSegment = uidService.addMaxId();

        maxUid = idSegment.getMaxId();
        step = idSegment.getStep();

        curUid = maxUid;
        return true;
    }

    @Override
    public void run() {
        LinkedBlockingDeque<DeferredResult<Result>> linkedQueue = ThreadQueue.getIndexMap(threadId);
        while(true){
            DeferredResult<Result> request = linkedQueue.poll();
            if(request == null){
                continue;
            }
            if(curUid == maxUid + step){
                setFromDb();
            }
            curUid++;
            Map<String, Integer> data = new HashMap();
            data.put("uid", curUid);
            request.setResult(new Result(true, StatusCode.OK, "获取成功", data));
        }
    }
}
