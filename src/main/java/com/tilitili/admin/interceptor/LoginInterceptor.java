package com.tilitili.admin.interceptor;

import com.google.gson.Gson;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    private final AdminMapper adminMapper;
    private final Gson gson;

    @Autowired
    public LoginInterceptor(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
        gson = new Gson();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Admin admin = (Admin)session.getAttribute("admin");

        //未登录
        if (admin == null){
            this.returnResp(response,new BaseModel("请重新登录"));
            return false;
        }

        Admin oldAdmin = adminMapper.getById(admin.getId());

        if (oldAdmin == null) {
            this.returnResp(response,new BaseModel("请重新登录"));
            return false;
        }

        return true;
    }

    private void returnResp(HttpServletResponse response, BaseModel baseModel) {
        PrintWriter writer = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            writer = response.getWriter();
            writer.print(gson.toJson(baseModel));
        } catch (IOException e) {
            log.error("returnResp error, ", e);
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }
}
