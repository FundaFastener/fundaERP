package com.company.funda.erp.web.importBD.excel;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractWindow;

public class AskPreLoadExcel extends AbstractWindow {

    public void toImportWorkRecord() {
        openWindow("LoadWorkRecorder", WindowManager.OpenType.THIS_TAB);
    }
}