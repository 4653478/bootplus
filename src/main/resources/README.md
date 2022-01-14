# Spring其他说明
```java
// ResourceUtils.CLASSPATH_URL_PREFIX
// ResourceUtils.getFile 不能以'/'开头 (ClassUtils.getDefaultClassLoader()获取的path以/开头会返回null)
```

# Actuator健康监控说明
> 配置项 `management.context-path`

| 端点名(URI) | 描述 | 鉴权 |
| :---: | :---: | :---: |
| conditions | 所有自动配置信息 | true |
| auditevents| 审计事件| true |
| beans| 所有Bean的信息| true |
| configprops| 所有自动化配置属性| true |
| threaddump| 线程状态信息| true |
| env| 当前环境信息| true |
| health| 应用健康状况| true |
| info| 当前应用信息| true |
| metrics| 应用的各项指标| true |
| mappings| 应用@RequestMapping映射路径| true |
| shutdown| 关闭当前应用（默认关闭）| true |
| httptrace| 追踪信息（最新的http请求）| true |
| trace| 基本追踪信息 | true |

# 代理配置说明
> Java中代理的实现一般分为三种：JDK静态代理、JDK动态代理以及CGLIB动态代理

[SpringBoot中修改proxyTargetClass，但事务代理始终为CGLIB](https://blog.csdn.net/laoxilaoxi_/article/details/99896738)

##### 基于CGLIB的代理与基于JDK的动态代理实现的声明式事务的区别
- CGLIB基于继承实现，JDK动态代理基于实现接口实现
- CGLIB的代理类需要事务注解@Transactional标注在类上（或方法）;而JDK动态代理类事务注解@Transactional可以标注在接口上（或方法），也可以标注在实现类上（或方法）
- CGLIB采用的是继承，所以不能对final修饰的类进行代理
- CGLIB或JDK动态代理是不是不能代理`private`或`final`修饰的类或方法
- CGLIB不能对目标类的`private`、`final`修饰的方法进行代理，因为父类的`final`方法不允许子类重写，父类的`private`方法不允许子类访问
- 被`final`修饰的类只能使用JDK动态代理，因为被`final`修饰的类不能被继承

> 配置项 `spring.aop.auto`&`spring.aop.proxy-target-class`&`@EnableTransactionManagement(proxyTargetClass = true)`

| auto | proxy-target-class | proxyTargetClass | 代理技术 | 备注 |
| :---: | :---: | :---: | :---: | :---: |
| true | false | false | JDK动态代理 |  |
| true | true | false | CGLIB | 默认值 |
| true | false | true | CGLIB |  |
| true | true | true | CGLIB |  |
| false | false | false | JDK动态代理 |  |
| false | true | false | JDK动态代理 |  |
| false | false | true | CGLIB |  |
| false | true | true | CGLIB |  |

> 启用AspectJ对Annotation的支持
- `Spring XML`:
```xml
 <!-- 启用AspectJ对Annotation的支持 -->
<aop:aspectj-autoproxy proxy-target-class="true" expose-proxy="true"/>
```
- `SpringBoot`:
```
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
```
> 获取当前代理对象【从ThreadLocal获取代理对象】(不建议使用)-【expose-proxy必须设为true】
`Object proxy = org.springframework.aop.framework.AopContext.currentProxy();`