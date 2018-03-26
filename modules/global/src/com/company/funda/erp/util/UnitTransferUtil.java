package com.company.funda.erp.util;

import java.math.BigDecimal;

public class UnitTransferUtil {
	
	public static BigDecimal THOUSAND = new BigDecimal(1000);

	public static BigDecimal getPcsFromKg(BigDecimal kg,BigDecimal unitWeightG) {

		return validateBiggerThenZero(kg).multiply(THOUSAND).divide(unitWeightG,0, BigDecimal.ROUND_FLOOR);
	} 
	
	public static BigDecimal getKgFromPcs(BigDecimal pcs,BigDecimal unitWeightG) {
		return validateBiggerThenZero(pcs).multiply(unitWeightG).divide(THOUSAND);
	} 
	
	public static BigDecimal validateBiggerThenZero(BigDecimal target) {
		return (target.compareTo(BigDecimal.ZERO) > 0) ? target : BigDecimal.ZERO;
	}
	
	public static void main(String[] args) {
		System.out.println(getPcsFromKg(new BigDecimal(-5), new BigDecimal(7)).toString());
		
		System.out.println(getKgFromPcs(new BigDecimal(5000), new BigDecimal(7.789)).toString());
	}
}
