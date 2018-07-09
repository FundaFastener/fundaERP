package com.company.funda.erp.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.company.funda.erp.enums.MachineType;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;

@NamePattern("%s %s|no,name")
@Table(name = "FE_MACHINE")
@Entity(name = "fe$Machine")
public class Machine extends StandardEntity {
    private static final long serialVersionUID = 7889330406748152945L;

    @Column(name = "NO_", nullable = false, unique = true, length = 10)
    protected String no;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "machine")
    protected List<MachineProcesses> processTypes;

    @Column(name = "NAME", nullable = false, length = 50)
    protected String name;

    @Column(name = "TYPE_")
    protected Integer type;


    @Column(name = "BRAND", length = 30)
    protected String brand;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPARTMENT_ID")
    protected Department department;

    @Lob
    @Column(name = "REMARK")
    protected String remark;




    public void setProcessTypes(List<MachineProcesses> processTypes) {
        this.processTypes = processTypes;
    }

    public List<MachineProcesses> getProcessTypes() {
        return processTypes;
    }



    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return department;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public MachineType getType() {
        return type == null ? null : MachineType.fromId(type);
    }

    public void setType(MachineType type) {
        this.type = type == null ? null : type.getId();
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
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