package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponceCode;
import com.mmall.common.ServerResponce;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import com.mmall.service.IUserService;
import com.mmall.service.impl.UserServiceImpl;
import com.mmall.vo.OrderVo;
import net.sf.jsqlparser.schema.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 张柏桦 on 2017/7/16.
 */
@Controller
@RequestMapping("/manage/order/")
public class OrderManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IOrderService iOrderService;

    //后台获取订单List页
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponce<PageInfo> orderList(HttpSession session , @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSeccess()) {
            //填充业务逻辑
            return iOrderService.manageList(pageNum,pageSize);
        }else{
            return ServerResponce.createByErrorMessage("无权限操作");
        }
    }

    //后台获得订单详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponce<OrderVo> detail(HttpSession session , Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSeccess()) {
            //填充业务逻辑
            return iOrderService.manageDetail(orderNo);
        }else{
            return ServerResponce.createByErrorMessage("无权限操作");
        }
    }

    //后台搜索
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponce<PageInfo> orderSearch(HttpSession session , Long orderNo,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSeccess()) {
            //填充业务逻辑
            return iOrderService.manageSearch(orderNo,pageNum,pageSize);
        }else{

            return ServerResponce.createByErrorMessage("无权限操作");
        }
    }

    //发货
    @RequestMapping("send_goods.do")
    @ResponseBody
    public ServerResponce<String> orderSendGoods(HttpSession session , Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSeccess()) {
            //填充业务逻辑
            return iOrderService.manageSendGoods(orderNo);
        }else{
            return ServerResponce.createByErrorMessage("无权限操作");
        }
    }


}
