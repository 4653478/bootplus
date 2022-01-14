package com.age.config;

import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.age.common.typehandler.MyTypeHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * MybatisPlus Config
 *
 * @author age
 */
@Configuration
@MapperScan("com.age.**.dao*")
@Slf4j
public class MybatisPlusConfig {

    //    @MyAutowired
    private SqlSessionFactory sqlSessionFactoryBean;

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
//        paginationInterceptor.setLocalPage(true);
        /**
         * 方言实现类
         * @see com.baomidou.mybatisplus.plugins.pagination.DialectFactory#getDialect(DBType, String)
         */
//        paginationInterceptor.setDialectClazz(MySqlDialect.class.getName());
        return paginationInterceptor;
    }

    @PostConstruct
    public void myBatisHandle() {
        if (null != sqlSessionFactoryBean) {
            org.apache.ibatis.session.Configuration configuration =
                    sqlSessionFactoryBean.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // FIXME ALL_TYPE_HANDLERS_MAP 会覆盖掉相同的key值，应该从 TYPE_HANDLER_MAP 读取
            Collection<TypeHandler<?>> handlers = typeHandlerRegistry.getTypeHandlers();
            if (null != handlers) {
                handlers.forEach(typeHandler -> {
                    // 抽离父类，过滤掉内置类型转换器
                    // if (MyTypeHandler.class.isAssignableFrom(typeHandler.getClass())) {
                    if (typeHandler instanceof MyTypeHandler) {
                        // log.debug("自定义MyBatis类型转换器注册成功:{}", typeHandler);
                    }
                });
            }
        }
    }

}
