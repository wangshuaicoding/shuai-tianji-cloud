package com.shuai.common.autoconfigure.xxljob;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
// 用于将配置文件中的属性绑定到Java类字段上
@ConfigurationProperties(prefix = "shuai.xxl-job")
public class XxlJobProperties {
    /**
     * 访问令牌，用于身份验证
     * 这个字段通常用于确保 调度中心与执行器之间的通信是安全的，他们之间进行通信需要提供该令牌进行身份验证
     * 如果不设置或者为空，则表示不启动访问令牌验证
     */
    private String accessToken;
    /**
     * 调度中心的相关配置信息
     */
    private Admin admin;
    /**
    * 执行器的相关配置信息
    */
    private Executor executor;

    /**
     * 专门处理与调度中心相关的配置
     * 静态内部类
     *      1、代码结构清晰
     *      2、提高了可读性
     *      3、增强模块化
     */
    @Data
    public static class Admin {
        private String address;
    }

    /**
     * 专注于执行器的配置
     */
    @Data
    public static class Executor {
        /**
         * 表示的是当前的执行器的命名，方便后续在调度中心对执行器的选取。
         */
        private String appName;
        /**
         * 表示的是 XXL-JOB 服务器，也就是调度中心的地址。
         */
        private String address;
        /**
         * 表示当前执行器的 IP 地址，主要作用是：给调用中心发送信息给执行器所用到的 IP 地址
         */
        private String ip;
        /**
         * 表示当前执行器的端口，主要作用是：给调用中心发送信息给执行器所用到的端口
         */
        private Integer port;
        /**
         * 日志地址
         */
        private String logPath;
        /**
         * 日志保留天数
         */
        private Integer logRetentionDays;

    }
}