package com.company.funda.erp.web.crud.PartProcessesStandard;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.PartProcessesStandard;
import com.company.funda.erp.service.PartProcessesService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.components.actions.CreateAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.GroupDatasource;

public class PartProcessesStandardBrowse extends AbstractLookup {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Inject
	private Table<PartProcessesStandard> partProcessesStandardsTable;
	@Inject
	private PartProcessesService ppservice;
	@Inject
	private GroupDatasource<Entity<PartProcessesStandard>, PartProcessesStandard> partProcessesStandardsDs;
	
	@Override
	public void init(Map<String, Object> params) {
		
		RemoveAction removeAction = new RemoveAction(partProcessesStandardsTable, true, "remove") {
		    @SuppressWarnings("rawtypes")
			@Override
		    protected void doRemove(Set<Entity> selected, boolean autocommit) {
     		        
		        for (Entity item : selected) {		        	
		        	PartProcessesStandard pps = (PartProcessesStandard)item;
		        	ppservice.deletePartProcessesStandard(pps);
		        	logger.info("Remove Called..{}",pps.getPartNo());
		        }
		        partProcessesStandardsDs.refresh();
		    }
		};
		
		CreateAction createAction = new CreateAction(partProcessesStandardsTable) {
			
			@Override
			protected void afterWindowClosed(Window window) {
				partProcessesStandardsDs.refresh();
//				super.afterWindowClosed(window);
			}
			
		};
		partProcessesStandardsTable.addAction(removeAction);
		partProcessesStandardsTable.addAction(createAction);
		super.init(params);
	}
}