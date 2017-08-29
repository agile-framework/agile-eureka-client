package com.agilefreamwork.eurekaclientone.mvc.service;

import com.agilefreamwork.eurekaclientone.common.base.RETURN;
import com.agilefreamwork.eurekaclientone.common.server.MainService;
import com.agilefreamwork.eurekaclientone.common.util.FactoryUtil;
import com.agilefreamwork.eurekaclientone.common.util.JSONUtil;
import com.agilefreamwork.eurekaclientone.common.util.ObjectUtil;
import com.agilefreamwork.eurekaclientone.mvc.model.dao.DictionaryMainRepository;
import com.agilefreamwork.eurekaclientone.mvc.model.entity.DictionaryMainEntity;
import com.agilefreamwork.eurekaclientone.restInterfaces.ClientTwoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;


/**
* Created by 佟盟
*/
@Service
public class DictionaryMainService extends MainService {
    @Autowired
    private ClientTwoInterface clientTwoInterface;

    /**
     * 新增
     * 地址：http://localhost:8080/agile/DictionaryMainService/save
     */
    public RETURN save() {
        String a = clientTwoInterface.homePageUrl();
        this.setOutParam("a",JSONUtil.parse(a));
        return RETURN.SUCCESS;
//        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
//        DictionaryMainEntity entity = ObjectUtil.getObjectFromMap(DictionaryMainEntity.class, this.getInParam());
//        if (entity.hashCode() == 0) return RETURN.PARAMETER_ERROR;
//        dao.save(entity);
//        return RETURN.SUCCESS;
    }

    /**
     * 删除
     * 地址：http://localhost:8080/agile/DictionaryMainService/delete
     */
    public RETURN delete(){
        DictionaryMainRepository dao = FactoryUtil.getBean(DictionaryMainRepository.class);
        if (this.containsKey("ids")){
            String[] ids = this.getInParamOfString("ids").split(",");
            for (int i = 0 ; i < ids.length ; i++) {
                dao.delete((Integer) ObjectUtil.cast(Integer.class,ids[i].trim()));
            }
            return RETURN.SUCCESS;
        }
        return RETURN.PARAMETER_ERROR;
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
