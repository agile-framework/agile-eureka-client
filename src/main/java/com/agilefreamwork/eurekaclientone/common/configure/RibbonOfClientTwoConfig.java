package com.agilefreamwork.eurekaclientone.common.configure;

import com.agilefreamwork.eurekaclientone.common.annotation.ExcludeComponentScan;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置ribbon负载算法
 * Random随机/RoundRobinRule轮询
 * Created by 佟盟 on 2017/8/25
 */
@Configuration
@ExcludeComponentScan
public class RibbonOfClientTwoConfig {
    @Bean
    public IRule ribbonRule(){
        return new RandomRule();
    }
}
