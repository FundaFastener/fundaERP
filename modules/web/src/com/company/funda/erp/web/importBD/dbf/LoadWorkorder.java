package com.company.funda.erp.web.importBD.dbf;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.enums.DbfImportType;
import com.company.funda.erp.service.ImportDBFService;
import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Frame.NotificationType;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public class LoadWorkorder extends AbstractWindow {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private Table<WorkOrder> workOrdersTable;
	@Inject
	private CollectionDatasource<WorkOrder,UUID> workOrdersDs;
	@Inject
	private DateField dfFrom;
	@Inject
	private DateField dfTo;
	@Inject
	private TextField no;
	@Inject 
	private LookupField lookupField;
	@Inject 
	private ImportDBFService importDBFService;
	
	@Override
	public void init(Map<String, Object> params) {
		//
		super.init(params);
		setDefault();
		
		//
	}
	
    public void selectAll() {
    	workOrdersTable.selectAll();
    }

    public void query() {
    	Map<String, Object> params = getContext().getParams();
    	params.put(ImportDBFService.DATE_FROM, dfFrom.getValue());
		params.put(ImportDBFService.DATE_TO, dfTo.getValue());
		params.put(ImportDBFService.NO, no.getValue());
		workOrdersDs.refresh(params);
		
    }
    
    private void setDefault() {
    	LocalDate today = LocalDate.now();
    	LocalDate dFrom = today.with(TemporalAdjusters.firstDayOfMonth());
    	LocalDate fTo = today.with(TemporalAdjusters.lastDayOfMonth());
		dfFrom.setValue(FundaDateUtil.dateFromLocalDate(dFrom));
		dfTo.setValue(FundaDateUtil.dateFromLocalDate(fTo));
		no.setValue(null);
		lookupField.setValue(DbfImportType.SKIP_EXISTING);
		workOrdersDs.clear();
    }
    
    public void onImportBtnClick() {
    	int selected = workOrdersTable.getSelected().size();
    	logger.info("size:{},type:{}",workOrdersTable.getSelected().size(),lookupField.getValue());
    	Map<String,String> msgMap = importDBFService.setWorkOrderToDb(workOrdersTable.getSelected(), lookupField.getValue());
    	logger.info("executed:{}",msgMap.get("counter"));
//    	showMessageDialog("result", "executed:"+count, MessageType.CONFIRMATION);
    	showNotification(messages.getMainMessage("result"), 
    			messages.getMainMessage("executed")+msgMap.get("counter")+"\n"+
    			messages.getMainMessage("selected")+selected, 
    			NotificationType.HUMANIZED);
    	int errorSize = Integer.parseInt(msgMap.get("errorSize"));
    	if(errorSize>0) {
    		showMessageDialog(messages.getMainMessage("error.list"), msgMap.get("errorList"), MessageType.CONFIRMATION);
    	}
    	setDefault();


    }
}