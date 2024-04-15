package com.example.entity;

import java.io.Serializable;

/**
 * 失物信息表
 */
public class Lost implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 物品名称 */
    private String name;
    /** 物品描述 */
    private String content;
    /** 物品图片 */
    private String img;
    /** 用户ID */
    private Integer userId;
    /* 状态 */
    private String status;
    private String viceStatus;
    /* 时间 */
    private String time;

    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getViceStatus() {
        return viceStatus;
    }

    public void setViceStatus(String viceStatus) {
        this.viceStatus = viceStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


}