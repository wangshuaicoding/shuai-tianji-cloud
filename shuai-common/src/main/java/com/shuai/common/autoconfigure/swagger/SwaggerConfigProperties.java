package com.shuai.common.autoconfigure.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 *  添加 Swagger 属性配置类，用于封装 Swagger 配置参数
 **/
@Data
@ConfigurationProperties(prefix = "shuai.swagger")
public class SwaggerConfigProperties implements Serializable {

    private Boolean enable = false;
    private Boolean enableResponseWrap = false;

    public String packagePath;

    public String title;

    public String description;

    public String contactName;

    public String contactUrl;

    public String contactEmail;

    public String version;
}
