package com.company.funda.erp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

@NamePattern("%s %s|no,name")
@Table(name = "FE_DEPARTMENT")
@Entity(name = "fe$Department")
public class Department extends StandardEntity {
    private static final long serialVersionUID = -7643379281202312072L;

    @Column(name = "NO_", nullable = false, unique = true, length = 6)
    protected String no;

    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGED_BY_ID")
    protected Employee managedBy;

    public Employee getManagedBy() {
        return managedBy;
    }

    public void setManagedBy(Employee managedBy) {
        this.managedBy = managedBy;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}