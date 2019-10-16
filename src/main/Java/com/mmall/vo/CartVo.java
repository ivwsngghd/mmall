package com.mmall.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车；存放的商品列表
 */
public class CartVo {
    private List<CartProductVo> cartProductVoList;  //购物车存放的商品列表
    private BigDecimal cartTotalPrice;  //  商品的总价
    private Boolean allChecked; //均已勾选
    private String imageHost;   //图片主地址

    public List<CartProductVo> getCartProductVoList() {
        return cartProductVoList;
    }

    public void setCartProductVoList(List<CartProductVo> cartProductVoList) {
        this.cartProductVoList = cartProductVoList;
    }

    public BigDecimal getCartTotalPrice() {
        return cartTotalPrice;
    }

    public void setCartTotalPrice(BigDecimal cartTotalPrice) {
        this.cartTotalPrice = cartTotalPrice;
    }

    public Boolean getAllChecked() {
        return allChecked;
    }

    public void setAllChecked(Boolean allChecked) {
        this.allChecked = allChecked;
    }

    public String getImageHost() {
        return imageHost;
    }

    public void setImageHost(String imageHost) {
        this.imageHost = imageHost;
    }
}
