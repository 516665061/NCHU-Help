package com.example.common.enums;

public enum OrdersPropertyEnum {
    GOODS("商品"),
    ERRAND("悬赏");

    private String value;

    OrdersPropertyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
