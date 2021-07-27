package com.tilitili.admin.controller;

import com.tilitili.common.entity.view.BaseModel;
import com.tilitili.common.exception.AssertException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class BaseController {

    @ExceptionHandler(AssertException.class)
    @ResponseBody
    public BaseModel handleAssertError(Exception ex) {
        return new BaseModel(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseModel handleError(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL(), ex);
        return new BaseModel(ex.getMessage());
    }
}
