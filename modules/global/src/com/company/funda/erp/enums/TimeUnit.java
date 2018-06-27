package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum TimeUnit implements EnumClass<Integer> {

    SECOND(10),
    MINUTE(20),
    HOUR(30),
    DAY(40),
    WEEK(50),
    MONTH(60),
    SEASON(70),
    YEAR(80);

    private Integer id;

    TimeUnit(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static TimeUnit fromId(Integer id) {
        for (TimeUnit at : TimeUnit.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}