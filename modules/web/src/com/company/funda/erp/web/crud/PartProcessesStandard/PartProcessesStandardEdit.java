package com.company.funda.erp.web.crud.PartProcessesStandard;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.PartProcessesStandard;
import com.company.funda.erp.service.PartProcessesService;
import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.data.Datasource;

public class PartProcessesStandardEdit extends AbstractEditor<PartProcessesStandard> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Inject
	private Datasource<PartProcessesStandard> partProcessesStandardDs;
	@Inject
	private PartProcessesService pps;

	@Override
	protected boolean preCommit() {
		Pair<Boolean, String> result = null;
		if (PersistenceHelper.isNew(partProcessesStandardDs.getItem())) {
			// create
			result = pps.newPartProcessesStandard(partProcessesStandardDs.getItem());
			logger.info("preCommit :create");
		} else {
			// edit
			result = pps.editPartProcessesStandard(partProcessesStandardDs.getItem());
			logger.info("preCommit :edit");
		}
		
		logger.info(" ~~ preCommit ~~ :{},{}", result.getFirst(), result.getSecond());
		if (!result.getFirst()) {
//			showNotification(result.getSecond());
			showMessageDialog("Error", result.getSecond(), MessageType.CONFIRMATION);
		}else {
			close(CLOSE_ACTION_ID, true);
		}
		
		return false;
	}
	
}