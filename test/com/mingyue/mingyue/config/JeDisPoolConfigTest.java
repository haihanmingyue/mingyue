package com.mingyue.mingyue.config;

import com.mingyue.mingyue.jedis.JeDisPoolConfig;
import com.mingyue.mingyue.mq.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.test.context.SpringBootTest;

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