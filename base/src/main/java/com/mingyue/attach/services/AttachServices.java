package com.mingyue.attach.services;

import com.mingyue.attach.bean.Attach;
import com.mingyue.base.bean.ReturnBean;
import com.mingyue.mingyue.config.Config;
import com.mingyue.attach.dao.AttachDao;
import com.mingyue.base.service.BaseService;
import io.netty.util.internal.StringUtil;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AttachServices extends BaseService<Attach,AttachDao> {

    @Autowired
    private AttachDao dao;

    @Override
    public AttachDao getDao() {
        return dao;
    }



    @Transactional(rollbackFor = {RuntimeException.class,Exception.class})
    public ReturnBean upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        // 获得文件：  FileParam fileParam = new FileParam();
//        logger.warn("xxx-> " + Config.upload);
        String fn = "fileToUpload";
        String subType = ServletRequestUtils.getStringParameter(request,"subType");
        Attach attach = null;
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            List<MultipartFile> files = multipartRequest.getFiles(fn);
            for (MultipartFile file : files) {

                File savePos = new File(Config.upload);
                if (!savePos.exists()) {  // 不存在，则创建该文件夹
                    savePos.mkdir();
                }
                String realPath = savePos.getCanonicalPath();
                // 上传该文件/图像至该文件夹下
                try {


                    String fileName = file.getOriginalFilename();
                    if (StringUtil.isNullOrEmpty(fileName))
                        throw new RuntimeException("fileName is null");
                    int i  = fileName.lastIndexOf(".");
                    String UUid = UUID.randomUUID().toString();
                    String type = StringUtils.hasText(fileName.substring(i)) ? fileName.substring(i).replaceFirst(".","") : "null";
                    String newFileName = UUid + fileName.substring(i); //改名

                    //创建附件数据
                    attach = new Attach();
                    attach.setName(fileName);
                    attach.setUuid(UUid);
                    attach.setPath(realPath);
                    attach.setType(type);
                    attach.setAttachSubType(subType);
                    create(attach);

                    file.transferTo(new File(realPath + "/" + newFileName)); //存储磁盘
                    logger.info("upload success");

                } catch (IOException e) { ;
                    response.getWriter().println("upload failed");
                    logger.warn("error->", e);
                    throw e;
                }
            }
        }
        else {
            throw new RuntimeException("没有检测到文件");
        }


        if (attach != null && StringUtils.hasText(attach.getUuid())) {
            return ReturnBean.ok("上传成功").setData(attach.getUuid());
        }
        return ReturnBean.error("上传失败");
    }


    public List<Attach> findBySubType(Map<String,?> params) {

        return getDao().findBySubType(params);

    }
}
