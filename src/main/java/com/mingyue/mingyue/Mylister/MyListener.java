package com.mingyue.mingyue.Mylister;

import com.mingyue.mingyue.config.RabbitmqConfig;
import com.rabbitmq.client.Channel;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

@Component
public class MyListener extends AbstractAdaptableMessageListener {



    public Logger logger = Logger.getLogger(this.getClass());
    public static List<String> list = new LinkedList<>();


    @RabbitListener(queues = RabbitmqConfig.HUMAN_QUEUE)
    public void human(String msg){
        System.out.println("消费者运行："+msg);
    }

    @RabbitListener(queues = RabbitmqConfig.HAPPY_QUEUE)
    public void happy(String msg){
        System.out.println("消费者运行："+msg);
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);

            System.out.println("监听到消息:{" + msg + "} 通道信息：{" + message.getMessageProperties().getReceivedExchange() +"}");
            System.out.println("key:{" + message.getMessageProperties().getReceivedRoutingKey() + "}");
//            throw new RuntimeException("112233");
            System.out.println("ACK消息消费确认.....");

            // 消息确认 basicAck
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            //todo 业务处理
        } catch (Exception e) {
            System.out.println("消息处理失败");
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }

    //    1.6 RabbitMQ如何保证消费的顺序性 ?
//    一个队列只设置一个消费者消费即可 , 多个消费者之间是无法保证消息消费顺序性的
}
