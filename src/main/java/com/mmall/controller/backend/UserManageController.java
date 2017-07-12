package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponce;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by 张柏桦 on 2017/7/10.
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponce<User> login(String username, String password, HttpSession session){
        ServerResponce<User> responce = iUserService.login(username,password);
        if(responce.isSeccess()){
            User user =  responce.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                //登陆的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return responce;
            }else {
                return ServerResponce.createByErrorMessage("不是管理员，无法登陆");
            }
        }
        return responce;
    }
}
