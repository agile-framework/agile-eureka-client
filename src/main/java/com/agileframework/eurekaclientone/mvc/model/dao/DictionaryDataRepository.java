package com.agileframework.eurekaclientone.mvc.model.dao;

import com.agileframework.eurekaclientone.mvc.model.entity.DictionaryDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* Created by 佟盟
*/
public interface DictionaryDataRepository extends JpaRepository<DictionaryDataEntity,Integer> {

}
