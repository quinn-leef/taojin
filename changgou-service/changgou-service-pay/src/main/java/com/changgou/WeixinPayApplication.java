package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 描述
 *
 * @author quinn
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
public class WeixinPayApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeixinPayApplication.class,args);
    }


//    @Autowired
//    private Environment env;
//
//    //创建队列
//    @Bean
//    public Queue createQueue(){
//        return new Queue(env.getProperty("mq.pay.queue.order"));
//    }
//
//    //创建交换机
//
//    @Bean
//    public DirectExchange basicExchanage(){
//        return new DirectExchange(env.getProperty("mq.pay.exchange.order"));
//    }
//
//    //绑定
//
//    @Bean
//    public Binding basicBinding(){
//        return  BindingBuilder.bind(createQueue()).to(basicExchanage()).with(env.getProperty("mq.pay.routing.key"));
//    }

//    public final static String SFG_MESSAGE_QUEUE = "sfg-message-queue";
//
//    @Bean
//    Queue queue() {
//        return new Queue(SFG_MESSAGE_QUEUE, false);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange("spring-boot-exchange");
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(SFG_MESSAGE_QUEUE);
//    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
//                                             MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(SFG_MESSAGE_QUEUE);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(ProductMessageListener receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }


}
