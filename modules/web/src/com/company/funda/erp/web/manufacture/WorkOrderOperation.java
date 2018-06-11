package com.company.funda.erp.web.manufacture;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.enums.OperateType;
import com.company.funda.erp.enums.WorkOrderStatus;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.company.funda.erp.enums.WorkRecordStatus;
import com.company.funda.erp.service.WorkHourAgentService;
import com.company.funda.erp.service.WorkRecordService;
import com.company.funda.erp.shift.WorkHour;
import com.company.funda.erp.util.EnumUtil;
import com.company.funda.erp.util.FundaDateUtil;
import com.company.funda.erp.web.FundaWebConfig;
import com.company.funda.erp.web.util.ScreenUtil;
import com.company.funda.erp.web.util.ext.IntervalJob;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Action.ActionPerformedEvent;
import com.haulmont.cuba.gui.components.Action.Status;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.DialogAction;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.Timer;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

/**
 * 
 * @author Howard Chang
 *
 */
public class WorkOrderOperation extends AbstractWindow  {
	
	@Inject
	protected Messages messages;
	@Inject
	private ComponentsFactory componentsFactory;
	@Inject
	private Label sysTimeLabel;
	@Inject
	private TextField chargeTF;
	@Inject
	private TextField machineTF;
	@Inject
	private TextField workOrderTF;
	@Inject
	private TextField typeOfMaterialTF;
	@Inject
	private TextField predictionWeightTF;
	@Inject
	private TextField stateTF;
	@Inject
	private TextField startTimeTF;
	@Inject
	private TextField periodTF;
	@Inject
	private TextField completedQuantityTF;
	@Inject
	private TextField remainingQuantityTF;
	@Inject
	private TextField containerNoTF;
	@Inject
	private Button startTaskBtn;
	@Inject
	private Button stopTaskBtn;
	@Inject
	private Button closeCaseBtn;
	@Inject
	private Table<WorkRecord> wrTable;
	@Inject
	private GroupBoxLayout workOrderRecordStateGB;
	@Inject
	private GroupBoxLayout taskTypeAndStatusGB;
	@Inject
	private GroupBoxLayout manualOperateGB;
	@Inject 
	private Datasource<WorkOrder> workOrderDs;
	@Inject 
	private Datasource<WorkRecord> workRecordDs;
	@Inject
	private WorkRecordService workRecordService;
	@Inject
	private OptionsGroup optionsGroup;
	@Inject
	private FundaWebConfig fundaConfig;
	@Inject
	private CollectionDatasource<WorkRecord, UUID> workRecordsDs;
	@Inject
	private WorkHourAgentService workHourAgentService;
	
	private LocalDateTime workRecordStart = null;
	
	private WorkOrderTimer workOrderTimer = new WorkOrderTimer();
	
	private WorkOrderHelper workOrderHelper = new WorkOrderHelper();
	
	private StatusHelper statusHelper = new StatusHelper();
	
	private WorkHourHelper workHourHelper = new WorkHourHelper();
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static String genWorkOrderDesc(WorkOrder workOrder,Messages messages) {
		StringBuilder workOrderDesc = new StringBuilder();
		workOrderDesc.append(workOrder.getNo())
		.append(":<")
		.append(workOrder.getInventoryItem().getNo())
		.append(">:")
		.append(workOrder.getQuantity())
		.append(messages.getMessage(workOrder.getUnit()));
		return workOrderDesc.toString();
	}
	
	@Override
	public void init(Map<String, Object> params) {
		super.init(params);
		replaceDataOfTable();
		WorkOrder workOrder =  workOrderHelper.getCurrentlyWorkOrder();
		setCaption(messages.formatMessage(getClass(), 
										  "workOrderRecordOperation", 
										  workOrder.getNo(),
										  workOrder.getInventoryItem().getNo()));
		
		setTextFields(workOrder);
		refreshQuantity(workOrder);
		setLayoutByStatus(params, workOrder);
		
		optionsGroup.addValueChangeListener(e->{
			if(null!=e.getValue()) {
				statusHelper.startWorkRecord(yesH -> {
		    		startRecording((WorkRecordStatus)e.getValue());
		        },noH -> {
		        	optionsGroup.setValue(e.getPrevValue());
		        });
			}
		});
		workOrderTimer.makeSystemTimer(componentsFactory, this);
	}

	private void setTextFields(WorkOrder workOrder) {
		chargeTF.setValue(workOrderHelper.getEmployee().getInstanceName());
		machineTF.setValue(workOrderHelper.getMachine().getNo());
		workOrderTF.setValue(WorkOrderOperation.genWorkOrderDesc(workOrder,messages));
		typeOfMaterialTF.setValue("--測試 材料種類--");//TODO-H Don't Hard code.
		containerNoTF.setValue("--測試 容器編號--");//TODO-H Don't Hard code.
		BigDecimal predicteWeight = workOrder.getInventoryItem().getMaterialWeight().multiply((workOrder.getQuantity())).divide(new BigDecimal(1000));
		predictionWeightTF.setValue(getUnitFormat(WorkOrderUnit.KG, predicteWeight));
		statusHelper.setStateTF(workOrder.getStatus());
		setDefaultTimeTF();
	}

	private void setLayoutByStatus(Map<String, Object> params, WorkOrder workOrder) {
		if(workOrder.getStatus()==WorkOrderStatus.NOT_STARTED) {
			buttonsControll(false,startTaskBtn);
			optionsGroup.setValue(null);
			optionsGroup.setEnabled(false);
		}else if(workOrder.getStatus()==WorkOrderStatus.FINISHED) {
			buttonsControll(false);
			optionsGroup.setValue(null);
			optionsGroup.setEnabled(false);
		}else {
			buttonsControll(true,startTaskBtn,stopTaskBtn);
			statusHelper.refreshWorkRecordStatus();
			if(StringUtils.equals((String)params.get("actionId"), AskUnfinishedWorkRecord.PROCEED)) {
				
				WorkRecord workRecord = workRecordService.getLatestRecord(workOrderHelper.getWorkOrder());
				workRecordStart = FundaDateUtil.localDateTimeFromDate(workRecord.getStartTime());
				workHourHelper.initWorkHour(workOrderHelper.getEmployee(), workRecordStart);
				startTimeTF.setValue(FundaDateUtil.format(workRecordStart, FundaDateUtil.Type.DAY_TIME_HYPHEN));
				statusHelper.setStateTF(workRecord.getStatus());
				buttonsControll(false,stopTaskBtn);
				optionsGroup.setValue(workRecord.getStatus());
				optionsGroup.setEnabled(false);
				
			}else if(StringUtils.equals((String)params.get("actionId"), AskUnfinishedWorkRecord.INTERIM)) {

				optionsGroup.setValue(null);
			}
		}
	}
	
	private String getUnitFormat(WorkOrderUnit workOrderUnit,BigDecimal quentity) {
		return workOrderUnit.toSelf(quentity) + " " + messages.getMessage(workOrderUnit);
	}

	private void replaceDataOfTable() {
		wrTable.addGeneratedColumn("finishedQuantity", entity -> {
	        Label label = componentsFactory.createComponent(Label.class);
	        WorkRecord wr = (WorkRecord) wrTable.getItemDatasource(entity).getItem();
	        if(null != wr.getFinishedQuantity()) {
	        	label.setValue(getUnitFormat(wr.getUnit(), wr.getFinishedQuantity()));
	        }
	        return label;
	    });
		wrTable.addGeneratedColumn("setupLossQuantity", entity -> {
	        Label label = componentsFactory.createComponent(Label.class);
	        WorkRecord wr = (WorkRecord) wrTable.getItemDatasource(entity).getItem();
	        if(null != wr.getSetupLossQuantity()) {
	        	label.setValue(getUnitFormat(wr.getSetupLossUnit(), wr.getSetupLossQuantity()));
	        }
	        return label;
	    });
		wrTable.addGeneratedColumn("materialLossQuantity", entity -> {
	        Label label = componentsFactory.createComponent(Label.class);
	        WorkRecord wr = (WorkRecord) wrTable.getItemDatasource(entity).getItem();
	        if(null != wr.getMaterialLossQuantity()) {
	        	label.setValue(getUnitFormat(wr.getMaterialLossUnit(), wr.getMaterialLossQuantity()));
	        }
	        return label;
	    });
		wrTable.addGeneratedColumn("ngLossQuantity", entity -> {
	        Label label = componentsFactory.createComponent(Label.class);
	        WorkRecord wr = (WorkRecord) wrTable.getItemDatasource(entity).getItem();
	        if(null != wr.getNgLossQuantity()) {
	        	label.setValue(getUnitFormat(wr.getNgLossUnit(), wr.getNgLossQuantity()));
	        }
	        return label;
	    });
	}
	
    public void createWorkRecord() {
    	logger.info("create~~");
    	WorkRecord wr = workOrderHelper.genDefaultWorkRecord();
    	wr.setOperateType(OperateType.MANUAL);
    	openWorkRecorkInput(wr);
    }

    public void editWorkRecord() {
    	logger.info("edit~~");
    	final WorkRecord wr =  wrTable.getSingleSelected();
    	if(btnOperatableCheck(wr)) {
        	WorkRecord workRecord = getTableMainWR(wr);	
        	
    		ArrayList<WorkRecord> workRecords = getTableDetachedWR(wr);
        		
    		if(null != workRecord) {
    			openWorkRecorkInput(workRecord, workRecords);
    		}else {
    			deniedAutoWrDialog();
    		}
    	}

    }

	private void deniedAutoWrDialog() {
		showMessageDialog(messages.getMainMessage("notice"), 
				messages.getMainMessage("cannot.modify.record.by.auto.generated"), 
				MessageType.WARNING);
	}

	private ArrayList<WorkRecord> getTableDetachedWR(final WorkRecord wr) {
		ArrayList<WorkRecord> workRecords = (ArrayList<WorkRecord>) workRecordsDs.getItems().stream()
		    .filter(it->(it.getRecordNo()==wr.getRecordNo() && it.getOperateType()==OperateType.DETACH))
		    .collect(Collectors.toList());
		return workRecords;
	}

	private WorkRecord getTableMainWR(final WorkRecord wr) {
		WorkRecord workRecord = workRecordsDs.getItems().stream()
				.filter(it->(it.getRecordNo().equals(wr.getRecordNo()) && EnumUtil.equalIn(it.getOperateType(), OperateType.MANUAL,OperateType.INTERIM)))
				.findAny()
				.orElse(null);
		return workRecord;
	}

	private boolean btnOperatableCheck(final WorkRecord wr) {
		boolean result = true;
		if(null == wr) {
    		showMessageDialog(messages.getMainMessage("notice"), 
    				messages.getMainMessage("please.select.record"), 
    				MessageType.CONFIRMATION);
    		return result = false;
    	}

    	final Date nowDate = new Date();
		Date fromDate = DateUtils.addDays(nowDate,-fundaConfig.getWorkRecordFromDaysBefore());
		fromDate = DateUtils.round(fromDate, Calendar.DATE);
		if(!FundaDateUtil.isBetweenNarrowly(wr.getStartTime(), fromDate, nowDate)) {
			showMessageDialog(messages.getMainMessage("notice"),
					messages.formatMainMessage("please.select.startTime.from.s.to.now", 
							FundaDateUtil.format(fromDate, FundaDateUtil.Type.DAY_SLASH)),
					MessageType.CONFIRMATION);
			return result = false;
		}
		return result;
	}
    
    public void deleteWorkRecord() {
    	logger.info("delete~~");
    	final WorkRecord wr =  wrTable.getSingleSelected();
    	if(btnOperatableCheck(wr)) {
    		WorkRecord workRecord = getTableMainWR(wr);	
    		if(null != workRecord) {
    			
    			showOptionDialog(messages.getMainMessage("delete.confirm"), 
    					messages.formatMainMessage("are.you.sure.delete.recordNo", workRecord.getRecordNo()),
					    MessageType.CONFIRMATION, 
					new Action[] {
				        new DialogAction(DialogAction.Type.YES, Status.PRIMARY).withHandler(e -> {
				        	int deleteRow = workRecordService.deleteBy(wr.getRecordNo());
			    			showMessageDialog(messages.formatMainMessage("recordNo", wr.getRecordNo()), 
			    					messages.formatMainMessage("delete.rows", deleteRow), 
			    					MessageType.CONFIRMATION);
			    			workRecordsDs.refresh();
				        }),
				        new DialogAction(DialogAction.Type.NO, Status.NORMAL)
				    }
    		    );
    			
    		}else {
    			deniedAutoWrDialog();
    		}
    		
    		
    	}
    }
	
    public void onStopTaskBtnClick() {
    	//open an dialog page which load the latest working record data.
    	getContext().getParams().put("manualEndTime",new Date());
    	WorkRecord workRecord = workRecordService.getLatestRecord(workOrderHelper.getWorkOrder());
    	openWorkRecorkInput(workRecord);
    	
    }

	private void openWorkRecorkInput(WorkRecord workRecord) {
		workHourHelper.clearWorkHour();
    	getContext().getParams().put("workRecord",workRecord);
    	AbstractWindow abstractWindow = openWindow("WorkRecordInput", OpenType.DIALOG,getContext().getParams());
    	
    	abstractWindow.addCloseListener(actionId->{
    		if(getContext().getParams().containsKey("manualEndTime")) {
    			getContext().getParams().remove("manualEndTime");
    		}
    		if(getContext().getParams().containsKey("workRecords")) {
    			getContext().getParams().remove("workRecords");
    		}
    		getDsContext().refresh();
    		statusHelper.refreshWorkRecordStatus();
    		if(StringUtils.equals(actionId, CLOSE_ACTION_ID)) {
    			
    			if(statusHelper.isInWorkRecording()) {
    				stopTaskBtn.setEnabled(true);
    				workHourHelper.initWorkHour(workOrderHelper.getEmployee(), LocalDateTime.now());
    			}else {
    				stopTaskBtn.setEnabled(false);
    			}
    			ScreenUtil.setAllButtonEnableOrNot(manualOperateGB, !stopTaskBtn.isEnabled());
    			
    		}else if(StringUtils.equals(actionId, COMMIT_ACTION_ID)) {
    			buttonsControll(true,startTaskBtn,stopTaskBtn);
        		optionsGroup.setValue(null);
        		optionsGroup.setEnabled(true);
        		statusHelper.setStateTF(WorkOrderStatus.IN_PROCESS);
        		setDefaultTimeTF();
        		refreshQuantity(workOrderHelper.getWorkOrder());
    		}
    	});
	}
	
	private void openWorkRecorkInput(WorkRecord workRecord,List<WorkRecord> workRecords) {
		getContext().getParams().put("workRecords",workRecords);
		openWorkRecorkInput(workRecord);
	}
    
    public void onStartTaskBtnClick() {
    	buttonsControll(true,startTaskBtn,stopTaskBtn);
    	optionsGroup.setValue(null);
    	optionsGroup.setEnabled(true);
    	statusHelper.setStateTF(WorkOrderStatus.IN_PROCESS);
    	statusHelper.changeWorkOrderStatus(WorkOrderStatus.IN_PROCESS);
    	
    }
	
    public void onCloseCaseBtnClick() {
    	showOptionDialog(getMessage("close.workOrder.dialog"), 
			getMessage("close.workOrder.dialog.msg"), MessageType.CONFIRMATION, 
			new Action[] {
		        new DialogAction(DialogAction.Type.YES, Status.PRIMARY).withHandler(e -> {
		        	statusHelper.changeWorkOrderStatus(WorkOrderStatus.FINISHED);
		        	close(CLOSE_ACTION_ID);
		        }),
		        new DialogAction(DialogAction.Type.NO, Status.NORMAL).withHandler(e ->{
		        	closeCaseBtn.setEnabled(true);
		        })
		    }
    	);

    }
	
	@Override
	protected boolean preClose(String actionId) {
		if(statusHelper.isInWorkRecording()) {
			onStopTaskBtnClick();
		}
		return super.preClose(actionId);
	}
	
	private void startRecording(WorkRecordStatus workRecordStatus) {
    	workRecordStart = LocalDateTime.now();
    	buttonsControll(false,stopTaskBtn);
    	optionsGroup.setEnabled(false);
		startTimeTF.setValue(FundaDateUtil.format(workRecordStart, FundaDateUtil.Type.DAY_TIME_HYPHEN));
		statusHelper.setStateTF(workRecordStatus);
    	workOrderHelper.genWorkRecord(workRecordStatus);
    	workRecordsDs.refresh();
    	statusHelper.refreshWorkRecordStatus();
    	workHourHelper.initWorkHour(workOrderHelper.getEmployee(), workRecordStart);
    }
	
	private void refreshQuantity(WorkOrder workOrder) {
		final BigDecimal completedQuantity = workOrderHelper.getCompletedQuentity();
		completedQuantityTF.setValue(getUnitFormat(workOrder.getUnit(), completedQuantity));
		remainingQuantityTF.setValue(getUnitFormat(workOrder.getUnit(), workOrderHelper.getRemainQuentity(workOrder,completedQuantity)));
    }

	private void buttonsControll(boolean isEnable,Button ...versaButtons) {
		ScreenUtil.setAllButtonEnableOrNot(workOrderRecordStateGB, isEnable);
		ScreenUtil.setAllButtonEnableOrNot(taskTypeAndStatusGB, isEnable);
		ScreenUtil.setAllButtonEnableOrNot(manualOperateGB, isEnable);
		for (Button button : versaButtons) {
			button.setEnabled(!isEnable);
		}
		
    }
	
	private void setDefaultTimeTF() {
		startTimeTF.setValue(FundaDateUtil.DEFAULT_TIME_LITERAL);
		periodTF.setValue(FundaDateUtil.DEFAULT_TIME_LITERAL);
	}
	
	
	//Below contain Inner Class ========================================================================
	
	class WorkHourHelper{
		
		public static final int LEVEL_ONE_MIN = 60000;
		public static final int LEVEL_TEN_MIN = 600000;
		private WorkHour nowWork = null;
		
		private int countDown = 0;
		
		public void initCountDown(LocalDateTime localDateTime) {
			logger.info(" initCountDown ");
			int result = 0;
			if(null != getWorkHour()) {
				final Long seconds = Duration.between(localDateTime.toLocalTime(), getWorkHour().getTo()).getSeconds();
				final int restSec = seconds.intValue()*1000;
				
				if(restSec>LEVEL_TEN_MIN) {
					result = calcCountDown(LEVEL_TEN_MIN);
				}else if(restSec>LEVEL_ONE_MIN) {
					result = calcCountDown(LEVEL_ONE_MIN);
				}
				logger.info(" seconds:{},restSec:{},result:{}",seconds,restSec,result);
			}
			setCountDown(result);
		}
		
		public void initWorkHour(Employee employee,LocalDateTime localDateTime) {
			nowWork = workHourAgentService.getNowWorkHour(employee, localDateTime);
			setCountDown(0);
		}
		
		public void clearWorkHour() {
			nowWork = null;
		}
		
		public WorkHour getWorkHour() {
			return nowWork;
		}
		
		private int calcCountDown(int resetSec) {
			int result = resetSec/10;
			if(result>fundaConfig.getFundaClockRefreshDelay()) {
				result = result/fundaConfig.getFundaClockRefreshDelay();
			}else {
				result = 0;
			}
			return result;
		}

		public int getCountDown() {
			return countDown;
		}

		public void setCountDown(int countDown) {
			this.countDown = countDown;
		}
		
		public void countOneTimes() {
			this.countDown--;
		}
		
		public void workHourTimeOut(LocalDateTime localDateTime) {
			if(null != getWorkHour()) {
				if(getCountDown()>0) {
//					logger.info("count down:{}",getCountDown());
					countOneTimes();
				}else if(getCountDown()==0) {
//					logger.info("count down ==0 ");
					final LocalTime localTime = localDateTime.toLocalTime();
					if(localTime.isAfter(getWorkHour().getTo())) {
//						logger.info("localTime is afger deadline:{}",localTime.isAfter(getWorkHour().getTo()));
						getContext().getParams().put("manualEndTime",FundaDateUtil.timeToTodayDateTime(localTime));
						onStopTaskBtnClick();
					}else {
//						logger.info("localTime is afger deadline:{}",localTime.isAfter(getWorkHour().getTo()));
						initCountDown(localDateTime);
					}
					
				}
			}
		}
		

	}
	
	/**
	 * 協助外部類別處理Timer，可以視情況增加Instance。
	 * @author Howard Chang
	 *
	 */
	class WorkOrderTimer extends AbstractWorkOrderTimer{

		@Override
		void hook() {
			sysTimeLabel.setValue(new Date());
			if(statusHelper.isInWorkRecording()&& null!=workRecordStart) {
				periodTF.setValue(FundaDateUtil.getChronograph(workRecordStart));

				workHourHelper.workHourTimeOut(LocalDateTime.now());
			}
		}

		@Override
		void onStop() {
			//showNotification(getMessage("TimerIsStopped"), NotificationType.HUMANIZED);
			logger.debug("{},{}",(String)workOrderTF.getValue(),getMessage("TimerIsStopped"));
		}
		
	}
	
	/**
	 * 抽象類別，須被實作Timer執行時的hook()，以及結束時要執行的onStop()。
	 * @author Howard Chang
	 *
	 */
	abstract class AbstractWorkOrderTimer implements IntervalJob{
		
		abstract void hook();
		abstract void onStop();

		@Override
		public Timer makeSystemTimer(ComponentsFactory componentsFactory,Window windowScreen) {

			Timer sysTimer = componentsFactory.createTimer();
			windowScreen.addTimer(sysTimer);
			sysTimer.setDelay(fundaConfig.getFundaClockRefreshDelay());
			sysTimer.setRepeating(true);
			sysTimer.addActionListener(listener -> {
				hook();
			});
			sysTimer.addStopListener(listener -> {
				onStop();
			});
			sysTimer.start();
			return sysTimer;
		}
	}
	
	/**
	 * 協助畫面處理資料存取及轉換
	 * @author Howard Chang
	 *
	 */
	class WorkOrderHelper {
		
		private String workOrderLiteral = "workOrder";
		
		private WorkOrder getCurrentlyWorkOrder() {
			workOrderDs.setItem(getWorkOrder());
			workOrderDs.refresh();
			getContext().getParams().put(workOrderLiteral, workOrderDs.getItem());
			return getWorkOrder();
		}
		
		private WorkOrder getWorkOrder() {
			return (WorkOrder)getContext().getParams().get(workOrderLiteral);
		}
		
		private Employee getEmployee() {
			return (Employee)getContext().getParams().get("employee");
		}

		private Machine getMachine() {
			return (Machine)getContext().getParams().get("machine");
		}
		
		private BigDecimal getCompletedQuentity() {
			BigDecimal completedQuantity = new BigDecimal(0);
			workRecordsDs.refresh();
			for(WorkRecord wr:workRecordsDs.getItems()) {
				if(null != wr.getFinishedQuantity()) {
					logger.info("{}",wr.getFinishedQuantity().toString());
					completedQuantity = completedQuantity.add(wr.getFinishedQuantity());
				}
			}
			return completedQuantity;
		}
		
		private BigDecimal getRemainQuentity(WorkOrder workOrder,BigDecimal completedQuantity) {
			BigDecimal remainQuentity = workOrder.getQuantity().subtract(completedQuantity);
			if (remainQuentity.compareTo(BigDecimal.ZERO)<0) {
				remainQuentity = BigDecimal.ZERO;
			}
			return remainQuentity;
		}

		private void executeOptionDialog(Consumer<ActionPerformedEvent> yesHandler,Consumer<ActionPerformedEvent> noHandler,String title,String message) {
    		showOptionDialog(title,message, MessageType.CONFIRMATION,new Action[] {
    		        new DialogAction(DialogAction.Type.YES, Status.PRIMARY).withHandler(yesHandler),
    		        new DialogAction(DialogAction.Type.NO, Status.NORMAL).withHandler(noHandler)
    		});
	    }
		
		private void commitWorkOrder(WorkOrder workOrder) {
	    	workOrderDs.setItem(workOrder);
	    	workOrderDs.commit();
		}
		
		private void genWorkRecord(WorkRecordStatus workRecordStatus) {
			WorkRecord wr = genDefaultWorkRecord();
			wr.setStartTime(FundaDateUtil.dateFromLocalDateTime(workRecordStart));
			wr.setStatus(workRecordStatus);
			wr.setOperateType(OperateType.AUTO);
			workRecordDs.setItem(wr);
			workRecordDs.commit();
		}

		private WorkRecord genDefaultWorkRecord() {
			WorkRecord wr = new WorkRecord();
			wr.setWorkOrder(getWorkOrder());
			wr.setRecordNo(workRecordService.getNextRecordNo());
			wr.setEmployee(workOrderHelper.getEmployee());
	    	wr.setUnitWeight(workOrderHelper.getWorkOrder().getInventoryItem().getNetWeight());
	    	wr.setUnit(WorkOrderUnit.PC);	
	    	wr.setSetupLossQuantity(BigDecimal.ZERO);
	    	wr.setSetupLossUnit(WorkOrderUnit.KG);
	    	wr.setNgLossQuantity(BigDecimal.ZERO);
	    	wr.setNgLossUnit(WorkOrderUnit.KG);
	    	wr.setMaterialLossQuantity(BigDecimal.ZERO);
	    	wr.setMaterialLossUnit(WorkOrderUnit.KG);
			return wr;
		}
			    
	}
	
	/**
	 * 處理畫面中關於狀態的邏輯
	 * @author Howard Chang
	 *
	 */
	class StatusHelper{
		
		private boolean isInWorkRecording = false;
		
		private void changeWorkOrderStatus(WorkOrderStatus stataus) {
			WorkOrder workOrder = workOrderHelper.getWorkOrder();
	    	workOrder.setStatus(stataus);
	    	workOrderHelper.commitWorkOrder(workOrder);
		}
		
		private boolean isInWorkRecording() {
			return isInWorkRecording;
		}
		
		private void refreshWorkRecordStatus() {
			isInWorkRecording = workRecordService.isInWorkRecording(workOrderHelper.getWorkOrder());
		}
		
	    private void startWorkRecord(Consumer<ActionPerformedEvent> yesHandler,Consumer<ActionPerformedEvent> noHandler) {
	    	workOrderHelper.executeOptionDialog(yesHandler,noHandler,getMessage("check.switch.status"), getMessage("check.switch.status.msg"));	
	    }
	    
		private void setStateTF(@SuppressWarnings("rawtypes") Enum caller) {
			stateTF.setValue(caller);
		}
		
	}
	


}

