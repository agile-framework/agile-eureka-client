package com.agileframework.eurekaclientone.mvc.model.dao;

import com.agileframework.eurekaclientone.mvc.model.entity.SysUsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* Created by 佟盟
*/
public interface SysUsersRepository extends JpaRepository<SysUsersEntity,Integer> {

}
