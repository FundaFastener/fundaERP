package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum MachineType implements EnumClass<Integer> {

    FORMING(1),
    HEADING(2),
    THREADING(3),
    PLC_LATHE(4),
    CNC_LATHE(5);

    private Integer id;

    MachineType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static MachineType fromId(Integer id) {
        for (MachineType at : MachineType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}