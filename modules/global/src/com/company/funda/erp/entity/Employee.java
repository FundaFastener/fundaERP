package com.company.funda.erp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

@NamePattern("%s %s %s|no,firstNameCh,lastNameCh")
@Table(name = "FE_EMPLOYEE")
@Entity(name = "fe$Employee")
public class Employee extends StandardEntity {
    private static final long serialVersionUID = 7231560661728490147L;

    @Column(name = "NO_", nullable = false, unique = true, length = 10)
    protected String no;

    @Column(name = "FIRST_NAME_CH", length = 10)
    protected String firstNameCh;

    @Column(name = "LAST_NAME_CH", length = 10)
    protected String lastNameCh;

    @Column(name = "FIRST_NAME_EN", length = 30)
    protected String firstNameEn;

    @Column(name = "LAST_NAME_EN", length = 10)
    protected String lastNameEn;

    @Column(name = "FIRST_NAME_OTHER", length = 30)
    protected String firstNameOther;

    @Column(name = "LAST_NAME_OTHER", length = 30)
    protected String lastNameOther;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    protected Department department;

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }


    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setFirstNameCh(String firstNameCh) {
        this.firstNameCh = firstNameCh;
    }

    public String getFirstNameCh() {
        return firstNameCh;
    }

    public void setLastNameCh(String lastNameCh) {
        this.lastNameCh = lastNameCh;
    }

    public String getLastNameCh() {
        return lastNameCh;
    }

    public void setFirstNameEn(String firstNameEn) {
        this.firstNameEn = firstNameEn;
    }

    public String getFirstNameEn() {
        return firstNameEn;
    }

    public void setLastNameEn(String lastNameEn) {
        this.lastNameEn = lastNameEn;
    }

    public String getLastNameEn() {
        return lastNameEn;
    }

    public void setFirstNameOther(String firstNameOther) {
        this.firstNameOther = firstNameOther;
    }

    public String getFirstNameOther() {
        return firstNameOther;
    }

    public void setLastNameOther(String lastNameOther) {
        this.lastNameOther = lastNameOther;
    }

    public String getLastNameOther() {
        return lastNameOther;
    }
}