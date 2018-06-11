package com.company.funda.erp.web.datasource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.company.funda.erp.entity.WorkRecord;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

public class WorkRecordDatasource extends CustomCollectionDatasource<WorkRecord, UUID> {

	private List<WorkRecord> customList = new ArrayList<>();
	
	@Override
	protected Collection<WorkRecord> getEntities(Map<String, Object> params) {
		return customList;
	}

}
