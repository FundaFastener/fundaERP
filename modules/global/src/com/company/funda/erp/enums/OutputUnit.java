package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum OutputUnit implements EnumClass<Integer> {

    PCS(10),
    KG(20),
    BAG(30),
    BOX(40),
    PALLET(50);

    private Integer id;

    OutputUnit(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static OutputUnit fromId(Integer id) {
        for (OutputUnit at : OutputUnit.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}