package com.example.entity;
import java.io.Serializable;
/**
 * 功能: 分类
 * 作者：荒
 * 日期：2024/3/22 8:56
 */

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;
    /** 名称 */
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}