package com.company.funda.erp.service;

import java.time.LocalDateTime;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.enums.WorkHourType;
import com.company.funda.erp.shift.Shift;
import com.company.funda.erp.shift.WorkHour;

public interface WorkHourAgentService {
    String NAME = "fe_WorkHourAgentService";
    
    boolean isOnJob(Employee employee,LocalDateTime now);
    
    WorkHourType getWorkHourType(Employee employee,LocalDateTime now);
    
    Shift getShift(Employee employee);
    
    WorkHour getNowWorkHour(Employee employee,LocalDateTime now);
}