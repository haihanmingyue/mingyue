package com.mingyue.mingyue.service;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mingyue.mingyue.bean.UserAccount;
import com.mingyue.mingyue.dao.MethodInterFace;
import com.mingyue.mingyue.dao.TestDao;
import com.mingyue.mingyue.utils.MapUtil;
import io.netty.util.internal.StringUtil;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class TestService implements ApplicationContextAware {


    @Autowired
    private TestDao testDao;


    @Resource
    private JedisPool jedisPool;

    public TestService getTestService() {
        return this;
    }


    public TestDao getTestDao() {
        return this.testDao;
    }

    private Map<String,MethodInterFace> methodInterFaceMap = new HashMap<>();


    private final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("thread007-%d").build();
    /** 线程池 */
    private final ScheduledExecutorService eventNotifyScheduledThreadPool = new ScheduledThreadPoolExecutor(2, namedThreadFactory);

    static Integer number = 20;
    static boolean loop = true;
    static final String lock = "testlock";
    static LinkedList<String> list = new LinkedList<>();
    ReentrantLock reentrantLock = new ReentrantLock();

    CopyOnWriteArrayList collection = new CopyOnWriteArrayList();

    public PageInfo test(HttpServletRequest request) throws ServletRequestBindingException {
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(1,2);
        List<String> list = testDao.test(map);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }


    public PageInfo test() throws ServletRequestBindingException {
        Map<String,Object> map = new HashMap<>();
        PageHelper.startPage(1,2);
        List<String> list = testDao.test(map);
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Transactional(rollbackFor = {Exception.class,RuntimeException.class})
    public void insert(HttpServletRequest request) throws ServletRequestBindingException {

        for (int i = 0 ; i< 2000000; i++) {
            List<UserAccount> list = new LinkedList<>();
            UserAccount userAccount = new UserAccount();
            userAccount.setUuid(UUID.randomUUID().toString());
            userAccount.setUserName(userAccount.getUuid() + i);
            userAccount.setStatus((short) 0);
            userAccount.setDeleteStatus((short) 0);
            userAccount.setCreatedDate(new Date());
            userAccount.setUpdateDate(new Date());
            userAccount.setPassWord("123456");
            list.add(userAccount);
            try {
                System.err.println("插入中------");
                testDao.batchInsert(list);
                System.err.println("插入结束-----");
            }catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }

    }


    @Async
    public String redistest(HttpServletRequest request) throws ServletRequestBindingException {


        String method = ServletRequestUtils.getStringParameter(request,"method");
        if (StringUtil.isNullOrEmpty(method)) {
            throw new RuntimeException("方法不能为空");
        } else {
            Jedis jedis = jedisPool.getResource();
            switch (method) {
                case "add":
                    jedis.set("key","你好");
                    break;
                case "watch":
                    Object o = jedis.get("key");
                    return JSON.toJSONString(o);
                case "hash":

                    Thread thread = new Thread(() -> {
                        Jedis jedis1 = jedisPool.getResource();
                        PageHelper.startPage(1,1000000);
                        List<UserAccount> list1 = testDao.findByWhere(MapUtil.genMap());
                        PageInfo<UserAccount> l = new PageInfo<>(list1);
//                        Transaction transaction = jedis.multi(); //开启redis事务
                        for (UserAccount userAccount : l.getList()) {
                            String u = JSONObject.toJSONString(userAccount);

                            double d = Double.parseDouble(userAccount.getId()+"");

                            jedis1.zadd("useraccount", d, u );


                            System.err.println("写入jedis");
                        }
//                        transaction.exec();
                        System.err.println("success");
                    });

                    thread.start();
                    break;
            }
             return "success";
        }

    }

    public void print() throws InterruptedException {


        class th implements Runnable{
            @Override
            public void run() {
                if (collection.size() < 1) {
                    boolean f = collection.add("1");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        th th = new th();
        new Thread(th).start();
        new Thread(th).start();
        new Thread(th).start();
        Thread.sleep(2000);
        System.err.println(Thread.currentThread().getName() + " " + JSON.toJSONString(collection));
    }

    public static void main(String[] args) throws InterruptedException {


    }

    public String method(String initType) throws ServletRequestBindingException {
        MethodInterFace methodInterFace = methodInterFaceMap.get(initType);
        if (methodInterFace != null) {
            return methodInterFace.method(this);
        } else {
            return "no type";
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        Map<String, MethodInterFace> map = applicationContext.getBeansOfType(MethodInterFace.class);

        map.values().forEach(methodInterFace -> {
            methodInterFaceMap.put(methodInterFace.initType(),methodInterFace);
        });
        System.err.println(methodInterFaceMap.size());
    }

    public void testPrint() {
        System.err.println("112233");
    }

     /**
      *
      * 策略模式
      * if else 很多的时候用这个 ，看起来直观
      *
      * */


     @Component
     static class A implements MethodInterFace {


         @Override
         public String initType() {
             return "A";
         }

         @Override
         public String method(Object params) {

             return "A";
         }


     }


     @Component
     static class B implements MethodInterFace {

         @Override
         public String initType() {
             return "B";
         }


        public String method(Object params) throws ServletRequestBindingException {

             TestService testService = (TestService) params;
             return JSON.toJSONString(testService.test());
        }
    }

    public String getContent(){
      return   testDao.getContent();
    }

}
