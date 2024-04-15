package com.example.entity;
import java.io.Serializable;
/**
 * 功能：圈子
 * 作者：封延民
 * 日期：2024/3/22 11:33
 */

public class Circles implements Serializable {
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
