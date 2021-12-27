package com.tilitili.admin.controller;

import static org.junit.Assert.*;

import com.tilitili.StartApplication;
import com.tilitili.admin.entity.view.RedisQuery;
import com.tilitili.admin.entity.view.RedisView;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class RedisControllerTest {
    @Resource
    RedisController tester;

    @Test
    public void listRedis() {
        BaseModel<PageModel<RedisView>> baseModel = tester.listRedis(new RedisQuery().setPageSize(20).setCurrent(1));
        System.out.println(baseModel);
    }

    @Test
    public void delRedisKey() {
    }

    @Test
    public void editRedisKey() {
    }
}