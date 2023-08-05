package com.mingyue.mingyue.dao;


import com.mingyue.mingyue.bean.AttachBean;
import com.mingyue.mingyue.bean.UserAccount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttachDao extends BaseDao<AttachBean>{

    List<AttachBean> findBySubType(Map<String,?> params);
}
