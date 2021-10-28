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

import java.util.Arrays;

import static com.tilitili.common.emnus.GroupEmum.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StartApplication.class)
@EnableAutoConfiguration
public class MiraiManagerTest {
    @Value("${mirai.master-qq}")
    private Long MASTER_QQ;
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
//        String session = miraiManager.auth();
//        System.out.println(session);
//        miraiManager.verify(session);

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("temp").setMessageType("Plain").setQq(906892635L).setGroup(XIAO_HEI_HE_GROUP.value)
//                .setMessage("[手动回复]私聊没有色图"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("group").setMessageType("Plain").setGroup(QIAN_QIAN_GROUP.value)
//                .setMessage("我也要！送到雾之湖畔就行"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("group").setMessageType("Image").setGroup(TEST_GROUP.value)
//                .setUrl("https://api.lolicon.app/assets/img/lx.jpg"));

        miraiManager.sendMessage(new MiraiMessage().setSendType("group").setGroup(TEST_GROUP.value).setMessageType("List").setMessageChainList(Arrays.asList(
                new MessageChain().setType("Quote").setId(1044L).setGroupId(TEST_GROUP.value).setSenderId(MASTER_QQ),
                new MessageChain().setType("Plain").setText("?")
        )));
    }


}
