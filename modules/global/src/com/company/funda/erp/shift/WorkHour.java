package com.company.funda.erp.shift;

import java.io.Serializable;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class WorkHour implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1874534638740555241L;
	
	private LocalTime from;
	private LocalTime to;
	
	public WorkHour() {
		super();
	}
	
	public WorkHour(LocalTime from, LocalTime to) {
		this();
		this.from = from;
		this.to = to;
	}

	public LocalTime getFrom() {
		return from;
	}

	public void setFrom(LocalTime from) {
		this.from = from;
	}

	public LocalTime getTo() {
		return to;
	}

	public void setTo(LocalTime to) {
		this.to = to;
	}
	
	public boolean isWorkingTime(LocalTime localTime) {
		boolean result = false;
		localTime = localTime.truncatedTo(ChronoUnit.MINUTES);
		if(localTime.isAfter(from) &&
				(!localTime.isAfter(to))) {
			result = true;
		}
		return result;
	}
	
	public static void main(String[] args) {
		LocalTime f = LocalTime.now();
		LocalTime f1 = f.truncatedTo(ChronoUnit.SECONDS);

		System.out.println(f);
		System.out.println(f1);
	}
}
