package com.agilefreamwork.eurekaclientone.restInterfaces;

import com.agilefreamwork.eurekaclientone.common.annotation.ExcludeComponentScan;
import feign.Contract;
import feign.Logger;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 佟盟 on 2017/8/27
 */
@Configuration
@ExcludeComponentScan
public class ClientTwoInterfaceConfigure {
    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}
