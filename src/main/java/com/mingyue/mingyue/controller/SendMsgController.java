package com.mingyue.mingyue.controller;


import com.mingyue.mingyue.Mylister.MyListener;
import com.mingyue.mingyue.mq.RabbitmqConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * rabbitmq测试
 *
 * */
@Controller
@RequestMapping("send")
public class SendMsgController extends ThrowExceptionController {

    //注入rabbitmq模板
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private SimpleMessageListenerContainer container;

    @Autowired
    private MyListener listener;

    @GetMapping("/sendmsg")
    @ResponseBody
    public String sendMsg(@RequestParam String msg, @RequestParam String key) {
        /**
         * 发送消息
         *  参数1：交换机名称
         *  参数2：路由 key
         *  参数3：发送的消息
         */

        try {

//    CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
            rabbitTemplate.convertAndSend("xxTopicExchange", key, msg);
//    rabbitTemplate.convertAndSend(RabbitmqConfig.ITEM_TOPIC_EXCHANGE,key,msg,correlationData); 为单个消息单独设置过期时间

        } catch (Exception e) {
            logger.warn("error->", e);
            throw e;
        }
//返回消息
        return "发送消息成功";
    }


    /**
     * 动态创建消息通道
     */
    @RequestMapping("/created")
    @ResponseBody
    public String created() {

        try {


            Exchange exchange = ExchangeBuilder.topicExchange("xxTopicExchange").build();
//            System.err.println(exchange.getName());
            Queue queue = QueueBuilder.durable("xxQueue").build();
            Binding binding = BindingBuilder.bind(queue).to(exchange).with("xx.#").noargs();
            //*         可以代替一个单词。
            //#        可以代替零或多个单词
            amqpAdmin.declareQueue(queue);
            amqpAdmin.declareExchange(exchange);
            amqpAdmin.declareBinding(binding);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }


    @RequestMapping("/customer")
    @ResponseBody
    public String customer() {
        String name = RabbitmqConfig.ITEM_QUEUE;
        try {

            String[] listeners = container.getQueueNames();
//            System.err.println(JSON.toJSONString(listeners));
            boolean f = true;
            for (String quename : listeners) {
                if (quename.equals(name)) {
                    f = false;
                    break;
                }
            }
            if (f) {
                container.addQueueNames(name);
                container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
                //设置消息监听处理类
                container.setMessageListener(listener);
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("/getMsg")
    @ResponseBody
    public String getMsg() throws InterruptedException {
        // 手动获取一个通道的消息

        Message o;
        do {
           o = (Message) rabbitTemplate.receive(RabbitmqConfig.ITEM_QUEUE);

        } while (o != null);

        return "success";
    }



}
