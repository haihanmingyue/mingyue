package com.mingyue.base.controller;


import com.alibaba.fastjson2.JSON;
import com.documents4j.api.DocumentType;
import com.lowagie.text.DocumentException;
import com.mingyue.base.bean.UserAccount;
import com.mingyue.mingyue.config.Config;
import com.mingyue.base.service.PdfService;
import com.mingyue.base.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mingyue.base.service.UserAccountServices;
import com.mingyue.mingyue.utils.BaseContextUtils;
import com.mingyue.mingyue.utils.MapUtil;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * 测试一些方法用
 * */
@Controller
@RequestMapping("hello")
public class HellowController extends ThrowExceptionController implements ApplicationContextAware {
    @Autowired
    private Configuration configuration;

    @Autowired
    private UserAccountServices userAccountServices;

    @Autowired
    private TestService testService;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedissonClient redisson;

    @Autowired
    private PdfService pdfService;

    @RequestMapping("/hello")
    @ResponseBody
    public void hello(HttpServletRequest request,HttpServletResponse response) throws ServletRequestBindingException, IOException {
        response.getWriter().write("你好");
//        System.err.println(BaseContextUtils.getCurrentHumanId());
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
//        System.err.println("arr 耗时->" + (System.currentTimeMillis()  - time));
        time = System.currentTimeMillis();
        for (int i =0;i <100000;i++) {
            linkedList.add(i);
        }
//        System.err.println("link 耗时->" + (System.currentTimeMillis()  - time));
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
//
//            System.err.println(System.currentTimeMillis() - time);
//            System.err.print("/ms");
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




        return os;
    }

    @RequestMapping("/method")
    @ResponseBody
    public String method(HttpServletRequest request) throws ServletRequestBindingException {
        String k = testService.method(ServletRequestUtils.getStringParameter(request,"method"));
        return k;
    }



    @RequestMapping("/test")
//    @loggerInter(setClass = UserAccount.class)
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) UserAccount userAccount) throws ServletRequestBindingException {
        String s = ServletRequestUtils.getStringParameter(request,"key");


        return s+ "烦恼会" + userAccount.getUuid();

    }


//12 34 56    56 34 12


    public static void main(String[] args) {
       String s  = "";


//       Color color = Color.getColor(s);
//       if (color == null) {
//           return;
//       }
//        switch (color) {
//            case Red -> System.err.println(color.name);
//            case Green -> System.err.println(color.id);
//            case Yellow -> System.err.println(color.id + color.name);
//        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        jedisPool.getResource().setex("key",60000L,"123");
//        System.err.println(jedisPool.getResource().ttl("key"));
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


    @RequestMapping("getFile")
    @ResponseBody
     public Map getFile(HttpServletRequest request,HttpServletResponse response) {

        List<String> fileList = getFileNames(Config.upload);
        int total = fileList == null ? 0 : fileList.size();


        return MapUtil.genMap("code",0,"version",1.0,"data",MapUtil.genMap("rows",fileList,"total",total),"msg","success");
     }

    /**
     * 得到文件名称
     *
     * @param path 路径
     * @return {@link List}<{@link String}>
     */
    private static List<String> getFileNames(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        return getFileNames(file, fileNames);
    }

    /**
     * 得到文件名称
     *
     * @param file      文件
     * @param fileNames 文件名
     * @return {@link List}<{@link String}>
     */
    private static List<String> getFileNames(File file, List<String> fileNames) {
        if (file != null) {
            File[] files = file.listFiles() ;
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) { // 判断是不是文件夹
                        getFileNames(f, fileNames);
                    } else {
                        fileNames.add(f.getName());
                    }
                }
                return fileNames;
            }
            return null;
        }
        return null;
    }

}
