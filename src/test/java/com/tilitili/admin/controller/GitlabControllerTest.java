package com.tilitili.admin.controller;

import com.tilitili.StartApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
public class GitlabControllerTest {
	@Resource
	GitlabController tester;

	@Test
	public void deleteContainer() {
		tester.deleteContainer("tilitili-admin");
		tester.deleteContainer("tilitili-job");
		tester.deleteContainer("tilitili-bot");
		tester.deleteContainer("tilitili-spider");
		tester.deleteContainer("tilitili-oss");
	}
}