package com.shuai.common.autoconfigure.mvc;

import com.shuai.common.utils.AuthUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class AuthAutoConfiguration {

    @Bean
    public AuthUtil authUtil(StringRedisTemplate stringRedisTemplate){
        return new AuthUtil(stringRedisTemplate);
    }
}
