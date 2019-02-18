package com.my.uid;

import com.my.Octopus.dataconfig.RuntimeConfig;
import com.my.Octopus.net.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UidApp {

    private static Logger logger = null;

    public static void main(String[] args) {
        try {
            RuntimeConfig.instance.init(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        logger = LoggerFactory.getLogger(UidApp.class);

        HttpServer server = new HttpServer();
        server.start("0.0.0.0", 9001);
        logger.info("match server started!");
    }
}
