package com.age.util.exception;

/**
 * 自定义异常接口
 *
 * @author Created by age on 2020/4/10
 */
public interface MyException {

    /**
     * 获取错误码
     *
     * @return default:500
     */
    default int getCode() {
        return 500;
    }

    /**
     * 获取错误信息
     *
     * @return String
     */
    String getMsg();

}
