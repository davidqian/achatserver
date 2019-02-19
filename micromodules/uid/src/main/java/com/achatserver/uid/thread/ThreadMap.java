package com.achatserver.uid.thread;

import org.springframework.stereotype.Component;

@Component
public class ThreadMap {

    public static void initMap(){
        for(int i = 0; i < 4; i++){
            WorkThread workThread = new WorkThread(i);
            workThread.start();
        }
    }
}
