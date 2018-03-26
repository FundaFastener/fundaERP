package com.company.funda.erp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
		final LocalDate nowDate = thisTime.toLocalDate();
		final LocalTime nowTime = thisTime.toLocalTime();
		final WorkDay todayWork = regularShift.getWorkDayBy(employee,nowDate);
		if(todayWork.isWorkingDay()) {
			result = regularShift.isWorkHour(todayWork.getWorkHours(),nowTime);
		}
		return result;
	}
	
	@Override
	public WorkHourType getWorkHourType(Employee employee,LocalDateTime now) {
		return isOnJob(employee,now)==true ? WorkHourType.REGULAR:WorkHourType.OVERTIME;
	}

	@Override
	public Shift getShift(Employee employee) {
		return regularShift;
	}
	
	@Override
	public WorkHour getNowWorkHour(Employee employee, LocalDateTime now) {
		WorkHour result = null;
		final LocalDate nowDate = now.toLocalDate();
		final WorkDay todayWork = regularShift.getWorkDayBy(employee,nowDate);
		if(todayWork.isWorkingDay()) {
			final LocalTime nowTime = now.toLocalTime();
			return regularShift.getWorkHour(todayWork.getWorkHours(), nowTime);
		}
		return result;
	}
	
}