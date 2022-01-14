package com.age.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.ImmutableMap;
import com.age.config.aop.service.BaseAopService;
import com.age.dao.SysRoleMenuDao;
import com.age.entity.SysRoleMenuEntity;
import com.age.frame.cache.annotation.MyCacheEvict;
import com.age.frame.cache.annotation.MyCacheable;
import com.age.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 角色与菜单对应关系
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Service
public class SysRoleMenuServiceImpl extends BaseAopService<SysRoleMenuServiceImpl, SysRoleMenuDao, SysRoleMenuEntity>
        implements SysRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    @MyCacheEvict(key = "#root.target.cachePrefix + '_' + #roleId")
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        if (CollectionUtil.isEmpty(menuIdList)) {
            return;
        }
        // 先删除角色与菜单关系
        baseMapper.deleteNoMapper(roleId);

        // 保存角色与菜单关系
        Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("roleId", roleId)
                .put("menuIdList", menuIdList).build();
        // 一定要加上 self 关键字，不然缓存切面不会生效
//        self.clearMenuIdList(roleId);
        baseMapper.save(map);
    }

    @MyCacheable(key = "#root.target.cachePrefix + '_' + #roleId")
    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    /**
     * 清除缓存
     *
     * @param roleId 角色ID
     */
    @Override
    @MyCacheEvict(key = "#root.target.cachePrefix + '_' + #roleId")
    public void clearMenuIdList(Long roleId) {
    }

    @Override
    public String getCachePrefix() {
        return "SYS_ROLE_MENU";
    }

}
