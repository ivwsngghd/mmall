package com.mmall.dao;

import com.mmall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByPrimaryKeyUserId(@Param("userId") Integer userId, @Param("shippingId") Integer shippingId);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByPrimaryKeyUserId(Shipping record);

    Shipping selectByPrimaryKeyUserId(@Param("userId") Integer userId, @Param("id") Integer id);

    List<Shipping> selectByUserId(Integer userId);

}