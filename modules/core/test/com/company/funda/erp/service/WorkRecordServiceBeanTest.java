package com.company.funda.erp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.FeTestContainer;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.entity.WorkRecord;
import com.company.funda.erp.util.FundaDateUtil;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;

public class WorkRecordServiceBeanTest {

	//@ClassRule
	public static FeTestContainer cont = FeTestContainer.Common.INSTANCE;

	private static WorkRecordService WorkRecordService;
	private static Metadata metadata;
	private static Persistence persistence;
	private static DataManager dataManager;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	//@BeforeClass
	public static void setUp() throws Exception {
		metadata = cont.metadata();
		persistence = cont.persistence();
		dataManager = AppBeans.get(DataManager.class);
		WorkRecordService = AppBeans.get(WorkRecordService.class);
	}
	boolean isAllHaveEndTime(List<WorkRecord> workRecords) {
		boolean result = true;
//		result = workRecords.stream()
//		.filter(wr->(wr.getStartTime()!=null && wr.getEndTime()!=null))
//		.findAny()
//		.isPresent();
		result = workRecords.stream().allMatch(wr->(wr.getEndTime()!=null));
		return result;
	}
	
	boolean isInSection(List<WorkRecord> workRecords,Date date) {
		boolean result = false;
		result = workRecords.stream().anyMatch(wr->(FundaDateUtil.isBetweenBroadly(date, wr.getStartTime(), wr.getEndTime())));
		return result;
	}
	
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
			from = datesLine.get(i);
			to  =  datesLine.get(i+1);
			if(FundaDateUtil.isBetweenBroadly(start, from, to) && FundaDateUtil.isBetweenBroadly(end, from, to)) {
				return true;
			}
		}
		
		return result;
	}

	@Ignore
	//@Test
	public void testGetLatestDaysRecords() {

		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();

			UUID uuid = UUID.fromString("9dd37607-1d30-11cd-3930-586deb06da17");
			TypedQuery<WorkOrder> query = em.createQuery("select wo from fe$WorkOrder wo where wo.id=:id",
					WorkOrder.class);
			query.setParameter("id", uuid);
			WorkOrder wo = query.getFirstResult();
			List<WorkRecord> wrs = WorkRecordService.getLatestDaysRecords(wo, "8");

			
			printResult(wrs, LocalDateTime.of(2018, 3, 8, 11, 0), LocalDateTime.of(2018, 3,8, 15, 0));

//			printResult(wrs, LocalDateTime.of(2018, 3, 12, 11, 0), LocalDateTime.of(2018, 3, 12, 15, 0));
//			printResult(wrs, LocalDateTime.of(2018, 3, 9, 16, 0), LocalDateTime.of(2018, 3, 9, 17, 0));
//			printResult(wrs, LocalDateTime.of(2018, 3, 9, 11, 0), LocalDateTime.of(2018, 3, 9, 15, 0));
//			printResult(wrs, LocalDateTime.of(2018, 3, 6, 11, 0), LocalDateTime.of(2018, 3, 16, 15, 0));
		}

	}
	private void printResult(List<WorkRecord> wrs, LocalDateTime start, LocalDateTime end) {
		logger.info("start :{},end:{}",start,end);
		logger.info("isInSection :{}",isInSection(wrs,FundaDateUtil.dateFromLocalDateTime(start)));
		logger.info("isInSection :{}",isInSection(wrs,FundaDateUtil.dateFromLocalDateTime(end)));
		logger.info("inSameSection :{} \n\n",inSameSection(wrs, FundaDateUtil.dateFromLocalDateTime(start), FundaDateUtil.dateFromLocalDateTime(end)));
	}

	@Ignore
	//@Test
	public void testGetAccumulateQuantity() {

		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();

			UUID uuid = UUID.fromString("aacb25f4-cb8f-8f6f-4804-5c8588fdceca");
			TypedQuery<WorkRecord> query = em.createQuery("select wr from fe$WorkRecord wr where wr.id=:id",
					WorkRecord.class);
			query.setParameter("id", uuid);
			List<WorkRecord> wrl = query.getResultList();

			// logger.info("11111:{}",PrintUtil.printMultiLine(WorkRecordService.makeAccumulateQuantity(wrl.get(0))));

		}

	}

	//@Test
	@Ignore
	public void testGetLatestRecord() {
		testGetLatestRecordTest();
	}

	@Ignore
	//@Test
	public void testGetLatestRecordTest() {

		try (Transaction tx = persistence.createTransaction()) {
			EntityManager em = persistence.getEntityManager();

			UUID uuid = UUID.fromString("9dd37607-1d30-11cd-3930-586deb06da17");
			TypedQuery<WorkOrder> query = em.createQuery("select wo from fe$WorkOrder wo where wo.id=:id",
					WorkOrder.class);
			query.setParameter("id", uuid);
			List<WorkOrder> wol = query.getResultList();
			logger.debug("11111:{}", wol.size());
			WorkRecord wr = WorkRecordService.getLatestRecord(wol.get(0));
			logger.info("22222:{}",WorkRecordService.isInWorkRecording(wr));
			;
		}

	}
	
	//TODO-H take off
	public static void main(String[] args) {
		ArrayList<WorkRecord> wrList = new ArrayList<>();
		WorkRecord wr = new WorkRecord();
		wr.setFinishedQuantity(new BigDecimal(10000));
		wr.setNgLossQuantity(new BigDecimal(5));
		wr.setMaterialLossQuantity(new BigDecimal(4));
		wr.setSetupLossQuantity(new BigDecimal(3));
		wrList.add(wr);
		WorkRecord wr1 = new WorkRecord();
		wr1.setFinishedQuantity(new BigDecimal(5000));
		wr1.setNgLossQuantity(new BigDecimal(11));
		wr1.setMaterialLossQuantity(new BigDecimal(12));
		wr1.setSetupLossQuantity(new BigDecimal(13));
		wrList.add(wr1);
		WorkRecord wr2 = new WorkRecord();
		wr2.setFinishedQuantity(new BigDecimal(5000));
		wr2.setNgLossQuantity(new BigDecimal(1));
		wr2.setMaterialLossQuantity(new BigDecimal(1));
		wr2.setSetupLossQuantity(new BigDecimal(1));
		wrList.add(wr2);
		
		System.out.println(test(wrList).toString());

	}
	
	//TODO-H take off
	public static BigDecimal test(List<WorkRecord> workRecords) {
		return workRecords.stream()
		.map(wr->{
			return wr.getFinishedQuantity().add(wr.getNgLossQuantity());
		})
		.reduce(BigDecimal.ZERO, BigDecimal::add);

	}

}
