package com.achatserver.seqid.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/seqid")
public class SeqIdController {

    @RequestMapping("/{uid}")
    public Result getSeqId(@PathVariable String uid){
       //TODO: 需要验证uid是否在当前的服务中
       return new Result(true, StatusCode.OK, "获取成功");
    }
}
