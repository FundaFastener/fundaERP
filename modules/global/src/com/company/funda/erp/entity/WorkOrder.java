package com.company.funda.erp.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.company.funda.erp.enums.ProcessType;
import com.company.funda.erp.enums.WorkOrderStatus;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import javax.persistence.Lob;

@NamePattern("%s :<%s>: %s[%s]|no,inventoryItem,quantity,status")
@Table(name = "FE_WORK_ORDER")
@Entity(name = "fe$WorkOrder")
public class WorkOrder extends StandardEntity {
    private static final long serialVersionUID = -6376992558579871289L;

    @Column(name = "NO_", nullable = false, unique = true, length = 20)
    protected String no;

    @NotNull
    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "INVENTORY_ITEM_ID")
    protected InventoryItem inventoryItem;

    @NotNull
    @Column(name = "QUANTITY", nullable = false, precision = 20, scale = 0)
    protected BigDecimal quantity;

    @NotNull
    @Column(name = "UNIT", nullable = false)
    protected Integer unit;

    @NotNull
    @Column(name = "PROCESS_TYPE", nullable = false)
    protected Integer processType;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEFAULT_MACHINE_ID")
    protected Machine defaultMachine;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BACKUP_MACHINES_ID")
    protected Machine backupMachines;

    @Column(name = "STATUS")
    protected Integer status;

    @Lob
    @Column(name = "REMARK")
    protected String remark;

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }


    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setStatus(WorkOrderStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public WorkOrderStatus getStatus() {
        return status == null ? null : WorkOrderStatus.fromId(status);
    }

    public void setDefaultMachine(Machine defaultMachine) {
        this.defaultMachine = defaultMachine;
    }

    public Machine getDefaultMachine() {
        return defaultMachine;
    }

    public void setBackupMachines(Machine backupMachines) {
        this.backupMachines = backupMachines;
    }

    public Machine getBackupMachines() {
        return backupMachines;
    }

    public WorkOrderUnit getUnit() {
        return unit == null ? null : WorkOrderUnit.fromId(unit);
    }

    public void setUnit(WorkOrderUnit unit) {
        this.unit = unit == null ? null : unit.getId();
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType == null ? null : processType.getId();
    }

    public ProcessType getProcessType() {
        return processType == null ? null : ProcessType.fromId(processType);
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getNo() {
        return no;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}