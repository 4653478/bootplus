package com.age.service;

import com.baomidou.mybatisplus.service.IService;
import com.age.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author age
 * @Email 4653478@qq.com
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    /**
     * 保存角色或更新角色
     *
     * @param roleId     Long
     * @param menuIdList List<Long>
     */
    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 清除缓存
     *
     * @param roleId 角色ID
     */
    void clearMenuIdList(Long roleId);

}
