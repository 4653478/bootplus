package com.age.controller;

import com.age.util.config.Constant;
import com.age.util.spring.EhcacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Controller公共组件
 *
 * @author age
 * @Email 4653478@qq.com
 */
abstract class AbstractController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 常量帮助类
     */
    @Resource
    protected Constant constant;

    @Resource
    protected EhcacheUtil ehcacheUtil;

}
