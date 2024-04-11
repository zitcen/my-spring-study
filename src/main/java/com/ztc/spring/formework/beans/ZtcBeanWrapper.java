package com.ztc.spring.formework.beans;

/**
 * ztc
 * BeanWrapper 主要用于封装创建后的对象实例，代理对象 (Proxy Object) 或者原生对象
 * (OriginalObject ) 都由 BeanWrapper 来保存。
 */
public class ZtcBeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public ZtcBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance() {
        return wrappedInstance;
    }

    // 返回代理以后的class
    // 可能回事这个 $Proxy0
    public Class<?> getWrappedClass() {
        return wrappedClass;
    }
}
