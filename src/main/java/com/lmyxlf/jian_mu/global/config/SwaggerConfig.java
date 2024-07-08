package com.lmyxlf.jian_mu.global.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 开启 swagger2
 * @since 17
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger", name = "enabled", havingValue = "true", matchIfMissing = false)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public Docket createRestApi() {
        HashSet<String> produces = new HashSet<>(5);
        produces.add("application/json;charset=UTF-8");

        return new Docket(DocumentationType.SWAGGER_2)
                .produces(produces)
                .host("lmyxlf.com")
                .apiInfo(apiInfo())
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.lmyxlf.jian_mu"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(globalOperationParameters());
    }

    /**
     * 公共参数
     *
     * @return
     */
    private List<Parameter> globalOperationParameters() {
        List<Parameter> parameters = new ArrayList<>();

        Parameter rt = new ParameterBuilder()
                .name("_rt")
                .defaultValue("1720445725224")
                .description("当前请求时间戳，毫秒值，每个请求都需要重新⽣成")
                .modelRef(new ModelRef("Long"))
                .parameterType("query")
                .order(-30)
                .required(true).build();

        parameters.add(rt);
        return parameters;
    }

    /**
     * 构建 api 文档的详细信息函数
     *
     * @return ApiInfo
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 页面标题
                .title("lmyxlf api 文档")
                .version("1.0")
                // 描述
                .description("lmyxlf api 文档")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        // 添加 swagger 自带文档页面
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        // 添加第三方提供的 swagger-ui 页面
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}