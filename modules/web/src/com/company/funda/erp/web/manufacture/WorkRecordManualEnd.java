package com.company.funda.erp.web.manufacture;

import java.util.Date;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.util.FundaDateUtil;
import com.company.funda.erp.web.validate.DatePeriodValidator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Label;

/**
 * Screen.For a case that work record should input it's end time.
 * @author Howard Chang
 *
 */
public class WorkRecordManualEnd extends AbstractWindow {

	@Inject
	private Label workOrderName;
	@Inject
	private Label workRecordName;
	@Inject
	private Label workOrderDesc;
	@Inject
	private Label workRecordDesc;
	@Inject
	protected Messages messages;
	@Inject
	private DateField manualEndTime;
	@Inject
	private DatePeriodValidator datePeriodValidator;
	
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		WorkRecord workRecord = (WorkRecord)params.get("workRecord");
		workOrderName.setValue(messages.getMainMessage("WorkOrder"));
		workOrderDesc.setValue(params.get("workOrderDesc"));
		workRecordName.setValue(messages.getMainMessage("WorkRecord"));
		workRecordDesc.setValue(genWorkRecordDesc(workRecord));
		manualEndTime.setValue(genDefaultEnd(workRecord.getStartTime()));
		
		manualEndTime.addValidator(value->{
			datePeriodValidator.reasonablePeriod(workRecord.getStartTime(), manualEndTime.getValue());
		});
	}
	
    public void confirm(Component source) {
    	if(isValid()) {
    		Map<String, Object> params = getContext().getParams();
    		params.put("manualEndTime", manualEndTime.getValue());
			AbstractWindow abstractWindow = openWindow("WorkRecordInput", OpenType.DIALOG,getContext().getParams());
			
			abstractWindow.addCloseListener(actionId->{
	    		if(StringUtils.equals(actionId, COMMIT_ACTION_ID)) {
	    			close(COMMIT_ACTION_ID);
	    		}
	    	}); 		
    	}else {
    		//TODO-H Don't hard code here.
    		showNotification("Please input correct end time!", NotificationType.WARNING);
    	}
    }

    public void cancel(Component source) {
    	close(CLOSE_ACTION_ID);
    }
    
    public Date getManualEndTime() {
    	return manualEndTime.getValue();
    }
    
    private String genWorkRecordDesc(WorkRecord workRecord) {
    	StringBuilder workRecordDescSb = new StringBuilder();
		workRecordDescSb
		.append(messages.getMainMessage("WorkRecord"))
		.append(" ")
		.append(messages.getMainMessage("status"))
		.append(" - ")
		.append(messages.getMessage(workRecord.getStatus()))
		.append(messages.getMainMessage("punchuation.comma"))
		.append(messages.getMainMessage("WorkRecord"))
		.append(" ")
		.append(messages.getMainMessage("startTime"))
		.append(" - ")
		.append(FundaDateUtil.format(workRecord.getStartTime(), FundaDateUtil.Type.DAY_TIME_SLASH))
		.append(messages.getMainMessage("punchuation.period"));
		return workRecordDescSb.toString();
    }
    
    private Date genDefaultEnd(Date start) {
    	Date now = new Date();
    	if(DateUtils.isSameDay(start, now)) {
    		return now;
    	}else {
    		return FundaDateUtil.offWorkDate(start);
    	}
    }
}