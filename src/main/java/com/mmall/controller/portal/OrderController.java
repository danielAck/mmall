package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponceCode;
import com.mmall.common.ServerResponce;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.params.Params;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 张柏桦 on 2017/7/14.
 */
@Controller
@RequestMapping("/order/")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService iOrderService;

    //扫码支付
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponce pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        //TODO getServletContex().getRealPath()?
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo, user.getId(), path);
    }

    //支付宝回调
    @RequestMapping("alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request) {

        //用 Map 承载支付宝回调分割好的的内容
        Map<String, String> params = Maps.newHashMap();

        //拿到支付宝的回调
        Map requestParams = request.getParameterMap();
        // Map方法中的 keySet用于获取Map中所有的key值
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valuestr = "";
            //拼接string数组中的元素并用逗号分隔
            for (int i = 0; i < values.length; i++) {
                //遍历从0开始，因此 判断条件为 (i == values.length-1)
                valuestr = (i == values.length - 1) ? valuestr + values[i] : valuestr + values[i] + ",";
            }
            params.put(name, valuestr);
        }
        logger.info("支付宝回调，sign：{}，trade_satatus：{}，参数：{}", params.get("sign"), params.get("trade_status"), params.toString());

        //非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (alipayRSACheckedV2) {
                return ServerResponce.createByErrorMessage("非法请求，验证不通过，再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }
        //TODO 验证各种数据

        ServerResponce serverResponce = iOrderService.aliCallBack(params);
        if (serverResponce.isSeccess()) {
            return Const.AlipayCallback.RESPONCE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONCE_FAILED;
    }

    //前端轮询订单状态
    @RequestMapping("query_order_pay_status.do")
    @ResponseBody
    public ServerResponce<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        ServerResponce serverResponce = iOrderService.queryOrderPayStatus(user.getId(),orderNo);
        if (serverResponce.isSeccess()) {
            return serverResponce.createBySuccess(true);
    }
        return serverResponce.createBySuccess(false);
    }

    //生成订单
    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponce create(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    //取消订单接口
    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponce cancel(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.cancel(user.getId(),orderNo);
    }

    //获取购物车中已经勾选的商品
    @RequestMapping("get_order_cart_product.do")
    @ResponseBody
    public ServerResponce getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    //获得订单详情
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponce detail(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    //获得订单列表
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponce list(HttpSession session,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponce.createByErrorCodeMessage(ResponceCode.NEED_LOGIN.getCode(), ResponceCode.NEED_LOGIN.getDesc());
        }
        return iOrderService.getOrderList(user.getId(),pageSize,pageNum);
    }
}