package com.ztc.spring.formework.annotation;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZtcRequestParam {
    String value() default "";
}
