package com.company.funda.erp.web.manufacture;

import java.util.HashMap;

import javax.inject.Inject;

import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.LookupField;

public class PickUpOperatorAndMachine extends AbstractWindow {
	
	@Inject
	private LookupField departmentsField;
	@Inject
	private LookupField employeesField;
	@Inject
	private LookupField machinesField;
	
	public void onPickupWorkOrderClick() {
    	
    	if(validateAll()) {
    		HashMap<String, Object> params =  new HashMap<>();
    		params.put("department", departmentsField.getValue());
    		params.put("employee", employeesField.getValue());
    		params.put("machine", machinesField.getValue());
    		openWindow("PickUpWorkOrder", OpenType.THIS_TAB,params);
    	}
    }
}