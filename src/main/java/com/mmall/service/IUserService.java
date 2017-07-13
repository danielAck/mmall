package com.mmall.service;

import com.mmall.common.ServerResponce;
import com.mmall.pojo.User;

import java.rmi.ServerError;

/**
 * Created by 张柏桦 on 2017/7/10.
 */

//取名 I 开头目的是看名字能清楚地的识别为接口类
public interface IUserService {

    ServerResponce<User> login(String uername, String password);

    ServerResponce<String> register(User user);

    ServerResponce<String> checkValid(String str,String type);

    ServerResponce<String> selectQuestion(String username);

    ServerResponce<String> checkAnswer(String username,String question,String answer);

    ServerResponce<String> forgetResetPassword(String username,String passwordNew,String forgetToken);

    ServerResponce<String> resetPassword(String passwordOld,String passwordNew,User user);

    ServerResponce<User> updateInformation(User user);

    ServerResponce<User> getInformation(Integer userId);

    ServerResponce checkAdminRole(User user);
}
