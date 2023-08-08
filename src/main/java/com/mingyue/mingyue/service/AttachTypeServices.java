package com.mingyue.mingyue.service;

import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.dao.AttachTypeDao;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AttachTypeServices extends BaseService<AttachType, AttachTypeDao>{

    @Autowired
    private AttachTypeDao dao;

    @Override
    public AttachTypeDao getDao() {
        return dao;
    }

    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public void save(AttachType attachType) {

        if (StringUtils.hasText(attachType.getUuid())) {
            update(attachType);
        } else {
            attachType.setUuid(UUID.randomUUID().toString());
            create(attachType);
        }
    }
}
