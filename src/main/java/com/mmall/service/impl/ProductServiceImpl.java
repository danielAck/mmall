package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponceCode;
import com.mmall.common.ServerResponce;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import com.sun.scenario.effect.impl.prism.PrDrawable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceConfigurationError;

/**
 * Created by 张柏桦 on 2017/7/11.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    //平级调用 -> Service调Service
    @Autowired
    private ICategoryService iCategoryService;
    public ServerResponce saveOrUpdateProduct(Product product){
        if(product != null){

            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId() != null){
                //通过 id 更新产品信息
                int rowCount = productMapper.updateByPrimaryKey(product);
                if(rowCount > 0){
                    return ServerResponce.createBySuccess("更新产品成功");
                }
                return ServerResponce.createByErrorMessage("更新产品失败");
            }else{
                //新增商品
                int rowCount = productMapper.insert(product);
                if(rowCount > 0){
                    return ServerResponce.createBySuccess("新增商品成功");
                }
                return ServerResponce.createByErrorMessage("新增商品失败");
            }
        }

        return ServerResponce.createByErrorMessage("新增产品参数不正确");
    }

    public ServerResponce<String> setSaleStatus(Integer productId,Integer status){
        if(productId == null|| status == null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.ILLEGAL_ARGUMENT.getCode(),ResponceCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0){
            return ServerResponce.createBySuccess("修改产品销售状态成功");
        }
        return ServerResponce.createByErrorMessage("修改产品销售状态失败");
    }


    public ServerResponce<ProductDetailVo> manageProductDetail(Integer productId){
        if(productId == null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.ILLEGAL_ARGUMENT.getCode(),ResponceCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponce.createByErrorMessage("产品已下架或删除");
        }
//        vo对象--value Object
        //pojo -> bo(bussiness object) -> vo(value object)
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponce.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){

        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImage(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost
        //从配置文件中以流的形式读取
        productDetailVo.setImagHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        //parentCategoryId
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setPareantCategoryId(0);//默认根节点
            //FIXME
        }else {
            productDetailVo.setPareantCategoryId(category.getId());
        }

        //createTime
        //updateTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }

    public ServerResponce<PageInfo> getProductList(int pageNum,int pageSize){
        //startpage
        //填充自己的sql查询逻辑
        //pageHelper -收尾

        // -1.start
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        // -2.填充sql查询逻辑
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        // -3.收尾
//        PageInfo pageResult = new PageInfo(productList);
//        pageResult.setList(productListVoList);
//        return ServerResponce.createBySuccess(pageResult);
//        //TODO??
        PageInfo pageResult = new PageInfo(productListVoList);
        return ServerResponce.createBySuccess(pageResult);

    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubTitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());

        return productListVo;
    }

    public ServerResponce<PageInfo> searchProduct(String productName,Integer productId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVos.add(productListVo);
        }
        //重新 set List
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVos);
        return ServerResponce.createBySuccess(pageResult);
    }

    public ServerResponce<ProductDetailVo> getProdctDetail(Integer productId){
        if(productId == null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.ILLEGAL_ARGUMENT.getCode(),ResponceCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponce.createByErrorMessage("产品已下架或删除");
        }
        if(product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()){
            return ServerResponce.createByErrorMessage("产品已下架或删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ServerResponce.createBySuccess(productDetailVo);
    }

    public ServerResponce<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){

        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.ILLEGAL_ARGUMENT.getCode(),ResponceCode.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类，并且还没有关键字，这时候返回一个空的结果集，不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoArrayList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoArrayList);
                return ServerResponce.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.selectCategoryAndChildrenById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }
        PageHelper.startPage(pageNum,pageSize);

        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.productListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                //pageHelper 的orderBy方法参数 是"排序字段 排序方法(desc/asc)"
                //组装 通过split后的orderBy参数的两个元素(排序字段 排序方法)
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVoList);
        return ServerResponce.createBySuccess(pageInfo);
    }
}
