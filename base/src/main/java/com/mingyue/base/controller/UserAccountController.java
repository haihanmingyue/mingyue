package com.mingyue.base.controller;

import com.mingyue.base.bean.ReturnBean;
import com.mingyue.base.bean.UserAccount;
import com.mingyue.base.dao.UserAccountDao;
import com.mingyue.base.service.UserAccountServices;
import com.mingyue.mingyue.utils.BaseContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("user")
public class UserAccountController extends BaseController<UserAccount, UserAccountDao,UserAccountServices> {



    @Autowired
    private UserAccountServices accountServices;



    @RequestMapping("/register")
    @ResponseBody
    public ReturnBean register(@RequestBody @Validated UserAccount user,HttpServletRequest request) throws Exception {
        accountServices.register(user,request);
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

    @RequestMapping("/checkLogin")
    @ResponseBody
    public ReturnBean checkLogin(HttpServletRequest request) throws Exception {
        return ReturnBean.ok("查询成功").setData("success");
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
