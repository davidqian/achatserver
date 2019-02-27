package com.achatserver.seqid.thread;

import com.achatserver.seqid.pojo.SeqIdSegment;
import com.achatserver.seqid.request.DeferRequest;
import com.achatserver.seqid.service.SeqIdService;
import com.achatserver.seqid.util.SpringContextUtil;
import entity.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class WorkThreadMap {

    private static SeqIdService seqIdService;

    private static int threadNum = 4;

    public static Map<Integer, LinkedBlockingDeque<DeferRequest>> threadMap = new HashMap<Integer, LinkedBlockingDeque<DeferRequest>>();

    public static Map<Long, SeqIdSegment> segmentMaxSeqIdMap = new HashMap<>();

    public static void initMap(){

        for(int i = 0; i < threadNum; i++){
            LinkedBlockingDeque<DeferRequest> linkedQueue = new LinkedBlockingDeque<DeferRequest>();
            threadMap.put(i, linkedQueue);
        }

        for(int i = 0; i < threadNum; i++){
            WorkThread workThread = new WorkThread(i);
            workThread.start();
        }

        seqIdService = (SeqIdService) SpringContextUtil.getBean(SeqIdService.class);
    }

    public static void addQueue(DeferRequest deferRequest){
        int index = deferRequest.getUid().divideAndRemainder(new BigDecimal(threadNum))[1].intValue();
        threadMap.get(index).addLast(deferRequest);
    }

    public static SeqIdSegment getSegment(BigDecimal uid){
        SeqIdSegment retSeqIdSegment = null;
        for(Map.Entry<Long, SeqIdSegment> entry : segmentMaxSeqIdMap.entrySet()){
            SeqIdSegment seqIdSegment = entry.getValue();
            if(seqIdSegment.getStart().compareTo(uid) <= 0 && seqIdSegment.getEnd().compareTo(uid) > 0){
                retSeqIdSegment = entry.getValue();
                break;
            }
        }
        return retSeqIdSegment;
    }

    public static BigDecimal getSeqId(BigDecimal uid, BigDecimal currSeqId){

        //TODO: 需要处理异常情况
        SeqIdSegment seqIdSegment = getSegment(uid);

        //TODO: 如何处理用户初始化?

        if(!checkMaxSeqId(seqIdSegment, currSeqId)){
            synchronized (seqIdSegment){
                if(!checkMaxSeqId(seqIdSegment, currSeqId)){
                    addFromDb(seqIdSegment);
                }
            }
        }
        return currSeqId.add(new BigDecimal(1));
    }

    public static Boolean checkMaxSeqId(SeqIdSegment seqIdSegment, BigDecimal currSeqId){
        if(seqIdSegment.getMaxSeqId().compareTo(currSeqId) > 0){
            return true;
        }else{
            return false;
        }
    }

    public static void addFromDb(SeqIdSegment seqIdSegment){
        SeqIdSegment dbSeqIdSegment = seqIdService.addMaxId(seqIdSegment);
        seqIdSegment.setMaxSeqId(seqIdSegment.getMaxSeqId().add(dbSeqIdSegment.getStep()));
    }
}
