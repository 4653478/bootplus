package com.age.service;

import com.age.base.BaseAppTest;
import com.age.util.StringUtils;
import com.age.util.spring.AopTargetUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 角色菜单测试
 *
 * @author Created by age on 2020/4/22
 */
public class SysRoleMenuServiceTest extends BaseAppTest {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    /**
     * 根据角色ID，获取菜单ID列表
     */
    @Test
    public void queryMenuIdList() throws Exception {
        SysRoleMenuService targetSelf = AopTargetUtils.getTargetSelf(sysRoleMenuService);
        long roleId = 13L;
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        outLog(menuIdList);
        menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        outLog(menuIdList);
        sysRoleMenuService.clearMenuIdList(roleId);
        sysRoleMenuService.saveOrUpdate(roleId, Arrays.asList(1L));
        menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
        outLog(menuIdList);
//        menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
//        sysRoleMenuService.clearMenuIdList(roleId);
//        outLog(menuIdList);


        // 防止Spring守护线程池提前结束
        /*try {
            System.in.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }

    private void outLog(List<Long> menuIdList) {
        for (Long menuId : menuIdList) {
            log.debug(StringUtils.toString(menuId));
        }
    }

}
