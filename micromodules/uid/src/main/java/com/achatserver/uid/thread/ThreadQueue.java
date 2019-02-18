package com.achatserver.uid.thread;

import entity.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class ThreadQueue {

    public static Map<Integer, LinkedBlockingDeque<DeferredResult<Result>>> threadMap = new HashMap<Integer, LinkedBlockingDeque<DeferredResult<Result>>>();

    public static void initMap() {
        //TODO:提取配置
        for (int i = 0; i < 4; i++) {
            LinkedBlockingDeque<DeferredResult<Result>> linkedQueue = new LinkedBlockingDeque<DeferredResult<Result>>();
            threadMap.put(i, linkedQueue);
        }
    }

    public static LinkedBlockingDeque<DeferredResult<Result>> getIndexMap(int threadNum) {
        return threadMap.get(threadNum);
    }
}
