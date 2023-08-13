package com.mingyue.base.controller;

import com.mingyue.base.bean.ReturnBean;
import org.apache.log4j.Logger;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public abstract class ThrowExceptionController {
    public Logger logger = Logger.getLogger(this.getClass());
    /**
     * 基于@ExceptionHandler的统一异常处理，返回json数据
     * 拦截spring参数验证错误
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ReturnBean exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        String errorMsg = "服务器内部错误，请稍后再试";
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException e = (MethodArgumentNotValidException) ex;
            List<ObjectError> list = e.getAllErrors();
            for (ObjectError error : list) {
                errorMsg = error.getDefaultMessage();
                logger.error(error);
                break;
            }
        }
        else if (ex instanceof RuntimeException) {
            RuntimeException e = (RuntimeException) ex;
            logger.error(e);
            e.printStackTrace();
            errorMsg = e.getMessage();
        }
        else {
            logger.error(ex);
        }
        return ReturnBean.error(errorMsg);
    }
}
