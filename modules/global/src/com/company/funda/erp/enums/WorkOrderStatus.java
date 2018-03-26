package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum WorkOrderStatus implements EnumClass<Integer> {

    NOT_STARTED(1),
    IN_PROCESS(2),
    FINISHED(3);

    private Integer id;

    WorkOrderStatus(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static WorkOrderStatus fromId(Integer id) {
        for (WorkOrderStatus at : WorkOrderStatus.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}