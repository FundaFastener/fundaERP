package com.company.funda.erp;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;

@Source(type = SourceType.APP)
public interface FundaCoreConfig extends Config {

	@Property("dbf.partition")
	String getDbfPartition();
}
