package com.company.funda.erp.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.jamel.dbf.processor.DbfProcessor;
import org.jamel.dbf.processor.DbfRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.company.funda.erp.FundaCoreConfig;
import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.enums.DbfImportType;
import com.company.funda.erp.importDbf.DbfBean;
import com.company.funda.erp.importDbf.InventoryItemDbfBean;
import com.company.funda.erp.importDbf.MachineDbfBean;
import com.company.funda.erp.importDbf.WorkOrderDbfBean;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.PersistenceHelper;

@Service(ImportDBFService.NAME)
public class ImportDBFServiceBean implements ImportDBFService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private Persistence persistence;
	@Inject
	private FundaCoreConfig fundaCoreConfig;

	@SuppressWarnings("unchecked")
	@Override
	public <E extends StandardEntity> List<E> loadFromDBF(Map<String, Object> params) {
		DbfBean dbfBean = getDbfBean((Class<E>) params.get(ImportDBFService.TYPE_KEY));
		String fileName = fundaCoreConfig.getDbfPartition()+dbfBean.getFileName();
		Path dbf = Paths.get(fileName);
		return (List<E>) DbfProcessor.loadData(dbf.toFile(), (DbfRowMapper<E>) dbfBean.getDbfRowMapper(params));
	}

	private <E extends StandardEntity> DbfBean getDbfBean(Class<E> classEntity) {
		DbfBean dbfBean = null;
		if (classEntity.equals(Machine.class)) {
			dbfBean = new MachineDbfBean();
		} else if (classEntity.equals(InventoryItem.class)) {
			dbfBean = new InventoryItemDbfBean();
		} else if (classEntity.equals(WorkOrder.class)) {
			dbfBean = new WorkOrderDbfBean();
		}
		return dbfBean;
	}

	@Override
	public int setInventoryItemToDb(Set<InventoryItem> datas, DbfImportType dbfImportType) {
		try (Transaction tx = persistence.getTransaction()) {
			EntityManager entityManager = persistence.getEntityManager();
			AtomicInteger counter = new AtomicInteger(0);
			datas.forEach(it -> {
				InventoryItem dbItem;
				TypedQuery<InventoryItem> query = entityManager
						.createQuery(" select it from fe$InventoryItem it where it.no = ?1 ", InventoryItem.class);
				query.setParameter(1, it.getNo());
				dbItem = query.getFirstResult();

				if (null == dbItem) {
					entityManager.persist(it);
					counter.incrementAndGet();
				} else if (dbfImportType == DbfImportType.MANDATORY_OVERWRITE) {
					it.setId(dbItem.getId());
					entityManager.merge(it);
					counter.incrementAndGet();
				}

			});

			tx.commit();
			return counter.get();
		}

	}

	@Override
	public int setMachineToDb(Set<Machine> datas, DbfImportType dbfImportType) {
		try (Transaction tx = persistence.getTransaction()) {
			EntityManager entityManager = persistence.getEntityManager();
			AtomicInteger counter = new AtomicInteger(0);
			datas.forEach(m -> {
				Machine dbMachine;
				TypedQuery<Machine> query = entityManager.createQuery(" select m from fe$Machine m where m.no = ?1 ",
						Machine.class);
				query.setParameter(1, m.getNo());
				dbMachine = query.getFirstResult();

				if (null == dbMachine) {
					entityManager.persist(m);
					counter.incrementAndGet();
				} else if (dbfImportType == DbfImportType.MANDATORY_OVERWRITE) {
					m.setId(dbMachine.getId());
					entityManager.merge(m);
					counter.incrementAndGet();
				}

			});

			tx.commit();
			return counter.get();
		}
	}
	
	

	@Override
	public Map<String,String> setWorkOrderToDb(Set<WorkOrder> datas, DbfImportType dbfImportType) {
		HashMap<String,String> msgMap = new HashMap<>();
		ArrayList<String> errorList = new ArrayList<>();
		try (Transaction tx = persistence.getTransaction()) {
			EntityManager entityManager = persistence.getEntityManager();
			AtomicInteger counter = new AtomicInteger(0);
			datas.forEach(w -> {
				WorkOrder dbWorkOrder;
				TypedQuery<WorkOrder> query = entityManager
						.createQuery(" select w from fe$WorkOrder w where w.no = ?1 ", WorkOrder.class);
				query.setParameter(1, w.getNo());
				dbWorkOrder = query.getFirstResult();
				
				try {
					if (null == dbWorkOrder) {
						updateCascade(entityManager, w);
						entityManager.persist(w);
						counter.incrementAndGet();
					} else if (dbfImportType == DbfImportType.MANDATORY_OVERWRITE) {
						updateCascade(entityManager, w);
						w.setId(dbWorkOrder.getId());
						entityManager.merge(w);
						counter.incrementAndGet();
					}
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
					errorList.add(w.getNo());
				}

			});

			tx.commit();
			msgMap.put("counter", String.valueOf(counter.get()));
			msgMap.put("errorSize",String.valueOf(errorList.size()));
			msgMap.put("errorList",errorList.stream().collect(Collectors.joining("\n")));
			return msgMap;
		}

	}

	private void updateCascade(EntityManager entityManager, WorkOrder w) {
		if(PersistenceHelper.isNew(w.getDefaultMachine())) {
			entityManager.persist(w.getDefaultMachine());
		}
		if(PersistenceHelper.isNew(w.getInventoryItem())) {
			entityManager.persist(w.getInventoryItem());
		}
	}
	
}