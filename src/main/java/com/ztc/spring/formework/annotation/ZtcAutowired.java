package com.ztc.spring.formework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZtcAutowired {
    String value() default "";
}
