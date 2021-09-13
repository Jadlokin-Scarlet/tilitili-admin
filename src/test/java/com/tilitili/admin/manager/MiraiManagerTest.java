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
        Integer integer = miraiManager.sendMessage(new MiraiMessage().setSendType("group").setMessageType("Image").setGroup(QIAN_QIAN_GROUP.value).setUrl("http://c2cpicdw.qpic.cn/offpic_new/545459363//545459363-1650411805-E7E658A46AB963FE09B0080806BBE90B/0?term=2"));

    }


}
