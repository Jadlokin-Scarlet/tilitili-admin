package com.tilitili.admin.service;

import com.tilitili.common.entity.VideoData;
import com.tilitili.common.manager.VideoDataManager;
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
    @Resource
    private VideoDataManager videoDataManager;
    @Test
    void reRank() {
        VideoData hisData = videoDataManager.getByAvAndIssue(715997051L, 44);
    }
}