package com.company.funda.erp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

@NamePattern("%s %s|no,shortName")
@Table(name = "FE_COMPANY")
@Entity(name = "fe$Company")
public class Company extends StandardEntity {
    private static final long serialVersionUID = -8223099737966322008L;


    @Column(name = "NO_", nullable = false, unique = true, length = 50)
    protected String no;

    @Column(name = "NAME", nullable = false, length = 80)
    protected String name;

    @Column(name = "SHORT_NAME", length = 20)
    protected String shortName;




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

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }




}