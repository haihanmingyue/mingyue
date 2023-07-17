package com.mingyue.mingyue.Mylister;

import com.mingyue.mingyue.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MyListener {
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

//    1.6 RabbitMQ如何保证消费的顺序性 ?
//    一个队列只设置一个消费者消费即可 , 多个消费者之间是无法保证消息消费顺序性的
}
