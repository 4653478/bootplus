package com.age.controller.admin;

import com.age.base.BaseAppShiroTest;
import com.age.controller.QrCodeController;
import com.age.service.SysUserLoginLogService;
import com.age.service.SysUserRoleService;
import com.age.service.impl.SysUserRoleServiceImpl;
import com.age.util.spring.SpringContextUtils;
import org.junit.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 系统用户控制器测试
 *
 * @author age
 * @Email 4653478@qq.com
 */
public class SysUserControllerTest extends BaseAppShiroTest {

    /**
     * 测试管理员用户列表
     *
     * @throws Exception
     */
    @Test
    public void testUserList() throws Exception {
        String uri = "/admin/sys/user/list?offset=0&limit=100";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name());
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        log.info(status + "===>" + content);
    }

    /**
     * @throws Exception
     * @see org.springframework.transaction.interceptor.TransactionAspectSupport#invokeWithinTransaction(Method, Class, TransactionAspectSupport.InvocationCallback)
     */
    @Test
    public void testProxy() throws Exception {
        QrCodeController bean1 = SpringContextUtils.getBean(QrCodeController.class);
        SysUserRoleService bean = SpringContextUtils.getBean(SysUserRoleService.class);
        SysUserRoleService bean2 = SpringContextUtils.getBean(SysUserRoleServiceImpl.class);
        SysUserLoginLogService bean222 = SpringContextUtils.getBean(SysUserLoginLogService.class);
        log.debug("是否是代理调用:{}", AopUtils.isAopProxy(bean));
        log.debug("是否是cglib类代理调用:{}", AopUtils.isCglibProxy(bean));
        log.debug("是否是jdk动态接口代理调用:{}", AopUtils.isJdkDynamicProxy(bean));
        String uri = "/share/qrcode";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();
        mvcResult.getResponse().setCharacterEncoding(StandardCharsets.UTF_8.name());
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentType();
        // 测试junit-SpringBoot @WebFilter和@WebListener 注解是否生效
        String allowMethods = mvcResult.getResponse().getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
        log.info(status + "===>" + content + "===>" + allowMethods);
//        bean.qrCode(200, 200, "https://github.com/4653478", null);
    }

}
