package com.mmall.service;


import com.google.common.base.Splitter;
import com.mmall.BaseTest;
import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.util.DateTimeUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import com.mmall.vo.OrderItemVo;
import com.mmall.vo.OrderVo;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
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

    @Test
    public void manageDetailTest() {
        ServerResponse<OrderVo> serverResponse = iOrderService.manageDetail(1492091076073L);
        if (!serverResponse.isSuccess()) return;

        OrderVo orderVo = serverResponse.getData();
        System.out.println("订单编号" + orderVo.getOrderNo());
        System.out.println("订单金额" + orderVo.getPayment());
        System.out.println("收货人" + orderVo.getReceiverName());

        List<OrderItemVo> orderVoList = orderVo.getOrderItemVoList();
        Iterator<OrderItemVo> itemVoIterator = orderVoList.iterator();

        while (itemVoIterator.hasNext()){
            OrderItemVo orderItemVo = itemVoIterator.next();
            System.out.println(orderItemVo.getProductId());
            System.out.println(orderItemVo.getProductName());
            System.out.println(orderItemVo.getQuantity());
            System.out.println(orderItemVo.getTotalPrice());
        }

    }

}
