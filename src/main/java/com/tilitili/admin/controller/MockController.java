package com.tilitili.admin.controller;

import com.tilitili.common.emnus.SendTypeEmum;
import com.tilitili.common.entity.view.bot.BotMessage;
import com.tilitili.common.manager.BotManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/api/mock")
public class MockController {
    @Value("${mirai.master-qq}")
    private Long masterQQ;
    private final BotManager botManager;

    @Autowired
    public MockController(BotManager botManager) {
        this.botManager = botManager;
    }

    @RequestMapping("")
    public void mock(HttpServletRequest request, @RequestBody(required = false) String body) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        body = body == null? "": body;
        String cookie = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[]{})).map(c -> String.format("%s=%s", c.getName(), c.getValue())).collect(Collectors.joining("; "));
        String message = String.format("%s?%s%nbody=%s%ncookie=%s", requestURL, queryString, body, cookie);
        botManager.sendMessage(BotMessage.simpleTextMessage(message).setSendType(SendTypeEmum.Friend_Message.sendType).setQq(masterQQ));
    }
}
