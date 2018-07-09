package com.company.funda.erp.web.importBD.dbf;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.enums.DbfImportType;
import com.company.funda.erp.service.ImportDBFService;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public class LoadMachine extends AbstractWindow {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private Table<Machine> machinesTable;
	@Inject
	private CollectionDatasource<Machine, UUID> machinesDs;
	@Inject
	private TextField no;
	@Inject 
	private LookupField lookupField;
	@Inject 
	private ImportDBFService importDBFService;
	
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		setDefault();
		
	}
	
    public void selectAll() {
    	machinesTable.selectAll();
    }
    
    public void query() {
    	Map<String, Object> params = getContext().getParams();
		params.put(ImportDBFService.NO, no.getValue());
		machinesDs.refresh(params);
    }
    
    private void setDefault() {
		no.setValue(null);
		lookupField.setValue(DbfImportType.SKIP_EXISTING);
		machinesDs.clear();
    }
    
    public void onImportBtnClick() {
    	int selected = machinesTable.getSelected().size();
    	logger.info("size:{},type:{}",machinesTable.getSelected().size(),lookupField.getValue());
    	int count = importDBFService.setMachineToDb(machinesTable.getSelected(), lookupField.getValue());
    	logger.info("executed:{}",count);
    	showNotification(messages.getMainMessage("result"), 
    			messages.getMainMessage("executed")+count+"\n"+
    			messages.getMainMessage("selected")+selected, 
    			NotificationType.HUMANIZED);
    	setDefault();

    }

}