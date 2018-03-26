package com.company.funda.erp.web.util;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Component.Container;

public class ScreenUtil {

	/**
	 * Let the screen's button all enable or disable.
	 * @param component
	 * @param isEnable
	 */
	public static void setAllButtonEnableOrNot(Component component,boolean isEnable) {
		if(component instanceof Container) {
			((Container) component).getOwnComponents().forEach(c->{
				setAllButtonEnableOrNot(c,isEnable);
			});
		}else {
			if(component instanceof Button) {
				component.setEnabled(isEnable);
			}
		}
	}
}
