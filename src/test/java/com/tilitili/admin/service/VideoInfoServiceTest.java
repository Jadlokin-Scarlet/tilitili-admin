package com.tilitili.admin.service;

import com.tilitili.StartApplication;
import com.tilitili.admin.entity.count.sub.NewVideoCount;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
class VideoInfoServiceTest extends TestCase {
    @Autowired
    private VideoInfoService videoInfoService;
    @Test
    void getNewVideoData() {
        List<NewVideoCount> newVideoCountList = videoInfoService.getNewVideoCount();
        System.out.println(newVideoCountList);
    }
}