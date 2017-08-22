package com.agilefreamwork.eurekaclientone.common.base;

import com.agilefreamwork.eurekaclientone.common.util.ServletUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by 佟盟 on 2017/1/9
 */
public class Head implements Serializable {
    private static final long serialVersionUID = 97555324631150979L;
    private String url = "";
    private String ip = "";
    private String code = "";
    private String msg = "";

    public Head(RETURN returnState, HttpServletRequest request) {
        this.ip = ServletUtil.getCustomerIPAddr(request);
        this.url = request.getScheme() + "://" + ServletUtil.localhostFormat(request.getLocalAddr())+ ":" + request.getLocalPort() + request.getRequestURI();
        this.code = returnState.getCode();
        this.msg = returnState.getMsg();

//        Logger logger = LogManager.getLogger(this.getClass());
//        logger.info("\n" + "[接口调用][调用方]" + ip +"\n"
//                + "[接口调用][URL]" + url +"\n"
//                + "[接口调用][参数]" + request.getQueryString() +"\n"
//                + "[接口调用][响应]" + msg +"\n"
//        );
    }

    public String getUrl() {
        return url;
    }

    public String getIp() {
        return ip;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}