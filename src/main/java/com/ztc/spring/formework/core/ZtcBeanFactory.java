package com.ztc.spring.formework.core;

/**
 * ztc
 * BeanFactory是Spring框架的核心接口，用于管理和获取Bean对象。
 * 它是一个容器，负责创建、配置和管理应用程序中的Bean。
 * 的主要功能是根据Bean的定义返回Bean对象的实例。
 * 通过调用'getBean()'方法，可以从'BeanFactory'中获取Bean实例。
 * BeanFactory通常通过XML配置文件或Java注解进行配置，定义和管理Bean对象。
 * BeanFactory在获取Bean时动态创建Bean对象，即按需创建。
 *
 */
public interface ZtcBeanFactory {

    /**
     * 根据 beanName 从 IoC 容器中获得一个实例 Bean
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;

    /**
     * 根据类型从 IoC 容器中获得一个实例 Bean
     * @param beanClass
     * @return
     * @throws Exception
     */
    public Object getBean(Class <?> beanClass) throws Exception;

}
