package com.example.entity;

/**
 * 功能
 * 作者：荒
 * 日期：2023/12/8 11:23
 */
public class Address {
    /** ID */
    private Integer id;
    /** 地址 */
    private String address;
    /** 门牌号 */
    private String doorNo;
    /** 联系人 */
    private String userName;
    /** 联系电话 */
    private String phone;
    /** 关联用户的名称 */
    private String user;
    /** 关联用户ID */
    private Integer userId;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(String doorNo) {
        this.doorNo = doorNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
