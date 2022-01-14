package com.age.service;

import com.age.entity.OrderEntity;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**

 */
public interface OrderService extends IService<OrderEntity> {

    List<OrderEntity> queryOrdersLastMonth(Long size);
}
