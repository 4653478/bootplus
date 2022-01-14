package com.age.service;

import cn.hutool.core.collection.CollectionUtil;
import com.age.base.BaseAppTest;
import com.age.util.spring.EhcacheUtil;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * EhCache 缓存测试
 *
 * @author Created by age on 2020/4/23
 */
public class EhcacheTest extends BaseAppTest {

    @Resource
    protected EhcacheUtil ehcacheUtil;

    @Resource
    protected CacheManager cacheManager;

    /**
     * 缓存测试
     */
    @Test
    public void testCache() {
        String[] ehcacheNames = ehcacheUtil.getManager().getCacheNames();
        CollectionUtil.newArrayList(ehcacheNames).forEach(ehcacheName -> {
            log.debug("EhCache--ehcacheName=【{}】", ehcacheName);
        });

        Collection<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.forEach(cacheName -> {
            log.debug("CacheManager--cacheName=【{}】", cacheName);
        });
    }

}
