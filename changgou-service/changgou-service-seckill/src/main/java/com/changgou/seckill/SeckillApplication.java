package com.changgou.seckill;

import entity.IdWorker;
import entity.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

//@SpringBootApplication
@SpringBootApplication
@EnableEurekaClient
//@EnableFeignClients
//@MapperScan(basePackages = {"com.changgou.seckill.dao"})
@EnableFeignClients(basePackages = "com.changgou.pay.feign")
@MapperScan(value = "com.changgou.seckill.dao")
@EnableScheduling
@EnableAsync
//@EnableRabbit
public class SeckillApplication {


    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

    @Bean
    public TokenDecode tokenDecode() {
        return new TokenDecode();
    }
}