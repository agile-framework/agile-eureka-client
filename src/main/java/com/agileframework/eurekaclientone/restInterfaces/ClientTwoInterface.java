package com.agileframework.eurekaclientone.restInterfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 其他平台开发能力
 * Created by 佟盟 on 2017/8/27
 */
@FeignClient(name = "client-two",fallbackFactory = ClientTwoInterfaceFallbackFactory.class)
public interface ClientTwoInterface {
    @RequestMapping(value = "/client-two/DictionaryMainService/query",method = RequestMethod.GET)
    String homePageUrl();
}
