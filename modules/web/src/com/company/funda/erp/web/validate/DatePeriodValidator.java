package com.company.funda.erp.web.validate;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.gui.components.ValidationException;

@Component
public class DatePeriodValidator {

	public void reasonablePeriod(Date start,Date end) throws ValidationException{
		if(end != null && start != null) {
			if(start.after(end)) {
				throw new ValidationException("Work Record endTime can't less then start time!");
			}
		}
	}
	
	public static void main(String[] args) throws ValidationException{
		Date start = FundaDateUtil.parse("2018/01/02 12:00:02", FundaDateUtil.Type.DAY_TIME_SLASH);
		Date end = FundaDateUtil.parse("2018/01/02 12:00:01", FundaDateUtil.Type.DAY_TIME_SLASH);
		new DatePeriodValidator().reasonablePeriod(start, end);
	}
}
