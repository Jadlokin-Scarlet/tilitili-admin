package com.tilitili.admin.manager;

import com.tilitili.common.StartApplication;
import com.tilitili.common.entity.pixivmoe.SearchIllust;
import com.tilitili.common.manager.PixivMoeManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class PixivManagerTest {
    @Resource
    PixivMoeManager pixivMoeManager;

    @Test
    public void test() throws InterruptedException {
        List<SearchIllust> list = pixivMoeManager.search("チルノ", 1L);
    }
}
