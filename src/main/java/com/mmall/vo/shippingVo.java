package com.mmall.vo;

/**
 * Created by 张柏桦 on 2017/7/15.
 */
public class shippingVo {

    /**
     *收货姓名
     */
    private String receiverName;

    /**
     *收货固定电话
     */
    private String receiverPhone;

    /**
     *收货移动电话
     */
    private String receiverMobile;

    /**
     *省份
     */
    private String receiverProvince;

    /**
     *城市
     */
    private String receiverCity;

    /**
     *区/县
     */
    private String receiverDistrict;

    /**
     *详细地址
     */
    private String receiverAddress;

    /**
     *邮编
     */
    private String receiverZip;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverProvince(String receiverProvince) {
        return this.receiverProvince;
    }

    public void setReceiverProvince(String receiverProvince) {
        this.receiverProvince = receiverProvince;
    }

    public String getReceiverCity(String receiverCity) {
        return this.receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverDistrict() {
        return receiverDistrict;
    }

    public void setReceiverDistrict(String receiverDistrict) {
        this.receiverDistrict = receiverDistrict;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverZip() {
        return receiverZip;
    }

    public void setReceiverZip(String receiverZip) {
        this.receiverZip = receiverZip;
    }
}
