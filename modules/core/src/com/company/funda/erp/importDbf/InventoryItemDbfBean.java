package com.company.funda.erp.importDbf;

import static com.company.funda.erp.util.FundaDateUtil.localDateFromDate;
import static com.company.funda.erp.util.FundaStringUtil.dencodeBig5;
import static com.company.funda.erp.util.MinguodateUtil.isValidMinguo;
import static com.company.funda.erp.util.MinguodateUtil.parse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jamel.dbf.processor.DbfRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.funda.erp.entity.InventoryItem;
import com.company.funda.erp.enums.InventoryCategory;
import com.company.funda.erp.service.ImportDBFService;
import com.company.funda.erp.util.MinguodateUtil;
import com.company.funda.erp.util.MinguodateUtil.Delimeter;

public class InventoryItemDbfBean implements DbfBean {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String getFileName() {
		return "bscrew.dbf";
	}

	@Override
	public DbfRowMapper<InventoryItem> getDbfRowMapper(Map<String, Object> params) {
//		logger.info("getDbfRowMapper:{},{},{}",params.get(ImportDBFService.DATE_FROM),params.get(ImportDBFService.DATE_TO),params.get(ImportDBFService.NO));
		Date dateFrom = (Date)params.get(ImportDBFService.DATE_FROM);
		LocalDate localDateFrom = (null != dateFrom) ? localDateFromDate((Date)params.get(ImportDBFService.DATE_FROM)):null;

		Date dateTo = (Date)params.get(ImportDBFService.DATE_TO);
		LocalDate localDateTo = (null != dateTo) ? localDateFromDate((Date)params.get(ImportDBFService.DATE_TO)):null;

		String no = (String)params.get(ImportDBFService.NO);
		DateTimeFormatter dtf = MinguodateUtil.getDateTimeFormatter(Delimeter.PERIOD);
		
		Object skipDateCheck = params.get(ImportDBFService.SKIP_DATE_CHECK);
		
		return new DbfRowMapper<InventoryItem>() {

			public InventoryItem mapRow(Object[] row) {
				
				if (null == skipDateCheck) {
					String minguoDate = dencodeBig5(row[39]);
					if (!isValidMinguo(minguoDate)) {
						return null;
					}
					MinguoDate dataDate = parse(dtf, minguoDate);
					if (null != localDateFrom && dataDate.isBefore(localDateFrom)) {
						return null;
					}
					if (null != localDateTo && dataDate.isAfter(localDateTo)) {
						return null;
					} 
				}
				String dataNo = dencodeBig5(row[0]);
				if(StringUtils.isNotBlank(no) && !StringUtils.containsIgnoreCase(dataNo, no)) {
					return null;
				}
				
				InventoryItem inventoryItem = new InventoryItem();
				try {
					inventoryItem.setNo(dataNo);
					inventoryItem.setMaterialWeight(new BigDecimal((Double) row[6]));
					inventoryItem.setName1(dencodeBig5(row[15]));
					inventoryItem.setNetWeight(new BigDecimal((Double) row[17]));
					inventoryItem.setCategory(InventoryCategory.FINISHED_PRODUCT);

				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
				
				return inventoryItem;
			}
		};
	}


	
}
