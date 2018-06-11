package com.company.funda.erp.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FundaStringUtil {
	
	private static Logger logger = LoggerFactory.getLogger(FundaStringUtil.class);

	public static String dencodeBig5(Object toBeEncode) {
		String endoded = "";
		try {
			endoded = new String((byte[]) toBeEncode, "Big5");
		} catch (Exception e) {
			logger.error(e.getMessage(), (Throwable) e);
		}
		return StringUtils.trim(endoded);
	}
	
	public static void main(String[] args) {
		Object toBeEncode = null;
		System.out.println(dencodeBig5(toBeEncode));
	}
}
