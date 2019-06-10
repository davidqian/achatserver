package com.achatserver.seqid.thread;

import com.achatserver.seqid.pojo.SeqIdRelation;
import com.achatserver.seqid.pojo.SeqIdSegment;
import com.achatserver.seqid.request.DeferRequest;
import com.achatserver.seqid.service.SeqIdService;
import com.achatserver.seqid.util.SpringContextUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class WorkThreadMap {

    private static SeqIdService seqIdService;

    private static int threadNum = 4;

    public static Map<Integer, LinkedBlockingDeque<DeferRequest>> threadMap = new HashMap<Integer, LinkedBlockingDeque<DeferRequest>>();

    public static Map<Long, SeqIdRelation> segmentMaxSeqIdMap = new ConcurrentHashMap<>();

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

    public static LinkedBlockingDeque<DeferRequest> getThreadQueue(int threadId){
        return threadMap.get(threadId);
    }

    public static void addQueue(DeferRequest deferRequest){
        int index = deferRequest.getUid().divideAndRemainder(new BigDecimal(threadNum))[1].intValue();
        threadMap.get(index).addLast(deferRequest);
    }

    public static Long getUidBelong(BigDecimal uid){
        long belong = 0L;
        for(Map.Entry<Long, SeqIdRelation> entry : segmentMaxSeqIdMap.entrySet()){
            SeqIdRelation tmpSeqIdRelation = entry.getValue();
            if(tmpSeqIdRelation.getStart().compareTo(uid) <= 0
                    && tmpSeqIdRelation.getEnd().compareTo(uid) > 0){
                belong = entry.getKey();
                break;
            }
        }
        return belong;
    }

    public static SeqIdRelation getSegment(BigDecimal uid, long belong){
        if(belong == 0) {
            belong = getUidBelong(uid);
        }
        return  segmentMaxSeqIdMap.get(belong);
    }

    public static BigDecimal getSeqId(BigDecimal uid, long belong, BigDecimal currSeqId){

        SeqIdRelation seqIdRelation = getSegment(uid, belong);

        if(seqIdRelation == null){
            return null;
        }

        if(currSeqId == null){
            currSeqId = seqIdRelation.getBeginSeqId();
        }

        if(!checkMaxSeqId(seqIdRelation, currSeqId)){
            synchronized (seqIdRelation){
                if(!checkMaxSeqId(seqIdRelation, currSeqId)){
                    addFromDb(seqIdRelation);
                }
            }
        }
        return currSeqId.add(new BigDecimal(1));
    }

    public static Boolean checkMaxSeqId(SeqIdRelation seqIdRelation, BigDecimal currSeqId){
        if(seqIdRelation.getMaxSeqId().compareTo(currSeqId) > 0){
            return true;
        }else{
            return false;
        }
    }

    public static void addFromDb(SeqIdRelation seqIdRelation){
        SeqIdSegment seqIdSegment = seqIdService.addMaxId(seqIdRelation);
        seqIdRelation.setBeginSeqId(seqIdSegment.getMaxSeqId());
        seqIdRelation.setStart(seqIdSegment.getStart());
        seqIdRelation.setEnd(seqIdSegment.getEnd());
        seqIdRelation.setStep(seqIdSegment.getStep());
        seqIdRelation.setMaxSeqId(seqIdSegment.getMaxSeqId().add(seqIdSegment.getStep()));
    }
}
