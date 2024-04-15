package com.example.common.enums;

public enum GoodsStatusEnum {
    NOT_AUDIT("待审核"),
    ACCEPT("通过"),
    REJECT("拒绝");

    public String value;

    GoodsStatusEnum(String value) {
        this.value = value;
    }
}
