package com.company.funda.erp.shift;

import java.time.LocalDate;
import java.util.List;

public class WorkDay {
	
	private LocalDate localDate;
	private List<WorkHour> workHours;
	private boolean isWorkingDay;

	public WorkDay() {
		super();
	}
	
	public WorkDay(LocalDate localDate, List<WorkHour> workHours,boolean isWorkingDay) {
		this();
		this.localDate = localDate;
		this.workHours = workHours;
		this.isWorkingDay = isWorkingDay;
	}

	public LocalDate getLocalDate() {
		return localDate;
	}

	public void setLocalDate(LocalDate localDate) {
		this.localDate = localDate;
	}

	public List<WorkHour> getWorkHours() {
		return workHours;
	}

	public void setWorkHours(List<WorkHour> workHours) {
		this.workHours = workHours;
	}
	public boolean isWorkingDay() {
		return isWorkingDay;
	}

	public void setWorkingDay(boolean isWorkingDay) {
		this.isWorkingDay = isWorkingDay;
	}
}
