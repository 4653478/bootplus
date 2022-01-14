package com.age.service.impl;

import com.age.dao.AhUserDao;
import com.age.entity.AhUserEntity;
import com.age.service.AhUserService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


@Service
public class AhUserServiceImpl extends ServiceImpl<AhUserDao, AhUserEntity> implements AhUserService {

    @Resource
    private AhUserDao dao;

    @Override
    @Transactional
    public void save(AhUserEntity user) throws Exception {
        //user.setCreateTime(DateUtils.);
        // sha256加密
        //user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        baseMapper.insert(user);

    }

    @Override
    @Transactional
    public void updateUser(AhUserEntity entity) {

        dao.updateUser(entity);
        // 保存用户与角色关系
        //sysUserRoleService.saveOrUpdate(entity.getUserId(), entity.getRoleIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        baseMapper.deleteBatch(userId);
    }

    @Override
    public Page<AhUserEntity> queryListByPage(Integer offset, Integer limit, String did,String sort, Boolean order) {
        Wrapper<AhUserEntity> wrapper = new EntityWrapper<AhUserEntity>();
        if (StringUtils.isNoneBlank(sort) && null != order) {
            wrapper.orderBy(sort, order);
        }
        if (StringUtils.isNoneBlank(did)) {
            wrapper.like("did", did);
        }

        Page<AhUserEntity> page = new Page<>(offset, limit);
        return this.selectPage(page, wrapper);
    }


}
