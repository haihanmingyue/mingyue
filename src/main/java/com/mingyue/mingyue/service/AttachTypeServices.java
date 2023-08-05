package com.mingyue.mingyue.service;

import com.mingyue.mingyue.bean.AttachBean;
import com.mingyue.mingyue.bean.AttachType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.config.Config;
import com.mingyue.mingyue.dao.AttachDao;
import com.mingyue.mingyue.dao.AttachTypeDao;
import io.netty.util.internal.StringUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
