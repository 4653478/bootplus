package com.age.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.age.config.aop.service.BaseAopService;
import com.age.dao.SysUserLoginLogDao;
import com.age.entity.SysUserLoginLogEntity;
import com.age.service.SysUserLoginLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 用户登录日志
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Service
public class SysUserLoginLogServiceImpl extends BaseAopService<SysUserLoginLogServiceImpl, SysUserLoginLogDao, SysUserLoginLogEntity>
        implements SysUserLoginLogService {

    @Override
    public Page<SysUserLoginLogEntity> getSelf(Integer offset, Integer limit, Long adminId, String loginIp, String sort,
                                               Boolean order) {
        // Spring XML:<!-- 启用AspectJ对Annotation的支持 -->
        //    <aop:aspectj-autoproxy proxy-target-class="true" expose-proxy="true"/>
        // SpringBoot:@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
        // 获取当前代理对象【从ThreadLocal获取代理对象】(不建议使用)-【expose-proxy必须设为true】
        // 只会取到代理对象,如果当前对象为空，则一层层往上获取代理对象【如果不存在会报错】
//        Object proxy = org.springframework.aop.framework.AopContext.currentProxy();
        Wrapper<SysUserLoginLogEntity> wrapper = new EntityWrapper<SysUserLoginLogEntity>();
        wrapper.eq("user_id", adminId);
        if (StringUtils.isNoneBlank(sort) && null != order) {
            wrapper.orderBy(sort, order);
        }
        if (StringUtils.isNoneBlank(loginIp)) {
            wrapper.like("login_ip", loginIp);
        }
        Page<SysUserLoginLogEntity> page = new Page<SysUserLoginLogEntity>(offset, limit);
        return this.selectPage(page, wrapper);
    }

}
