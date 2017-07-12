package com.mmall.dao;

import com.mmall.pojo.Cart;
import com.mmall.pojo.Category;

import java.util.List;

public interface CartMapper {

    int deleteByPrimaryKey(Integer id);

    //有字段的空判断
    int insert(Cart record);

    //没有字段的空判断
    int insertSelective(Cart record);

    Cart  selectByPrimaryKey(Integer id);

    //有字段的空判断
    int updateByPrimaryKeySelective(Cart record);

    //没有字段的空判断
    int updateByPrimaryKey(Cart record);

}