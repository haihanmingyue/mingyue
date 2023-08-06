package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.service.UserAccountServices;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("user")
public class UserAccountController extends BaseController{



    @Autowired
    private UserAccountServices accountServices;



    @RequestMapping("/register")
    @ResponseBody
    public ReturnBean register(@RequestBody @Validated UserAccount user) throws Exception {
        accountServices.register(user);
        return ReturnBean.ok("注册成功").setData("注册成功");
    }

}
