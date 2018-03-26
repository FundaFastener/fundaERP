package com.company.funda.erp.enums;

import javax.annotation.Nullable;

import com.haulmont.chile.core.datatypes.impl.EnumClass;


public enum HighestEducation implements EnumClass<Integer> {

    NULL(0),
    ELEMENTARY_SCHOOL(10),
    JUNIOR_HIGH_SCHOOL(20),
    SENIOR_HIGH_SCHOOL(30),
    VOCATIONAL_HIGH_SCHOOL(40),
    JUNIOR_COLLEGE(50),
    BACHELORS_DEGREE(60),
    MASTERS_DEGREE(70),
    DOCTORAL_DEGREE(80),
    OTHERS(90);

    private Integer id;

    HighestEducation(Integer value) {
        this.id = value;
    }

    public Integer getId() {
        return id;
    }

    @Nullable
    public static HighestEducation fromId(Integer id) {
        for (HighestEducation at : HighestEducation.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}