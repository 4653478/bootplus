package com.age.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class EncryptConfig {

    public static String KEY;
    public static String IV;


    @Value("${aesencrypt.key}")
    public String key ;

    @Value("${aesencrypt.iv}")
    public String iv ;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次;
    public void transValues() {
        KEY = this.key;
        IV = this.iv;
    }
}
