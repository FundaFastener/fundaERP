package com.company.funda.erp.shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.company.funda.erp.entity.Employee;

public interface Shift {
	WorkDay getWorkDayBy(Employee employee,LocalDate localDate);
	List<WorkHour> getWorkHours(WorkDay workDay);
	boolean isWorkingDay(WorkDay workDay);
	boolean isWorkHour(List<WorkHour> workHours,LocalTime nowTime);
	WorkHour getWorkHour(List<WorkHour> workHours,LocalTime nowTime);
}
