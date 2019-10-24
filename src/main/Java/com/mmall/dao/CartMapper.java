package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId,@Param("productId")Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    /**
     * 查询该用户的购物车的未选中商品数量；用于检查是否已经全选
     * @param userId
     * @return 返回值是未选数量
     */
    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,@Param("productId")Integer productId, @Param("checked")Integer checked);

    Integer selectCartProductCount(Integer userId);


    /**
     * 查询购物车中被勾选的数据
     * @param userId
     * @return
     */
    List<Cart> selectCheckedCartByUserId(Integer userId);

}