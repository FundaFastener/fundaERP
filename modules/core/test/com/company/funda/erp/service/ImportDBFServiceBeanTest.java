package com.company.funda.erp.service;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.jamel.dbf.processor.DbfRowMapper;
import org.jamel.dbf.structure.DbfHeader;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.FeTestContainer;
import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.enums.MachineType;
import com.company.funda.erp.enums.ProcessType;
import com.company.funda.erp.importDbf.MachineDbfBean;
import com.haulmont.cuba.core.global.AppBeans;

public class ImportDBFServiceBeanTest {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ImportDBFService importDBFService;
	
	@ClassRule
	public static FeTestContainer cont = FeTestContainer.Common.INSTANCE;
	
	@Before
	public void setUp() throws Exception{
		importDBFService = AppBeans.get(ImportDBFService.class);
	}
	
	//@Test
	public void testInventoryItem() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(ImportDBFService.TYPE_KEY, InventoryItem.class);
		List<InventoryItem> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		logger.info("resultList :{}",resultList.size());
		resultList.forEach(e->{
			logger.info("{}",e.getName1());
		});
	}
	
//	@Test
	public void testMachine() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(ImportDBFService.TYPE_KEY, Machine.class);
		List<Machine> resultList = importDBFService.loadFromDBF(params);
		resultList.removeIf(Objects::isNull);
		logger.info("resultList :{}",resultList.size());
		resultList.forEach(e->{
			logger.info("{}",e.getName());
		});
	}
	
//	@Test
	public void testInfoOfDBF() {
		
		Path dbf = Paths.get("Z:\\BLOADW.DBF");
		
		try(DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(dbf.toFile())))){
			DbfHeader header = DbfHeader.read(in);
			
			logger.info("getNumberOfRecords:{}",header.getNumberOfRecords());
		}catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		
	}
	
//	@Test
	public void testLoadFromDBF() {
		
		DbfRowMapper<Machine> machingRowMapper = new DbfRowMapper<Machine>() {
			
			int pointer = 0;
			int start = 1;
			int end = start+5;
			
			@Override
			public Machine mapRow(Object[] row) {
				
				if(encodeBig5(row[2]).equalsIgnoreCase("Y")) {
					return null;
				}
				
				pointer++;
				if(pointer < start || pointer >= end) {
					return null;
				}
				
				Machine machine = new Machine();
				try {
					machine.setNo(encodeBig5(row[0]));
					machine.setName(encodeBig5(row[1]));
					MachineType type = null;
					ProcessType pType = null;
					String firstChar = machine.getNo().substring(0, 1);
					if (StringUtils.equalsIgnoreCase((String) "F", (String) firstChar)) {
						type = MachineType.FORMING;
						pType = ProcessType.FORMING;
					} else if (StringUtils.equalsIgnoreCase((String) "H", (String) firstChar)) {
						type = MachineType.HEADING;
						pType = ProcessType.FORMING;
					} else if (StringUtils.equalsIgnoreCase((String) "R", (String) firstChar)) {
						type = MachineType.THREADING;
						pType = ProcessType.THREADING_ROLLER;
					} else if (StringUtils.equalsIgnoreCase((String) "W", (String) firstChar)) {
						type = MachineType.THREADING;
						pType = ProcessType.THREADING_FLAT;
					} 
					machine.setType(type);
					machine.setRemark(encodeBig5(row[9]));

				} catch (Exception e) {
					logger.error(e.getMessage(), (Throwable) e);
				}
//				logger.debug("{}", (Object) PrintUtil.printShort(machine));
				return machine;
			}
		
		};
		
		MachineDbfBean MachineDbfBean = new MachineDbfBean();
		
		
		List<Machine> resultList = importDBFService.loadFromDBF(null);
		
		resultList.removeIf(Objects::isNull);
		
		logger.info("size:{}",resultList.size());
		
		resultList.forEach(m->{
			logger.info("{}-{}",m.getNo(),m.getName());
		});
	}
	
	private String encodeBig5(Object toBeEncode) {
		String endoded = "";
		try {
			endoded = new String((byte[]) toBeEncode, "Big5");
		} catch (UnsupportedEncodingException e) {
			this.logger.error(e.getMessage(), (Throwable) e);
		}
		return StringUtils.trim(endoded);
	}

}
