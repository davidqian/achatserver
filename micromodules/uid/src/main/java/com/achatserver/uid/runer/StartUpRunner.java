package com.achatserver.uid.runer;

import com.achatserver.uid.thread.ThreadMap;
import com.achatserver.uid.thread.ThreadQueue;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class StartUpRunner implements CommandLineRunner, Ordered {
    @Override
    public void run(String... args) throws Exception {
        ThreadQueue.initMap();
        ThreadMap.initMap();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
