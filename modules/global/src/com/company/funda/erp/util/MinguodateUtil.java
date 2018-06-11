package com.company.funda.erp.util;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinguodateUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MinguodateUtil.class);
	
	/**
	 * 
	 * @author Howard Chang
	 *
	 */
	public enum Delimeter{
		
		DASH("-"),SLASH("/"),PERIOD(".");
		
		private String literal;
		Delimeter(String literal){
			this.literal = literal;
		}
		
		public String getIt() {
			return literal;
		}
	}
	
	public static DateTimeFormatter getDateTimeFormatter(Delimeter delimeter) {
		Locale locale = Locale.getDefault(Locale.Category.FORMAT);
		DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseLenient()
		        .appendPattern("yyy"+delimeter.getIt()+"MM"+delimeter.getIt()+"dd").toFormatter().withChronology(MinguoChronology.INSTANCE)
		        .withDecimalStyle(DecimalStyle.of(locale));
		return dtf;
	}
	
	public static MinguoDate parse(DateTimeFormatter dtf,String minguoDate) {
		TemporalAccessor temporal = dtf.parse(minguoDate);
		return MinguoChronology.INSTANCE.date(temporal);
	}
	
	public static LocalDate parseLocalDate(DateTimeFormatter dtf,String minguoDate) {
		return LocalDate.from(dtf.parse(minguoDate));
	}
	
	public static String parseStr(DateTimeFormatter dtf,String minguodate) {
		String result = "";
		ChronoLocalDate chronoLocalDate;
		try {
			chronoLocalDate = parse(dtf,minguodate);
			result = LocalDate.from(chronoLocalDate).toString();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
				
		return result;
	}
	public static boolean isValidMinguo(String minguodate) {
		boolean result = false;
		if(StringUtils.isNotBlank(minguodate) && minguodate.length()>5) {
			result =  true;
		}
		return result;
	}
	
	public static void main(String ...strings) {

		DateTimeFormatter dtf = getDateTimeFormatter(Delimeter.PERIOD);
		MinguoDate md = parse(dtf, "107.6.4");
		
		System.out.println(md.isBefore(LocalDate.now()));
		System.out.println(md.isEqual(LocalDate.now()));
		System.out.println(md.isAfter(LocalDate.now()));
		
		System.out.println(parseStr(dtf, "107.6.4"));
		
//		MinguoDate.of(107, 5, 28);
//		System.out.println(LocalDate.from(MinguoDate.of(107, 5, 28)));
		
	}
}
