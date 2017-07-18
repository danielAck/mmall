package com.mmall.vo;

import com.mmall.pojo.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by 张柏桦 on 2017/7/15.
 */
public class OrderProductVo {

    private List<OrderItemVo> orderItemVoList;

    private BigDecimal productTotalPrice;

    private String imagHost;

    public List<OrderItemVo> getOrderItemVoList() {
        return orderItemVoList;
    }

    public void setOrderItemVoList(List<OrderItemVo> orderItemVoList) {
        this.orderItemVoList = orderItemVoList;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public String getImagHost() {
        return imagHost;
    }

    public void setImagHost(String imagHost) {
        this.imagHost = imagHost;
    }
}
