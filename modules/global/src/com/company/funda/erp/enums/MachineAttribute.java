package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum MachineAttribute implements EnumClass<Integer> {

    AUTO(10),
    SEMI_AUTO(20),
    MANUAL(30);

    private Integer id;

    MachineAttribute(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static MachineAttribute fromId(Integer id) {
        for (MachineAttribute at : MachineAttribute.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}