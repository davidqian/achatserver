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
       return new Result(true, StatusCode.OK, "获取成功");
    }
}
