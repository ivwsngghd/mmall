package com.mmall.service;


import com.mmall.BaseTest;
import com.mmall.common.ServerResponse;
import com.mmall.dao.OrderItemMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.vo.OrderVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceTestOrderItem extends BaseTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    public void orderSelectByOrderNoAndUserId() {
        List<OrderItem> orderItemList = orderItemMapper.getByOrderNoUserId(1492089528889l, 1);
        for (OrderItem orderItem : orderItemList) {
            System.out.println(orderItem.getProductName());
            System.out.println(orderItem.getCurrentUnitPrice());
            System.out.println();
        }
    }




}
