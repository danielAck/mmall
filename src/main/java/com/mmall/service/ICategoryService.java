package com.mmall.service;

import com.mmall.common.ServerResponce;
import com.mmall.pojo.Category;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 张柏桦 on 2017/7/11.
 */

public interface ICategoryService {

    ServerResponce addCategory(String categoryName, Integer parentId);

    ServerResponce updateCategoryName(Integer categoryId,String categoryName);

    ServerResponce<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponce<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
