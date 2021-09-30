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
import static com.tilitili.common.emnus.GroupEmum.TEST_GROUP;

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
                .setMessage("[手动回复]有红点就是私聊和特关"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("group").setMessageType("Plain").setGroup(QIAN_QIAN_GROUP.value)
//                .setMessage("我也要！送到雾之湖畔就行"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("group").setMessageType("Image").setGroup(QIAN_QIAN_GROUP.value)
//                .setUrl("http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-233841123-982D642D177D895D8F1609EB111860CB/0?term=2"));
    }


}
