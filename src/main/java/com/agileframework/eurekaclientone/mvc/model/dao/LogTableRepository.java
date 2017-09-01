package com.agileframework.eurekaclientone.mvc.model.dao;

import com.agileframework.eurekaclientone.mvc.model.entity.LogTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* Created by 佟盟
*/
public interface LogTableRepository extends JpaRepository<LogTableEntity,Integer> {

}
