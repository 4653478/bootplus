package com.age.common.typehandler;

/**
 * 自定义类型转换处理
 *
 * @author Created by age on 2020/4/2
 */
public interface MyTypeHandler {

    /**
     * 获取转化后的数据类型
     *
     * @return Class<?>
     */
    Class<?> getClazz();

}
