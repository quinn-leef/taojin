package com.changgou.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class RabbitConfig {

    @Autowired
    private Environment env;

    //创建队列
    @Bean
    public Queue createQueue(){
        return new Queue(env.getProperty("mq.pay.queue.order"));
    }

    //创建交换机

    @Bean
    public DirectExchange basicExchanage(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.order"));
    }

    //绑定

    @Bean
    public Binding basicBinding(){
        return  BindingBuilder.bind(createQueue()).to(basicExchanage()).with(env.getProperty("mq.pay.routing.key"));
    }


    /////////////////////////////////////////////////////////////////////
    //创建秒杀队列
    @Bean(name="seckillQueue")
    public Queue createSeckillQueue(){
        return new Queue(env.getProperty("mq.pay.queue.seckillorder"));
    }

    //创建秒杀交换机

    @Bean(name="seckillExchanage")
    public DirectExchange basicSeckillExchanage(){
        return new DirectExchange(env.getProperty("mq.pay.exchange.seckillorder"));
    }

    //绑定秒杀

    @Bean(name="SeckillBinding")
    public Binding basicSeckillBinding(){
        return  BindingBuilder.bind(createSeckillQueue()).to(basicSeckillExchanage()).with(env.getProperty("mq.pay.routing.seckillkey"));
    }

    ///////////////////////////////////////////////////////
    /**
     * 到期数据队列
     * @return
     */
    @Bean
    public Queue seckillOrderTimerQueue() {
        return new Queue(env.getProperty("mq.pay.queue.seckillordertimer"), true);
    }

    /**
     * 超时数据队列
     * @return
     */
    @Bean
    public Queue delaySeckillOrderTimerQueue() {
        return QueueBuilder.durable(env.getProperty("mq.pay.queue.seckillordertimerdelay"))
                .withArgument("x-dead-letter-exchange", env.getProperty("mq.pay.exchange.order"))        // 消息超时进入死信队列，绑定死信队列交换机
                .withArgument("x-dead-letter-routing-key", env.getProperty("mq.pay.queue.seckillordertimer"))   // 绑定指定的routing-key
                .build();
    }

    /***
     * 交换机与队列绑定
     * @return
     */
    @Bean
    public Binding basicDelayBinding() {
        return BindingBuilder.bind(seckillOrderTimerQueue())
                .to(basicExchanage())
                .with(env.getProperty("mq.pay.queue.seckillordertimer"));
    }
}
