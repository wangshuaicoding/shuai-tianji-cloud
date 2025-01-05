package com.shuai.common.autoconfigure.xxljob;

import com.shuai.common.utils.StringUtils;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnClass(XxlJobSpringExecutor.class)
// 这个注解用于显式地将带有@ConfigurationProperties注解的类注册为 Spring 的Bean,即加入到IOC容器中
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobConfig {

    /**
     * XxlJobSpringExecutor : 这是一个用于管理 XXL-JOB 任务调度的执行器类
     * @param prop
     * @return
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties prop) {
        log.info(">>>>>>>>>>> xxl-job config init.>>>>>>>>>>>>");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        XxlJobProperties.Admin admin = prop.getAdmin();
        if (admin != null && StringUtils.isNotEmpty(admin.getAddress())) {
            xxlJobSpringExecutor.setAdminAddresses(admin.getAddress());
        }
        XxlJobProperties.Executor executor = prop.getExecutor();
        if (executor != null) {
            if (executor.getAppName() != null)
                xxlJobSpringExecutor.setAppname(executor.getAppName());
            if (executor.getIp() != null)
                xxlJobSpringExecutor.setIp(executor.getIp());
            if (executor.getPort() != null)
                xxlJobSpringExecutor.setPort(executor.getPort());
            if (executor.getLogPath() != null)
                xxlJobSpringExecutor.setLogPath(executor.getLogPath());
            if (executor.getLogRetentionDays() != null)
                xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        }
        if (prop.getAccessToken() != null)
            xxlJobSpringExecutor.setAccessToken(prop.getAccessToken());
        log.info(">>>>>>>>>>> xxl-job config end.>>>>>>>>>>>>>>");
        return xxlJobSpringExecutor;
    }
}
