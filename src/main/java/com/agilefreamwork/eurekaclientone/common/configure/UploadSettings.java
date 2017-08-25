package com.agilefreamwork.eurekaclientone.common.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 佟盟 on 2017/8/23
 */
@ConfigurationProperties(prefix = "agile.mvc.upload")
public class UploadSettings {

    /**
     * 最大上传数量
     */
    private int maxUploadSize = 204800;

    /**
     * 编码
     */
    private String defaultEncoding = "utf-8";

    public int getMaxUploadSize() {
        return maxUploadSize;
    }

    public void setMaxUploadSize(int maxUploadSize) {
        this.maxUploadSize = maxUploadSize;
    }

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }
}
