package com.mmall.common;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Const {
    public static final String CURRENT_USER = "currentUser";
    public final static String EMAIL = "email";
    public final static String USERNAME = "username";
    public static final String TOKEN_PREFIX = "token_";     //用于找回密码答案的cookie前缀，存活12小时

    public interface  RedisCacheExtime{
        int REDIS_SESSION_EXTIME = 60 * 30; //session存活时间为30分钟
    }

    public interface ProductListOrderBy{
        //前面代表orderBy哪个字段，后面是升序还是降序
        //Set的Contains的时间复杂度是O(1)
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");

    }

    public interface Cart{
        int CHECKED = 1; //即购物车选中状态；
        int UN_CHECKED = 0; //购物车未被选中状态；

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface Role{
        public final static int ROLE_CUSTOMER = 0; //普通用户
        int ROLE_ADMIN = 1; //管理员
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在售");

        private String value;
        private int code;
        ProductStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }
    }

    public enum OrderStatusEnum{
        CANCELED(0,"已取消"),
        NO_PAY(10,"未支付"),
        PAID(20,"已付款"),
        SHIPPED(40,"已发货"),
        ORDER_SUCCESS(50,"订单完成"),
        ORDER_CLOSED(60,"订单关闭");

        private  String value;
        private int code;

        OrderStatusEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public interface AlipayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONSE_SUCCESS = "success";
        String RESPONSE_FAILED = "failed";

    }

    public enum PayPlatformEnum{
        ALIPAY(1,"支付宝");

        private  String value;
        private int code;

        PayPlatformEnum(int code,String value){
            this.code = code;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum PaymentTypeEnum{
        ONLINE_PAY(1,"在线支付");

        PaymentTypeEnum(int code,String value){
            this.code = code;
            this.value = value;
        }
        private String value;
        private int code;

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }


        public static PaymentTypeEnum codeOf(int code){
            for(PaymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }

    }

    public interface REDIS_LOCK{
        String CLOSE_ORDER_TASK_LOCK = "CLOSE_ORDER_TASK_LOCK"; //关闭订单的分布式锁
    }

    public interface INDEX_CACHE_KEYWORDS{
        String KEYWORDS_CACHE = "KEYWORDS_CACHE:";
        Set<String> ICKEYWORDS = Sets.newHashSet("手机","数码","电脑","办公配件","电视","空调","冰箱","洗衣机","厨卫家电","小家电","家具","家装","护肤化妆","清洁用品","纸品","母婴用品","儿童玩具","童装童鞋","鞋靴","箱包","钟表","珠宝","图书","音像","电子书","挂饰");

    }

}
