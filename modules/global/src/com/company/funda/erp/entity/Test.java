package com.company.funda.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@Table(name = "FE_TEST")
@Entity(name = "fe$Test")
public class Test extends StandardEntity {
    private static final long serialVersionUID = 967940910522656146L;

    @Column(name = "NAME", unique = true)
    protected String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}