package com.agilefreamwork.eurekaclientone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
//开启Eureka客户端
@EnableEurekaClient
//开启配置注解
@Configuration
//开启自动识别注解
@ComponentScan
//开启自动配置
@EnableAutoConfiguration
public class EurekaClientOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientOneApplication.class, args);
	}
}
