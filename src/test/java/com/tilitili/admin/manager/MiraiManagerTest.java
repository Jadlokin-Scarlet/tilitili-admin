package com.tilitili.admin.manager;

import com.tilitili.common.StartApplication;
import com.tilitili.common.emnus.GroupEmum;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static com.tilitili.common.emnus.GroupEmum.QIAN_QIAN_GROUP;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class MiraiManagerTest {

    @Resource
    private MiraiManager miraiManager;
    @Resource
    private VideoInfoMapper videoInfoMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private RecommendVideoMapper recommendVideoMapper;
    @Resource
    private RecommendMapper recommendMapper;
    @Resource
    private RecommendTalkMapper recommendTalkMapper;

    @Test
    public void test0() {
        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("temp").setMessageType("Plain").setQq(1578611368L).setGroup(QIAN_QIAN_GROUP.value)
                .setMessage("[手动回复]rc\nxxx叫我xxx   这个还不够吗   左边大多数关于时间的描述都可以"));

    }


}
