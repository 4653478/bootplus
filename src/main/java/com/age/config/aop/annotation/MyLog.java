package com.age.config.aop.annotation;

import java.lang.annotation.*;

/**
 * 自定义日志注解
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Inherited // 可以被继承
public @interface MyLog {

    String value() default "https://github.com/4653478";

}