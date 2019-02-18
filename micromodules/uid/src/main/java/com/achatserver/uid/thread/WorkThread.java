package com.achatserver.uid.thread;

import com.achatserver.uid.dao.IdSegmentDao;
import com.achatserver.uid.pojo.IdSegment;
import com.achatserver.uid.service.UidService;
import com.achatserver.uid.util.SpringContextUtil;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

public class WorkThread extends Thread{

    private int threadId;

    private int maxUid;

    private int step;

    private int curUid;


    public WorkThread(int threadId){
        this.threadId = threadId;
        this.setFromDb();
    }

    private boolean setFromDb(){
        UidService uidService = (UidService)SpringContextUtil.getBean(UidService.class);
        IdSegment idSegment = uidService.addMaxId();

        maxUid = idSegment.getMaxId();
        step = idSegment.getStep();

        System.out.println("maxUid = " + maxUid);
        System.out.println("step = " + step);
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
