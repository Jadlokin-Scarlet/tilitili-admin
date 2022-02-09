package com.tilitili.admin.manager;

import com.tilitili.common.StartApplication;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.mapper.rank.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("TempMessage").setMessageType("Plain").setQq(2010851224L).setGroup(QIAN_QIAN_GROUP.value)
//                .setMessage("¿"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("GroupMessage").setMessageType("Plain").setGroup(QIAN_QIAN_GROUP.value)
//                .setMessage("我也要！送到雾之湖畔就行"));

//        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("GroupMessage").setMessageType("Image").setGroup(TEST_GROUP.value)
//                .setUrl("https://api.lolicon.app/assets/img/lx.jpg"));

//        miraiManager.sendMessage(new MiraiMessage().setMessageType("List").setQuote(1048L).setMessageChainList(Arrays.asList(
//                new MiraiMessageChain().setType("Plain").setText("?")
//        )));
    }


}
