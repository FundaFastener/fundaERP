package com.company.funda.erp.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.company.funda.erp.enums.OperateType;
import com.company.funda.erp.enums.WorkHourType;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.company.funda.erp.enums.WorkRecordStatus;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;

@NamePattern("%s %s|workOrder,recordNo")
@Table(name = "FE_WORK_RECORD")
@Entity(name = "fe$WorkRecord")
public class WorkRecord extends StandardEntity {
    private static final long serialVersionUID = -5391042174892164557L;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "WORK_ORDER_ID")
    protected WorkOrder workOrder;

    @Column(name = "OPERATE_TYPE")
    protected Integer operateType;

    @NotNull(message = "{msg://com.company.funda.erp.entity/NotNull}")
    @Column(name = "RECORD_NO", nullable = false)
    protected Long recordNo;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMPLOYEE_ID")
    protected Employee employee;

    @Column(name = "WORK_HOUR_TYPE")
    protected Integer workHourType;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "{msg://com.company.funda.erp.entity/NotNull}")
    @Column(name = "START_TIME", nullable = false)
    protected Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    protected Date endTime;

    @Column(name = "TIME_USED")
    protected Integer timeUsed;

    @NotNull(message = "{msg://com.company.funda.erp.entity/NotNull}")
    @Column(name = "STATUS", nullable = false)
    protected Integer status;

    @Column(name = "UNIT")
    protected Integer unit;

    @DecimalMin(message = "+", value = "0")
    @Column(name = "UNIT_WEIGHT")
    protected BigDecimal unitWeight;

    @DecimalMin(message = "+", value = "0")
    @Column(name = "FINISHED_QUANTITY")
    protected BigDecimal finishedQuantity;

    @DecimalMin(message = "+", value = "0")
    @Column(name = "SETUP_LOSS_QUANTITY")
    protected BigDecimal setupLossQuantity;

    @DecimalMin(message = "+", value = "0")
    @Column(name = "NG_LOSS_QUANTITY")
    protected BigDecimal ngLossQuantity;

    @DecimalMin(message = "+", value = "0")
    @Column(name = "MATERIAL_LOSS_QUANTITY")
    protected BigDecimal materialLossQuantity;

    @Column(name = "SETUP_LOSS_UNIT")
    protected Integer setupLossUnit;

    @Column(name = "NG_LOSS_UNIT")
    protected Integer ngLossUnit;

    @Column(name = "MATERIAL_LOSS_UNIT")
    protected Integer materialLossUnit;

    @Lob
    @Column(name = "REMARK")
    protected String remark;

    public Long getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(Long recordNo) {
        this.recordNo = recordNo;
    }


    public void setOperateType(OperateType operateType) {
        this.operateType = operateType == null ? null : operateType.getId();
    }

    public OperateType getOperateType() {
        return operateType == null ? null : OperateType.fromId(operateType);
    }


    public WorkHourType getWorkHourType() {
        return workHourType == null ? null : WorkHourType.fromId(workHourType);
    }

    public void setWorkHourType(WorkHourType workHourType) {
        this.workHourType = workHourType == null ? null : workHourType.getId();
    }

    

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }


    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }

    public Integer getTimeUsed() {
        return timeUsed;
    }


    public void setSetupLossQuantity(BigDecimal setupLossQuantity) {
        this.setupLossQuantity = setupLossQuantity;
    }

    public BigDecimal getSetupLossQuantity() {
        return setupLossQuantity;
    }

    public void setNgLossQuantity(BigDecimal ngLossQuantity) {
        this.ngLossQuantity = ngLossQuantity;
    }

    public BigDecimal getNgLossQuantity() {
        return ngLossQuantity;
    }

    public void setMaterialLossQuantity(BigDecimal materialLossQuantity) {
        this.materialLossQuantity = materialLossQuantity;
    }

    public BigDecimal getMaterialLossQuantity() {
        return materialLossQuantity;
    }

    public void setSetupLossUnit(WorkOrderUnit setupLossUnit) {
        this.setupLossUnit = setupLossUnit == null ? null : setupLossUnit.getId();
    }

    public WorkOrderUnit getSetupLossUnit() {
        return setupLossUnit == null ? null : WorkOrderUnit.fromId(setupLossUnit);
    }

    public void setNgLossUnit(WorkOrderUnit ngLossUnit) {
        this.ngLossUnit = ngLossUnit == null ? null : ngLossUnit.getId();
    }

    public WorkOrderUnit getNgLossUnit() {
        return ngLossUnit == null ? null : WorkOrderUnit.fromId(ngLossUnit);
    }

    public void setMaterialLossUnit(WorkOrderUnit materialLossUnit) {
        this.materialLossUnit = materialLossUnit == null ? null : materialLossUnit.getId();
    }

    public WorkOrderUnit getMaterialLossUnit() {
        return materialLossUnit == null ? null : WorkOrderUnit.fromId(materialLossUnit);
    }

    public void setWorkOrder(WorkOrder workOrder) {
        this.workOrder = workOrder;
    }

    public WorkOrder getWorkOrder() {
        return workOrder;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }

    public WorkOrderUnit getUnit() {
        return unit == null ? null : WorkOrderUnit.fromId(unit);
    }

    public void setUnit(WorkOrderUnit unit) {
        this.unit = unit == null ? null : unit.getId();
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setStatus(WorkRecordStatus status) {
        this.status = status == null ? null : status.getId();
    }

    public WorkRecordStatus getStatus() {
        return status == null ? null : WorkRecordStatus.fromId(status);
    }

    public void setUnitWeight(BigDecimal unitWeight) {
        this.unitWeight = unitWeight;
    }

    public BigDecimal getUnitWeight() {
        return unitWeight;
    }

    public void setFinishedQuantity(BigDecimal finishedQuantity) {
        this.finishedQuantity = finishedQuantity;
    }

    public BigDecimal getFinishedQuantity() {
        return finishedQuantity;
    }

}