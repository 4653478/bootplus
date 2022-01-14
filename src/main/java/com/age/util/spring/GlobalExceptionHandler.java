package com.age.util.spring;

import com.age.frame.controller.MainSiteErrorController;
import com.age.util.exception.BaseRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 全局异常处理器
 *
 * @author age
 * @Email 4653478@qq.com
 * @see RestGlobalExceptionHandler
 */
@Deprecated
//@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义异常处理器
     * 会与默认的冲突
     *
     * @param e BaseRuntimeException
     * @return String
     */
    @ExceptionHandler(BaseRuntimeException.class)
    public String handleMyException(BaseRuntimeException e) {
        log.error("系统异常出错={}", e.toString(), e);
        return MainSiteErrorController.ERROR_PATH;
    }


}