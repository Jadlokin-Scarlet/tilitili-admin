package com.tilitili.admin.service;

import com.tilitili.common.entity.Admin;
import com.tilitili.common.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (admin == null) {
            return null;
        }
        if (!Objects.equals(password, admin.getPassword())) {
            return null;
        }
        return admin;
    }

}
