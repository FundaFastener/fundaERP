package com.company.funda.erp.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.company.funda.erp.enums.InventoryCategory;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

@NamePattern("%s %s|no,name1")
@Table(name = "FE_INVENTORY_ITEM")
@Entity(name = "fe$InventoryItem")
public class InventoryItem extends StandardEntity {
    private static final long serialVersionUID = -3163414240657292260L;

    @Column(name = "NO_", nullable = false, unique = true, length = 50)
    protected String no;

    @Column(name = "NAME1", length = 200)
    protected String name1;

    @Column(name = "NAME2", length = 200)
    protected String name2;

    @Column(name = "CATEGORY")
    protected String category;

    @Column(name = "MATERIAL_WEIGHT")
    protected BigDecimal materialWeight;

    @Column(name = "NET_WEIGHT")
    protected BigDecimal netWeight;





    public void setCategory(InventoryCategory category) {
        this.category = category == null ? null : category.getId();
    }

    public InventoryCategory getCategory() {
        return category == null ? null : InventoryCategory.fromId(category);
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName1() {
        return name1;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getName2() {
        return name2;
    }

    public void setMaterialWeight(BigDecimal materialWeight) {
        this.materialWeight = materialWeight;
    }

    public BigDecimal getMaterialWeight() {
        return materialWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }
}