package com.mingyue.mingyue.controller;


import com.alibaba.fastjson2.JSON;
import com.documents4j.api.DocumentType;
import com.lowagie.text.DocumentException;
import com.mingyue.mingyue.bean.ChildRenFactory3;
import com.mingyue.mingyue.config.Config;
import com.mingyue.mingyue.service.PdfService;
import com.mingyue.mingyue.service.TestService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mingyue.mingyue.utils.MapUtil;
import com.mingyue.mingyue.utils.SetContentTypeUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequestMapping("hello")
public class HellowController extends BaseController{
    @Autowired
    private Configuration configuration;

    @Autowired
    private TestService testService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private PdfService pdfService;


    @RequestMapping("/upload")
    @ResponseBody
    public void hellow(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException {
        // 获得文件：  FileParam fileParam = new FileParam();
        logger.warn("xxx-> " + Config.upload);
        String fn = "fileToUpload";
        if (request instanceof MultipartHttpServletRequest multipartRequest) {
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
                    String newFileName = UUid + fileName.substring(i);
                    System.err.println(newFileName);
                    file.transferTo(new File(realPath + "/" + newFileName));

                    logger.info("upload success");
                    response.getWriter().println("upload success fileId-> " + UUid);
                } catch (IOException e) { ;
                    response.getWriter().println("upload failed");
                    logger.warn("error->", e);
                    throw e;
                }
            }
        }
    }

    @RequestMapping("/download")
    public void getP(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException {
       String name = ServletRequestUtils.getStringParameter(request,"uuid");
       String type = ServletRequestUtils.getStringParameter(request,"type");
       String open = ServletRequestUtils.getStringParameter(request,"open");

       if(open == null) {
           open = "inline";
       } else {
           if (!open.equals("inline") && !open.equals("attachment")) {
               open = "inline";
           }
       }
       File file = new File(Config.upload + "/" + name + "." + type);
       String fileName = file.getName();


        response.setContentType(SetContentTypeUtil.map.get(type));

        response.addHeader("Content-Length", "" + file.length());
        response.setHeader("Content-Disposition", open +";fileName="+ fileName);//在线查看
//        response.setHeader("Content-Disposition", "attachment;fileName="+ fileName);//下载

       logger.warn("fileName->" + fileName);
       byte[] bytes = new byte[1024];
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


    @RequestMapping("/insert")
    @ResponseBody
    public void insert(HttpServletRequest request) throws ServletRequestBindingException {
        testService.insert(request);
    }

    @RequestMapping("/redistest")
    @ResponseBody
    public String redistest(HttpServletRequest request) throws ServletRequestBindingException {
        String k = testService.redistest(request);
        return k;
    }


    @RequestMapping("/bytes")
    @ResponseBody
    public void bytes(HttpServletRequest request, HttpServletResponse response) throws ServletRequestBindingException, IOException {
        String k = "112233";

        byte[] bytes = k.getBytes(StandardCharsets.UTF_8);
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);

        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        Long time = System.currentTimeMillis();
        for (int i =0;i <100000;i++) {
            arrayList.add(i);
        }
        System.err.println("arr 耗时->" + (System.currentTimeMillis()  - time));
        time = System.currentTimeMillis();
        for (int i =0;i <100000;i++) {
            linkedList.add(i);
        }
        System.err.println("link 耗时->" + (System.currentTimeMillis()  - time));
    }

    @RequestMapping("/findByWhere")
    @ResponseBody
    public String findByWhere(HttpServletRequest request) throws ServletRequestBindingException {

        try {
            Long time = System.currentTimeMillis();
            Integer page = ServletRequestUtils.getIntParameter(request,"page");
            Integer rows = ServletRequestUtils.getIntParameter(request,"rows");
            if (page == null || page < 0) {
                page = 0;
            } else {
                page--;
            }
            if (rows == null) {
                rows = 10;
            }

            Integer offset = page * rows;

            Jedis jedis = jedisPool.getResource();
            Map map = new HashMap();
            Set<String> strings = jedis.zrevrangeByScore(
                    "useraccount", // list的key

                    String.valueOf(Double.MAX_VALUE), // source最大值默认给double最大值就完事了
                    "1", // source最小值，这两个给定查询范围

                    offset, //偏移量 就是第几页，
                    rows  // 每页多少个
            );
            Long total = jedis.zcard("useraccount"); //总量


            map.put("version", "1.0");
            map.put("rows", strings);
            map.put("total", total);
            map.put("code",200);

            System.err.println(System.currentTimeMillis() - time);
            System.err.print("/ms");
            jedis.close();
            return JSON.toJSONString(map);
        }catch (Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }

    /**
     * 转word可以直接word xml填充
     *
     * 转 pdf 还是得html，而且目前中文不支持，显示空白
     *
     * word转pdf没有涉及填充的话可以转，涉及填充再研究
     *
     * */
    @RequestMapping("/pdf")
    public void pdf(HttpServletRequest request,HttpServletResponse response) throws TemplateException, DocumentException, IOException {

        try {


            String templateString;

            templateString = testService.getContent();


            List<Map<String,Object>> list = new LinkedList<>();
            list.add(MapUtil.genMap("a","1","b","2","c","3","d","4","e","书厮守","f","我开房"));
            list.add(MapUtil.genMap("a","11","b","22","c","33","d","44","e","嗯嗯","f","哦无痕大神"));
            Map map = new HashMap();
            map.put("list",list);

//            pdfService.html2Word(templateString,list, DocumentType.DOC,response,"htmlToDoc");



            pdfService.wordFlt2Pdf(templateString,map,DocumentType.DOC,response,"pdf");

//            InputStream in = new ByteArrayInputStream();

//            String str = stringWriter.toString(); //导出pdf就new一个writer
//            ITextRenderer renderer = new ITextRenderer(); //ITextRenderer 只支持html转pdf 且不支持中文,还得特殊处理

            // step 3 解决中文支持
//            ITextFontResolver fontResolver = renderer.getFontResolver();
//            fontResolver.addFont();

//
//            renderer.setDocumentFromString(stringWriter.toString());
//            renderer.layout();
//            renderer.createPDF(response.getOutputStream());
//            renderer.finishPDF();


        } catch (Exception e) {
            logger.error("导出word异常：", e);
        }

    }
    public  String getCurrentOperatingSystem(){
        String os = System.getProperty("os.name").toLowerCase();
        logger.info("---------当前操作系统是-----------" + os);


        ChildRenFactory3 childRenFactory3 = new ChildRenFactory3();

        ChildRenFactory3 childRenFactory31 = new ChildRenFactory3();

        Boolean f = childRenFactory3.equals(childRenFactory31);

        return os;
    }

    @RequestMapping("/method")
    @ResponseBody
    public String method(HttpServletRequest request) throws ServletRequestBindingException {
        String k = testService.method(ServletRequestUtils.getStringParameter(request,"method"));
        return k;
    }
//12 34 56    56 34 12


    public static void main(String[] args) {
       String s  = "";


       Color color = Color.getColor(s);
       if (color == null) {
           return;
       }
        switch (color) {
            case Red -> System.err.println(color.name);
            case Green -> System.err.println(color.id);
            case Yellow -> System.err.println(color.id + color.name);
        }
    }

    enum Color{
        Green("green", 0),
        Yellow("yellow",1),
        Red("red",2);

        // 初始化的时候,每一个枚举类都会对应一个唯一的id，第一个是0，依次递增，switch就是用这个来判断枚举
        //其实是这样

        Color(String name, int id) {
            this.name = name;
            this.id = id;
        }

        private String name;
        private Integer id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public static Color getColor(String name) {
           Color[] colors = Color.values();
            for (Color color : colors) {
                if (color.name.equals(name))
                    return color;
            }
           return null;
        }
    }



}