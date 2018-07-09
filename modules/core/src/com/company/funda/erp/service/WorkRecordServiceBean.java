package com.company.funda.erp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.enums.OperateType;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.app.UniqueNumbersAPI;

@Service(WorkRecordService.NAME)
public class WorkRecordServiceBean implements WorkRecordService {
	
	@Inject
    private Persistence persistence;
	@Inject
    private UniqueNumbersAPI uniqueNumbers;

    private ValidateWorkRecord validateWorkRecord = new ValidateWorkRecord() ;
	
//	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public WorkRecord getLatestRecord(WorkOrder workOrder) {
		WorkRecord wr = null;
		try(Transaction tx = persistence.getTransaction()){
			TypedQuery<WorkRecord> query = persistence.getEntityManager().createQuery(
	                " select wr from fe$WorkRecord wr where wr.workOrder.id = ?1 "
	                + " and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.DETACH) "
	                + " and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.INTERIM) "
	                + " order by wr.startTime desc ",WorkRecord.class);
			query.setParameter(1, workOrder);
			wr = query.getFirstResult();
			tx.commit();
		}
		return wr;
	}

	@Override
	public boolean isInWorkRecording(WorkOrder workOrder) {
		WorkRecord workRecord = getLatestRecord(workOrder);
		return isInWorkRecording(workRecord);
	}
	
	@Override
	public boolean isInWorkRecording(WorkRecord workRecord) {
		return validateWorkRecord.isInWorkRecording(workRecord);
	}

	@Override
	@Transactional
	public void commitWorkRecords(WorkOrder workOrder,WorkRecord workRecord,Collection<WorkRecord> workRecords) throws ValidationException {
		try(Transaction tx = persistence.getTransaction()){
			subtractLoss(workRecord);
			persistence.getEntityManager().merge(workRecord);
			deleteAllDetachItem(workOrder, workRecord.getRecordNo());
			tx.commitRetaining();
			workRecords.forEach(wr->{
				persistence.getEntityManager().merge(wr);
				tx.commitRetaining();
			});
			tx.commit();
		}
	}

	@Override
	public void subtractLoss(WorkRecord workRecord) throws ValidationException {
		final BigDecimal finishedQuantity = 
				workRecord.getFinishedQuantity().subtract(convertTo(workRecord.getUnit(), 
						workRecord.getSetupLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getSetupLossUnit())).subtract(convertTo(workRecord.getUnit(), 
						workRecord.getMaterialLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getMaterialLossUnit())).subtract(convertTo(workRecord.getUnit(), 
						workRecord.getNgLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getNgLossUnit()));
				if(finishedQuantity.compareTo(BigDecimal.ZERO)<0) {
					throw new ValidationException("finishedQuantity < 0");
				}
				
				workRecord.setFinishedQuantity(finishedQuantity);
	}
	
	@Override
	public WorkRecord addLoss(WorkRecord workRecord) {
		final BigDecimal finishedQuantity = 
				workRecord.getFinishedQuantity().add(convertTo(workRecord.getUnit(), 
						workRecord.getSetupLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getSetupLossUnit())).add(convertTo(workRecord.getUnit(), 
						workRecord.getMaterialLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getMaterialLossUnit())).add(convertTo(workRecord.getUnit(), 
						workRecord.getNgLossQuantity(), 
						workRecord.getUnitWeight(), 
						workRecord.getNgLossUnit()));
				
				workRecord.setFinishedQuantity(finishedQuantity);
	    return workRecord;
	}

	@Override
	public List<WorkRecord> getAllWorkRecordShouldAccumulate(WorkOrder workOrder) {
		List<WorkRecord> wr = null;
		try(Transaction tx = persistence.getTransaction()){
			TypedQuery<WorkRecord> query = persistence.getEntityManager().createQuery(
	                " select wr from fe$WorkRecord wr "
	                + " where wr.workOrder.id = ?1 "
	                + " and wr.endTime is not null "
	                + " and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.DETACH)"
	                + " and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.INTERIM) ",
	                WorkRecord.class);
			query.setParameter(1, workOrder.getId());
			wr = query.getResultList();
			tx.commit();
		}
		return wr;
	}

	@Override
	public BigDecimal getAccumulateQuantity(WorkOrder workOrder,WorkOrderUnit targetUnit) {
		List<WorkRecord> shouldAccumulateList = getAllWorkRecordShouldAccumulate(workOrder);
		final BigDecimal accumulate =shouldAccumulateList.stream()
		.map(e->{return uniformUnitBy(targetUnit, e);})
		.reduce(BigDecimal.ZERO, BigDecimal::add);
		return accumulate;
	}
	
	private BigDecimal uniformUnitBy(final WorkOrderUnit targetUnit,final WorkRecord workRecord) {
		BigDecimal returnValue = convertTo(targetUnit, 
				workRecord.getFinishedQuantity(), 
				workRecord.getUnitWeight(), workRecord.getUnit());
		
		returnValue = returnValue.add(convertTo(targetUnit, 
				workRecord.getSetupLossQuantity(), 
				workRecord.getUnitWeight(), 
				workRecord.getSetupLossUnit()));
		
		returnValue = returnValue.add(convertTo(targetUnit, 
				workRecord.getMaterialLossQuantity(), 
				workRecord.getUnitWeight(), 
				workRecord.getMaterialLossUnit()));
		
		returnValue = returnValue.add(convertTo(targetUnit, 
				workRecord.getNgLossQuantity(), 
				workRecord.getUnitWeight(), 
				workRecord.getNgLossUnit()));
		return returnValue;
	}
	
	private BigDecimal convertTo(WorkOrderUnit targetUnit,BigDecimal value,BigDecimal unitWeight,WorkOrderUnit unit) {
		BigDecimal returnValue = null;
		if(WorkOrderUnit.KG == targetUnit) {
			returnValue = unit.toKG(value,unitWeight);
		}else if(WorkOrderUnit.PC == targetUnit) {
			returnValue = unit.toPC(value, unitWeight);
		}
		return returnValue;
		 
	}

	@Override
	public long getNextRecordNo() {
		return uniqueNumbers.getNextNumber("recordNo");
	}

	@Override
	public void deleteAllDetachItem(WorkOrder workOrder,Long recordNo) {
		try(Transaction tx = persistence.getTransaction()){
			TypedQuery<WorkRecord> query = persistence.getEntityManager().createQuery(
	                "select wr from fe$WorkRecord wr "
	                + "where wr.workOrder.id = :id "
	                + "and wr.recordNo = :recordNo "
	                + "and wr.operateType = @enum(com.company.funda.erp.enums.OperateType.DETACH)",
	                WorkRecord.class);
			query.setParameter("id", workOrder.getId());
			query.setParameter("recordNo", recordNo);
			query.getResultList().forEach(wr->{
				persistence.getEntityManager().remove(wr);
			});
			tx.commit();
		}
	}
	
	@Override
	public List<WorkRecord> getLatestDaysCorrectedRecords(WorkOrder workOrder, String dayFrom) {
		return getRecords(workOrder, dayFrom, false);
	}

	@Override
	public List<WorkRecord> getLatestDaysRecords(WorkOrder workOrder, String dayFrom) {
		return getRecords(workOrder, dayFrom, true);
	}
	
	private List<WorkRecord> getRecords(WorkOrder workOrder, String dayFrom,boolean containInterim) {
		List<WorkRecord> wr = null;
		try(Transaction tx = persistence.getTransaction()){
			TypedQuery<WorkRecord> query = persistence.getEntityManager().createQuery(
	                " select wr from fe$WorkRecord wr "
	                + " where wr.workOrder.id = :woId "
	                + " and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.DETACH) "
	                + (containInterim ? "":" and wr.operateType <> @enum(com.company.funda.erp.enums.OperateType.INTERIM) ")
	                + ((NumberUtils.isNumber(dayFrom))?" and @between(wr.startTime, now-"+dayFrom+", now, day) ":"")
	                + " order by wr.startTime desc ",
	                WorkRecord.class);
			query.setParameter("woId", workOrder.getId());
			wr = query.getResultList();
			tx.commit();
		}
		return wr;
	}


	class ValidateWorkRecord{
		
		//是否在狀態中
		boolean isInWorkRecording(WorkRecord workRecord) {
			boolean result = false;
			if(null != workRecord && 
					workRecord.getOperateType()==OperateType.AUTO && 
					null == workRecord.getEndTime()) {
				result = true;
			}
			
			return result;
		}
		
		//檢查是否都有起訖
		boolean withoutInterim(List<WorkRecord> workRecords) {
			boolean result = false;
			result = workRecords.stream().allMatch(wr->(wr.getOperateType()!=OperateType.INTERIM));
			return result;
		}
		
		//時間不可在某區段中
		boolean isInRecordSection(List<WorkRecord> workRecords,Date date) {
			return workRecords.stream().anyMatch(wr->(FundaDateUtil.isBetweenBroadly(date, wr.getStartTime(), wr.getEndTime())));
		}
		
		//檢查起訖都在同一區段
		boolean inSameSection(List<WorkRecord> workRecords,Date start,Date end) {
			boolean result = false;
			Date from = null;
			Date to = null;
			final List<Date> datesLine = new ArrayList<>();
			datesLine.add(null);
			workRecords.forEach(wr->{
				datesLine.add(wr.getEndTime());
				datesLine.add(wr.getStartTime());
			});
			datesLine.add(null);

			for(int i=0;i<datesLine.size()-1;i++) {
				from = datesLine.get(i+1);
				to  =  datesLine.get(i);
				if(FundaDateUtil.isBetweenBroadly(start, from, to) && FundaDateUtil.isBetweenBroadly(end, from, to)) {
					return true;
				}
			}
			return result;
		}
		
	}

	@Override
	public boolean isAllHaveEndTime(List<WorkRecord> workRecords) {
		List<WorkRecord> workRecordsCopy = new ArrayList<>(workRecords);
		if(validateWorkRecord.isInWorkRecording(workRecordsCopy.get(0))) {
			workRecordsCopy.remove(0);
		}
		return validateWorkRecord.withoutInterim(workRecordsCopy);
	}

	@Override
	public boolean isInRecordSection(List<WorkRecord> workRecords, Date date) {
		return validateWorkRecord.isInRecordSection(workRecords, date);
	}

	@Override
	public boolean inSameSection(List<WorkRecord> workRecords, Date start, Date end) {
		return validateWorkRecord.inSameSection(workRecords, start, end);
	}

	@Override
	public int deleteBy(Long recordNo) {
		int result = 0;
		try(Transaction tx = persistence.getTransaction()){
			TypedQuery<WorkRecord> query = persistence.getEntityManager().createQuery(
	                "select wr from fe$WorkRecord wr "
	                + "where wr.recordNo = :recordNo ",
	                WorkRecord.class);
			query.setParameter("recordNo", recordNo);
			query.getResultList().forEach(wr->{
				persistence.getEntityManager().remove(wr);
			});
			result = query.getResultList().size();
			tx.commit();
		}catch (Exception e) {
			result = 0;
		}
	
		return result;
	}
	
}