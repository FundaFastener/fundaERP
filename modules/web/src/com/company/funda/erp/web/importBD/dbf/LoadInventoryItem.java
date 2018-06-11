package com.company.funda.erp.web.importBD.dbf;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.enums.DbfImportType;
import com.company.funda.erp.service.ImportDBFService;
import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public class LoadInventoryItem extends AbstractWindow {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private Messages messages;
	@Inject
	private Table<InventoryItem> inventoryItemTable;
	@Inject
	private CollectionDatasource<InventoryItem,UUID> inventoryItemsDs;
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
    	inventoryItemTable.selectAll();
    }

    public void query() {
    	Map<String, Object> params = getContext().getParams();
    	params.put(ImportDBFService.DATE_FROM, dfFrom.getValue());
		params.put(ImportDBFService.DATE_TO, dfTo.getValue());
		params.put(ImportDBFService.NO, no.getValue());
		inventoryItemsDs.refresh(params);
    }
    
    private void setDefault() {
    	LocalDate today = LocalDate.now();
    	LocalDate dFrom = today.with(TemporalAdjusters.firstDayOfMonth());
    	LocalDate fTo = today.with(TemporalAdjusters.lastDayOfMonth());
		dfFrom.setValue(FundaDateUtil.dateFromLocalDate(dFrom));
		dfTo.setValue(FundaDateUtil.dateFromLocalDate(fTo));
		no.setValue(null);
		lookupField.setValue(DbfImportType.SKIP_EXISTING);
		inventoryItemsDs.clear();
    }
    
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println("current date : " + localDate);

        LocalDate with = localDate.with(TemporalAdjusters.firstDayOfMonth());
        System.out.println("firstDayOfMonth : " + with);

        LocalDate with1 = localDate.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println("lastDayOfMonth : " + with1);
	}

    public void onImportBtnClick() {
    	int selected = inventoryItemTable.getSelected().size();
    	logger.info("size:{},type:{}",inventoryItemTable.getSelected().size(),lookupField.getValue());
    	int count = importDBFService.setInventoryItemToDb(inventoryItemTable.getSelected(), lookupField.getValue());
    	logger.info("executed:{}",count);
    	showNotification(messages.getMainMessage("result"), 
    			messages.getMainMessage("executed")+count+"\n"+
    			messages.getMainMessage("selected")+selected, 
    			NotificationType.HUMANIZED);

    	setDefault();
    }
}