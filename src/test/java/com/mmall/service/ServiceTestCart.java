package com.mmall.service;


import com.google.common.base.Splitter;
import com.mmall.BaseTest;
import com.mmall.dao.CartMapper;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceTestCart extends BaseTest {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ICartService iCartService;

    @Test
    public void deleteCartProduct(){
        String productIds = "26";
        List<String> productList = Splitter.on(",").splitToList(productIds);
        cartMapper.deleteByUserIdProductIds(1,productList);
        CartVo cartVo =  iCartService.list(1).getData();
        List<CartProductVo> cartList = cartVo.getCartProductVoList();

        for (CartProductVo cartProductVo : cartList){
            System.out.println(cartProductVo.getProductId());
            System.out.println(cartProductVo.getProductName());
            System.out.println(cartProductVo.getQuantity());
        }

    }

}
