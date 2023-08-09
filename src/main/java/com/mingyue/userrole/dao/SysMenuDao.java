package com.mingyue.userrole.dao;

import com.mingyue.mingyue.dao.BaseDao;
import com.mingyue.userrole.bean.SysMenu;
import com.mingyue.userrole.bean.UserMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface SysMenuDao extends BaseDao<SysMenu> {
    List<SysMenu> findByWhere(Map<String,?> params);
}
