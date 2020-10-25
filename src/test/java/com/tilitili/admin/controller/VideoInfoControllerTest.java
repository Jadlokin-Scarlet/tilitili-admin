package com.tilitili.admin.controller;

import com.google.gson.GsonBuilder;
import com.tilitili.StartApplication;
import com.tilitili.common.entity.VideoInfo;
import com.tilitili.common.entity.query.VideoInfoQuery;
import com.tilitili.common.entity.view.BaseModel;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
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
public class VideoInfoControllerTest {

    @Autowired
    private VideoInfoController videoInfoController;

    @Test
    public void listVideoByCondition() {
        BaseModel res = videoInfoController.getVideoInfoByCondition(new VideoInfoQuery());
        log.info(new GsonBuilder().setPrettyPrinting().create().toJson(res));
    }

}