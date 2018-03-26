package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum ProcessType implements EnumClass<Integer> {

    FORMING(1),
    THREADING_FLAT(2),
    THREADING_ROLLER(3),
    OPEN_DIE_FORMING(4),
    LATHE(6),
    HEAT_TREATMENT_REFINING(5),
    HEAT_TREATMENT_NORMALIZING(7),
    HEAT_TREATMENT_CURBURIZATION(8),
    HEAT_TREATMENT_QUENCHING(9),
    HEAT_TREATMENT_TEMPERING(10),
    HEAT_FREQUENCY_HARDENING(11),
    ELECTORPLATING(12),
    COATING(13),
    PICKLING(14),
    CLEANING(15),
    ANTIRUST(16),
    SANDBLASTING(17),
    AUTO_INSPECTION(18),
    MANUAL_INSPECTION(19),
    PACKING(20);

    private Integer id;

    ProcessType(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static ProcessType fromId(Integer id) {
        for (ProcessType at : ProcessType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}