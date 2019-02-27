package com.achatserver.seqid.thread;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class WorkThread extends Thread{

    private int threadId;

    private Map<Long, Map<BigDecimal, BigDecimal>> uidSeqIdMap = new HashMap<>();

    public WorkThread(int threadId){
        this.threadId = threadId;
    }

    @Override
    public void run(){

    }
}
