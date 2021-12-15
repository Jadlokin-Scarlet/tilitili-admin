package com.tilitili.admin.controller;

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
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/api/pub/qq")
public class PubController extends BaseController{
    private final BaiduManager baiduManager;
    private final MiraiManager miraiManager;
    private String lastMessage = null;

    @Autowired
    public PubController(MiraiManager miraiManager, BaiduManager baiduManager) {
        this.miraiManager = miraiManager;
        this.baiduManager = baiduManager;
    }

    @PostMapping("/group/{group}")
    @ResponseBody
    public BaseModel sendMessage(@RequestBody String message, @PathVariable Long group) {
        Asserts.notBlank(message, "消息为空");
        miraiManager.sendGroupMessage("Plain", message, group);
        return BaseModel.success();
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

        if (Objects.equals(lastMessage, message)) {
            download(request, response, wavFile);
            return;
        }
        lastMessage = message;

        if (wavFile.exists()) Asserts.isTrue(wavFile.delete(), "删除wav失败");
        if (slkFile.exists()) Asserts.isTrue(slkFile.delete(), "删除slk失败");

        String jpText = baiduManager.translate("jp", message);

        String speakShell = String.format("sh /home/admin/silk/run.sh %s", jpText);
        Runtime.getRuntime().exec(speakShell);

        Thread.sleep(1000);

        download(request, response, wavFile);
    }

    @PostMapping("/translate")
    @ResponseBody
    public BaseModel<String> translate(@RequestBody String text) {
        return BaseModel.success(baiduManager.translate(text));
    }
}