package com.age.dao;

import com.age.entity.AhUserEntity;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ah用户

 */
public interface AhUserDao extends BaseDao<AhUserEntity> {




    void updateUser(AhUserEntity entity);


}
