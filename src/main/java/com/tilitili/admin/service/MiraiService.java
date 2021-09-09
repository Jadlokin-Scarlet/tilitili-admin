package com.tilitili.admin.service;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.mirai.BaseMessageHandle;
import com.tilitili.common.entity.mirai.MessageChain;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.ResourcesManager;
import com.tilitili.common.utils.Asserts;
import com.tilitili.common.utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tilitili.common.utils.AsciiUtil.sbc2dbcCase;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Slf4j
@Service
public class MiraiService {

    private final List<BaseMessageHandle> messageHandleList;
    private final ResourcesManager resourcesManager;
    private final MiraiManager miraiManager;

    @Autowired
    public MiraiService(List<BaseMessageHandle> messageHandleList, ResourcesManager resourcesManager, MiraiManager miraiManager) {
        this.messageHandleList = messageHandleList;
        this.resourcesManager = resourcesManager;
        this.miraiManager = miraiManager;
    }

    public MiraiMessage handleGroupMessage(MiraiMessageView message, MiraiSessionService.MiraiSession miraiSession) {
        try {
            MiraiRequest miraiRequest = new MiraiRequest(message, miraiSession);

            messageHandleList.sort(Comparator.comparing(a -> a.getKeyword().size(), Comparator.reverseOrder()));
            for (BaseMessageHandle handle : messageHandleList) {
                if (handle.getSendType().equals("group")) {
                    if (handle.getKeyword().isEmpty() || handle.getKeyword().contains(miraiRequest.getTitle())) {
                        return handle.handleMessage(miraiRequest);
                    }
                }
            }
            return new MiraiMessage().setMessage("").setMessageType("Plain");
        } catch (AssertException e) {
            log.error(e.getMessage());
            return new MiraiMessage().setMessage("").setMessageType("Plain");
        } catch (Exception e) {
            log.error("处理消息回调失败", e);
            return new MiraiMessage().setMessage("").setMessageType("Plain");
        }

    }

    public MiraiMessage handleMessage(MiraiMessageView message, MiraiSessionService.MiraiSession miraiSession) {
        try {
            MiraiRequest miraiRequest = new MiraiRequest(message, miraiSession);

            if (message.getType().equals("TempMessage")) {
                if (resourcesManager.isForwardTempMessage()) {
                    miraiManager.sendFriendMessage("Plain", miraiRequest.getText());
                }
            }

            for (BaseMessageHandle messageHandle : messageHandleList) {
                if (messageHandle.getSendType().equals("friend")) {
                    if (messageHandle.getKeyword().contains(sbc2dbcCase(miraiRequest.getTitle()))) {
                        return messageHandle.handleMessage(miraiRequest);
                    }
                }
            }

            return new MiraiMessage().setUrl("http://m.qpic.cn/psc?/V53UUlnk2IehYn4WcXfY2dBFO92OvB1L/TmEUgtj9EK6.7V8ajmQrEPBYbjL66rmGmhZeULQk5K23cRElRpiBGW67YBgbgQxSQQ*jZ1sT2lB3FSogwc0t5DyuSeiAT17yAwmaSTNULPo!/b&bo=aABPAAAAAAABFxc!&rf=viewer_4").setMessageType("Image");
        } catch (AssertException e) {
            log.error(e.getMessage());
            return new MiraiMessage().setMessage(e.getMessage()).setMessageType("Plain");
        } catch (Exception e) {
            log.error("处理消息回调失败",e);
            return new MiraiMessage().setUrl("http://m.qpic.cn/psc?/V53UUlnk2IehYn4WcXfY2dBFO92OvB1L/TmEUgtj9EK6.7V8ajmQrENdFC7iq*X8AsvjACl.g*DjfOPu0Ohw4r47052XDpNQGtOBy0dw5ZNtRggzAZvOvUBGBlTjwCDv4o3k*J7IWang!/b&bo=eABcAAAAAAABFxQ!&rf=viewer_4").setMessageType("Image");
        }
    }
}
