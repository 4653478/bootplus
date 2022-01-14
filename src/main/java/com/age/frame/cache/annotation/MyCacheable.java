package com.age.frame.cache.annotation;

import com.age.frame.cache.CacheNameConstant;
import org.springframework.cache.annotation.Cacheable;
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
@Cacheable
public @interface MyCacheable {

    /**
     * @see Cacheable#value()
     */
    @AliasFor(annotation = Cacheable.class, attribute = "value")
    String[] value() default {CacheNameConstant.NORMAL};

    /**
     * @see Cacheable#key()
     */
    @AliasFor(annotation = Cacheable.class, attribute = "key")
    String key() default "";

    /**
     * @see Cacheable#cacheManager()
     */
    @AliasFor(annotation = Cacheable.class, attribute = "cacheManager")
    String cacheManager() default "";
}
