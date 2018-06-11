package com.company.funda.erp.web.datasource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.company.funda.erp.entity.WorkOrder;
import com.company.funda.erp.service.ImportDBFService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

public class DbfWorkOrderDatasource extends CustomCollectionDatasource<WorkOrder, UUID> {

	private ImportDBFService importDBFService ;
	
	
	@Override
	protected Collection<WorkOrder> getEntities(Map<String, Object> params) {
		importDBFService = AppBeans.get(ImportDBFService.NAME);
		params.put(ImportDBFService.TYPE_KEY, WorkOrder.class);
		List<WorkOrder> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		return resultList;
	}

}
