package com.example.entity;

/**
 * 功能
 * 作者：荒
 * 日期：2024/3/30 11:54
 */

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单信息
 */
public class GoodsOrders implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 商品名称 */
    private String goodsName;
    /** 商品图片 */
    private String goodsImg;
    /** 订单编号 */
    private String orderNo;
    /** 总价 */
    private BigDecimal price;
    /** 下单时间 */
    private String time;
    /** 支付单号 */
    private String payNo;
    /** 支付时间 */
    private String payTime;
    private String arriveTime;
    /** 下单人ID */
    private Integer userId;
    /** 收货地址 */
    private String address;
    /** 联系方式 */
    private String phone;
    /** 收货人名称 */
    private String userName;
    /** 订单状态 */
    private String status;
    /** 卖家ID */
    private Integer acceptId;
    private Integer goodsId;
    private Integer targetId;
    //收货地址 类型（Address）
    private Address targetAddress;
    private String salesName;
    private String category;
    private Integer minutes;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getMinutes() {
        return minutes;
    }


    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public Address getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(Address targetAddress) {
        this.targetAddress = targetAddress;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(Integer acceptId) {
        this.acceptId = acceptId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}