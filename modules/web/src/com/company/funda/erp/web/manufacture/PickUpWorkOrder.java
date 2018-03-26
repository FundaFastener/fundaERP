package com.company.funda.erp.web.manufacture;

import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.service.WorkRecordService;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowContext;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.CollectionDatasource;

public class PickUpWorkOrder extends AbstractWindow {
	
	@Inject
	private TextField chargeField;
	@Inject
	private TextField machineField;
	@Inject
	private WindowContext windowContext;
	@Inject
	private CollectionDatasource<WorkOrder, UUID> workOrdersDs;
	@Inject
	protected Messages messages;
	@Inject
	private WorkRecordService workRecordService;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void init(Map<String, Object> params) {
		super.init(params);		
		Employee employee = (Employee)params.get("employee");
		Machine machine = (Machine)params.get("machine");
		setCaption(messages.formatMessage(getClass(), "pickupWorkOrder", machine.getNo()));
		chargeField.setValue(employee.getInstanceName());
		machineField.setValue(machine.getInstanceName());
		workOrdersDs.refresh(ParamsMap.of("machineId", machine.getId()));

	}
	
    public void goToWorkOrderOperate() {
		WorkOrder workOrder = workOrdersDs.getItem();
		Map<String, Object> params = windowContext.getParams();
		params.put("workOrder", workOrder);
		if(workRecordService.isInWorkRecording(workOrder)) {
			WorkRecord workRecord = workRecordService.getLatestRecord(workOrder);
//			logger.info("pick : {}",PrintUtil.printMultiLine(workRecord));
			params.put("workRecord", workRecord);
			AbstractWindow auwrWindow = openWindow("AskUnfinishedWorkRecord", OpenType.THIS_TAB, params);
			auwrWindow.addCloseListener(actionId->{
				if(!StringUtils.equals(actionId, AskUnfinishedWorkRecord.MANUAL)) {
					params.put("actionId", actionId);
					AbstractWindow wooWindow = openWindow("WorkOrderOperation", OpenType.THIS_TAB, params);
					//When child page closed,this page will kept showing.But screen won't interplay.
					whenClosing(params, wooWindow);
				}
			});
		}else {
			AbstractWindow wooWindow = openWindow("WorkOrderOperation", OpenType.THIS_TAB, params);
			//When child page closed,this page will kept showing.But screen won't interplay.
			whenClosing(params, wooWindow);
		}
		
		
    }

	private void whenClosing(Map<String, Object> params, AbstractWindow window) {
		window.addCloseListener(id->{
			//The work order's state may changed.
			Machine machine = (Machine)params.get("machine");
			workOrdersDs.refresh(ParamsMap.of("machineId", machine.getId()));
		});
	}
    


}