package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponce;
import com.mmall.vo.OrderVo;

import java.util.Map;

/**
 * Created by 张柏桦 on 2017/7/14.
 */
public interface IOrderService {

    ServerResponce pay(Long orderNo, Integer userId, String path);

    ServerResponce aliCallBack(Map<String,String> params);

    ServerResponce queryOrderPayStatus(Integer userid,Long orderNo);

    ServerResponce createOrder(Integer userId,Integer shipppingId);

    ServerResponce<String> cancel(Integer userId,Long orderNo);

    ServerResponce getOrderCartProduct(Integer userId);

    ServerResponce<OrderVo> getOrderDetail(Integer userid, Long orderNo);

    ServerResponce<PageInfo> getOrderList(Integer userId, int pageSize, int pageNum);

    //backend
    ServerResponce<PageInfo> manageList(int pageNum,int pageSize);

    ServerResponce<OrderVo> manageDetail(Long orderNo);

    ServerResponce<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    ServerResponce<String> manageSendGoods(Long orderNo);
}
