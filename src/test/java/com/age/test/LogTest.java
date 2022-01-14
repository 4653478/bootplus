package com.age.test;

import com.age.base.BaseAppTest;
import com.age.frame.log.LogUtil;
import org.junit.Test;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;

/**
 * 日志测试类
 *
 * @author Created by age on 2020/3/17
 */
public class LogTest extends BaseAppTest {

    /**
     * TODO logback.xml 是如何加载的？
     * ch.qos.logback.ext.spring.web.LogbackConfigListener
     *
     * @see ch.qos.logback.classic.util.ContextInitializer#AUTOCONFIG_FILE
     * 改回默认logback即可正常在main方法使用,-spring是通过SpringBoot来自动装配的
     * @see org.springframework.boot.logging.logback.LogbackLoggingSystem#loadConfiguration(LoggingInitializationContext, String, LogFile)
     */
    public static void main(String[] args) {
//        ContextInitializer.AUTOCONFIG_FILE = "";
        // 此方法可用，不能加classpath或/
        /**
         * @see org.springframework.boot.logging.logback.LogbackLoggingSystem#CONFIGURATION_FILE_PROPERTY
         */
        System.setProperty("logback.configurationFile", "logback-spring.xml");
        // 输出文件
        LogUtil.MyLogger logger = LogUtil.getInstance().getFileStatementLogger();
        logger.debug("debug file Statement");
        logger.getLogger().warn("warn file Statement");
        logger.error("error file Statement");
    }

    /**
     * 输出文件
     */
    @Test
    public void testLogForFile() {
        LogUtil.MyLogger logger = LogUtil.getInstance().getFileStatementLogger();
        logger.debug("debug file Statement");
        logger.getLogger().warn("warn file Statement");
        logger.error("error file Statement");
    }

}
