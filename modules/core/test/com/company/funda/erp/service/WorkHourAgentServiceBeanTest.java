package com.company.funda.erp.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.FeTestContainer;
import com.company.funda.erp.entity.Employee;
import com.company.funda.erp.enums.WorkHourType;
import com.haulmont.cuba.core.global.AppBeans;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class WorkHourAgentServiceBeanTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private WorkHourAgentService whasb ;
	
	@ClassRule
	public static FeTestContainer cont = FeTestContainer.Common.INSTANCE;
	
	@Before
	public void setUp() throws Exception {
		whasb = AppBeans.get(WorkHourAgentService.class);
	}
	
	@Test
	public void testIsOnJob() {

		logger.info("start test isOnJob ----- ");
		
		LocalDateTime sunday = LocalDateTime.of(2018, 4, 29, 8, 0);
		LocalDateTime mondayBeforeMorningWork = LocalDateTime.of(2018, 4, 30, 7, 59);
		LocalDateTime mondayOnMorningWork = LocalDateTime.of(2018, 4, 30, 8, 1);
		LocalDateTime mondayAfterMorningWork = LocalDateTime.of(2018, 4, 30, 12, 1);

		Employee employee = new Employee();
		assertFalse(whasb.isOnJob(employee, sunday));
		assertFalse(whasb.isOnJob(employee, mondayBeforeMorningWork));
		assertTrue(whasb.isOnJob(employee, mondayOnMorningWork));
		assertFalse(whasb.isOnJob(employee, mondayAfterMorningWork));
		
		logger.info("end test isOnJob ----- ");
	}
	
	@Test
	public void testGetWorkHourType() {
		
		LocalDateTime sunday = LocalDateTime.of(2018, 4, 29, 8, 0);
		LocalDateTime mondayBeforeMorningWork = LocalDateTime.of(2018, 4, 30, 7, 59);
		LocalDateTime mondayOnMorningWork = LocalDateTime.of(2018, 4, 30, 8, 1);
		LocalDateTime mondayAfterMorningWork = LocalDateTime.of(2018, 4, 30, 12, 1);
		
		assertThat(WorkHourType.OVERTIME, is(whasb.getWorkHourType(null, sunday)));
		assertThat(WorkHourType.OVERTIME, is(whasb.getWorkHourType(null, mondayBeforeMorningWork)));
		assertThat(WorkHourType.REGULAR, is(whasb.getWorkHourType(null, mondayOnMorningWork)));
		assertThat(WorkHourType.OVERTIME, is(whasb.getWorkHourType(null, mondayAfterMorningWork)));
		
	}

}
