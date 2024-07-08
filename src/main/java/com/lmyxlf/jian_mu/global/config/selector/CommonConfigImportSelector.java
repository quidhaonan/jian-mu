package com.lmyxlf.jian_mu.global.config.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 通用配置导入选择器
 * @since 17
 */
public class CommonConfigImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                "com.lmyxlf.jian_mu.global.config.SpringConfig",
                "com.lmyxlf.jian_mu.global.config.SwaggerConfig",
                "com.lmyxlf.jian_mu.global.config.LogConfig",
        };
    }
}