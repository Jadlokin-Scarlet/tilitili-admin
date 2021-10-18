package com.tilitili.admin.manager;

import com.tilitili.common.StartApplication;
import com.tilitili.common.manager.PixivManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class PixivManagerTest {
    @Resource
    PixivManager pixivManager;

    @Test
    public void test() {
        pixivManager.search("チルノ", 1L);
    }
}
