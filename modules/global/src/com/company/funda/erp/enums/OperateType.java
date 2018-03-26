package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OperateType implements EnumClass<Integer> {

    MANUAL(10),
    AUTO(20),
    DETACH(30),
    INTERIM(40);

    private Integer id;

    OperateType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static OperateType fromId(Integer id) {
        for (OperateType at : OperateType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}