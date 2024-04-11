package com.ztc.spring.formework.core;

/**
 * ztc
 * 它是一个BeanFactory, 用于创建其他 Bean。它提供了一种灵活的方式来创建和配置复杂的Bean对象。
 * FactoryBean是一个接口，需要自定义实现该接口的类，
 * 并通过XML配置文件或者Java注解进行配置，以创建和管理其他Bean对象。
 * FactoryBean在容器初始化时创建，然后将其作为一个普通的Bean管理。
 */
public interface ZtcFactoryBean {
}
