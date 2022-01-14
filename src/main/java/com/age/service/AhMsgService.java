package com.age.service;

import com.age.entity.AhMsgEntity;
import com.age.entity.AhUserEntity;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户
 */
public interface AhMsgService extends IService<AhMsgEntity> {

    void save(AhMsgEntity user) throws Exception;

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
    Page<AhMsgEntity> queryListByPage(Integer offset, Integer limit, int from_user, String sort, Boolean order);

}
