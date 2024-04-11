package com.ztc.spring.formework.beans.config;

import lombok.Getter;
import lombok.Setter;

/**
 * ztc
 * 用来存储配置文件中的信息，相当于保存在内存中的配置
 *
 */
@Setter
@Getter
public class ZtcBeanDefinition {
    // 原生 Bean 的全类名
    private String beanClassName;

    // 标记是否延时加载
    private boolean lazyInit = false;

    //保存 beanName，在 IoC 容器中存储的 key
    private String factoryBeanName;

}
