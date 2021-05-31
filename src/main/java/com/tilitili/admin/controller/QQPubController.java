package com.tilitili.admin.controller;

import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.manager.MiraiManager;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/pub/qq")
public class QQPubController extends BaseController{
    private final MiraiManager miraiManager;

    @Autowired
    public QQPubController(MiraiManager miraiManager) {
        this.miraiManager = miraiManager;
    }

    @PostMapping
    @ResponseBody
    public BaseModel sendMessage(@RequestBody String message) {
        Asserts.notBlank(message, "消息为空");
        miraiManager.sendGroupMessage("Plain", message);
        return BaseModel.success();
    }
}
