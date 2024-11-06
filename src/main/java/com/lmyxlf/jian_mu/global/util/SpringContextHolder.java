package com.lmyxlf.jian_mu.global.util;

import cn.hutool.core.collection.CollUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

/**
 * @author lmy
 * @email 2130546401@qq.com
 * @date 2024/7/6 17:32
 * @description Spring 容器持有类
 * @since 17
 */
@Component
public class SpringContextHolder implements ApplicationContextAware, BeanFactoryPostProcessor {

    private static ApplicationContext applicationContext;

    /**
     * spring 应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {

        SpringContextHolder.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(@NotNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        SpringContextHolder.beanFactory = configurableListableBeanFactory;
    }

    public static ApplicationContext getApplicationContext() {

        assertApplicationContext();
        return applicationContext;
    }

    /**
     * 获取类型为 requiredType 的对象
     *
     * @param requiredType
     * @return
     */
    public static <T> T getBean(Class<T> requiredType) {

        assertApplicationContext();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取对象
     *
     * @param beanName
     * @return 一个以所给名字注册的 bean 的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {

        assertApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    /**
     * 通过类上的注解获取类
     *
     * @param annotation anno
     * @return map
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotation) {

        assertApplicationContext();
        return applicationContext.getBeansWithAnnotation(annotation);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {

        assertApplicationContext();
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {

        assertApplicationContext();
        return beanFactory.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {

        assertApplicationContext();
        return beanFactory.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {

        assertApplicationContext();
        return beanFactory.getAliases(name);
    }

    /**
     * 获取aop代理对象
     *
     * @param invoker
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {

        assertApplicationContext();
        return (T) AopContext.currentProxy();
    }

    /**
     * 获取当前的环境配置，无配置返回null
     *
     * @return 当前的环境配置
     */
    public static String[] getActiveProfiles() {

        assertApplicationContext();
        return applicationContext.getEnvironment().getActiveProfiles();
    }

    /**
     * 获取当前的环境配置，当有多个环境配置时，只获取第一个
     *
     * @return 当前的环境配置
     */
    public static String getActiveProfile() {

        assertApplicationContext();
        final String[] activeProfiles = getActiveProfiles();
        return CollUtil.isNotEmpty(Arrays.asList(activeProfiles)) ? activeProfiles[0] : null;
    }

    /**
     * 获取配置文件中的值
     *
     * @param key 配置文件的key
     * @return 当前的配置文件的值
     */
    public static String getRequiredProperty(String key) {

        assertApplicationContext();
        return applicationContext.getEnvironment().getRequiredProperty(key);
    }

    private static void assertApplicationContext() {

        if (SpringContextHolder.applicationContext == null) {

            throw new RuntimeException("application Context属性为 null,请检查是否注入了 SpringContextHolder!");
        }
    }
}