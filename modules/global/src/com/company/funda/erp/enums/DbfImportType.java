package com.company.funda.erp.enums;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum DbfImportType implements EnumClass<Integer> {

    SKIP_EXISTING(10),
    MANDATORY_OVERWRITE(20);

    private Integer id;

    DbfImportType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static DbfImportType fromId(Integer id) {
        for (DbfImportType at : DbfImportType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}