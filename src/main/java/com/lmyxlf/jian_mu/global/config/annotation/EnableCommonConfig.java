package com.lmyxlf.jian_mu.global.config.annotation;

import com.lmyxlf.jian_mu.global.config.selector.CommonConfigImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/8 13:05
 * @description 导入通用配置
 * @since 17
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CommonConfigImportSelector.class)
public @interface EnableCommonConfig {

}