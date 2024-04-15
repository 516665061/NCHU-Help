package com.example.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 评论表
 */
public class Evaluate implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 内容 */
    private String content;
    /** 评论人 */
    private Integer userId;
    /** 父级ID */
    private Integer pid;
    /** 评论时间 */
    private String time;
    /** 关联ID */
    private Integer fid;
    /** 模块 */
    private String module;
    /** 根节点ID */
    private Integer rootId;
    private String userName;
    private String avatar;
    private String parentUserName;
    private List<Evaluate> children;

    public String getParentUserName() {
        return parentUserName;
    }

    public void setParentUserName(String parentUserName) {
        this.parentUserName = parentUserName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Evaluate> getChildren() {
        return children;
    }

    public void setChildren(List<Evaluate> children) {
        this.children = children;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getRootId() {
        return rootId;
    }

    public void setRootId(Integer rootId) {
        this.rootId = rootId;
    }

}