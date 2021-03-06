package com.agileframework.eurekaclientone.mvc.service;

import com.agileframework.eurekaclientone.common.base.RETURN;
import com.agileframework.eurekaclientone.common.server.MainService;
import com.agileframework.eurekaclientone.common.util.FactoryUtil;
import com.agileframework.eurekaclientone.common.util.JSONUtil;
import com.agileframework.eurekaclientone.common.util.ObjectUtil;
import com.agileframework.eurekaclientone.mvc.model.dao.DictionaryMainRepository;
import com.agileframework.eurekaclientone.mvc.model.entity.DictionaryMainEntity;
import com.agileframework.eurekaclientone.restInterfaces.ClientTwoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
* Created by 佟盟
*/
@Service
@Scope("prototype")
public class DictionaryMainService extends MainService {
    @Autowired
    private ClientTwoInterface clientTwoInterface;

    /**
     * 新增
     * 地址：http://localhost:8080/agile/DictionaryMainService/save
     */
    public RETURN save() {
        String a = clientTwoInterface.queryClientTwoInfo();
        this.setOutParam("c",JSONUtil.parse(a));
//        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
//        DictionaryMainEntity entity = ObjectUtil.getObjectFromMap(DictionaryMainEntity.class, this.getInParam());
//        if (entity.hashCode() == 0) return RETURN.PARAMETER_ERROR;
//        dao.save(entity);
        return RETURN.SUCCESS;
    }

    /**
     * 删除
     * 地址：http://localhost:8080/agile/DictionaryMainService/delete
     */
    public RETURN delete(){
//        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
//        if (this.containsKey("ids")){
//            String[] ids = this.getInParamOfString("ids").split(",");
//            for (int i = 0 ; i < ids.length ; i++) {
//                dao.deleteById((Integer) ObjectUtil.cast(Integer.class,ids[i].trim()));
//            }
            return RETURN.SUCCESS;
//        }
//        return RETURN.PARAMETER_ERROR;
    }

    /**
     * 修改
     * 地址：http://localhost:8080/agile/SysUsersService/update
     */
    public RETURN update() {
        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
        DictionaryMainEntity entity = ObjectUtil.getObjectFromMap(DictionaryMainEntity.class, this.getInParam());
        if (ObjectUtil.isEmpty(entity.getCode())) return RETURN.PARAMETER_ERROR;
        dao.saveAndFlush(entity);
        return RETURN.SUCCESS;
    }

    /**
     * 查询
     * 地址：http://localhost:8080/agile/DictionaryMainService/query
     */
    public RETURN query(){
        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
        this.setOutParam("queryList",dao.findAll(this.getPageInfo()));
        return RETURN.SUCCESS;
    }
}
