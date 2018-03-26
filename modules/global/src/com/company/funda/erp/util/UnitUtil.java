package com.company.funda.erp.util;

import java.math.BigDecimal;

public class UnitUtil {

	public static BigDecimal kgToPcs(BigDecimal kg,BigDecimal netWeight) {
		return kg.multiply(new BigDecimal(1000)).divide(netWeight,2, BigDecimal.ROUND_DOWN);
	}
}
