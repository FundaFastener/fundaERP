package com.company.funda.erp.web.manufacture;

import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;

public class WorkOrderPortal extends AbstractWindow {
	public void pickOperator() {
		openWindow("PickUpOperatorAndMachine", OpenType.THIS_TAB);
	}
	public void scanOperator() {
		
	}
}