package com.company.funda.erp.web.util.ext;

import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;


public interface IntervalJob {

	Timer makeSystemTimer(ComponentsFactory componentsFactory,Window windowScreen);

}
