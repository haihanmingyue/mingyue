package com.mingyue.attach.dao;


import com.mingyue.attach.bean.Attach;
import com.mingyue.base.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AttachDao extends BaseDao<Attach> {

    List<Attach> findBySubType(Map<String,?> params);
}
