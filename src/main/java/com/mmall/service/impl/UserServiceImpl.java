package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponce;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by 张柏桦 on 2017/7/10.
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponce<User> login(String username, String password) {

        int resultCount = userMapper.checkUsername(username);
        if(resultCount == 0 ){
            return ServerResponce.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user  = userMapper.selectLogin(username,md5Password);
        if(user == null){
            return ServerResponce.createByErrorMessage("密码错误");
        }

        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponce.createBySuccess("登录成功",user);
    }

    public ServerResponce<String> register(User user){
        //代码复用，调用本类的 checkValid方法
        ServerResponce validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSeccess()){
            return validResponse;
        }

        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSeccess()){
            return validResponse;
        }

        int resultCount = userMapper.checkEmail(user.getEmail());
        if(resultCount > 0){
            return ServerResponce.createByErrorMessage("email已存在");
        }

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return ServerResponce.createByErrorMessage("服务器内部错误，注册失败");
        }
        return ServerResponce.createBySuccessMessage("注册成功");
    }

    public ServerResponce<String> checkValid(String str,String type){
        //type 参数不为 " " 或 空
        if (StringUtils.isNotBlank(type)){
            //开始校验
            if(Const.USERNAME.equals(type)){
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0){
                    return ServerResponce.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponce.createByErrorMessage("邮箱已存在");
                }
            }
        }else{
            return ServerResponce.createByErrorMessage("参数错误");
        }

        return ServerResponce.createBySuccessMessage("校验成功");
    }

    public ServerResponce<String> selectQuestion(String username){
        ServerResponce validResponce = this.checkValid(username,Const.USERNAME);
        if(validResponce.isSeccess()){
            //用户名不存在
            return ServerResponce.createByErrorMessage("用户名不存在");
        }
        String question = userMapper.selecQuestionByUserName(username);
        if (StringUtils.isNotBlank(question)){
            return ServerResponce.createBySuccess(question);
        }
        return ServerResponce.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponce<String> checkAnswer(String username,String question,String answer){
        int resultCount= userMapper.checkAnswer(username,question,answer);
        if( resultCount > 0){
            //说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username,forgetToken);
            return ServerResponce.createBySuccess(forgetToken);
        }
        return ServerResponce.createBySuccessMessage("问题答案错误");
    }

    public ServerResponce<String> forgetResetPassword(String username,String passwordNew,String forgetToken){
        if(org.apache.commons.lang3.StringUtils.isBlank(forgetToken)){
            return ServerResponce.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponce validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSeccess()){
            //用户不存在
            return ServerResponce.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(org.apache.commons.lang3.StringUtils.isBlank(token)){
            return ServerResponce.createByErrorMessage("token无效或者过期");
        }

        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            String md5Password  = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username,md5Password);

            if(rowCount > 0){
                return ServerResponce.createBySuccessMessage("修改密码成功");
            }
        }else{
            return ServerResponce.createByErrorMessage("token错误,请重新获取重置密码的token");
        }
        return ServerResponce.createByErrorMessage("修改密码失败");
    }

    public ServerResponce<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount == 0){
            return ServerResponce.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ServerResponce.createBySuccessMessage("密码更新成功");
        }
        return ServerResponce.createByErrorMessage("密码更新失败");
    }

    public ServerResponce<User> updateInformation(User user){
        //username是不能被更新的
        //email也要进行一个校验,校验新的email是不是已经存在,并且存在的email如果相同的话,不能是我们当前的这个用户的.
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount > 0){
            return ServerResponce.createByErrorMessage("email已存在,请更换email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ServerResponce.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponce.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponce<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponce.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
        return ServerResponce.createBySuccess(user);

    }


    //backend

    public ServerResponce checkAdminRole(User user){
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponce.createBySuccess();
        }
        return ServerResponce.createByError();
    }
}