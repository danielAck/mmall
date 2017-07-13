package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponceCode;
import com.mmall.common.ServerResponce;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 张柏桦 on 2017/7/11.
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private  IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    //添加 category
    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponce addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSeccess()){
            //是管理员
            //增加处理分类的逻辑
            return iCategoryService.addCategory(categoryName, parentId);

        }else {
            return  ServerResponce.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    //修改category的名称
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponce setCategoryName(HttpSession session,Integer categoryId,String categoryName){

        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSeccess()){
            //是管理员
            //更新categoryName
            return iCategoryService.updateCategoryName(categoryId,categoryName);

        }else {
            return  ServerResponce.createByErrorMessage("无权限操作，需要管理员权限");
        }

    }

    //查询 子平级category
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponce getChildrenParallelCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSeccess()){
            //是管理员
            //查询子节点的Category信息，并且不递归，保持评级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else {
            return  ServerResponce.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }

    //深度查询category
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponce getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user==null){
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        //校验一下是否是管理员
        if(iUserService.checkAdminRole(user).isSeccess()){
            //查询当前节点的id和递归子节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else {
            return  ServerResponce.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }
}
