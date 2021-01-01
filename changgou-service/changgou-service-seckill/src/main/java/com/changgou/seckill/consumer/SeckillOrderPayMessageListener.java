package com.changgou.seckill.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
//@RabbitListener(queues = "${mq.pay.queue.seckillorder}")
public class SeckillOrderPayMessageListener {

    @Autowired
    private SeckillOrderService seckillOrderService;

    /**
     * 监听消费消息
     * @param message
     */
//    @RabbitHandler
    @RabbitListener(queues = "${mq.pay.queue.seckillorder}")
    public void consumeMessage(@Payload String message){
        System.out.println(message);
//        boolean flag = true;
//        if (flag) {
//            return;
//        }

        //将消息转换成Map对象
        Map<String,String> resultMap = JSON.parseObject(message,Map.class);
        System.out.println("监听到的消息:"+resultMap);

        String returnCode = resultMap.get("return_code");
        String resultCode = resultMap.get("result_code");

        if ("success".equalsIgnoreCase(returnCode)) {
            String outtradeno = resultMap.get("outtradeno");

            Map<String, String> attachMap = JSON.parseObject(resultMap.get("attach"), Map.class);

            if ("success".equalsIgnoreCase(resultCode)) {
                seckillOrderService.updatePayStatus(outtradeno, resultMap.get("transaction_id"), attachMap.get("username"));
            } else {
                // pay fail logic
                //支付失败,删除订单
                seckillOrderService.closeOrder(attachMap.get("username"));
            }
        }
    }
}
