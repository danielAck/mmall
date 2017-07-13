package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponce;
import com.mmall.pojo.Shipping;

/**
 * Created by 张柏桦 on 2017/7/13.
 */
public interface IShippingService {
    ServerResponce add(Integer userId, Shipping shipping);

    ServerResponce<String> del(Integer userId,Integer shippingId);

    ServerResponce update(Integer userId, Shipping shipping);

    ServerResponce<Shipping> select(Integer userId, Integer shippingId);

    ServerResponce<PageInfo> list(Integer userId, int pageNum, int pageSize);
}
