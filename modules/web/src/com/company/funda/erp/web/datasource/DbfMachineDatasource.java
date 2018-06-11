package com.company.funda.erp.web.datasource;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.service.ImportDBFService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

public class DbfMachineDatasource extends CustomCollectionDatasource<Machine, UUID> {
	
	private ImportDBFService importDBFService ;

	@Override
	protected Collection<Machine> getEntities(Map<String, Object> params) {
		importDBFService = AppBeans.get(ImportDBFService.NAME);
		params.put(ImportDBFService.TYPE_KEY, Machine.class);
		List<Machine> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		return resultList;
	}

}
