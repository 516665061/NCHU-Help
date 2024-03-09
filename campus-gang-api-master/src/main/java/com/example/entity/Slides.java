package com.example.entity;

/**
 * 功能
 * 作者：封延民
 * 日期：2024/2/22 22:31
 */
public class Slides {
    /**ID*/
    private Integer id;
    /**幻灯片地址*/
    private String image;
    /**幻灯片跳转地址*/
    private String url;

    public Slides() {
    }

    public Slides(Integer id, String image, String url) {
        this.id = id;
        this.image = image;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
