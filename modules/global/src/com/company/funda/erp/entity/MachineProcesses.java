package com.company.funda.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.company.funda.erp.enums.MachineAttribute;
import com.company.funda.erp.enums.ProcessType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.UniqueConstraint;

@NamePattern("%s |processType")
@Table(name = "FE_MACHINE_PROCESSES", uniqueConstraints = {
    @UniqueConstraint(name = "IDX_FE_MACHINE_PROCESSES_UNQ", columnNames = {"MACHINE_ID", "PROCESS_TYPE"})
})
@Entity(name = "fe$MachineProcesses")
public class MachineProcesses extends StandardEntity {
    private static final long serialVersionUID = -339550747877793493L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MACHINE_ID")
    protected Machine machine;

    @Column(name = "PROCESS_TYPE", nullable = false)
    protected Integer processType;

    @Column(name = "ATTRIBUTE")
    protected Integer attribute;

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType == null ? null : processType.getId();
    }

    public ProcessType getProcessType() {
        return processType == null ? null : ProcessType.fromId(processType);
    }

    public void setAttribute(MachineAttribute attribute) {
        this.attribute = attribute == null ? null : attribute.getId();
    }

    public MachineAttribute getAttribute() {
        return attribute == null ? null : MachineAttribute.fromId(attribute);
    }


}