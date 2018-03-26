package com.company.funda.erp.gui.format;

import com.haulmont.cuba.gui.components.Formatter;

public class ColonFormatter implements Formatter<String> {

	@Override
	public String format(String value) {
		return String.format("%s : ", value);
	}

}
