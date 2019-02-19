package com.achatserver.uid.service;

import com.achatserver.uid.dao.IdSegmentDao;
import com.achatserver.uid.pojo.IdSegment;
import com.achatserver.uid.thread.ThreadMap;
import com.achatserver.uid.thread.ThreadQueue;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.LinkedBlockingDeque;

@Service
public class UidService {

    private static int curQueueId = 0;

    private static int maxQueueId = 3;

    @Autowired
    private IdSegmentDao idSegmentDao;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public IdSegment addMaxId(){
        IdSegment idSegment = idSegmentDao.queryByType("uid");
        idSegmentDao.addMaxIdByType("uid");
        return idSegment;
    }

    @Async
    public void execute(DeferredResult<Result> deferred){
        //TODO: 需要对线程状态进行判断
        if(curQueueId > maxQueueId){
            curQueueId = 0;
        }
        LinkedBlockingDeque<DeferredResult<Result>> linkedQueue = ThreadQueue.getIndexMap(curQueueId);
        linkedQueue.addLast(deferred);
        curQueueId ++;
    }
}
