package com.company.funda.erp.test;

import java.util.Date;

import javax.inject.Inject;

import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Label;

public class Testpage extends AbstractWindow {
	
	@Inject
	private Label label;

    public void sayHellow() {
    	label.setValue("Hi ~~~ this is test page."+FundaDateUtil.format(new Date(), FundaDateUtil.Type.DAY_TIME_SLASH));
    }
}