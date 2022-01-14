package com.age.dao;

import com.age.entity.OrderEntity;

import java.util.List;

/**

 */
public interface OrderDao extends BaseDao<OrderEntity> {

    /**

     */
    List<OrderEntity> queryOrdersLastMonth(Long size);


}
