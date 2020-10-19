package com.tilitili.admin.controller;

import com.tilitili.admin.entity.Admin;
import com.tilitili.admin.entity.view.BaseModel;
import com.tilitili.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/isLogin")
    @ResponseBody
    public BaseModel isLogin(@SessionAttribute(value = "admin", required = false) Admin admin) {
        if (admin == null) {
            return new BaseModel("请重新登陆");
        }else {
            return new BaseModel("已登录", true, admin.getUserName());
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public BaseModel login(@RequestBody Admin reqAdmin, HttpSession session) {
        Admin admin = adminService.login(reqAdmin.getUserName(), reqAdmin.getPassword());
        if (admin == null) {
            return new BaseModel("用户名密码错误");
        }
        session.setAttribute("admin", admin);
        return new BaseModel("登录成功", true, admin.getUserName());
    }

    @PostMapping("/loginOut")
    @ResponseBody
    public BaseModel loginOut(HttpSession session) {
        session.removeAttribute("admin");
        return new BaseModel("退出登录成功", true);
    }
}
