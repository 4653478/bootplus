package com.age.dao;

import com.age.entity.SysRoleEntity;

/**
 * 角色管理
 *
 * @author age
 * @Email 4653478@qq.com
 */
public interface SysRoleDao extends BaseDao<SysRoleEntity> {

    /**
     * 更新角色
     *
     * @param role SysRoleEntity
     */
    void updateNoMapper(SysRoleEntity role);

}
