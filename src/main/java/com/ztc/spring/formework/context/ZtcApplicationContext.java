package com.ztc.spring.formework.context;

import com.ztc.spring.formework.annotation.ZtcAutowired;
import com.ztc.spring.formework.annotation.ZtcController;
import com.ztc.spring.formework.annotation.ZtcService;
import com.ztc.spring.formework.beans.ZtcBeanWrapper;
import com.ztc.spring.formework.beans.config.ZtcBeanDefinition;
import com.ztc.spring.formework.beans.config.ZtcBeanPostProcessor;
import com.ztc.spring.formework.beans.support.ZtcDefaultListableBeanFactory;
import com.ztc.spring.formework.context.suport.ZtcBeanDefinitionReader;
import com.ztc.spring.formework.core.ZtcBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ApplicationContext 是直接接触用户的入口，
 * 主要实现 DefaultListableBeanFactory 的 refreshO
 */
public class ZtcApplicationContext extends ZtcDefaultListableBeanFactory implements ZtcBeanFactory {

    private String[] configLocations;

    private ZtcBeanDefinitionReader reader;

    // 单例的 IoC 容器缓存
    private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String,Object>();

    //通用的 IoC 容器
    private Map<String, ZtcBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String,ZtcBeanWrapper>();

    public ZtcApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**依赖注入，从这里开始，读取 BeanD吐inition 中的信息
     *  然后通过反射机制创建一个实例并返回
     *  Spring 做法是，不会把最原始的对象放出去，会用一个 B妇nWrapper来进行一次包装
     *  装饰器模式 ：
     *  1. 保留原来的 OOP 关系
     *  2. 需要对*  它进行扩展、增强（为了以后的 AOP 打基础 ）
     *  依赖注入 (DI) 的入口是 getBeanO 方法，
     */
    @Override
    public Object getBean(String beanName) throws Exception {
        ZtcBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        try {
//            ZtcBeanPostProcessor beanPostProcessor = new ZtcBeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if(Objects.isNull(instance)){
                return null;
            }
            ZtcBeanPostProcessor beanPostProcessor = new ZtcBeanPostProcessor();
            //在初始化之前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            ZtcBeanWrapper ztcBeanWrapper = new ZtcBeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName,ztcBeanWrapper);
            //在实例初始化后再调用一次
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);

            populateBean(beanName,instance);
            //通过这样调用，相当千给我们自己留有了可操作的 空 间
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param beanClass
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(Class<?> beanClass) throws Exception {


        return getBean(beanClass.getName());
    }

    @Override
    public void refresh() throws Exception {
        // 1、定位配置文件
        reader = new ZtcBeanDefinitionReader(this.configLocations);
        // 2、加载配置文件，扫描相关的类，把它们封装为 ztcBeanDefinition
        List<ZtcBeanDefinition> ztcBeanDefinitions = reader.loadBeanDefinitions();
        // 3、注册，将配置文件中的信息放到容器中(伪 IoC 容器)
        doRegisterBeanDefinition(ztcBeanDefinitions);
        // 4、把不是延时加载的的类提前初始化

    }
    private void doRegisterBeanDefinition (List<ZtcBeanDefinition> beanDefinitions) throws Exception {
        for (ZtcBeanDefinition beanDefinition : beanDefinitions) {
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception(" The " + beanDefinition.getFactoryBeanName() + "is exists !");

            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
        // 到这里为止，容器初始化完毕
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(
                new String[this.beanDefinitionMap.size()]);
    }
    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }
    public Properties getConfig(){
        return this.reader.getConfig();
    }

    /**
     * 传递一个beanDefinition，就返回一个实例
     * @param beanDefinition
     * @return
     */
    private Object instantiateBean(ZtcBeanDefinition beanDefinition){
        Object instance = null;
        try {
            String className = beanDefinition.getBeanClassName();
            if (this.factoryBeanInstanceCache.containsKey(className)) {
                instance = this.factoryBeanObjectCache.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        } catch (Exception e) {
          e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName,Object instance){
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(ZtcController.class) || clazz.isAnnotationPresent(ZtcService.class))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(ZtcAutowired.class)) {
                continue;
            }
            ZtcAutowired autowired = field.getAnnotation(ZtcAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }
}
