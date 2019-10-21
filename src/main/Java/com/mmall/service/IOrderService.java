package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

public interface IOrderService {
    ServerResponse pay(Integer userId, Long orderNo, String path);

    /**
     * 该接口用于验证回调数据，是否存在该订单，该订单是否已支付(重复通知)，
     * @param params
     * @return
     */
    ServerResponse aliCallback(Map<String,String> params);

    /**
     * 验证该用户的订单信息是否已经完成？
     * @param userId
     * @param orderNo
     * @return
     */
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
