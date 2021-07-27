package com.tilitili.admin.service.mirai;

import com.tilitili.common.emnus.GroupEmum;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.manager.BaiduManager;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class VoiceHandle implements BaseMessageHandle {
    private final BaiduManager baiduManager;
    private final MiraiManager miraiManager;

    @Autowired
    public VoiceHandle(BaiduManager baiduManager, MiraiManager miraiManager) {
        this.baiduManager = baiduManager;
        this.miraiManager = miraiManager;
    }

    @Override
    public List<String> getKeyword() {
        return Arrays.asList("说", "s");
    }

    @Override
    public String getDescription() {
        return "文本转语音（日语）";
    }

    @Override
    public String getSendType() {
        return "group";
    }

    @Override
    public MiraiMessage handleMessage(MiraiMessageView message, Map<String, String> map) throws IOException, InterruptedException {
        String text = map.get("body");

        String jpText = baiduManager.translate("jp", text);

        String speakShell = String.format("sh /home/admin/slik/run.sh %s", jpText);
        Runtime.getRuntime().exec(speakShell);

        Thread.sleep(1000);

        File slkFile = new File("/home/admin/silk/voice.slk");
        Asserts.isTrue(slkFile.exists(), "转码slk失败");

        String voiceId = miraiManager.uploadVoice(slkFile);
        Asserts.notBlank(voiceId, "上传失败");

        return new MiraiMessage().setVoiceId(voiceId).setMessageType("Voice");
    }
}
