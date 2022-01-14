package com.age.service.impl;

import com.age.dao.AhMsgDao;
import com.age.dao.AhUserDao;
import com.age.entity.AhMsgEntity;
import com.age.entity.AhUserEntity;
import com.age.service.AhMsgService;
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
public class AhMsgServiceImpl extends ServiceImpl<AhMsgDao, AhMsgEntity> implements AhMsgService {

    @Resource
    private AhMsgDao dao;

    @Override
    @Transactional
    public void save(AhMsgEntity msg) throws Exception {
        //user.setCreateTime(DateUtils.);
        //sha256加密
        //user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        baseMapper.insert(msg);
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        baseMapper.deleteBatch(userId);
    }

    @Override
    public Page<AhMsgEntity> queryListByPage(Integer offset, Integer limit, int from_user,String sort, Boolean order) {
        Wrapper<AhMsgEntity> wrapper = new EntityWrapper<AhMsgEntity>();
        if (StringUtils.isNoneBlank(sort) && null != order) {
            wrapper.orderBy(sort, order);
        }
        if (from_user>0) {
            wrapper.eq("from_user", from_user);
        }

        Page<AhMsgEntity> page = new Page<>(offset, limit);
        return this.selectPage(page, wrapper);
    }


}
