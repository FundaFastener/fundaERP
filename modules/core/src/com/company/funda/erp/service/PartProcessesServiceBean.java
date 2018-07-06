package com.company.funda.erp.service;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.springframework.stereotype.Service;

import com.company.funda.erp.entity.PartProcessesStandard;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;

@Service(PartProcessesService.NAME)
public class PartProcessesServiceBean implements PartProcessesService {

	@Inject
	private Persistence persistence;
	
	@Override
	public Pair<Boolean,String> newPartProcessesStandard(PartProcessesStandard partProcessesStandard) {
		Boolean resultBoolean = Boolean.FALSE;
		String resultMsg = "";
		if(null != partProcessesStandard) {
			try (Transaction tx = persistence.getTransaction()) {
				EntityManager entityManager = persistence.getEntityManager();
				TypedQuery<PartProcessesStandard> query = entityManager
						.createQuery(" select pps from fe$PartProcessesStandard pps "
								   + " where pps.partNo.id = :partNo "
								   + " and pps.processType.id = :processType "
								   + " and pps.to is null "
								, PartProcessesStandard.class);
				query.setParameter("partNo", partProcessesStandard.getPartNo());
				query.setParameter("processType", partProcessesStandard.getProcessType());
				PartProcessesStandard previousPPS = query.getFirstResult();
				if(null != previousPPS) {
					cannotBeforeExistFromTime(partProcessesStandard, previousPPS);
					previousPPS.setTo(partProcessesStandard.getFrom());
					entityManager.merge(previousPPS);			
				}
				entityManager.persist(partProcessesStandard);
				tx.commit();
				resultBoolean = Boolean.TRUE;
				resultMsg = "new ok";
			}catch (Exception e) {
				resultMsg = "資料內容有誤!\n"+e.getMessage();
			}
		}else {
			resultMsg = "Input PartProcessesStandard can't be null !";
		}
		return new Pair<Boolean,String>(resultBoolean,resultMsg);
	}

	private void cannotBeforeExistFromTime(PartProcessesStandard partProcessesStandard,
			PartProcessesStandard previousPPS) throws Exception {
		if(partProcessesStandard.getFrom().before(previousPPS.getFrom())) {
			throw new Exception("The From time cannot before the all previousPPS.getFrom()");
		}
	}

	@Override
	public Pair<Boolean, String> editPartProcessesStandard(PartProcessesStandard partProcessesStandard) {
		Boolean resultBoolean = Boolean.FALSE;
		String resultMsg = "";
		if(null != partProcessesStandard) {
			try (Transaction tx = persistence.getTransaction()) {
				EntityManager entityManager = persistence.getEntityManager();
				TypedQuery<PartProcessesStandard> query = entityManager
						.createQuery(" select pps from fe$PartProcessesStandard pps "
								   + " where pps.partNo.id = :partNo "
								   + " and pps.processType.id = :processType "
								   + " and pps.to is not null "
								   + " order by pps.from desc "
								, PartProcessesStandard.class);
				query.setParameter("partNo", partProcessesStandard.getPartNo());
				query.setParameter("processType", partProcessesStandard.getProcessType());
				
				PartProcessesStandard previousPPS = query.getFirstResult();
				if(null != previousPPS) {
					cannotBeforeExistFromTime(partProcessesStandard, previousPPS);
					previousPPS.setTo(partProcessesStandard.getFrom());
					entityManager.merge(previousPPS);
					
				}
				entityManager.merge(partProcessesStandard);
				tx.commit();
				resultBoolean = Boolean.TRUE;
				resultMsg = "edit ok";
			}catch (Exception e) {
				resultMsg = "資料內容有誤!\n"+e.getMessage();
			}
		}else {
			resultMsg = "Input PartProcessesStandard can't be null !";
		}
		return new Pair<Boolean,String>(resultBoolean,resultMsg);
	}

	@Override
	public Pair<Boolean, String> deletePartProcessesStandard(PartProcessesStandard partProcessesStandard) {
		Boolean resultBoolean = Boolean.FALSE;
		String resultMsg = "";
		if(null != partProcessesStandard) {
			try (Transaction tx = persistence.getTransaction()) {
				EntityManager entityManager = persistence.getEntityManager();
				TypedQuery<PartProcessesStandard> query = entityManager
						.createQuery(" select pps from fe$PartProcessesStandard pps "
								   + " where pps.partNo.id = :partNo "
								   + " and pps.processType.id = :processType "
								, PartProcessesStandard.class);
				query.setParameter("partNo", partProcessesStandard.getPartNo());
				query.setParameter("processType", partProcessesStandard.getProcessType());
				List<PartProcessesStandard> ppss = query.getResultList();
				ppss.forEach(p->{
					entityManager.remove(p);
				});
				tx.commit();
				resultBoolean = Boolean.TRUE;
				resultMsg = "delete ok";
			}catch (Exception e) {
				resultMsg = e.getMessage();
			}
		}else {
			resultMsg = "Input PartProcessesStandard can't be null !";
		}
		return new Pair<Boolean,String>(resultBoolean,resultMsg);
	}

}