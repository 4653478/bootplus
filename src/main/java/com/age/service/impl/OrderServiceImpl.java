package com.age.service.impl;

import com.age.dao.OrderDao;
import com.age.entity.OrderEntity;
import com.age.service.OrderService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author age
 * @Email 4653478@qq.com
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity>
        implements OrderService {



    @Override
    public List<OrderEntity> queryOrdersLastMonth(Long size) {
        return baseMapper.queryOrdersLastMonth(size);
    }

}
