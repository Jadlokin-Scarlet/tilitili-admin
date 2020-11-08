package com.tilitili.admin.exception;

import com.tilitili.common.entity.view.BaseModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HandleException extends ResponseEntityExceptionHandler {


}
