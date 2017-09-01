package com.agileframework.eurekaclientone.mvc.model.dao;

import com.agileframework.eurekaclientone.mvc.model.entity.LogValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* Created by 佟盟
*/
public interface LogValueRepository extends JpaRepository<LogValueEntity,Integer> {

}
