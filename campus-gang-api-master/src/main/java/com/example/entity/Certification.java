package com.example.entity;

/**
 * 功能
 * 作者：封延民
 * 日期：2023/12/9 23:43
 */
public class Certification {
    /** ID */
    private Integer id;
    /** 账号 */
    private Integer userId;
    /** 名称 */
    private String name;
    /** 本人照片 */
    private String avatar;
    /** 联系方式 */
    private String phone;
    /** 身份证号码 */
    private String studentNo;
    /** 身份证正面 */
    private String card;
    /** 审核状态 */
    private String status;
    /** 审核理由 */
    private String reason;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
