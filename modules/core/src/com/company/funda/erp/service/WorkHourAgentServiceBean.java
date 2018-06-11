package com.company.funda.erp.service;

import java.time.LocalDateTime;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.enums.WorkHourType;
import com.company.funda.erp.shift.Shift;
import com.company.funda.erp.shift.WorkDay;
import com.company.funda.erp.shift.WorkHour;

@Service(WorkHourAgentService.NAME)
public class WorkHourAgentServiceBean implements WorkHourAgentService {
	
	@Inject
	private Shift regularShift;

	@Override
	public boolean isOnJob(Employee employee,LocalDateTime thisTime) {
		boolean result = false;
		final WorkDay todayWork = regularShift.getWorkDayBy(employee,thisTime.toLocalDate());
		if(regularShift.isWorkingDay(todayWork)) {
			result = regularShift.isWorkHour(todayWork.getWorkHours(),thisTime.toLocalTime());
		}
		return result;
	}
	
	@Override
	public WorkHourType getWorkHourType(Employee employee,LocalDateTime now) {
		return isOnJob(employee,now)==true ? WorkHourType.REGULAR:WorkHourType.OVERTIME;
	}

	@Override
	public Shift getShift(Employee employee) {
		//Should be a someone's specific shift return.But now is fake.
		return regularShift;
	}
	
	@Override
	public WorkHour getNowWorkHour(Employee employee, LocalDateTime now) {
		WorkHour result = null;
		final WorkDay todayWork = regularShift.getWorkDayBy(employee,now.toLocalDate());
		if(todayWork.isWorkingDay()) {
			result = regularShift.getWorkHour(todayWork.getWorkHours(), now.toLocalTime());
		}
		return result;
	}
	
}