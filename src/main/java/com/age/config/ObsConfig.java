package com.age.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ObsConfig {


    public static String EndPoint;
    public static String AK;
    public static String SK;
    public static String BucketName;
    public static String FileUploadPath;


    @Value("${huawei.obs.endPoint}")
    public String endPoint ;

    @Value("${huawei.obs.ak}")
    public String ak ;

    @Value("${huawei.obs.sk}")
    public String sk ;

    @Value("${huawei.obs.bucketName}")
    public String bucketName ;

    @Value("${huawei.obs.fileUploadPath}")
    public String fileUploadPath ;

    @PostConstruct //@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次;
    public void transValues() {
        EndPoint = this.endPoint;
        AK = this.ak;
        SK = this.sk;
        BucketName = this.bucketName;
        FileUploadPath = this.fileUploadPath;
    }

}
