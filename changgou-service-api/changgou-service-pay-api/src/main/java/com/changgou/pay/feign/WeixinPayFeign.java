package com.changgou.pay.feign;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name="weixinpay")
@RequestMapping("/weixin/pay")
public interface WeixinPayFeign {

    @GetMapping(value = "/closeOrder")
    Result closePay(Long orderId);



}
