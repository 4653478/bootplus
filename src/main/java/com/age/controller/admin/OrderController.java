package com.age.controller.admin;

import com.age.datasources.DataSource;
import com.age.datasources.DataSourceContextHolder;
import com.age.entity.OrderEntity;
import com.age.service.OrderService;
import com.age.util.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**

 */
@RestController
@RequestMapping("/admin/order")
public class OrderController extends AbstractController {

    @Resource
    private OrderService orderService;

    /**
     *
     */
    @RequestMapping("/select")
    //@RequiresPermissions("sys:role:select")
    @DataSource(DataSourceContextHolder.DATA_SOURCE_RW)
    public R select(Long size) {
        // 查询列表数据
        List<OrderEntity> list = orderService.queryOrdersLastMonth(size);

        return R.ok().put("list", list);
    }






}
