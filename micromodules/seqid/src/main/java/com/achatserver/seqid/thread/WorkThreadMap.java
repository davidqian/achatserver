package com.achatserver.seqid.thread;

import com.achatserver.seqid.request.DeferRequest;
import entity.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class WorkThreadMap {

    private static int threadNum = 4;

    public static Map<Integer, LinkedBlockingDeque<DeferRequest>> threadMap = new HashMap<Integer, LinkedBlockingDeque<DeferRequest>>();

    public static void initMap(){

        for(int i = 0; i < threadNum; i++){
            LinkedBlockingDeque<DeferRequest> linkedQueue = new LinkedBlockingDeque<DeferRequest>();
            threadMap.put(i, linkedQueue);
        }

        for(int i = 0; i < threadNum; i++){
            WorkThread workThread = new WorkThread(i);
            workThread.start();
        }
    }
}
