package com.mingyue.sendmsg.controller;

import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.controller.ThrowExceptionController;
import com.mingyue.sendmsg.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("email")
public class EmailController extends ThrowExceptionController {


    @Autowired
    private EmailService emailService;


    @RequestMapping("/getCode")
    @ResponseBody
    public ReturnBean getCode(HttpServletRequest request) throws ServletRequestBindingException {

        emailService.getCode(request);
        return ReturnBean.ok("验证码已发送");
    }

}
