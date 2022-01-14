package com.age.config.aop;

import com.age.App;
import com.age.config.aop.annotation.MyController;
import com.age.frame.spring.IStartUp;
import com.age.util.ClassUtil;
import com.age.util.StringUtils;
import com.age.util.spring.SpringBeanUtils;
import com.age.util.spring.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Spring动态代理手动注册Controller
 * 关于@Controller和@Component注解只是个见名知意的意思,方便区分具体bean的作用而已
 * 实际是去判断类注解是否有@Controller.class | @RequestMapping.class 这2个才注册URL
 *
 * @author Created by age on 2020/1/16
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#isHandler(Class)
 * @see AbstractHandlerMethodMapping#initHandlerMethods()
 */
@Slf4j
@Component
public class MyControllerRegistry implements IStartUp {

    @Override
    public void startUp(ApplicationContext applicationContext) throws Exception {
        // root application context 没有parent，他就是老大.
        if (applicationContext.getParent() == null) {
            // 这里只能获取注册了Bean的注解类
            Class<? extends Annotation> annotationClass = MyController.class;
            Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
            if (!ObjectUtils.isEmpty(beanWithAnnotation)) {
                Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
                for (Map.Entry<String, Object> entry : entitySet) {
                    // 获取bean对象
                    Object aopObject = entry.getValue();
                    Class<? extends Object> clazz = aopObject.getClass();
                    // 注册Controller
                    SpringBeanUtils.registerController(clazz);
                    // 获取注解类
                    MyController myController = AnnotationUtils.findAnnotation(clazz, MyController.class);
                    log.info("Spring手动注册Controller成功=={},value={}",
                            ClassUtil.getClass(aopObject), myController.value());
                }
            }
            // 这里从包扫描注册Controller
            handleRegister(App.SCAN_BASE_PACKAGES, annotationClass);
        }
    }

    /**
     * 貌似onApplicationEvent方法里注册Bean不生效
     * 使用@PostConstruct修饰的方法在Spring容器启动时会
     * 先于实现ApplicationContextAware接口的工具类 setApplicationContext()方法运行
     * 使用@DependsOn表示强制初始化该工具类【@DependsOn压根没用-~-】
     *
     * @throws Exception
     */
    @PostConstruct
    public void doSomeThing() throws Exception {
//        handleRegister(App.SCAN_BASE_PACKAGES, MyController.class);
    }

    /**
     * 通过扫描包名和注解类注册Controller
     *
     * @param scanBasePackages 扫描包名
     * @param annotationClass  注解类
     * @param <A>              Annotation
     * @throws Exception
     */
    private <A extends Annotation> void handleRegister(String scanBasePackages, Class<A> annotationClass) throws Exception {
        Assert.notNull(annotationClass, "AnnotationClass must not be null");
        ApplicationContext applicationContext = SpringContextUtils.applicationContext;
        Assert.notNull(applicationContext, "ApplicationContext初始化失败，" +
                "请确认调用方法未使用`@PostConstruct`注解！");
        // 通过包名获取包内所有类
        List<Class<?>> classList = ClassUtil.getAllClassByPackageName(scanBasePackages);
        for (Class<?> clazz : classList) {
            // 类是否含有注解
            if (clazz.isAnnotationPresent(annotationClass)) {
                // 获取类路径
                String className = ClassUtil.getClass(clazz);
                try {
                    // 获取注解类
                    A annotation = AnnotationUtils.findAnnotation(clazz, annotationClass);
                    // 声明beanName
                    String simpleName = null;
                    // 处理自定义beanName
                    if (annotation.annotationType().equals(MyController.class)) {
                        simpleName = ((MyController) annotation).value();
                    }
                    if (StringUtils.isEmpty(simpleName)) {
                        // 获取类名
                        simpleName = ClassUtil.getClassName(clazz);
                    }
                    simpleName = StringUtils.toLowerCaseFirstOne(simpleName);
                    // 这里因为还未注册Bean,需要先注册Bean（如果注册了跳过）
                    // 这里不能使用getBean方法判断，因为有缓存会导致后面不能注册
                    if (!applicationContext.containsBeanDefinition(simpleName)) {
                        // 不存在Bean则手动注册Bean
                        SpringBeanUtils.addBean(className, simpleName, null);
                    }
                    // 注册Controller
                    SpringBeanUtils.registerController(clazz);
                    log.info("手动注册Controller[{}]成功,={}",
                            className, StringUtils.defaultString(annotation.toString()));
                } catch (Exception e) {
                    log.error(String.format("手动注册Controller[%s]失败", className), e);
                    throw e;
                }
            } else {
                // log.info(ClassUtil.getClass(clazz));
            }
        }
    }

}
