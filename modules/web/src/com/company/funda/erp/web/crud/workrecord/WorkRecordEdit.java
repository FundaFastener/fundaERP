package com.company.funda.erp.web.crud.workrecord;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.service.WorkRecordService;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;

public class WorkRecordEdit extends AbstractEditor<WorkRecord> {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	private WorkRecordService workRecordService;
	@Inject
	private Datasource<WorkRecord> workRecordDs;
	@Named("fieldGroup.recordNo")
	private TextField recordNo;
	
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		WorkRecord item = (WorkRecord)WindowParams.ITEM.getEntity(params);
		if (PersistenceHelper.isNew(item)) {
			// create
			long recordNo = workRecordService.geteCurrentNo()+1L;
			logger.info("init {}:{}",recordNo);
			item.setRecordNo(recordNo);
			
		}
	}
	
	@Override
	protected boolean preCommit() {
		WorkRecord item = workRecordDs.getItem();
		if (PersistenceHelper.isNew(item)) {
			// create
			item.setRecordNo(workRecordService.getNextRecordNo());
			logger.info("preCommit :create:{}",item.getRecordNo());
		}
		return super.preCommit();
	}
}