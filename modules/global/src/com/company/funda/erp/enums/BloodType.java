package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum BloodType implements EnumClass<Integer> {

    A(1),
    B(2),
    AB(3),
    O(4);

    private Integer id;

    BloodType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static BloodType fromId(Integer id) {
        for (BloodType at : BloodType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}