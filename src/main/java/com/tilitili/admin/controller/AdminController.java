package com.tilitili.admin.controller;

import com.tilitili.admin.service.AdminService;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.utils.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/admin")
@Slf4j
public class AdminController extends BaseController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/isLogin")
    @ResponseBody
    public BaseModel<Admin> isLogin(@SessionAttribute(value = "admin", required = false) Admin admin) {
        if (admin == null) {
            return new BaseModel<>("请重新登陆");
        }else {
            return new BaseModel<>("已登录", true, admin);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public BaseModel<Admin> login(@RequestBody Admin reqAdmin, HttpSession session) {
        Asserts.notNull(reqAdmin, "参数异常");
        Asserts.notNull(reqAdmin.getUserName(), "请输入用户名");
        Asserts.notNull(reqAdmin.getPassword(), "请输入密码");
        Admin admin = adminService.login(reqAdmin.getUserName(), reqAdmin.getPassword());
        Asserts.notNull(admin, "账号未获取到");
        session.setAttribute("admin", admin);
        return new BaseModel<>("登录成功", true, admin);
    }

    @PostMapping("/loginOut")
    @ResponseBody
    public BaseModel<?> loginOut(HttpSession session) {
        session.removeAttribute("admin");
        return new BaseModel<>("退出登录成功", true);
    }
}
