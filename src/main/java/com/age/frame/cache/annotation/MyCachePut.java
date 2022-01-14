package com.age.frame.cache.annotation;

import com.age.frame.cache.CacheNameConstant;
import org.springframework.cache.annotation.CachePut;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义缓存注解
 * 实现默认缓存名
 *
 * @author Created by age on 2020/4/26
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@CachePut
public @interface MyCachePut {

    /**
     * @see CachePut#value()
     */
    @AliasFor(annotation = CachePut.class, attribute = "value")
    String[] value() default {CacheNameConstant.NORMAL};

    /**
     * @see CachePut#key()
     */
    @AliasFor(annotation = CachePut.class, attribute = "key")
    String key() default "";

    /**
     * @see CachePut#cacheManager()
     */
    @AliasFor(annotation = CachePut.class, attribute = "cacheManager")
    String cacheManager() default "";
}
