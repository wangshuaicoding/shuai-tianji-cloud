package com.shuai.api.config;

import com.shuai.api.client.course.fallback.CourseClientFallback;
import com.shuai.api.client.remark.fallback.RemarkClientFallback;
import com.shuai.api.client.user.fallback.UserClientFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FallbackConfig {

    @Bean
    public UserClientFallback userClientFallback(){
        return new UserClientFallback();
    }

    @Bean
    public CourseClientFallback courseClientFallback(){
        return new CourseClientFallback();
    }

    @Bean
    public RemarkClientFallback remarkClientFallback(){
        return new RemarkClientFallback();
    }

}
