package com.company.funda.erp.web.validate;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.ValidationException;

@Component
public class BiggerThenZeroValidator {
	
	@Inject
	private Messages messages;

	public void biggerThenZero(Object value) throws ValidationException {
		
		Integer valueInt = (Integer)value;
		if(!isBiggerThenZero(valueInt)) {
			throw new ValidationException(getMessage());
		}
	}
	
	public boolean isBiggerThenZero(Integer valueInt) {
		return (null == valueInt || valueInt.intValue() < 0) ?  false : true;
	}
	
	public String getMessage() {
		return messages.getMainMessage("less.then.zero");
	}
	
	public String getMessage(String preMessage) {
		return messages.formatMainMessage("less.then.zero", preMessage + " : ");
	}
}
