package com.achatserver.seqid.service;

import com.achatserver.seqid.dao.SeqIdDao;
import com.achatserver.seqid.pojo.SeqIdRelation;
import com.achatserver.seqid.pojo.SeqIdSegment;
import com.achatserver.seqid.request.DeferRequest;
import com.achatserver.seqid.thread.WorkThreadMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeqIdService {

    @Autowired
    SeqIdDao seqIdDao;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public SeqIdSegment addMaxId(SeqIdRelation seqIdRelation){

        SeqIdSegment dbSeqIdSegment = seqIdDao.queryBySeqId(seqIdRelation.getSeqId());
        seqIdDao.addMaxIdBySeqId(seqIdRelation.getSeqId());
        return dbSeqIdSegment;
    }

    @Async
    public void execute(DeferRequest deferred){

        WorkThreadMap.addQueue(deferred);
    }
}
