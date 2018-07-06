package com.company.funda.erp.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.company.funda.erp.enums.OutputUnit;
import com.company.funda.erp.enums.TimeUnit;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

@NamePattern("%s %s|partNo,processType")
@Table(name = "FE_PART_PROCESSES_STANDARD", uniqueConstraints = {
    @UniqueConstraint(name = "IDX_FE_PART_PROCESSES_STANDARD_UNQ", columnNames = {"PART_NO_ID", "PROCESS_TYPE_ID", "FROM_"})
})
@Entity(name = "fe$PartProcessesStandard")
public class PartProcessesStandard extends StandardEntity {
    private static final long serialVersionUID = 3789609058377212632L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PART_NO_ID")
    protected InventoryItem partNo;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROCESS_TYPE_ID")
    protected MachineProcesses processType;

    @Temporal(TemporalType.DATE)
    @Column(name = "FROM_", nullable = false)
    protected Date from;

    @Temporal(TemporalType.DATE)
    @Column(name = "TO_")
    protected Date to;

    @Column(name = "OUTPUT_")
    protected Integer output;

    @Column(name = "OUTPUT_UNIT")
    protected Integer outputUnit;

    @Column(name = "TIME_UNIT")
    protected Integer timeUnit;

    @Column(name = "MAJOR_SETUP_TIME")
    protected BigDecimal majorSetupTime;

    @Column(name = "MINOR_SETUP_TIME")
    protected BigDecimal minorSetupTime;

    public void setProcessType(MachineProcesses processType) {
        this.processType = processType;
    }

    public MachineProcesses getProcessType() {
        return processType;
    }


    public void setOutput(Integer output) {
        this.output = output;
    }

    public Integer getOutput() {
        return output;
    }


    public void setOutputUnit(OutputUnit outputUnit) {
        this.outputUnit = outputUnit == null ? null : outputUnit.getId();
    }

    public OutputUnit getOutputUnit() {
        return outputUnit == null ? null : OutputUnit.fromId(outputUnit);
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit == null ? null : timeUnit.getId();
    }

    public TimeUnit getTimeUnit() {
        return timeUnit == null ? null : TimeUnit.fromId(timeUnit);
    }

    public void setMajorSetupTime(BigDecimal majorSetupTime) {
        this.majorSetupTime = majorSetupTime;
    }

    public BigDecimal getMajorSetupTime() {
        return majorSetupTime;
    }

    public void setMinorSetupTime(BigDecimal minorSetupTime) {
        this.minorSetupTime = minorSetupTime;
    }

    public BigDecimal getMinorSetupTime() {
        return minorSetupTime;
    }




    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getFrom() {
        return from;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getTo() {
        return to;
    }


    public void setPartNo(InventoryItem partNo) {
        this.partNo = partNo;
    }

    public InventoryItem getPartNo() {
        return partNo;
    }


}