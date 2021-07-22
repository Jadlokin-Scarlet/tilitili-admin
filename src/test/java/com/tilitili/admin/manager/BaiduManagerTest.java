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
        miraiManager.sendGroupMessage("Plain", "臭威威", 229712256L);
//        String result = baiduManager.translateImage("http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-1286451716-A171CF4B966BAC4B3CEDE4A83DD0AE53/0?term=2");
//        System.out.println(result);
    }
}
