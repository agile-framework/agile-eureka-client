package com.agileframework.eurekaclientone.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 佟盟 on 2017/1/9
 */
public final class StringUtil extends StringUtils {
    /**
     * 特殊符号转驼峰式
     * @text text 任意字符串
     * @return 返回驼峰字符串
     */
    public static String signToCamel(String text){
        String regex = "((?=[\\x21-\\x7e]+)[^A-Za-z0-9])";
        if(ObjectUtil.isEmpty(getMatchedString(regex, text)))return text;
        
        text = text.toLowerCase();
        StringBuilder cacheStr = new StringBuilder(text);
        Matcher matcher = Pattern.compile(regex).matcher(text);
        int i = 0;
        while (matcher.find()){
            int position=matcher.end()-(i++);
            cacheStr.replace(position-1,position+1,cacheStr.substring(position,position+1).toUpperCase());
        }
        return cacheStr.toString();
    }

    /**
     * 字符串转首字母大写驼峰名
     * @text text 任意字符串
     * @return 返回首字母大写的驼峰字符串
     */
    public static String toUpperName(String text){
        if (isEmpty(text)) return "";
        String camelString = signToCamel(text);
        return camelString.substring(0,1).toUpperCase()+camelString.substring(1);
    }

    /**
     * 字符串转首字母小写驼峰名
     * @text text 任意字符串
     * @return 返回首字母小写的驼峰字符串
     */
    public static String toLowerName(String text){
        if (isEmpty(text)) return "";
        String camelString = signToCamel(text);
        return camelString.substring(0,1).toLowerCase()+camelString.substring(1);
    }

    /**
     * map格式转url参数路径
     * @text map 参数集合
     * @return url
     */
    public static String fromMapToUrl(HashMap<String,Object> map){
        StringBuffer mapOfString = new StringBuffer();
        for (HashMap.Entry<String, Object> entity : map.entrySet()) {
            mapOfString.append("&").append(entity.getKey());
            mapOfString.append("=").append(entity.getValue());
        }
        return String.valueOf(mapOfString);
    }

    /**
     * 字符串比较
     * @text resource 比较方
     * @text target 参照方
     * @return 是否相同
     */
    public static boolean compare(String resource, String target){
        return ObjectUtil.isEmpty(resource)?ObjectUtil.isEmpty(target):resource.equals(target);
    }

    /**
     * 获取字符串中匹配正则表达式的部分
     * @text regex 正则表达式
     * @text text 正文
     * @return 匹配的字符串
     */
    public static String getMatchedString(String regex,String text){
        return getMatchedString(regex,text,0);
    }

    /**
     * 获取字符串中匹配正则表达式的部分
     * @text regex 正则表达式
     * @text text 正文
     * @text index 第几组
     * @return 匹配的字符串
     */
    public static String getMatchedString(String regex,String text,int index){
        Pattern pattern=Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while(matcher.find()){
            return matcher.group(index);
        }
        return null;
    }
}
