package com.company.funda.erp.web.validate;

import org.springframework.stereotype.Component;

import com.haulmont.cuba.gui.components.ValidationException;

@Component
public class BiggerThenZeroValidator {
	
	final static String EXCEPTION_MESSAGE = "Less Then Zero.";

	public void biggerThenZero(Object value) throws ValidationException {
		
		Integer valueInt = (Integer)value;
		if(!isBiggerThenZero(valueInt)) {
			throw new ValidationException(EXCEPTION_MESSAGE);
		}
	}
	
	public boolean isBiggerThenZero(Integer valueInt) {
		return (null == valueInt || valueInt.intValue() < 0) ?  false : true;
	}
	
	public String getMessage() {
		return EXCEPTION_MESSAGE;
	}
	
	public String getMessage(String preMessage) {
		return preMessage + " : " +getMessage();
	}
}
