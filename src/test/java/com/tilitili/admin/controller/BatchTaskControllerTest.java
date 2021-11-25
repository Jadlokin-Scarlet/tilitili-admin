package com.tilitili.admin.controller;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.tilitili.StartApplication;
import com.tilitili.common.entity.query.BatchTaskQuery;
import com.tilitili.common.entity.view.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class BatchTaskControllerTest {
    @Resource
    BatchTaskController tester;

    @Test
    public void getBatchTaskCount() {
        BaseModel batchTaskCount = tester.getBatchTaskCount(new BatchTaskQuery().setStatus(2).setTime(1).setReasonList(Arrays.asList(2, 4)));
        System.out.println(new Gson().toJson(batchTaskCount));
    }
}