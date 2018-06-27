package com.company.funda.erp.importDbf;

import static com.company.funda.erp.util.FundaStringUtil.dencodeBig5;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jamel.dbf.processor.DbfRowMapper;

import com.company.funda.erp.entity.Machine;
import com.company.funda.erp.enums.MachineType;
import com.company.funda.erp.enums.ProcessType;
import com.company.funda.erp.service.ImportDBFService;

public class MachineDbfBean implements DbfBean{

	
	@Override
	public String getFileName() {
		return "BLOADW.DBF";
	}

	@Override
	public DbfRowMapper<Machine> getDbfRowMapper(Map<String, Object> params) {
		
		String no = (String)params.get(ImportDBFService.NO);

		return new DbfRowMapper<Machine>() {

			@Override
			public Machine mapRow(Object[] row) {

				if (dencodeBig5(row[2]).equalsIgnoreCase("Y")) {
					return null;
				}
				String dataNo = dencodeBig5(row[0]);
				if(StringUtils.isNotBlank(no) && !StringUtils.containsIgnoreCase(dataNo, no)) {
					return null;
				}
				if(dataNo.length()<2) {
					return null;
				}
				Machine machine = new Machine();
				try {
					machine.setNo(dataNo);
					machine.setName(dencodeBig5(row[1]));
					MachineType type = null;
					ProcessType pType = null;
					String firstChar = machine.getNo().substring(0, 1);
					String firstTwoChar = machine.getNo().substring(0, 2);
					if (StringUtils.equalsIgnoreCase((String) "F", (String) firstChar)) {
						type = MachineType.FORMING;
						pType = ProcessType.FORMING;
					} else if (StringUtils.equalsIgnoreCase((String) "H", firstChar)) {
						type = MachineType.HEADING;
						pType = ProcessType.FORMING;
					} else if (StringUtils.equalsIgnoreCase((String) "R", firstChar)) {
						type = MachineType.THREADING;
						pType = ProcessType.THREADING_ROLLER;
					} else if (StringUtils.equalsIgnoreCase((String) "W", firstChar)) {
						type = MachineType.THREADING;
						pType = ProcessType.THREADING_FLAT;
					} else if(StringUtils.equalsIgnoreCase((String) "P1", firstTwoChar)||
							StringUtils.equalsIgnoreCase((String) "P8", firstTwoChar)) {
						pType = ProcessType.OPEN_DIE_FORMING;
					} else if(StringUtils.equalsIgnoreCase((String) "P2", firstTwoChar)||
							StringUtils.equalsIgnoreCase((String) "P9", firstTwoChar)) {
						pType = ProcessType.SANDBLASTING;
					} else {
						return null;
					}
					machine.setType(type);
					machine.setProcessType(pType);
					machine.setRemark(dencodeBig5(row[9]));

				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				return machine;
			}
		};
		
	}

}
