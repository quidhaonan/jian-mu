package com.lmyxlf.jian_mu.global.mvc;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.lmyxlf.jian_mu.global.annotation.IgnoreAuthUrlAnnotation;
import com.lmyxlf.jian_mu.global.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/11/26 1:27
 * @description 获得允许匿名访问的请求路径，将其放入 Security 配置中使其生效
 * @since 17
 */
@Component
public class IgnoreAuthUrlProcessor {

    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {

        IgnoreAuthUrlProcessor.environment = environment;
    }

    public static List<String> getIgnoreAuthUrls() {

        List<String> result = new ArrayList<>();
        Map<String, Object> controllers = SpringContextHolder
                .getApplicationContext().getBeansWithAnnotation(Controller.class);

        for (Object bean : controllers.values()) {

            Class<?> beanClass = bean.getClass();
            if (ObjUtil.isNotNull(AnnotationUtils.findAnnotation(beanClass, Controller.class))) {

                // 获取类上的 RequestMapping
                RequestMapping classRequestMapping = AnnotationUtils
                        .findAnnotation(beanClass, RequestMapping.class);
                String classPath = ObjUtil.isNotNull(classRequestMapping) ?
                        String.join("", classRequestMapping.value()) : "";
                classPath = environment.resolvePlaceholders(classPath);
                // 类是否有特定注解
                boolean flag = ObjUtil.isNotNull(
                        AnnotationUtils.findAnnotation(beanClass, IgnoreAuthUrlAnnotation.class));

                // 遍历方法
                for (Method method : beanClass.getMethods()) {

                    if (flag || ObjUtil.isNotNull(AnnotationUtils.findAnnotation(method, IgnoreAuthUrlAnnotation.class))) {

                        String methodPath = processMethodAnnotations(method);
                        if (StrUtil.isNotEmpty(methodPath)) {

                            result.add(classPath + methodPath);
                        }
                    }
                }

            }
        }

        return result;
    }

    private static String processMethodAnnotations(Method method) {

        List<Class<? extends Annotation>> mappingAnnotations = List.of(
                GetMapping.class,
                PostMapping.class,
                PutMapping.class,
                DeleteMapping.class,
                PatchMapping.class,
                RequestMapping.class
        );

        // 遍历注解类型，找到第一个存在的 Mapping 注解
        for (Class<? extends Annotation> mappingAnnotation : mappingAnnotations) {

            Object annotation = AnnotationUtils.findAnnotation(method, mappingAnnotation);
            if (ObjUtil.isNotNull(annotation)) {

                // 根据注解类型解析路径值
                return getPathFromMappingAnnotation(annotation);
            }
        }

        return StrUtil.EMPTY;
    }

    /**
     * 根据具体的 Mapping 注解提取路径
     *
     * @param annotation
     * @return
     */
    private static String getPathFromMappingAnnotation(Object annotation) {

        if (annotation instanceof GetMapping) {

            return String.join("", ((GetMapping) annotation).value());
        } else if (annotation instanceof PostMapping) {

            return String.join("", ((PostMapping) annotation).value());
        } else if (annotation instanceof PutMapping) {

            return String.join("", ((PutMapping) annotation).value());
        } else if (annotation instanceof DeleteMapping) {

            return String.join("", ((DeleteMapping) annotation).value());
        } else if (annotation instanceof PatchMapping) {

            return String.join("", ((PatchMapping) annotation).value());
        } else if (annotation instanceof RequestMapping) {

            return String.join("", ((RequestMapping) annotation).value());
        }

        return StrUtil.EMPTY;
    }
}