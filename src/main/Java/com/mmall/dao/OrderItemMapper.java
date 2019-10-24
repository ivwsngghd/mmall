package com.mmall.dao;

import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

    /**
     * 用于查找同一订单的所有商品信息，并且返回
     * @param orderNo 要查询的订单号
     * @param userId 订单的拥有者
     * @return
     */
    List<OrderItem> getByOrderNoUserId(@Param("orderNo")Long orderNo,@Param("userId")Integer userId);

    List<OrderItem> getByOrderNo(@Param("orderNo")Long orderNo);

    void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);

}