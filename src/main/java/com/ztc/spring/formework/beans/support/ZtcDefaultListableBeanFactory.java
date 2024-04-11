package com.ztc.spring.formework.beans.support;

import com.ztc.spring.formework.beans.config.ZtcBeanDefinition;
import com.ztc.spring.formework.context.suport.ZtcAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ZtcDefaultListableBeanFactory extends ZtcAbstractApplicationContext {

    // 存储注册信息的 BeanDefinition
    protected final Map<String, ZtcBeanDefinition> beanDefinitionMap = new
            ConcurrentHashMap<String, ZtcBeanDefinition>();
}
