package com.age.dao;

import com.age.entity.SysMenuEntity;

import java.util.List;

/**
 * 菜单管理
 *
 * @author age
 * @Email 4653478@qq.com
 */
public interface SysMenuDao extends BaseDao<SysMenuEntity> {

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     *
     * @return List<SysMenuEntity>
     */
    List<SysMenuEntity> queryNotButtonList();
}
