package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public interface IProductService {
    /**
     * 保存或者新增商品
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product);

    /**
     * 更新商品状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse<String> setSaleStatus(Integer productId,Integer status);


    /**
     * 该方法用于获取产品的详细信息的，需要使用到一个具体的，VO封装类，譬如分页对象
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);


    /**
     * 获取产品List
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse getProductList(int pageNum,int pageSize);


    /**
     * 利用产品名字模糊获取产品列表
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
