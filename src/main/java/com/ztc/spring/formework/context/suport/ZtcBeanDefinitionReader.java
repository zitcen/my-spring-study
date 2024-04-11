package com.ztc.spring.formework.context.suport;

import com.ztc.spring.formework.beans.config.ZtcBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * 根据约定， BeanDefinitionReader 主要完成对 application.properties 配置文件的解析工作，实现
 * 逻辑非常简单。通过构造方法获取从 ApplicationContext 传过来的 locations 配置文件路径，然后解
 * 析，扫描并保存所有相关的类并提供统一的访问入口 。
 */
public class ZtcBeanDefinitionReader {

    private List<String> registryBeanClasses = new ArrayList<String>();

    private Properties config = new Properties();

    // 固定配置文件中的 key, 相对于 XML 的规范
    private final String SCAN_PACKAGE = "scanPackage";

    public ZtcBeanDefinitionReader(String... locations) {
        //通过 URL 定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                locations[0].replace("classpath:", ""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!Objects.isNull(is)) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    /**
     * 扫描路径
     *
     * @param scanPackage
     */
    private void doScanner(String scanPackage) {
        //将包的路径转换成文件路径
        URL url = this.getClass().getClassLoader().
                getResource("/" + scanPackage.replace("\\.", "/"));
        File classPath = new File(url.getFile());
        // 使用递归的方式扫描路径
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                // 如果还是文件夹，接着循环
                doScanner(scanPackage + "." + file.getName());
            } else {
                //不是 java 类文件，中断本次循环，接着循环后面的文件
                if (!file.getName().endsWith(".class")) {
                    continue;
                }
                // 获取类的名称
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                // 将需要注册到容器中的类，添加到集合中
                registryBeanClasses.add(className);
            }
        }
    }

    public Properties getConfig() {
        return this.config;
    }

    /**
     * 把配置文件中扫描到的所有配置信息转换为
     * ZtcBeanDefinition 对象，以便于之后的 IoC 操作
     */
    public List<ZtcBeanDefinition> loadBeanDefinitions() {
        //初始化 ZtcBeanDefinition 的集合
        List<ZtcBeanDefinition> result = new ArrayList<>();
        for (String className : registryBeanClasses) {
            Class<?> beanClass = null;
            try {
                // 通过类路径获取类
                beanClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            // 如果类是interface类型，跳出本次循环
            if (beanClass.isInterface()) {
                continue;
            }
            // 添加类
            result.add(doCreateBeanDefinition(
                    toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()));
            Class<?>[] interfaces = beanClass.getInterfaces();

            // 添加父类接口
            for (Class<?> i : interfaces) {
                result.add(doCreateBeanDefinition(i.getName(), beanClass.getName()));
            }
        }
        return result;
    }

    /**
     * 把每一个配置信息解析成一个 BeanDefinition
     * @param factoryBeanName
     * @param beanClassName
     * @return
     */
    private ZtcBeanDefinition doCreateBeanDefinition(String factoryBeanName,
                                                     String beanClassName) {
        ZtcBeanDefinition beanDefinition = new ZtcBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    /**
     * 将类名首字母改为小写
     * @param simpleName
     * @return
     */
    private String toLowerFirstCase(String simpleName) {
        char[] chars = simpleName.toCharArray();
        // 因为大小写字母的 ASCII 码相差 32
        // 而且大写字母的 ASCII 码要小于小写字母的 A5_CII 码
        // 在 java 中,对char做算术运算，实际上就是对 ASCII 码做算术运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
