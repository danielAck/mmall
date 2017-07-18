package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by 张柏桦 on 2017/7/10.
 */
public class Const {

    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    public interface Cart{
        int CHECKED = 1;//即购物车为选中状态
        int UN_CHECKED = 0;//购物车中未选中状态

        String LIMIT_NUM_FAIL = "LIMIT_NUM_FAIL";
        String LIMIT_NUM_SUCCESS = "LIMIT_NUM_SUCCESS";
    }

    public interface productListOrderBy{
        //Set集合的 contains方法时间复杂度是 O(1)，而 List集合 contain方法时间复杂度是 O(n)
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    //通过内部接口类实现分组
    //比 枚举 更轻
    public interface Role{
        int ROLE_CUSTOMER = 0;//普通用户
        int ROLE_ADMIN = 1;//管理员
    }

    public enum  ProductStatusEnum{
        ON_SALE("在线",1);

        private  String  value;
        private int code;

        ProductStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }

    public enum OrderStatusEnum{

        CANCELED("已取消",0),
        NO_PAY("未支付",10),
        PAID("已付款",20),
        SHIPPED("已发货",40),
        OEDER_FINISHED("订单完成",50),
        ORDER_CLOSED("订单关闭",60)
        ;

        OrderStatusEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private  int code;

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

        public static OrderStatusEnum codeOf(int code){
            for(OrderStatusEnum orderStatusEnum : values()){
                if(orderStatusEnum.getCode() == code){
                    return orderStatusEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

    public interface AlipayCallback{

        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";

        String RESPONCE_SUCCESS = "success";
        String RESPONCE_FAILED = "failed";
    }

    public enum payPlatformEnum{

        ALIPAY("支付宝",1),
        WIXIN("微信",2);

        payPlatformEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private  int code;

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

    public enum paymentTypeEnum{
        ONLINE_PAY("在线支付",1);

        paymentTypeEnum(String value, int code) {
            this.value = value;
            this.code = code;
        }

        private String value;
        private int code;

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

        public static paymentTypeEnum codeOf(int code){
            for(paymentTypeEnum paymentTypeEnum : values()){
                if(paymentTypeEnum.getCode() == code){
                    return paymentTypeEnum;
                }
            }
            throw new RuntimeException("没有找到对应的枚举");
        }
    }

}
