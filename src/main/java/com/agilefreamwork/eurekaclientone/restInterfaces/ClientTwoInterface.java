package com.agilefreamwork.eurekaclientone.restInterfaces;

import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * 其他平台开发能力
 * Created by 佟盟 on 2017/8/27
 */
@FeignClient(name = "client-two",configuration = ClientTwoInterfaceConfigure.class)
public interface ClientTwoInterface {

}
