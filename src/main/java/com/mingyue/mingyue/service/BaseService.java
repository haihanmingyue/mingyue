package com.mingyue.mingyue.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.BaseBean;
import com.mingyue.mingyue.dao.BaseDao;
import com.mingyue.mingyue.interfacePack.DeleteStatus;
import com.mingyue.mingyue.utils.BaseContextUtils;
import io.netty.util.internal.StringUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseService<T extends BaseBean,D extends BaseDao<T>>{

    public Logger logger = Logger.getLogger(this.getClass());

    public abstract D getDao();

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void create(T bean) {

        bean.setCreatedDate(new Date());
        bean.setUpdateDate(new Date());
        bean.setStatus((short)1);
        bean.setDeleteStatus((short)0);
        if (!StringUtils.hasText(bean.getUuid())) {
            bean.setUuid(UUID.randomUUID().toString());
        }
        if (BaseContextUtils.getCurrentHumanId() != null) {
            bean.setCreatedBy(BaseContextUtils.getCurrentHumanId());
            bean.setUpdateBy(BaseContextUtils.getCurrentHumanId());
        }
        getDao().create(bean);

    }

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void update(T bean) {
        bean.setId(null);
        bean.setUpdateDate(new Date());
        if (BaseContextUtils.getCurrentHumanId() != null) {
            bean.setUpdateBy(BaseContextUtils.getCurrentHumanId());
        }
        getDao().update(bean);

    }


    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public void save(T bean) {

        if (StringUtils.hasText(bean.getUuid())) {
            update(bean);
        } else {
            bean.setUuid(UUID.randomUUID().toString());
            create(bean);
        }
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

    @Transactional(rollbackFor = { RuntimeException.class, Exception.class })
    public void delete(T bean) {

        if (bean instanceof DeleteStatus) {
            bean.setDeleteStatus(DeleteStatus.DELETE_STATUS_DELETE_YES);
            update(bean);
        } else {
            getDao().delete(bean.getUuid());
        }

    }

}
