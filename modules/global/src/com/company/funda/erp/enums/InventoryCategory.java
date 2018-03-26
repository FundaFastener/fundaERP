package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum InventoryCategory implements EnumClass<String> {

    WIRE("W"),
    FINISHED_PRODUCT("P"),
    WORK_IN_PROCESS("B"),
    WIRE_ROD("R"),
    OTHER("D"),
    CUSTOMER_PART("C");

    private String id;

    InventoryCategory(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static InventoryCategory fromId(String id) {
        for (InventoryCategory at : InventoryCategory.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}