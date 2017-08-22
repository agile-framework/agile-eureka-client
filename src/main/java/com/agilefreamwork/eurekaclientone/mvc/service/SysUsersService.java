package com.agilefreamwork.eurekaclientone.mvc.service;

import com.agilefreamwork.eurekaclientone.common.base.RETURN;
import com.agilefreamwork.eurekaclientone.common.server.MainService;
import com.agilefreamwork.eurekaclientone.common.util.FactoryUtil;
import org.springframework.stereotype.Service;

/**
* Created by 佟盟
*/
@Service
public class SysUsersService extends MainService {

    /**
     * 新增
     * 地址：http://localhost:8080/agile/SysUsersService/save
     */
    public RETURN save() {
        return RETURN.SUCCESS;
    }
}
