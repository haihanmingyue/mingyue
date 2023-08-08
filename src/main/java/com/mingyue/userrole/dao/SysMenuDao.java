package com.mingyue.userrole.dao;

import com.mingyue.mingyue.dao.BaseDao;
import com.mingyue.userrole.bean.SysMenu;
import com.mingyue.userrole.bean.UserMenu;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface SysMenuDao extends BaseDao<SysMenu> {
}
