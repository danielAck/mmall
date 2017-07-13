package com.mmall.service;

import com.mmall.common.ServerResponce;
import com.mmall.vo.CartVo;
import org.springframework.stereotype.Service;

/**
 * Created by 张柏桦 on 2017/7/12.
 */

public interface ICartService {

    ServerResponce<CartVo> add(Integer userId, Integer productId, Integer count);

    ServerResponce<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponce<CartVo> deleteProduct (Integer userId,String productIds);

    ServerResponce<CartVo> selectOrUnSelect (Integer userId,Integer checked ,Integer productId);

    ServerResponce<CartVo> list (Integer userId);

    ServerResponce<Integer> getPeoductCount(Integer userId);
}
