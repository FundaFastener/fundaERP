package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum WorkHourType implements EnumClass<Integer> {

    REGULAR(10),
    OVERTIME(20);

    private Integer id;

    WorkHourType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static WorkHourType fromId(Integer id) {
        for (WorkHourType at : WorkHourType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}