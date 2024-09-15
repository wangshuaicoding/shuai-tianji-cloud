package com.shuai.gateway.config;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@ConfigurationProperties(prefix = "shuai")
public class AuthProperties implements InitializingBean {

    private Set<String> excludePath;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 添加默认不拦截的路径
        excludePath.add("/error/**");
        excludePath.add("/jwks");
    }
}
