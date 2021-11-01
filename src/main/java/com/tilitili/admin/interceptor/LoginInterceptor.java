package com.tilitili.admin.interceptor;

import com.google.gson.Gson;
import com.tilitili.common.entity.Admin;
import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.mapper.tilitili.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
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

        //登陆和资源下放不用登陆
        String url = request.getRequestURL().toString();
        String method = request.getMethod();
        if (url.contains("/admin")) {
            return true;
        }
        if (url.contains("/resources") && HttpMethod.GET.matches(method)) {
            return true;
        }
        if (url.contains("/pub")) {
            return true;
        }
        if (url.contains("error") || url.contains("ico")) {
            return true;
        }
        if (url.contains("/mock")) {
            return true;
        }

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

        if (oldAdmin.getType() == 2 && ! HttpMethod.GET.matches(method)) {
            this.returnResp(response,new BaseModel("暂无权限"));
            return false;
        }

//        if (! HttpMethod.GET.matches(method)) {
//            Log.info("request %s %s params: %s", method, request.getRequestURL(), request.());
//        }

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
