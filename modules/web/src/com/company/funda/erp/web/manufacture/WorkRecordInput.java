package com.company.funda.erp.web.manufacture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.enums.OperateType;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.company.funda.erp.service.WorkHourAgentService;
import com.company.funda.erp.service.WorkRecordService;
import com.company.funda.erp.util.FundaDateUtil;
import com.company.funda.erp.web.FundaWebConfig;
import com.company.funda.erp.web.validate.BiggerThenZeroValidator;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.FieldGroup;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextArea;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * 
 * @author Howard Chang
 *
 */
public class WorkRecordInput extends AbstractWindow {
	
	@Inject
	private Datasource<WorkRecord> workRecordDs;
	@Inject
	private LookupField wrStatusField;
	@Inject
	private TextField wrTimeUsedField;
	@Inject
	private TextArea wrRemarkTextArea;
	@Inject
	private CollectionDatasource<WorkRecord, UUID> workRecordsDs;
	@Inject
	private WorkRecordService workRecordService;
	@Named("fieldGroup.unit")
	private LookupField unit;
	@Named("fieldGroup.workHourType")
	private LookupField workHourType;
	@Inject
	protected Messages messages;
	@Inject
	private FieldGroup fieldGroup;
	@Inject
    private ComponentsFactory componentsFactory;
	@Inject 
	private WorkHourAgentService workHourAgentService;
	@Inject
	private GroupBoxLayout detachGB;
	@Inject
	private LookupField statusField;
	@Inject
	private DateField startTimeField;
	@Inject
	private DateField endTimeField;
	@Named("fieldGroup.timeUsed")
	private TextField timeUsed; 
	@Inject
	private BiggerThenZeroValidator lessThenZeroValidator;
	@Inject
	private Table<WorkRecord> workRecordsTable;
	@Inject
	private FundaWebConfig fundaConfig;
	
	private Date endTime;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TextField finishedQuantityAll;
	
	private TextField finishedIncludedLoss;
	
	private List<WorkRecord> allCorrectedWorkRecords;
	
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		defaultValue(params);
		quantityAll(params);
		detachGB.setExpanded(false);
		if(isAuto(params)) {
			statusField.setEditable(false);
			startTimeField.setEditable(false);
			endTimeField.setEditable(false);
		}
		addCalcTimeUsedListener(startTimeField);
		addCalcTimeUsedListener(endTimeField);
		
		//取合格的資料(AUTO and MANUAL)
		allCorrectedWorkRecords = workRecordService.getLatestDaysCorrectedRecords((WorkOrder)params.get("workOrder"), "");
		List<WorkRecord> allWorkRecords = workRecordService.getLatestDaysRecords((WorkOrder)params.get("workOrder"), "");

		if(!allWorkRecords.isEmpty() && !workRecordService.isAllHaveEndTime(allWorkRecords)) {
			
			showNotification(messages.getMainMessage("check.all.endTime.is.inputed"),NotificationType.WARNING);
			finishedQuantityAll.setEnabled(false);
		}
	}

	private void addCalcTimeUsedListener(DateField dateField) {
		dateField.addValueChangeListener(e->{	
			timeUsed.setValue("");
			if(startTimeField.getValue()!=null && endTimeField.getValue()!=null) {
				timeUsed.setValue(FundaDateUtil.minutesBetween(startTimeField.getValue(), endTimeField.getValue()));
			}
		});
	}

	private boolean isAuto(Map<String, Object> params) {
		final WorkRecord workRecord = (WorkRecord)params.get("workRecord");
		return workRecord.getOperateType()==OperateType.AUTO;
	}

	private void defaultValue(Map<String, Object> params) {
		
		final WorkRecord workRecord = (WorkRecord)params.get("workRecord");
		
		if(params.containsKey("manualEndTime")) {
			endTime = (Date) params.get("manualEndTime");
			workRecord.setEndTime(endTime);
		}
		if(null != workRecord.getStartTime() && null != workRecord.getEndTime()) {
			workRecord.setTimeUsed(FundaDateUtil.minutesBetween(workRecord.getStartTime(), workRecord.getEndTime()));
		}
		workRecordDs.setItem(workRecord);
		if(params.containsKey("workRecords")) {
			@SuppressWarnings("unchecked")
			List<WorkRecord> workRecords = (List<WorkRecord>)params.get("workRecords");
			workRecords.stream().forEach(wr->{
				workRecordsDs.addItem(wr);
			});
		}
		if(null != endTime) {
			final Employee emp = (Employee)params.get("employee");
			workHourType.setValue(workHourAgentService.getWorkHourType(emp, FundaDateUtil.localDateTimeFromDate(endTime)));
		}
		
	}

	/**
	 * 總量輸入邏輯
	 * @param params
	 */
	private void quantityAll(Map<String, Object> params) {
		FieldGroup.FieldConfig finishedQuantityAllConfig = fieldGroup.getField("finishedQuantityAll");
		finishedQuantityAll = componentsFactory.createComponent(TextField.class);
		finishedQuantityAll.setCaption(messages.getMainMessage("finishedQuantityAll"));
		finishedQuantityAll.setDatatype(Datatypes.get(BigDecimal.class));
		finishedQuantityAllConfig.setComponent(finishedQuantityAll);
		
		FieldGroup.FieldConfig finishedIncludedLossConfig = fieldGroup.getField("finishedIncludedLoss");
		finishedIncludedLoss = componentsFactory.createComponent(TextField.class);
		finishedIncludedLoss.setCaption(messages.getMainMessage("finishedIncludedLoss"));
		finishedIncludedLoss.setDatatype(Datatypes.get(BigDecimal.class));
		finishedIncludedLossConfig.setComponent(finishedIncludedLoss);
		
		WorkRecord workRecord = (WorkRecord)params.get("workRecord");
		logger.info("workRecord getFinishedQuantity:{}",workRecord.getFinishedQuantity());
		if(null != workRecord.getFinishedQuantity()) {
			workRecord = workRecordService.addLoss(workRecord);
			finishedIncludedLoss.setValue(workRecord.getFinishedQuantity());
		}

        finishedQuantityAll.addValueChangeListener(e->{
        	if(e.getValue() == null || StringUtils.isBlank(e.getValue().toString())) {
        		finishedIncludedLoss.setEnabled(true);
        	}else {
        		final WorkOrderUnit nowUnit = unit.getValue();
        		BigDecimal accumulateQuantity = workRecordService.getAccumulateQuantity(
        				(WorkOrder)params.get("workOrder"),nowUnit);
        		BigDecimal realTimeQuentity = (BigDecimal)e.getValue();
        		final WorkRecord workRecord_ = (WorkRecord)params.get("workRecord");
        		if(null != workRecord_.getFinishedQuantity()) {
        			accumulateQuantity = accumulateQuantity.subtract(workRecord_.getFinishedQuantity());
        		}
        		if(realTimeQuentity.compareTo(accumulateQuantity)<0) {

        			showMessageDialog(messages.getMainMessage("error"), 
        					messages.formatMainMessage("finishedQuantityAll.cannot.less.then", nowUnit.toSelf(accumulateQuantity)), 
        					MessageType.WARNING);
        			finishedQuantityAll.setValue(null);
        			return;
        		}
        		finishedIncludedLoss.setValue(realTimeQuentity.subtract(accumulateQuantity));
        		finishedIncludedLoss.setEnabled(false);
        	}

        });
        
        unit.addValueChangeListener(e->{
        	finishedQuantityAll.setValue(null);
        	finishedQuantityAll.setEnabled(finishedQuantityAll.isEnabled());
        	finishedIncludedLoss.setValue(null);
        	finishedIncludedLoss.setEnabled(true);
        });
	}
	
    public void onConfirm(Component source) {
    	if(validateAll()) {
    		ArrayList<String> errorMsg = new ArrayList<>();
    		boolean needList = customValidate(errorMsg);
    		if(!errorMsg.isEmpty()) {
    			showErrorMsg(errorMsg, needList);
    		}else {
    			try {
        			setFinishedQuentity();
        			setManualReplaceInterim();
    				workRecordService.commitWorkRecords((WorkOrder)getContext().getParams().get("workOrder")
    						,workRecordDs.getItem(),workRecordsDs.getItems());
    	    		close(COMMIT_ACTION_ID,true);   
    			} catch (Exception e) {
    				showNotification(e.getMessage());
    			}
    		}
    		
    	}
    }

	private void showErrorMsg(ArrayList<String> errorMsg, boolean needList) {
		logger.info("errorMsg.size():{}",errorMsg.size());
		StringBuilder sb = new StringBuilder();
		errorMsg.forEach(msg->{
			sb.append(errorMsg.indexOf(msg)).append(" : ")
			.append(msg)
			.append("\n");
			
		});
		if(needList) {
			sb.append(messages.getMainMessage("workRecord.startTime.endTime.list"));
			allCorrectedWorkRecords.forEach(wr->{
				sb.append(format(wr.getStartTime())).append(" - ").append(format(wr.getEndTime()));
				sb.append("\n");
			});
		}
		showOptionDialog(messages.getMainMessage("error"), sb.toString(), MessageType.CONFIRMATION, 
		new Action[] {
		        new DialogAction(DialogAction.Type.OK, Status.PRIMARY)
		    }
		);
	}

	private boolean customValidate(ArrayList<String> errorMsg) {
		boolean needList = false;
		//不可新增或編輯-x day to today 以外的時間
		checkModifiable(errorMsg);
		//起日要小於迄日
		checkPeriodReasonable(errorMsg);
		//起日 迄日 不可在報工紀錄區段 
		needList = checkInRecordSection(errorMsg);
		//起迄應在同一閒置區段
		needList = checkInSameSection(errorMsg);
		if(null != finishedQuantityAll.getValue()) {
			//起日是最新且資料正確時，可以輸入總量，反之則否
			final ArrayList<WorkRecord> correctedWorkRecords = (ArrayList<WorkRecord>) getAllCorrectedWorkRecords();
			if(correctedWorkRecords.size()>0) {
				final WorkRecord latestWorkRecord = correctedWorkRecords.get(0);
				if(!errorMsg.isEmpty() || startTimeField.getValue().before(latestWorkRecord.getEndTime())) {
					errorMsg.add(messages.getMainMessage("input.finishedquantityall.need.fixed.all.error"));					
					needList = true;
				}
			}
		}
		return needList;
	}
	
	private List<WorkRecord> getAllCorrectedWorkRecords(){
		ArrayList<WorkRecord> result = new ArrayList<>(allCorrectedWorkRecords);
		final WorkRecord currentWorkRecord = workRecordDs.getItem();
		logger.info("123:{}",result.size());
		result.removeIf(wr->(currentWorkRecord.getId().equals(wr.getId())));
		logger.info("456:{}",result.size());
		return result;
	}

	private boolean checkInSameSection(ArrayList<String> errorMsg) {
		boolean needList = false;
		if(getAllCorrectedWorkRecords().size()>0 && !workRecordService.inSameSection(getAllCorrectedWorkRecords(), 
				startTimeField.getValue(), 
				endTimeField.getValue())) {
			
			errorMsg.add(messages.formatMainMessage("must.in.same.section", startTimeField.getCaption(),endTimeField.getCaption()));	
			needList = true;
		}
		return needList;
	}

	private boolean checkInRecordSection(ArrayList<String> errorMsg) {
		boolean needList = false;
		if(workRecordService.isInRecordSection(getAllCorrectedWorkRecords(), (Date) startTimeField.getValue())) {
			
			errorMsg.add(messages.formatMainMessage("cannot.interrupt.other.record", startTimeField.getCaption()));
			needList = true;
		}
		if(workRecordService.isInRecordSection(getAllCorrectedWorkRecords(), (Date) endTimeField.getValue())) {
			
			errorMsg.add(messages.formatMainMessage("cannot.interrupt.other.record", endTimeField.getCaption()));
			needList = true;
		}
		return needList;
	}

	//因為兩個日期欄位，且日期欄位輸入日期時就init xxxx/xx/xx 00:00，所以無法使用DatePeriodValidator
	private void checkPeriodReasonable(ArrayList<String> errorMsg) {
		if(!lessThenZeroValidator.isBiggerThenZero(timeUsed.getValue())) {
			errorMsg.add(lessThenZeroValidator.getMessage(timeUsed.getCaption()));
		}
	}

	private void checkModifiable(ArrayList<String> errorMsg) {
		final Date nowDate = new Date();
		Date fromDate = DateUtils.addDays(nowDate,-fundaConfig.getWorkRecordFromDaysBefore());
		fromDate = DateUtils.round(fromDate, Calendar.DATE);
		if(!FundaDateUtil.isBetweenNarrowly(startTimeField.getValue(), fromDate, nowDate)) {
			errorMsg.add(messages.formatMainMessage("please.input.n.days.till.now", startTimeField.getCaption(),fundaConfig.getWorkRecordFromDaysBefore()));
		}
		if(!FundaDateUtil.isBetweenNarrowly(endTimeField.getValue(), fromDate, nowDate)) {
			errorMsg.add(messages.formatMainMessage("please.input.n.days.till.now", endTimeField.getCaption(),fundaConfig.getWorkRecordFromDaysBefore()));
		}
	}
    private String format(Date date) {
		return FundaDateUtil.format(date, FundaDateUtil.Type.DAY_TIME_SLASH);
	}
	private void setFinishedQuentity() {
		final WorkRecord workRecord = workRecordDs.getItem();
		workRecord.setFinishedQuantity(finishedIncludedLoss.getValue());
		workRecordDs.setItem(workRecord);
	}
	private void setManualReplaceInterim() {
		final WorkRecord workRecord = workRecordDs.getItem();
		if(workRecord.getOperateType()==OperateType.INTERIM) {
			workRecord.setOperateType(OperateType.MANUAL);
		}
		workRecordDs.setItem(workRecord);
	}

    public void onCancel(Component source) {
    	close(CLOSE_ACTION_ID);
    }

    public void onAddItem(Component source) {
    	if(isStatusValid() && isTimeUsedValid() && allTimeUsedLessThenMain(wrTimeUsedField.getValue())) {
    		WorkRecord wr = new WorkRecord();
        	wr = syncWithMainWorkRecord(wr);
        	wr.setId(UuidProvider.createUuid());
        	wr.setStatus(wrStatusField.getValue());
        	wr.setTimeUsed(wrTimeUsedField.getValue());
        	wr.setRemark(wrRemarkTextArea.getValue());
        	wr.setOperateType(OperateType.DETACH);
        	workRecordsDs.addItem(wr);
        	cleanAddInput();
    	}
    }
    
    private boolean isStatusValid() {
    	return isComponentValid(wrStatusField);
    }
    
    private boolean isTimeUsedValid() {
    	return isComponentValid(wrTimeUsedField);
    }
    
    private <T extends Field>boolean isComponentValid(T field) {
    	field.setRequired(true);
    	final boolean result = field.isValid();
    	if(!result) {
    		showNotification(field.getRequiredMessage());
    	}
    	field.setRequired(false);
    	field.requestFocus();
    	return result;
    }
    
    private void cleanAddInput() {
    	wrStatusField.setValue(null);
    	wrTimeUsedField.setValue(null);
    	wrRemarkTextArea.setValue(null);
    }
    
    private WorkRecord syncWithMainWorkRecord(WorkRecord workRecord) {
    	final WorkRecord mainWR = workRecordDs.getItem();
    	workRecord.setRecordNo(mainWR.getRecordNo());
    	workRecord.setStartTime(mainWR.getStartTime());
    	workRecord.setEndTime(mainWR.getEndTime());
    	workRecord.setEmployee(getContext().getParamValue("employee"));
    	workRecord.setWorkOrder(getContext().getParamValue("workOrder"));
    	workRecord.setWorkHourType(mainWR.getWorkHourType());
    	return workRecord;
    }
    
    private boolean allTimeUsedLessThenMain(Integer inputTimeused) {
    	final Integer sum = workRecordsDs.getItems().stream().mapToInt(WorkRecord::getTimeUsed).sum();
    	final Integer main = workRecordDs.getItem().getTimeUsed();
    	logger.info("input,sum,main,result :{},{},{},{}",inputTimeused,sum,main,(sum + inputTimeused <= main));
    	if(!(sum + inputTimeused <= main)) {
    		showNotification(messages.getMainMessage("cannot.make.time.used.bigger.then.main"));
    	}
    	return (sum + inputTimeused <= main);
    }

    public void onRemove(Component source) {
    	workRecordsDs.removeItem(workRecordsTable.getSingleSelected());
    }
}