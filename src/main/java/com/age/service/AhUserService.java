package com.age.service;

import com.age.entity.AhUserEntity;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户
 */
public interface AhUserService extends IService<AhUserEntity> {

    void save(AhUserEntity user) throws Exception;

    void updateUser(AhUserEntity entity);

    void deleteBatch(Long[] userIds);

    /**
     * 查询列表
     *
     * @param offset   开始
     * @param limit    条数
     * @param sort     排序字段
     * @param order    是否为升序
     * @return Page<SysUserEntity>
     */
    Page<AhUserEntity> queryListByPage(Integer offset, Integer limit, String did, String sort, Boolean order);

}
