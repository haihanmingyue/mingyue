package com.mingyue.userrole.controller;

import com.mingyue.base.controller.BaseController;
import com.mingyue.userrole.bean.SysMenu;
import com.mingyue.userrole.dao.SysMenuDao;
import com.mingyue.userrole.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 *
 * 像这种页面路由表基本上很少去改动的数据，可以存redis里
 *
 * */
@Controller
@RequestMapping("sysMenu")
public class SysMenuController extends BaseController<SysMenu, SysMenuDao,SysMenuService> {

    @Autowired
    private SysMenuService service;


    @Override
    protected SysMenuService getService() {
        return service;
    }
}
