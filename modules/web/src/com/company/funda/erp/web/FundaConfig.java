package com.company.funda.erp.web;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface FundaConfig extends Config {

	@Property("funda.clock.refresh.delay")
    int getFundaClockRefreshDelay();
	
	@Property("funda.workRecord.daysBefore")
	int getWorkRecordFromDaysBefore();
}
