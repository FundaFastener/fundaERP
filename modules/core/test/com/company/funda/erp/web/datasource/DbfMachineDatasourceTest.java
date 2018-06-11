package com.company.funda.erp.web.datasource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.FeTestContainer;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.service.ImportDBFService;
import com.haulmont.cuba.core.global.AppBeans;

public class DbfMachineDatasourceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ImportDBFService importDBFService;
	
	@ClassRule
	public static FeTestContainer cont = FeTestContainer.Common.INSTANCE;
	
	@Before
	public void setUp() throws Exception{
		importDBFService = AppBeans.get(ImportDBFService.class);
	}
	@Test
	public void test() {
		Map<String,Object> params = new HashMap<>();
		importDBFService = AppBeans.get(ImportDBFService.NAME);
		params.put(ImportDBFService.TYPE_KEY, Machine.class);
		params.put(ImportDBFService.NO, "R");
		List<Machine> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		logger.info("{}",resultList.size());
	}

}
