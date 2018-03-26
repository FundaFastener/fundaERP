package com.company.funda.erp.shift;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.util.FundaDateUtil;

@Component
public class RegularShift implements Shift{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public WorkDay getWorkDayBy(Employee employee,LocalDate localDate) {
		ArrayList<WorkHour> workHours = new ArrayList<>();
		boolean isWorkingDay = FundaDateUtil.isWeekend(localDate) ? false:true;
		if(isWorkingDay) {
			WorkHour phaseOne = new WorkHour(LocalTime.of(8, 0),LocalTime.of(12, 0));
			WorkHour phaseTwo = new WorkHour(LocalTime.of(13, 0),LocalTime.of(17, 00));
			workHours.add(phaseOne);
			workHours.add(phaseTwo);
		}
		WorkDay todayWork = new WorkDay(localDate, workHours,isWorkingDay);
		return todayWork;
	}

	@Override
	public boolean isWorkingDay(WorkDay workDay) {
		return workDay.isWorkingDay();
	}


	@Override
	public List<WorkHour> getWorkHours(WorkDay workDay) {
		return workDay.getWorkHours();
	}

	@Override
	public boolean isWorkHour(List<WorkHour> workHours,LocalTime nowTime) {
		logger.info("workHours.size:{},nowTime:{}",workHours.size(),nowTime);
		logger.info("result:{}",workHours.stream().filter(w->w.isWorkingTime(nowTime)).findAny().isPresent());
		return workHours.stream().filter(w->w.isWorkingTime(nowTime)).findAny().isPresent();
		
	}

	@Override
	public WorkHour getWorkHour(List<WorkHour> workHours, LocalTime nowTime) {
		return workHours.stream().filter(w->w.isWorkingTime(nowTime)).findAny().orElse(null);
	}

}
