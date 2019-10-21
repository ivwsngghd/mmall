package com.mmall.controller.protal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    /**
     *
     * @param session 用于校验用户登陆
     * @param orderNo 要处理的订单号
     * @param request 获取servlet上下文，获取upload文件夹，把二维码传送到ftp服务器
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(),orderNo,path);
    }


    /**
     *
     * @param request 支付宝把订单信息封装在请求里，服务器对信息进行确认(信息是否支付宝发送，订单是否对应，信息是否一致 等)
     * @return 支付宝只识别两个字符串 "success 和 failed"  返回一个object就可以了
     */
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();

        Map requestParams = request.getParameterMap(); //<String,String []>
        for (Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0;i < values.length;i++){
                //多个元素按“，”分割元素
                valueStr = (i == values.length-1)? valueStr + values[i] : valueStr + values[i] + "," ;
            }
            System.out.println(name + " : " + valueStr);
            params.put(name,valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要，验证回调的正确性(是否来自于支付宝)，并且还要避免重复通知；

        params.remove("sign_type");
        try {
            /**
             * RSA加密算法的公钥私钥用途：
             * 公钥加密，私钥解密。
             * 私钥数字签名，公钥验证。
             */
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if (!alipayRSACheckedV2){
                return ServerResponse.createByErrorByMessage("非法请求，验证不通过！");
            }

        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常，RSA验证失败",e);
        }



        //todo 验证各种数据
        //各种验证通过，利用支付宝发送的回调信息调用service层修改支付状态信息
        ServerResponse serverResponse = iOrderService.aliCallback(params);
        if (serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }


    /**
     * 用于提供给前端确认当前登录的用户是否已经把订单信息处理完毕了；
     * @param session
     * @param orderNo
     * @return
     */
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponse<Boolean> query_order_pay_status(HttpSession session, Long orderNo){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (serverResponse.isSuccess()){
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }

}
