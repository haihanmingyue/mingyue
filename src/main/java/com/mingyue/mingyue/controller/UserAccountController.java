package com.mingyue.mingyue.controller;

import com.mingyue.mingyue.bean.LoginInfo;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.dao.UserAccountDao;
import com.mingyue.mingyue.service.UserAccountServices;
import com.mingyue.mingyue.utils.Base64Util;
import com.mingyue.mingyue.utils.BaseContextUtils;
import com.mingyue.mingyue.utils.RsaUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("user")
public class UserAccountController extends BaseController<UserAccount, UserAccountDao,UserAccountServices> {



    @Autowired
    private UserAccountServices accountServices;



    @RequestMapping("/register")
    @ResponseBody
    public ReturnBean register(@RequestBody @Validated UserAccount user) throws Exception {
        accountServices.register(user);
        return ReturnBean.ok("注册成功").setData("注册成功");
    }

    @RequestMapping("/login")
    @ResponseBody
    public ReturnBean login(HttpServletRequest request) throws Exception {
        String username = ServletRequestUtils.getRequiredStringParameter(request,"username");
        String password = ServletRequestUtils.getRequiredStringParameter(request,"password");
        Integer rememberMe = ServletRequestUtils.getIntParameter(request,"rememberMe", 0);
        UserAccount userAccount = accountServices.findByUsername(username);
        return accountServices.login(userAccount,password,rememberMe);
    }


    @RequestMapping("/loginOut")
    @ResponseBody
    public ReturnBean loginOut(HttpServletRequest request, HttpServletResponse response){
        BaseContextUtils.loginout();
        return ReturnBean.ok("退出成功");
    }

    @Override
    protected UserAccountServices getService() {
        return accountServices;
    }
}
