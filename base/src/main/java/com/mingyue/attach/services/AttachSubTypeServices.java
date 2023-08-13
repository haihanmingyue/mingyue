package com.mingyue.attach.services;

import com.mingyue.attach.bean.AttachSubType;
import com.mingyue.attach.dao.AttachSubTypeDao;
import com.mingyue.base.service.BaseService;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AttachSubTypeServices extends BaseService<AttachSubType, AttachSubTypeDao> {

    @Autowired
    private AttachSubTypeDao dao;

    @Override
    public AttachSubTypeDao getDao() {
        return dao;
    }



    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public void save(AttachSubType attachSubType) {

        if (StringUtils.hasText(attachSubType.getUuid())) {

            update(attachSubType);

        } else {
            create(attachSubType);
        }
    }
}
