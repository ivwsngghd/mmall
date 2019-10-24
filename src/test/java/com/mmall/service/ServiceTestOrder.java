package com.mmall.service;


import com.google.common.base.Splitter;
import com.mmall.BaseTest;
import com.mmall.common.Const;
import com.mmall.dao.CartMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.pojo.Order;
import com.mmall.util.DateTimeUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class ServiceTestOrder extends BaseTest {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IOrderService iOrderService;

    @Test
    public void orderSelectByOrderNoAndUserId(){
        Order order = orderMapper.selectByUserIdAndOrderNo(1,1491830695216L);
        System.out.println(order.getPayment());
        System.out.println(order.getId());

    }

    @Test
    public void orderCallBack(){
        String tradeStatus = "TRADE_SUCCESS";
        Order order = orderMapper.selectByOrderNo(1492091102375L);
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            System.out.println("付款状态更新成功，开始更新订单信息");
            orderMapper.updateByPrimaryKeySelective(order);
            System.out.println("订单状态更新成功");
        }
    }


}
