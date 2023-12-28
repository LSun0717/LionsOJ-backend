package com.gzu.lionsoj.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Classname: SpringContextUtils
 * @Description: Spring 上下文获取工具
 * @Author: lions
 * @Datetime: 12/29/2023 12:31 AM
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * @Description: 通过名称获取 Bean
     * @param beanName Bean名称
     * @Return: Bean
     * @Author: lions
     * @Datetime: 12/29/2023 12:31 AM
     */
    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }


    /**
     * @Description: 通过 class 获取 Bean
     * @param beanClass bean的Class对象
     * @Return: Bean
     * @Author: lions
     * @Datetime: 12/29/2023 12:32 AM
     */
    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    /**
     * @Description: 通过名称和类型获取 Bean
     * @param beanName Bean名称
     * @param beanClass Bean Class对象
     * @Return: Bean
     * @Author: lions
     * @Datetime: 12/29/2023 12:32 AM
     */
    public static <T> T getBean(String beanName, Class<T> beanClass) {
        return applicationContext.getBean(beanName, beanClass);
    }
}