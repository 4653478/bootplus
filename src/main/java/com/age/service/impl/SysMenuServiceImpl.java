package com.age.service.impl;

import com.age.config.aop.service.BaseAopService;
import com.age.dao.SysMenuDao;
import com.age.entity.SysMenuEntity;
import com.age.frame.cache.annotation.MyCacheEvict;
import com.age.frame.cache.annotation.MyCacheable;
import com.age.service.SysMenuService;
import com.age.service.SysUserService;
import com.age.util.config.Constant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Service
public class SysMenuServiceImpl extends BaseAopService<SysMenuServiceImpl, SysMenuDao, SysMenuEntity>
        implements SysMenuService {

    @Resource
    private SysUserService sysUserService;
    @Resource
    private Constant constant;

    @Override
    public String getCachePrefix() {
        return "SYS_MENU";
    }

    @Override
    public List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<SysMenuEntity> menuList = baseMapper.queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }
        List<SysMenuEntity> userMenuList = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }


    @Override
    @MyCacheable(key = "#root.target.cachePrefix + '_LIST_' + #userId")
    public List<SysMenuEntity> getUserMenuList(Long userId) {
        List<SysMenuEntity> allMenuList;
        // 系统管理员，拥有最高权限
        if (constant.adminId.equals(userId)) {
            allMenuList = getAllMenuList(null);
        } else {
            // 用户菜单列表
            List<Long> menuIdList = sysUserService.queryAllMenuId(userId);
            allMenuList = getAllMenuList(menuIdList);
        }
        return allMenuList;
    }

    @Override
    @MyCacheEvict(key = "#root.target.cachePrefix + '_LIST_' + #userId")
    public void clearUserMenuList(Long userId) {
    }

    @Override
    public List<SysMenuEntity> queryList(Map<String, Object> map) {
        return baseMapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return baseMapper.queryTotal(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteBatch(Long[] menuIds) {
        return baseMapper.deleteBatch(menuIds);
    }

    /**
     * 获取所有菜单列表
     */
    private List<SysMenuEntity> getAllMenuList(List<Long> menuIdList) {
        // 查询根菜单列表
        List<SysMenuEntity> menuList = queryListParentId(0L, menuIdList);
        // 递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归
     */
    private List<SysMenuEntity> getMenuTreeList(List<SysMenuEntity> menuList, List<Long> menuIdList) {
        List<SysMenuEntity> subMenuList = new ArrayList<SysMenuEntity>();
        for (SysMenuEntity entity : menuList) {
            // 目录
            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }
        return subMenuList;
    }
}
