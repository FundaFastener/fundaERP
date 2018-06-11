package com.company.funda.erp.web.importBD.dbf;

import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;

public class Askpreload extends AbstractWindow {

    public void toImportMachine() {
    	openWindow("LoadMachine", OpenType.THIS_TAB);
    }

    public void toImportInventoryItem() {
    	openWindow("LoadInventoryItem", OpenType.THIS_TAB);
    }

    public void toImportWorkOrder() {
    	openWindow("LoadWorkOrder", OpenType.THIS_TAB);
    }
}