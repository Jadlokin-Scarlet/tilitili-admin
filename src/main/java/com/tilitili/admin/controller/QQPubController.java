package com.tilitili.admin.controller;

import com.tilitili.common.emnus.GroupEmum;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/api/pub/qq")
public class QQPubController extends BaseController{
    private final MiraiManager miraiManager;
    private final BaiduManager baiduManager;

    @Autowired
    public QQPubController(MiraiManager miraiManager, BaiduManager baiduManager) {
        this.miraiManager = miraiManager;
        this.baiduManager = baiduManager;
    }

    @PostMapping
    @ResponseBody
    public BaseModel sendMessage(@RequestBody String message) {
        Asserts.notBlank(message, "消息为空");
        miraiManager.sendGroupMessage("Plain", message);
        return BaseModel.success();
    }

    @PostMapping("/sendVoice")
    @ResponseBody
    public BaseModel runShell(String message) throws IOException, InterruptedException {
        String jpMessage = baiduManager.translate("jp", message);

        String speakShell = String.format("sh /home/admin/slik/run.sh %s", jpMessage);
        Runtime.getRuntime().exec(speakShell);

        Thread.sleep(1000);

        File slkFile = new File("/home/admin/silk/voice.slk");
        Asserts.isTrue(slkFile.exists(), "转码slk失败");

        String voiceId = miraiManager.uploadVoice(slkFile);
        Asserts.notBlank(voiceId, "上传失败");

        miraiManager.sendMessage(new MiraiMessage().setMessageType("Voice").setVoiceId(voiceId).setSendType("group").setGroup(GroupEmum.TEST_GROUP.getValue()));
        return BaseModel.success();
    }
}
