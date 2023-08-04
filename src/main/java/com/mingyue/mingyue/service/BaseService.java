package com.mingyue.mingyue.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.mingyue.bean.BaseBean;
import com.mingyue.mingyue.dao.BaseDao;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class BaseService<T extends BaseBean,D extends BaseDao<T>>{

    public Logger logger = Logger.getLogger(this.getClass());

    public abstract D getDao();

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void create(T bean) {

        bean.setCreatedDate(new Date());
        bean.setUpdateDate(new Date());
        bean.setStatus((short)1);
        bean.setDeleteStatus((short)0);
        getDao().create(bean);

    }

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void update(T bean) {
        bean.setId(null);
        bean.setUpdateDate(new Date());
        getDao().update(bean);

    }

    public T get(String uuid) {
        return getDao().get(uuid);
    }

    public List<T> findByWhere(Map<String,?> params) {
        return getDao().findByWhere(params);
    }

    public T findOneByWhere(Map<String,?> params) {
        PageHelper.startPage(1,1);
        List<T> list = findByWhere(params);
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return pageInfo.getList().get(0);
    }

}
