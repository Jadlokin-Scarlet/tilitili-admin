package com.tilitili.admin.controller;

import com.tilitili.common.manager.MiraiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Controller
@RequestMapping("/api/mock")
public class MockController {
    private final MiraiManager miraiManager;

    @Autowired
    public MockController(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @RequestMapping("")
    public void mock(HttpServletRequest request, @RequestBody(required = false) String body) {
        String requestURL = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        body = body == null? "": body;
        miraiManager.sendFriendMessage("Plain", requestURL + "?" + queryString + "\n" + body);
    }
}
