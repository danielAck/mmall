package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    //有字段的空判断
    int insert(Cart record);

    //没有字段的空判断
    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    //有字段的空判断
    int updateByPrimaryKeySelective(Cart record);

    //没有字段的空判断
    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUerIdProductIds(@Param("userId") Integer userId,@Param("productIdList") List<String> productIdlist);

    int checkedOrUnCheckedProduct(@Param("userId") Integer userId,@Param("checked") Integer checked,@Param("productId") Integer productId);

    int selectCartProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}