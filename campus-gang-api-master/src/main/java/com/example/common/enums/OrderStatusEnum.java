package com.example.common.enums;

/**
 * 功能
 * 作者：封延民
 * 日期：2023/12/7 19:13
 */
public enum OrderStatusEnum {
    CANCEL("已取消"),
    NO_ACCEPT("待接单"),
    NO_ARRIVE("待送达"),
    NO_RECEIVE("待收货"),
    NO_COMMENT("待评价"),
    DONE("已完成"),
    ;

    OrderStatusEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
