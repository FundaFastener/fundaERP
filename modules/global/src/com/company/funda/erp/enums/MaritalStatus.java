package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum MaritalStatus implements EnumClass<Integer> {

    MARRIED(1),
    SINGLE(2);

    private Integer id;

    MaritalStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static MaritalStatus fromId(Integer id) {
        for (MaritalStatus at : MaritalStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}