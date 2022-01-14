package com.age.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class WxConfig {


    public static String Token;
    public static String AppSecret;
    public static String AppId;
    public static String RevisitTempId;
    public static String ChatMsgTempId;


    @Value("${weixin.appId}")
    public String appId ;

    @Value("${weixin.appSecret}")
    public String appSecret ;

    @Value("${weixin.token}")
    public String token ;

    @Value("${weixin.subscribe.revisitTempId}")
    public String revisitTempId ;

    @Value("${weixin.subscribe.chatMsgTempId}")
    public String chatMsgTempId ;


    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次;
    public void transValues() {
        AppId = this.appId;
        AppSecret = this.appSecret;
        Token = this.token;
        RevisitTempId = this.revisitTempId;
        ChatMsgTempId = this.chatMsgTempId;
    }

}
