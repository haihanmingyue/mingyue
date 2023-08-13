package com.mingyue.sendmsg.services;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Service
public class EmailService {

    public Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private JedisPool jedisPool;

    public void getCode(HttpServletRequest request) throws ServletRequestBindingException {

        String email = ServletRequestUtils.getRequiredStringParameter(request,"email");

        StringBuilder code = new StringBuilder();

        //获取httpSession
        HttpSession session = request.getSession();

        //拼接随机四个整数
        Random random = new Random();
        while (code.length() < 4) {
            code.append(random.nextInt(10));
        }

        Jedis jedis = jedisPool.getResource();
        // 有效期5分钟
        jedis.setex(session.getId(),(1000L * 60 * 5),code.toString());

    }

    private void sendEmail() {

    }

}
