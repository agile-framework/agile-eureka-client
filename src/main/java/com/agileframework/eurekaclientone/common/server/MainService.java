package com.agileframework.eurekaclientone.common.server;

import com.agileframework.eurekaclientone.common.configure.ExceptionHandler;
import com.agileframework.eurekaclientone.common.base.RETURN;
import com.agileframework.eurekaclientone.common.util.ObjectUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by 佟盟 on 2017/1/9
 */
public abstract class MainService extends ExceptionHandler implements ServiceInterface {
    //日志工具
    private Logger logger = LogManager.getLogger(this.getClass());
    //输入
    private HashMap<String, Object> inParam = new LinkedHashMap<>();
    //输出
    private HashMap<String, Object> outParam = new LinkedHashMap<>();

    /**
     * 根据对象及方法名通过反射执行该对象的指定方法
     * @param methodName 服务内部的具体方法名
     * @return 返回执行结果
     */
    public RETURN executeMethod(String methodName,Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return (RETURN) this.getClass().getDeclaredMethod(methodName).invoke(object);
    }
//    @Transactional
    protected RETURN execute(Method method) throws IllegalAccessException,IllegalArgumentException,InvocationTargetException,SecurityException{
        //取消安全检测，提高性能
        method.setAccessible(true);
        return (RETURN) method.invoke(this);
    }
    /**
     * 控制层中调用该方法设置服务入参
     * @param inParam 参数集
     */
    public final void setInParam(HashMap<String, Object> inParam) {
        this.inParam = inParam;
    }

    /**
     * 服务中调用该方法获取入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected Object getInParam(String key) {
        return inParam.get(key);
    }

    /**
     * 服务中调用该方法获取字符串入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected String getInParamOfString(String key) {
        return String.valueOf(inParam.get(key));
    }

    /**
     * 服务中调用该方法获取字符串入参
     * @param key 入参索引字符串
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected String getInParamOfString(String key,String defaultValue) {
        return containsKey(key)?String.valueOf(inParam.get(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取整数入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected int getInParamOfInteger(String key) {
        return Integer.parseInt(getInParamOfString(key));
    }

    /**
     * 服务中调用该方法获取整数入参
     * @param key 入参索引字符串
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected int getInParamOfInteger(String key,int defaultValue) {
        return containsKey(key)?Integer.parseInt(getInParamOfString(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取浮点入参
     * @param key 入参索引字符串
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected float getInParamOfFloat(String key,float defaultValue) {
        return containsKey(key)?Float.parseFloat(getInParamOfString(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取浮点入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected float getInParamOfFloat(String key) {
        return Float.parseFloat(getInParamOfString(key));
    }

    /**
     * 服务中调用该方法获取日期入参
     * @param key 入参索引字符串
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected Date getInParamOfDate(String key,Date defaultValue) {
        return containsKey(key)?Date.valueOf(getInParamOfString(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取日期入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected Date getInParamOfDate(String key) {
        return Date.valueOf(getInParamOfString(key));
    }

    /**
     * 服务中调用该方法获取长整形入参
     * @param key 入参索引字符串
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected long getInParamOfLong(String key,long defaultValue) {
        return containsKey(key)?Long.valueOf(getInParamOfString(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取长整形入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected long getInParamOfLong(String key) {
        return Long.valueOf(getInParamOfString(key));
    }

    /**
     * 服务中调用该方法获取布尔形入参
     * @param defaultValue 默认值
     * @return 入参值
     */
    protected boolean getInParamOfBoolean(String key,boolean defaultValue) {
        return containsKey(key)?Boolean.valueOf(getInParamOfString(key)):defaultValue;
    }

    /**
     * 服务中调用该方法获取布尔形入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected boolean getInParamOfBoolean(String key) {
        return Boolean.valueOf(getInParamOfString(key));
    }


    /**
     * 服务中调用该方判断是否存在入参
     * @param key 入参索引字符串
     * @return 入参值
     */
    protected boolean containsKey(String key) {
        return inParam.containsKey(key);
    }

    /**
     * 服务中调用该方法获取入参集合
     * @return 入参集合
     */
    protected HashMap<String, Object> getInParam() {
        return inParam;
    }

    /**
     * 控制层中调用该方法获取响应参数
     * @return 响应参数集
     */
    public HashMap<String, Object> getOutParam() {
        return this.outParam;
    }

    /**
     * 服务中调用该方法设置响应参数
     * @param key 参数索引字符串
     * @param value 参数值
     */
    public void setOutParam(String key, Object value) {
        if(ObjectUtil.isEmpty(this.outParam)){
            this.outParam = new HashMap<>();
        }
        this.outParam.put(key,value);
    }

    protected PageRequest getPageInfo(){
        int page = 1,size =10;

        if(this.containsKey("page")){
            page = this.getInParamOfInteger("page");
        }
        if(this.containsKey("size")){
            size = this.getInParamOfInteger("size");
        }
        return PageRequest.of(page,size);
    }

}
