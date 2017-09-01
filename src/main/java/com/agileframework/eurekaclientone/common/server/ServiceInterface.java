package com.agileframework.eurekaclientone.common.server;

import com.agileframework.eurekaclientone.common.base.RETURN;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public interface ServiceInterface {
	//设置请求参数
	void setInParam(HashMap<String, Object> inParam);
	//提取响应参数
	HashMap<String, Object> getOutParam();
	//调用请求方法
	RETURN executeMethod(String methodName,Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,NoSuchMethodException, SecurityException;
}
