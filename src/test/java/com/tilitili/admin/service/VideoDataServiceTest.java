package com.tilitili.admin.service;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class VideoDataServiceTest extends TestCase {
    @Resource
    private VideoDataService videoDataService;
    @Test
    void reRank() {
        videoDataService.reRank(23);
    }
}