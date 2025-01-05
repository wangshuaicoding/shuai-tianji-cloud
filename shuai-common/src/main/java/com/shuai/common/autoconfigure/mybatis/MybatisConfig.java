package com.shuai.common.autoconfigure.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// 这个注解主要用于确保某些类存在时才启用特定的配置或组件。
@ConditionalOnClass({MybatisPlusInterceptor.class, BaseMapper.class})
public class MybatisConfig {

    @Bean
    /**
     * 避免重复定义 Bean：如果你的应用中已经存在某个类型的 Bean（可能是通过其他配置类或自动配置），那么带有 @ConditionalOnMissingBean 的 Bean 将不会被创建。
     * 支持自定义扩展：允许开发者提供自己的实现，而不会被框架默认的实现覆盖。
     *
     * 由于DynamicTableNameInnerInterceptor并不是每一个微服务都用了，所以这里加入了@Autowired(required= false)，避免未定义该拦截器的微服务报错。
     */
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(
            @Autowired(required = false) DynamicTableNameInnerInterceptor innerInterceptor
            ) {
        // 1.定义插件主体，注意顺序: 表名 > 多租户 > 分页 > 乐观锁 > 字段填充
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        // 2.表名插件
        if (mybatisPlusInterceptor != null) {
            mybatisPlusInterceptor.addInnerInterceptor(innerInterceptor);
        }

        // 3.装配分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置分页最大返回记录数为200
        paginationInnerInterceptor.setMaxLimit(200L);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }

    /**
     * 实现字段填充
     * 通过自定义拦截器来实现自动注入createTime、updateTime、creater和updater
     * @return
     */
    @Bean
    public MetaObjectHandler defaultMetaObjectHandler() {
        // 自动填充参数类
        return new BaseMetaObjectHandler();
    }
}
