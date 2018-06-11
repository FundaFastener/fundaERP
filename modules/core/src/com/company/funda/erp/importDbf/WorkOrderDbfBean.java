package com.company.funda.erp.importDbf;

import static com.company.funda.erp.util.FundaDateUtil.localDateFromDate;
import static com.company.funda.erp.util.FundaStringUtil.dencodeBig5;
import static com.company.funda.erp.util.MinguodateUtil.isValidMinguo;
import static com.company.funda.erp.util.MinguodateUtil.parse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jamel.dbf.processor.DbfRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.FundaCoreConfig;
import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.enums.WorkOrderStatus;
import com.company.funda.erp.enums.WorkOrderUnit;
import com.company.funda.erp.service.ImportDBFService;
import com.company.funda.erp.util.MinguodateUtil;
import com.company.funda.erp.util.MinguodateUtil.Delimeter;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;

public class WorkOrderDbfBean implements DbfBean {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private DataManager dataManager = AppBeans.get(DataManager.class);
	private ImportDBFService importDBFService = AppBeans.get(ImportDBFService.class);
	private Map<String,Machine> machines;
	private Map<String,InventoryItem> inventoryItems;

	@Override
	public String getFileName() {
		FundaCoreConfig fundaCoreConfig = AppBeans.get(Configuration.class)
		        .getConfig(FundaCoreConfig.class);
		return fundaCoreConfig.getDbfPartition()+"BMAKE.DBF";
	}

	@Override
	public DbfRowMapper<WorkOrder> getDbfRowMapper(Map<String, Object> params) {

		Date dateFrom = (Date)params.get(ImportDBFService.DATE_FROM);
		LocalDate localDateFrom = (null != dateFrom) ? localDateFromDate((Date)params.get(ImportDBFService.DATE_FROM)):null;

		Date dateTo = (Date)params.get(ImportDBFService.DATE_TO);
		LocalDate localDateTo = (null != dateTo) ? localDateFromDate((Date)params.get(ImportDBFService.DATE_TO)):null;

		String no = (String)params.get(ImportDBFService.NO);
		
		DateTimeFormatter dtf = MinguodateUtil.getDateTimeFormatter(Delimeter.PERIOD);
		
		machines = getFromDbf(Machine.class).stream().collect(Collectors.toMap(Machine::getNo, item -> item));
		inventoryItems = getFromDbf(InventoryItem.class).stream().collect(Collectors.toMap(InventoryItem::getNo, item -> item,(k,v)->k));
		
		return new DbfRowMapper<WorkOrder>() {

			public WorkOrder mapRow(Object[] row) {
				if (StringUtils.isNotBlank(dencodeBig5(row[14]))) {
					return null;
				}
				String minguoDate = dencodeBig5(row[10]);
				if(!isValidMinguo(minguoDate)) {
					return null;
				}
				MinguoDate dataDate = parse(dtf,minguoDate);
				if(null != localDateFrom && dataDate.isBefore(localDateFrom)) {
					return null;
				}
				if(null != localDateTo && dataDate.isAfter(localDateTo)) {
					return null;
				}
				String dataNo = dencodeBig5(row[0]);
				if(StringUtils.isNotBlank(no) && !StringUtils.containsIgnoreCase(dataNo, no)) {
					return null;
				}
				
				WorkOrder workOrder = new WorkOrder();
				try {
					workOrder.setNo(dataNo);
					InventoryItem item = loadInventoryItemByNo(dencodeBig5(row[1]));
					workOrder.setInventoryItem(item);
					Machine defaultMachine = loadMachineByNo(dencodeBig5(row[2]));
					workOrder.setDefaultMachine(defaultMachine);
					workOrder.setQuantity(new BigDecimal((Double) row[3]));
					workOrder.setUnit(WorkOrderUnit.PC);
					workOrder.setProcessType(defaultMachine.getProcessType());
					workOrder.setStatus(WorkOrderStatus.NOT_STARTED);

				} catch (Exception e) {
					logger.error("dataNo:{} , {}", dataNo,e.getMessage());
					logger.error(e.getMessage(), e);
					return null;
//					throw e;
				}
				
				return workOrder;
			}
		};
	}
	
	private Machine loadMachineByNo(String no) {
		LoadContext<Machine> loadContext = LoadContext.create(Machine.class)
				.setQuery(LoadContext.createQuery("select m from fe$Machine m where m.no = :no")
				.setParameter("no", (Object) no))
				.setView("machine-view");
		Machine machine = dataManager.load(loadContext);
		if(null == machine) {
			machine = machines.get(no);
		}
		logger.info("m no:{},machine:{}",no,machine.getId());
		return machine;
	}

	private <E extends StandardEntity>List<E> getFromDbf(Class<E> entityObj) {
		Map<String,Object> params = new HashMap<>();
		params.put(ImportDBFService.TYPE_KEY, entityObj);
		params.put(ImportDBFService.SKIP_DATE_CHECK, ImportDBFService.SKIP_DATE_CHECK);
		List<E> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		return resultList;
	}

	private InventoryItem loadInventoryItemByNo(String no) {
		logger.info(" ino:{}",no);
		LoadContext<InventoryItem> loadContext = LoadContext.create(InventoryItem.class)
				.setQuery(LoadContext.createQuery("select i from fe$InventoryItem i where i.no = :no")
				.setParameter("no", (Object) no))
				.setView("inventoryItem-view");
		InventoryItem inventoryItem = dataManager.load(loadContext);
		logger.info("db inventoryItem:{}",inventoryItem);
		if(null == inventoryItem) {
			inventoryItem = inventoryItems.get(no);
			logger.info("dbf inventoryItem:{}",inventoryItem);
		}
		return inventoryItem;
	}

}
