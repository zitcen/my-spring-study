package com.ztc.spring.formework.context.suport;

/**
 * IoC 容器实现类的顶层抽象类，实现IoC容器相关的公共逻辑。
 * 为了尽可能地简化，在这个Mini版本中，暂时只设计了一个 refresh() 方法
 */
public abstract class ZtcAbstractApplicationContext {
    // 受保护 ，只提供给子类重写
    public void refresh() throws Exception {}
}
