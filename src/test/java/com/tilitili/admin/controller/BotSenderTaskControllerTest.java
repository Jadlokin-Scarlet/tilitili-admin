package com.tilitili.admin.controller;

import static org.junit.Assert.*;

import com.google.gson.Gson;
import com.tilitili.StartApplication;
import com.tilitili.common.entity.query.BotSenderQuery;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class BotSenderTaskControllerTest {
	@Resource
	BotSenderTaskController tester;

	@Test
	public void getBotSenderTaskByCondition() {
		BaseModel<PageModel<Map<String, Object>>> model = tester.getBotSenderTaskByCondition(new BotSenderQuery());
		System.out.println(new Gson().toJson(model));
	}
}