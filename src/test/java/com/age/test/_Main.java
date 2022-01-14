package com.age.test;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.age.config.EncryptConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.age.common.enums.IEnumHelperFactory;
import com.age.config.filter.MyCorsFilter;
import com.age.entity.enums.SysMenuTypeEnum;
import com.age.frame.log.LogUtil;
import com.age.util.file.FileTypeEnum;
import com.age.util.http.RestTemplateUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import static com.age.util.encry.AesEncryptUtil.desEncrypt;
import static com.age.util.encry.AesEncryptUtil.encrypt;

/**
 * @author Created by age on 2020/1/9
 */
public class _Main {

    public static Map<String, String> bTOS(Map<?, ?> map) {
        return (HashMap<String, String>) map;
    }

    public static void main(String[] args) throws Exception {
        String test = "123456";

        String data = test;
        String key =  "bZ6mQaxTDeGzEXpY";
        String iv = "TatyS3w6h+ZYWGjE";
        System.out.println("原文："+data);
        data ="t7YO+D+iIYV3BF9P+p/4mBgHqSecGF5EU279IVH4ilcyv/IXI9N5L7bGizPiao40idLMwfB7wv93s3tN0twwgQ==" ;//encrypt(test, key, iv);

        System.out.println("密文："+data);
        String s = desEncrypt(data , key, iv);
        System.out.println("解密："+s);
    }
    public static void main0(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException {

        System.out.println(Locale.getDefault());
        System.out.println(Locale.getDefault().toString());

        Map<String, Object> testMap = Maps.newConcurrentMap();
        testMap.put("1", 1);
        testMap.forEach((s, o) -> {
            System.out.println(s + o);
        });
        testMap.clear();
        testMap.forEach((s, o) -> {
            System.out.println(s + o);
        });
        testMap.put("1", 1);
        testMap.forEach((s, o) -> {
            System.out.println(s + o);
        });

        Map<String, Object> bodyMap = new ImmutableMap.Builder<String, Object>()
                .put("mobile", "11111111111")
                .put("password", "111111")
                .put("count", "50000")
                .put("model", "1").build();
        String url = "https://www.bushu.run/api/mi/submit";
        String result = RestTemplateUtil.getForObject(url, bodyMap,
                MediaType.APPLICATION_FORM_URLENCODED, String.class, null, new MediaType[0]);
        System.out.println(result);
        result = RestTemplateUtil.postForObject(url, bodyMap,
                MediaType.APPLICATION_FORM_URLENCODED, String.class, null, new MediaType[0]);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);

        if (null != jsonObject) {
            if (jsonObject.getInteger("code") == 0) {
                System.out.println(result);
            } else {
                System.err.println(result);
            }
        }

        try (HttpResponse response = HttpUtil.createGet(url).form(bodyMap)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE).execute()) {
            System.out.println("response=" + response.body());
        }

        System.out.println(Collections.<String>emptyList());
        // 默认的临时目录
        System.out.println(System.getProperty("java.io.tmpdir"));
        // 用户的家目录
        System.out.println(System.getProperty("user.home"));
        // 用户当前的工作目录
        System.out.println(System.getProperty("user.dir"));
        File desktopDir = FileSystemView.getFileSystemView().getHomeDirectory();
        // 系统桌面路径
        System.out.println(desktopDir.getPath());

        Map<String, Boolean> testMap1 = new HashMap<String, Boolean>(2);
        testMap1.put("1", true);
        testMap1.put("2", false);

        Map<String, String> testMap11 = bTOS(testMap1);

        for (Iterator<String> nameIterator = testMap11.keySet().iterator(); nameIterator.hasNext(); ) {
            String name = nameIterator.next();
            System.out.println(name);
            //  java.lang.Boolean cannot be cast to java.lang.String
//            System.out.println(testMap11.get(name));
        }

        ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("start_time", System.currentTimeMillis())
                .put("enable_auto_cloud_recording", true).build();
//        System.out.println(RestTemplateUtil.postForObject("https://www.baidu.com/", map, MediaType.APPLICATION_FORM_URLENCODED));
        System.out.println(Thread.currentThread().getName());
        RestTemplateUtil.postForObject("https://www.gerensuodeshui.cn/", null, String.class);
        System.out.println(Thread.currentThread().getName());

        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map);
        System.out.println(Thread.currentThread().getName());

//        System.out.println(RestTemplateUtil.getForObject("http://gogs-git.hztywl.cn/", map));


        System.out.println("test2：" + test2());
        LogUtil.MyLogger logger = LogUtil.getInstance().getInterceptorStatementLogger();
        logger = LogUtil.getInstance().getInterceptorStatementLogger();
//        logger.info(Enum.valueOf(SysMenuTypeEnum.class, "0").getValue());
        IEnumHelperFactory.IEnumHelper sysMenuTypeHelper = IEnumHelperFactory.getInstance().getByClass(
                SysMenuTypeEnum.class);
        logger.info(IEnumHelperFactory.getInstance().getByClass(
                SysMenuTypeEnum.class).getByKey("0").getValue());
        logger.info(IEnumHelperFactory.getInstance().getByClass(
                FileTypeEnum.class).getByKey("IMAGE").getValue());
        String[] keyArray = sysMenuTypeHelper.getKeyArray();
        List list = sysMenuTypeHelper.getList();
        Logger finalLogger = logger.getLogger();
        list.forEach(o -> {
            finalLogger.info(o.toString());
        });
        for (String key : keyArray) {
            logger.info(key);
        }

        // 要想继承实现@RequestMapping和@ResponseBody 父类的访问修饰符必须是public，不然获取到的方法和实际的方法不一致
        Class<?> sysClass = Class.forName("com.age.controller.admin.SysLoginController");
        for (Method method : sysClass.getMethods()) {
            System.out.println(method.getName());
        }
        Method testString = sysClass.getMethod("testString", Model.class);
        JSONField annotation = TypeUtils.getAnnotation(testString, JSONField.class);
        annotation = AnnotationUtils.getAnnotation(testString, JSONField.class);
        annotation = AnnotationUtils.findAnnotation(testString, JSONField.class);
        annotation = AnnotatedElementUtils.findMergedAnnotation(testString, JSONField.class);
        System.out.println(testString);
        // 不同的平台生成相应平台的换行符

        // 获取操作系统对应的换行符
        System.out.println(File.pathSeparator);
        System.out.println(System.getProperty("line.separator", "\n"));
        System.out.println(MyCorsFilter.class.getSimpleName());
        System.out.println(MyCorsFilter.class.getName());
        System.out.println(MyCorsFilter.class.getTypeName());
        String[] array = ArrayUtils.toArray(
                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name(), HttpMethod.DELETE.name(), HttpMethod.PUT.name());
        System.out.println(StringUtils.join(array, ", "));
        System.out.println(ArrayUtils.toString(array
                , StringUtils.EMPTY));
        System.out.println(new ToStringBuilder(array, ToStringStyle.SIMPLE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringBuilder.getDefaultStyle()).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.MULTI_LINE_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.SHORT_PREFIX_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_CLASS_NAME_STYLE).append(array).toString());
        System.out.println(new ToStringBuilder(array, ToStringStyle.NO_FIELD_NAMES_STYLE).append(array).toString());

        // Spring原生线程池【写简书】
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        /* ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。 
        ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。 
        ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，执行后面的任务
        ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务 */
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        singleThreadPool.execute(() -> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();


        // 压缩文件夹进行Base64编码再写入文件存储
//        Map<String, Key> rsa = CryptoUtil.initKeyPair("RSA", 1024, new BouncyCastleProvider());
//        System.out.println(Base64.encodeBase64String(rsa.get(CryptoUtil.PRIVATE_KEY).getEncoded()));
//        System.out.println(Base64.encodeBase64String(rsa.get(CryptoUtil.PUBLIC_KEY).getEncoded()));
//
//        File file = new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\server-net\\server-net.zip");
//        FileInputStream inputFile = new FileInputStream(file);
//        byte[] buffer = new byte[(int) file.length()];
//        inputFile.read(buffer);
//        inputFile.close();
//        String base64Code = Base64.encodeBase64String(buffer);
//        FileUtils.writeStringToFile(new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\server-net\\smarthos-admin\\src\\main\\webapp\\tes.txt"), base64Code, "UTF-8");

//        byte[] bytes = Base64.decodeBase64(FileUtils.readFileToString(new File("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\code.txt"), Charset.forName("UTF-8")));
//        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator.QH-20150311UHWZ\\Desktop\\平安好医生\\code.rar");
//        out.write(bytes);
//        out.close();



    }


    public static int test2() {
        int i = 1;
        try {
            System.out.println("try语句块中");
            return 1;
        } finally {
            System.out.println("finally语句块中");
            return 2;
        }

    }

}
