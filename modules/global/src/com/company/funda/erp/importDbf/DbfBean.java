package com.company.funda.erp.importDbf;

import java.util.Map;

import org.jamel.dbf.processor.DbfRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haulmont.cuba.core.entity.StandardEntity;

public interface DbfBean {
	
	Logger logger = LoggerFactory.getLogger(DbfBean.class);

	String getFileName();
	public DbfRowMapper<? extends StandardEntity> getDbfRowMapper(Map<String, Object> params);
}
