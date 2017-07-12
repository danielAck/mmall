package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponce;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by 张柏桦 on 2017/7/11.
 */
 public interface IProductService {

    ServerResponce saveOrUpdateProduct(Product product);

    ServerResponce<String> setSaleStatus(Integer productId,Integer status);

    ServerResponce<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponce<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponce<PageInfo> searchProduct(String productName,Integer productId, int pageNum, int pageSize);

   ServerResponce<ProductDetailVo> getProdctDetail(Integer productId);

    ServerResponce<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
