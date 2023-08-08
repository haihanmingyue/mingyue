package com.mingyue.mingyue.mq;


import com.rabbitmq.client.Consumer;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {
    public static final String ITEM_TOPIC_EXCHANGE = "item_topic_exchange";

    public static final String HUMAN_TOPIC_EXCHANGE = "human_topic_exchange";

    public static final String HAPPY_TOPIC_EXCHANGE = "happy_topic_exchange";
    //队列名称
    public static final String ITEM_QUEUE = "item_queue";

    public static final String HUMAN_QUEUE = "human_queue";

    public static final String HAPPY_QUEUE = "happy_queue";

//    //声明交换机
//    @Bean("itemTopicExchange")
//    public Exchange topicExchange(){
//        return ExchangeBuilder.topicExchange(ITEM_TOPIC_EXCHANGE).durable(true).build();
//    }
//    //声明队列
//    @Bean("itemQueue")
//    public Queue itemQueue(){
//        return QueueBuilder.durable(ITEM_QUEUE).build();
//    }
//
//
//    //声明交换机
//    @Bean("humanTopicExchange")
//    public Exchange humanTopicExchange(){
//        return ExchangeBuilder.topicExchange(HUMAN_TOPIC_EXCHANGE).durable(true).build();
//    }
//    @Bean("humanQueue")
//    public Queue humanQueue(){
//        return QueueBuilder.durable(HUMAN_QUEUE).build();
//    }
//
//    //声明交换机
//    @Bean("happyTopicExchange")
//    public Exchange happyTopicExchange(){
//        return ExchangeBuilder.topicExchange(HAPPY_TOPIC_EXCHANGE).durable(true).build();
//    }
//    @Bean("happyQueue")
//    public Queue happyQueue(){
//        return QueueBuilder.durable(HAPPY_QUEUE)
////                .ttl()过期时间 ms
////                .deadLetterExchange() //指定死信交换机
//                .build();
//    }
//
//
//
//    //绑定队列和交换机
//    @Bean
//    public Binding humanQueueExchange(@Qualifier("humanQueue") Queue queue,
//                                     @Qualifier("humanTopicExchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("human.#").noargs();
//    }
//
//    //绑定队列和交换机
//    @Bean
//    public Binding happyQueueExchange(@Qualifier("happyQueue") Queue queue,
//                                     @Qualifier("happyTopicExchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("happy.#").noargs();
//    }
//
//    //绑定队列和交换机
//    @Bean
//    public Binding itemQueueExchange(@Qualifier("itemQueue") Queue queue,
//                                     @Qualifier("itemTopicExchange") Exchange exchange){
//        return BindingBuilder.bind(queue).to(exchange).with("item.#").noargs();
//    }

//    简单模式：一个生产者，一个消费者
//    work模式：一个生产者，多个消费者，每个消费者获取到的消息唯一
//    订阅模式：一个生产者发送的消息会被多个消费者获取
//    路由模式： 发送消息到交换机并且要指定路由key ，消费者将队列绑定到交换机时需要指定路由key
//    topic模式：将路由键和某模式进行匹配，此时队列需要绑定在一个模式上，“#”匹配一个词或多个词，“*”只匹配一个词
//    RPC模式：使用RabbitMQ构建RPC系统：客户端和可伸缩RPC服务器
//    发布确认：与发布者进行可靠的发布确认


//    选型：发布订阅模式（Publish/Subscribe）
//
//    一个生产者，多个消费者，每一个消费者都有自己的一个队列，生产者没有将消息直接发送到队列，而是发送到了交换机，每个队列绑定交换机，生产者发送的消息经过交换机，到达队列，实现一个消息被多个消费者获取的目的。


    /**
     * 用于动态添加消费者
     * 简单消息监听器
     *
     * */
    @Bean
    public SimpleMessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        return new SimpleMessageListenerContainer(connectionFactory);
    }


}
