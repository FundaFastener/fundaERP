package com.company.funda.erp.web.manufacture;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.enums.OperateType;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;

public class AskUnfinishedWorkRecord extends AbstractWindow {
	
	public static String PROCEED = "proceed";
	public static String MANUAL = "manual";
	public static String CANCEL = "cancel";
	public static String INTERIM = "interim";
	
	@Inject
	protected Messages messages;
	@Inject
	private Label workOrderDesc;
	@Inject 
	private Datasource<WorkOrder> workOrderDs;
	@Inject 
	private Datasource<WorkRecord> workRecordDs;

	public void proceed(Component source) {
		close(PROCEED);
    }
	
    public void manual(Component source) {
		Map<String,Object> params = getContext().getParams();
		params.put("workOrderDesc", workOrderDesc.getValue());
//		logger.info("ask : {}",PrintUtil.printMultiLine(workRecord));
		WorkRecordManualEnd workRecordManualEnd = (WorkRecordManualEnd)openWindow("WorkRecordManualEnd", OpenType.DIALOG,params);
		workRecordManualEnd.addCloseListener(actionId->{
			
			if(StringUtils.equals(actionId, COMMIT_ACTION_ID)) {
				close(INTERIM);
			}
			
		});
    }
    
    public void cancel(Component source) {
    	close(CANCEL);
    }
    
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		WorkOrder workOrder = (WorkOrder)params.get("workOrder");
		workOrderDs.setItem(workOrder);
		workOrderDs.refresh();
		WorkRecord workRecord = (WorkRecord)params.get("workRecord");
		workRecordDs.setItem(workRecord);
		workRecordDs.refresh();
		workOrderDesc.setValue(WorkOrderOperation.genWorkOrderDesc(workOrderDs.getItem(),messages));
	}
    
    public void interim(Component source) {
    	WorkRecord workRecord = workRecordDs.getItem();
    	workRecord.setOperateType(OperateType.INTERIM);
    	workRecordDs.setItem(workRecord);
    	workRecordDs.commit();
    	close(INTERIM);
    }
}