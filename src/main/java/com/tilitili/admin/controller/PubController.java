package com.tilitili.admin.controller;

import com.tilitili.common.emnus.GroupEmum;
import com.tilitili.common.emnus.SendTypeEmum;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.entity.view.bot.BotMessage;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.manager.BotManager;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/api/pub/qq")
public class PubController extends BaseController{
    @Value("${mirai.master-qq}")
    private Long masterQQ;
    @Value("${silk.path}")
    private String silkPath;
    private final BaiduManager baiduManager;
    private final BotManager botManager;
    private String lastMessage = null;

    @Autowired
    public PubController(BaiduManager baiduManager, BotManager botManager) {
        this.baiduManager = baiduManager;
        this.botManager = botManager;
    }

    @PostMapping("/group/{group}")
    @ResponseBody
    public BaseModel<?> sendMessage(@RequestBody String message, @PathVariable Long group) {
        Asserts.notBlank(message, "消息为空");
        botManager.sendMessage(BotMessage.simpleTextMessage(message).setSendType(SendTypeEmum.Group_Message.sendType).setGroup(group));
        return BaseModel.success();
    }

    @PostMapping("/friend")
    @ResponseBody
    public BaseModel<?> sendFriendMessage(@RequestBody String message) {
        Asserts.notBlank(message, "消息为空");
        botManager.sendMessage(BotMessage.simpleTextMessage(message).setSendType(SendTypeEmum.Friend_Message.sendType).setQq(masterQQ));
        return BaseModel.success();
    }

    @PostMapping
    @ResponseBody
    public BaseModel<?> sendMessage(@RequestBody String message) {
        Asserts.notBlank(message, "消息为空");
        botManager.sendMessage(BotMessage.simpleTextMessage(message).setSendType(SendTypeEmum.Group_Message.sendType).setGroup(GroupEmum.RANK_GROUP.value));
        return BaseModel.success();
    }

    @PostMapping("/downloadVoice")
    public void runShell(String message, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        File wavFile = new File(silkPath,"voice.wav");
        File slkFile = new File(silkPath, "voice.slk");

        if (Objects.equals(lastMessage, message)) {
            download(request, response, wavFile);
            return;
        }
        lastMessage = message;

        if (wavFile.exists()) Files.delete(wavFile.toPath());
        if (slkFile.exists()) Files.delete(slkFile.toPath());

        String jpText = baiduManager.translate("jp", message);

        String speakShell = String.format("sh %s%srun.sh %s", silkPath, File.pathSeparator, jpText);
        Runtime.getRuntime().exec(speakShell);

        Thread.sleep(1000);

        download(request, response, wavFile);
    }

    @PostMapping("/translate")
    @ResponseBody
    public BaseModel<String> translate(@RequestParam(required = false) String from, @RequestParam(required = false) String to, @RequestBody String text) {
        if (StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
            return BaseModel.success(baiduManager.translate(from, to, text));
        } else if (StringUtils.isNotBlank(to)) {
            return BaseModel.success(baiduManager.translate(to, text));
        } else {
            return BaseModel.success(baiduManager.translate(text));
        }
    }
}
