package com.agileframework.eurekaclientone;

import com.agileframework.eurekaclientone.common.annotation.ExcludeComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
//开启Eureka客户端
@EnableEurekaClient
//开启配置注解
@Configuration
//开启自动识别注解
@ComponentScan
//开启自动配置
@EnableAutoConfiguration
//开启自定义系统属性配置
@EnableConfigurationProperties({JpaProperties.class})
//排除加载范围
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = ExcludeComponentScan.class)})
//扫面servlet注解
@ServletComponentScan
//开启缓存功能
@EnableCaching
//开启feign组件
@EnableFeignClients
//开启Hystirx
@EnableCircuitBreaker
//开启刷新功能
@RefreshScope
public class EurekaClientOneApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaClientOneApplication.class, args);
	}
}
