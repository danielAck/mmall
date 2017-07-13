package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponce;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by 张柏桦 on 2017/7/13.
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    public ServerResponce add(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int resultCount = shippingMapper.insert(shipping);
        if (resultCount > 0) {
            Map result = Maps.newHashMap();
            result.put("shippingId",shipping.getId());
            return ServerResponce.createBySuccess("新建地址成功",result);
        }
        return ServerResponce.createByErrorMessage("新建地址失败");
    }

    public ServerResponce<String> del(Integer userId,Integer shippingId){
        int resultCount = shippingMapper.deleteByShippingIdAndUserId(userId,shippingId);
        if (resultCount > 0) {
            return ServerResponce.createBySuccess("删除地址成功");
        }
        return ServerResponce.createByErrorMessage("删除地址失败");
    }

    public ServerResponce update(Integer userId, Shipping shipping){
        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShipping(shipping);
        if (resultCount > 0) {
            return ServerResponce.createBySuccess("更新地址成功");
        }
        return ServerResponce.createByErrorMessage("更新地址失败");
    }

    public ServerResponce<Shipping> select(Integer userId, Integer shippingId){
        Shipping shipping = shippingMapper.selectByShippingIdAndUserId(userId,shippingId);
        if (shipping == null) {
            return ServerResponce.createByErrorMessage("无法查询到该地址");
        }
        return ServerResponce.createBySuccess("查询地址成功",shipping);
    }

    public ServerResponce<PageInfo> list(Integer userId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo pageInfo = new PageInfo(shippingList);
        return ServerResponce.createBySuccess(pageInfo);
    }
}
