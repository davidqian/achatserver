package com.achatserver.seqid.runner;

import com.achatserver.seqid.thread.WorkThreadMap;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class ThreadUpRunner implements CommandLineRunner, Ordered {
    @Override
    public void run(String... args) throws Exception {
        WorkThreadMap.initMap();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
