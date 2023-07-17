package com.mingyue.mingyue.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JeDisPoolConfigTest extends JeDisPoolConfig {

    @RabbitListener(queues = RabbitmqConfig.ITEM_QUEUE)
    public void msg(String msg){
        System.out.println("消费者运行："+msg);
    }

    @RabbitListener(queues = RabbitmqConfig.HUMAN_QUEUE)
    public void human(String msg){
        System.out.println("消费者运行："+msg);
    }

    @RabbitListener(queues = RabbitmqConfig.HAPPY_QUEUE)
    public void happy(String msg){
        System.out.println("消费者运行："+msg);
    }

    @Test
    public void test()  {


    }
}