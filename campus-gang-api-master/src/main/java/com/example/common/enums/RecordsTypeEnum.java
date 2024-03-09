package com.example.common.enums;

public enum RecordsTypeEnum {
    OUT("支出"),
    CHARGE("充值"),
    INCOME("收入"),
    CANCEL("取消");

    private String value;

    public String getValue() {
        return value;
    }

    RecordsTypeEnum(String value) {
        this.value = value;
    }
}
