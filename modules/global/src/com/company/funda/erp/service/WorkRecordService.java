package com.company.funda.erp.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.ValidationException;

import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.enums.WorkOrderUnit;

public interface WorkRecordService {
    String NAME = "fe_WorkRecordService";
    
    WorkRecord getLatestRecord(WorkOrder workOrder);
    
    boolean isInWorkRecording(WorkOrder workOrder);
    
    boolean isInWorkRecording(WorkRecord workRecord);
    
    void commitWorkRecords(WorkOrder workOrder,WorkRecord workRecord,Collection<WorkRecord> workRecords)throws Exception;

    List<WorkRecord> getAllWorkRecordShouldAccumulate(WorkOrder workOrder);
    
    BigDecimal getAccumulateQuantity(WorkOrder workOrder,WorkOrderUnit targetUnit);
    
    long getNextRecordNo();
    
    long geteCurrentNo();
    
    void deleteAllDetachItem(WorkOrder workOrder,Long recordNo);
    
    void subtractLoss(WorkRecord workRecord) throws ValidationException;
    
    WorkRecord addLoss(WorkRecord workRecord) ;
    
    List<WorkRecord> getLatestDaysCorrectedRecords(WorkOrder workOrder,String dayFrom);
    
    List<WorkRecord> getLatestDaysRecords(WorkOrder workOrder,String dayFrom);
    
    boolean isAllHaveEndTime(List<WorkRecord> workRecords);
    
    boolean isInRecordSection(List<WorkRecord> workRecords,Date date);
    
    boolean inSameSection(List<WorkRecord> workRecords,Date start,Date end); 
    
    int deleteBy(Long recordNo);
}