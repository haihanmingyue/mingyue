package com.mingyue.userrole.service;


import com.mingyue.base.service.BaseService;
import com.mingyue.userrole.bean.SysMenu;
import com.mingyue.userrole.dao.SysMenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysMenuService extends BaseService<SysMenu, SysMenuDao> {

    @Autowired
    private SysMenuDao dao;

    @Override
    public SysMenuDao getDao() {
        return dao;
    }
}
