package com.achatserver.seqid.service;

import com.achatserver.seqid.dao.SeqIdDao;
import com.achatserver.seqid.pojo.SeqIdSegment;
import com.achatserver.seqid.request.DeferRequest;
import com.achatserver.seqid.thread.WorkThreadMap;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.LinkedBlockingDeque;

public class SeqIdService {

    @Autowired
    SeqIdDao seqIdDao;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SeqIdSegment addMaxId(SeqIdSegment seqIdSegment){
        SeqIdSegment dbSeqIdSegment = seqIdDao.queryBySeqId(seqIdSegment.getSeqId());
        seqIdDao.addMaxIdBySeqId(seqIdSegment.getSeqId());
        return dbSeqIdSegment;
    }

    @Async
    public void execute(DeferRequest deferred){
        WorkThreadMap.addQueue(deferred);
    }
}
