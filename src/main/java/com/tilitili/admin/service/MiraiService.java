package com.tilitili.admin.service;

import com.tilitili.admin.entity.mirai.MiraiRequest;
import com.tilitili.admin.service.mirai.BaseMessageHandle;
import com.tilitili.common.entity.mirai.MiraiMessage;
import com.tilitili.common.entity.mirai.MiraiMessageView;
import com.tilitili.common.exception.AssertException;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.manager.ResourcesManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.tilitili.common.utils.AsciiUtil.sbc2dbcCase;

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

            List<BaseMessageHandle> groupList = messageHandleList.stream().filter(handle -> handle.getType().getSendType().equals("group")).filter(handle -> Arrays.asList(1, 2).contains(handle.getType().getType()))
                    .sorted(Comparator.comparing(a -> a.getType().getSort(), Comparator.reverseOrder())).collect(Collectors.toList());
            for (BaseMessageHandle handle : groupList) {
                handle.handleMessage(miraiRequest);
                if (handle.getType().getType().equals(2)) {
                    break;
                }
            }

            for (BaseMessageHandle handle : messageHandleList) {
                if (handle.getType().getSendType().equals("group") && handle.getType().getType().equals(0)) {
                    if (handle.getType().getKeyword().contains(miraiRequest.getTitle())) {
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
            Long sender = message.getSender().getId();
            MiraiRequest miraiRequest = new MiraiRequest(message, miraiSession);

            if (message.getType().equals("TempMessage")) {
                if (resourcesManager.isForwardTempMessage()) {
                    miraiManager.sendFriendMessage("Plain", String.format("%s\n%s", sender, miraiRequest.getText()));
                }
            }

            for (BaseMessageHandle messageHandle : messageHandleList) {
                if (messageHandle.getType().getSendType().equals("friend")) {
                    if (messageHandle.getType().getKeyword().contains(sbc2dbcCase(miraiRequest.getTitle()))) {
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
