package com.tilitili.admin.service;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RecommendVideoServiceTest extends TestCase {
    @Resource
    RecommendVideoService recommendVideoService;
    @Test
    void listIssue() {
        List<com.tilitili.common.entity.view.resource.Resource> resources = recommendVideoService.listIssue();
        System.out.println(resources);
    }
}