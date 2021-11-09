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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @PostMapping("/downloadVoice")
    public void runShell(String message, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        File wavFile = new File("/home/admin/silk/voice.wav");
        File slkFile = new File("/home/admin/silk/voice.slk");
        Asserts.isTrue(slkFile.delete(), "删除slk失败");
        Asserts.isTrue(wavFile.delete(), "删除wav失败");

        String jpText = baiduManager.translate("jp", message);

        String speakShell = String.format("sh /home/admin/silk/run.sh %s", jpText);
        Runtime.getRuntime().exec(speakShell);

        Thread.sleep(1000);

        download(request, response, wavFile);
    }
}
