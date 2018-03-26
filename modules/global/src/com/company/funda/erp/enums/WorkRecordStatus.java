package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum WorkRecordStatus implements EnumClass<Integer> {

    MAJOR_SETUP(1),
    MINOR_SETUP(2),
    PRODUCTION(3),
    WAITING_FOR_GC(4),
    WAITING_FOR_MOLD(5);

    private Integer id;

    WorkRecordStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static WorkRecordStatus fromId(Integer id) {
        for (WorkRecordStatus at : WorkRecordStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}