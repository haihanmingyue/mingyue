package com.mingyue.mingyue.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mingyue.mingyue.bean.AttachBean;
import com.mingyue.mingyue.bean.AttachSubType;
import com.mingyue.mingyue.bean.ReturnBean;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.config.Config;
import com.mingyue.mingyue.service.AttachServices;
import com.mingyue.mingyue.service.UserAccountServices;
import com.mingyue.mingyue.utils.MapUtil;
import com.mingyue.mingyue.utils.SetContentTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("attach")
public class AttachController extends BaseController{



    @Autowired
    private AttachServices attachServices;

    @RequestMapping("/upload")
    @ResponseBody
    public ReturnBean upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        return attachServices.upload(request,response);
    }


    @RequestMapping("/download")
    public void download(@RequestParam(name = "uuid") String uuid, HttpServletResponse response) throws IOException {

        AttachBean attachBean = attachServices.get(uuid);

        if (attachBean == null) {
            throw new RemoteException("附件不存在");
        }

        File file = new File(attachBean.getPath() + "/" + attachBean.getUuid() + "." + attachBean.getType());
        String fileName = file.getName();


        response.setContentType(SetContentTypeUtil.map.get(attachBean.getType()));
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,  "attachment;filename="+ fileName);

        response.addHeader(HttpHeaders.CONTENT_LENGTH, "" + file.length());
        byte[] bytes = new byte[4096];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            OutputStream outputStream = response.getOutputStream();
            int len;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (Exception e) {
            logger.error("error->", e);
            throw e;
        }
    }


    @RequestMapping("/open")
    public void open(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException {
        String uuid = ServletRequestUtils.getStringParameter(request,"uuid");


        AttachBean attachBean = attachServices.get(uuid);

        if (attachBean == null) {
            throw new RemoteException("附件不存在");
        }



        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            File file = new File(attachBean.getPath() + "/" + attachBean.getUuid() + "." + attachBean.getType());
            String fileName = file.getName();
            response.setContentType(SetContentTypeUtil.map.get(attachBean.getType()));
            outputStream = response.getOutputStream();
            response.reset();
            //获取请求头中Range的值
            String rangeString = request.getHeader(HttpHeaders.RANGE);

            //打开文件
            if (file.exists()) {
                //使用RandomAccessFile读取文件
                targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                long requestSize = fileLength;
                //分段下载视频
                if (StringUtils.hasText(rangeString)) {
                    //从Range中提取需要获取数据的开始和结束位置
                    long requestStart = 0, requestEnd = 0;
                    String[] ranges = rangeString.split("=");
                    if (ranges.length > 1) {
                        String[] rangeDatas = ranges[1].split("-");
                        requestStart = Integer.parseInt(rangeDatas[0]);
                        if (rangeDatas.length > 1) {
                            requestEnd = Integer.parseInt(rangeDatas[1]);
                        }
                    }
                    if (requestEnd != 0 && requestEnd > requestStart) {
                        requestSize = requestEnd - requestStart + 1;
                    }
                    //根据协议设置请求头
                    response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
                    response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");
                    if (!StringUtils.hasText(rangeString)) {
                        response.setHeader(HttpHeaders.CONTENT_LENGTH, fileLength + "");
                    } else {
                        long length;
                        if (requestEnd > 0) {
                            length = requestEnd - requestStart + 1;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + requestEnd + "/" + fileLength);
                        } else {
                            length = fileLength - requestStart;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + (fileLength - 1) + "/"
                                    + fileLength);
                        }
                    }
                    //断点传输下载视频返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(requestStart);
                } else {
                    //如果Range为空则下载整个视频
                    response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+ fileName);
                    //设置文件长度
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                }

                //从磁盘读取数据流返回
                byte[] cache = new byte[4096];
                try {
                    while (requestSize > 0) {
                        int len = targetFile.read(cache);
                        if (requestSize < cache.length) {
                            outputStream.write(cache, 0, (int) requestSize);
                        } else {
                            outputStream.write(cache, 0, len);
                            if (len < cache.length) {
                                break;
                            }
                        }
                        requestSize -= cache.length;
                    }
                } catch (IOException e) {
                    // tomcat原话。写操作IO异常几乎总是由于客户端主动关闭连接导致，所以直接吃掉异常打日志
                    //比如使用video播放视频时经常会发送Range为0- 的范围只是为了获取视频大小，之后就中断连接了
                    logger.info(e.getMessage());
                }
            } else {
                throw new RuntimeException("文件路劲有误");
            }
            outputStream.flush();
        } catch (Exception e) {
            logger.error("文件传输错误", e);
            throw new RuntimeException("文件传输错误");
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("流释放错误", e);
                }
            }
            if(targetFile != null){
                try {
                    targetFile.close();
                } catch (IOException e) {
                    logger.error("文件流释放错误", e);
                }
            }
        }

    }



    @RequestMapping("/list")
    @ResponseBody
    public ReturnBean list(HttpServletRequest request) {
        Map<String,String> map = MapUtil.getRequestParamsMap(request);
        PageHelper.startPage(1,999);
        List<AttachBean> list =  attachServices.findByWhere(map);
        PageInfo pageInfo = new PageInfo(list);
        return ReturnBean.ok("查询成功").setData("success").setData(MapUtil.genMap("rows",pageInfo.getList(),"total",pageInfo.getTotal()));
    }

    @RequestMapping("/findBySubType")
    @ResponseBody
    public ReturnBean findBySubType(HttpServletRequest request) {
        Map<String,String> map = MapUtil.getRequestParamsMap(request);
        PageHelper.startPage(1,999);
        List<AttachBean> list =  attachServices.findBySubType(map);
        PageInfo pageInfo = new PageInfo(list);
        return ReturnBean.ok("查询成功").setData("success").setData(MapUtil.genMap("rows",pageInfo.getList(),"total",pageInfo.getTotal()));
    }

}
