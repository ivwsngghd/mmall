package com.mmall.dao;

import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    /**
     * 通过userId和订单Id查询订单是否存在，并且返回
     * @param userId
     * @param orderNo
     * @return
     */
    Order selectByUserIdAndOrderNo(@Param("userId") Integer userId,@Param("orderNo")Long orderNo);

    /**
     * 通过订单号查询订单是否存在；
     * @param orderNo
     * @return
     */
    Order selectByOrderNo(Long orderNo);

}