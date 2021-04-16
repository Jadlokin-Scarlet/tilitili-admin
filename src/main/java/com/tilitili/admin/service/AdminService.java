package com.tilitili.admin.service;

import com.tilitili.common.entity.Admin;
import com.tilitili.common.mapper.AdminMapper;
import com.tilitili.common.utils.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.util.Objects;

@Service
public class AdminService {

    private final AdminMapper adminMapper;

    @Autowired
    public AdminService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public Admin login(String userName, String password) {
        Admin admin = adminMapper.getByName(userName);
        Asserts.isTrue(admin != null, "账号不存在");
        Assert.isTrue(Objects.equals(DigestUtils.md5DigestAsHex(password.getBytes()), admin.getPassword()), "密码错误");
        return admin;
    }

}
