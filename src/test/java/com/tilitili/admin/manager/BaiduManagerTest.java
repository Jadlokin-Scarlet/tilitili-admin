package com.tilitili.admin.manager;

import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.manager.MiraiManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaiduManagerTest {
    @Autowired
    private BaiduManager baiduManager;
    @Autowired
    private MiraiManager miraiManager;
    @Test
    public void test() {
    }
}
