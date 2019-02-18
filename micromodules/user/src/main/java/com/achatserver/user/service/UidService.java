package com.achatserver.user.service;

import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UidService {

    @Autowired
    private RestTemplate restTemplate;

    public Result getUid(){
        return restTemplate.getForObject("http://uid-service/uid/getUid", Result.class);
    }
}
