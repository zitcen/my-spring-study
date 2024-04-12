package com.ztc.spring.formework.beans.config;

/**
 * BeanPostProcessor 是为对象初始化事件设置的 一种回调机制 。
 * 这个版本中只做说明，不做具体实现，感兴趣的“小伙伴”可以继续深入研究 Spring 源码。
 */
public class ZtcBeanPostProcessor {
    //为在 Bean 的初始化之前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws
            Exception {
        return bean;
    }

    //为在 Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
