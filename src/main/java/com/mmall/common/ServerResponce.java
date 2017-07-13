package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by 张柏桦 on 2017/7/10.
 */

//保证序列化json的时候，如果是 null 的对象，key也会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//implement Serializable 实现序列化接口
public class ServerResponce<T> implements Serializable {

    private  int status;
    private String msg;
    private  T data;

    private ServerResponce (int status){
        this.status =  status;
    }
    private ServerResponce (int status, T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponce (int status,String msg, T data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerResponce (int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不在json序列化当中
    public boolean isSeccess(){
        return this.status == ResponceCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }

    public static <T> ServerResponce<T> createBySuccess(){
        return new ServerResponce(ResponceCode.SUCCESS.getCode());
    }
    public static  <T> ServerResponce<T> createBySuccessMessage(String msg){
        return new ServerResponce<T>(ResponceCode.SUCCESS.getCode(),msg);
    }
    public static  <T>ServerResponce<T> createBySuccess(T data){
        return new ServerResponce<T>(ResponceCode.SUCCESS.getCode(),data);
    }
    public static  <T>ServerResponce<T> createBySuccess(String msg,T data){
        return new ServerResponce<T>(ResponceCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServerResponce<T> createByError(){
        return new ServerResponce<T>(ResponceCode.ERROR.getCode());
    }
    public static <T> ServerResponce<T> createByErrorMessage(String errorMessage){
        return new ServerResponce<T>(ResponceCode.ERROR.getCode(),errorMessage);
    }
    public static <T> ServerResponce<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponce<T>(errorCode,errorMessage);
    }
}
