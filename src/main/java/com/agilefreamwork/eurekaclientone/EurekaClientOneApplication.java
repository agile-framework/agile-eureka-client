package com.agilefreamwork.eurekaclientone;

import com.agilefreamwork.eurekaclientone.common.annotation.ExcludeComponentScan;
import com.agilefreamwork.eurekaclientone.common.configure.RibbonOfClientTwoConfig;
import com.agilefreamwork.eurekaclientone.common.configure.UploadSettings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
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
@EnableConfigurationProperties({UploadSettings.class})
//针对其他微服务的Ribbon负载组件配置
@RibbonClient(name = "client-two",configuration = RibbonOfClientTwoConfig.class)
//排除加载范围
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,value = ExcludeComponentScan.class)})
public class EurekaClientOneApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientOneApplication.class, args);
	}
}
