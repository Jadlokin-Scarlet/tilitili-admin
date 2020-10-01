package com.tilitili.admin.manager;

import com.tilitili.admin.StartApplication;
import com.tilitili.admin.entity.Video;
import com.tilitili.admin.query.VideoQuery;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = StartApplication.class)
@WebAppConfiguration
public class VideoManagerTest {

    @Autowired
    private VideoManager videoManager;

    @Test
    public void test() {
        List<Video> videoList = videoManager.listVideo(new VideoQuery().setIssue(0));
        log.info(videoList.toString());
        Assert.notEmpty(videoList, "搜索视频异常");
    }

}