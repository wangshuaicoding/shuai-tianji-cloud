package com.shuai.common.autoconfigure.swagger;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import javax.annotation.Resource;

@Configuration
@EnableSwagger2WebMvc
@ConditionalOnProperty(prefix = "tj.swagger", name = "enable",havingValue = "true")
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerConfiguration {

    @Resource
    private SwaggerProperties swaggerProperties;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        // 1.初始化Docket
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        // 2.是否需要包装R
        // if (swaggerProperties.getEnableResponseWrap()) {
        //     docket.additionalModels(typeResolver.resolve(R.class));
        // }
        return docket.apiInfo(new ApiInfoBuilder()
                        .title(swaggerProperties.getTitle())
                        .description(swaggerProperties.getDescription())
                        .contact(new Contact(
                                this.swaggerProperties.getContactName(),
                                this.swaggerProperties.getContactUrl(),
                                this.swaggerProperties.getContactEmail()))
                        .version(this.swaggerProperties.getVersion())
                        .build())
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getPackagePath()))
                .paths(PathSelectors.any())
                .build();
    }
}
