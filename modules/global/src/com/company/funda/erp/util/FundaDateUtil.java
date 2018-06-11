package com.company.funda.erp.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.MinguoChronology;
import java.time.chrono.MinguoDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DecimalStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FundaDateUtil {
	
	/**
	 * DateTime的種類，讓DateTimeUtil各種方法當參數用。
	 * @author Howard Chang
	 *
	 */
	public enum Type{
		
		//Formatter of Time
		TIME_HHMMSS_COLON("HH:mm:ss"),
		TIME_HHMMSS("HHmmss"),
		//Formatter of DateTime
		DAY_TIME("(yyyyMMdd,HHmmss)"),
		DAY_TIME_HYPHEN("yyyy-MM-dd HH:mm:ss"),
		DAY_TIME_SLASH("yyyy/MM/dd HH:mm:ss"),
		//Formatter of Date
		DAY_HYPHEN("yyyy-MM-dd"),
		DAY_SLASH("yyyy/MM/dd"),
		
		//DEFAULT
		DEFAULT(DAY_TIME_SLASH.name);
		
		private String name;
		private DateTimeFormatter dateTimeFormat;
		
		private Type(String _name) {
			name = _name;
			dateTimeFormat = DateTimeFormatter.ofPattern(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public DateTimeFormatter getFormat() {
			return dateTimeFormat;
		}

		public void setFormat(DateTimeFormatter format) {
			this.dateTimeFormat = format;
		}

		@Override
		public String toString() {
			return this.name();
		}
		
	}
	
	public static final long SECONDS_PER_DAY = 24L * 60L * 60L;
	public static final String DEFAULT_TIME_LITERAL = "-- : --";
	private static Logger logger = LoggerFactory.getLogger(FundaDateUtil.class);
	
	public static Date parse(String dateLiteral,FundaDateUtil.Type type) {
		return dateFromLocalDateTime(LocalDateTime.parse(dateLiteral, type.getFormat()));

	}
	
	public static String format(Date date,FundaDateUtil.Type type) {
		if(null == date) {
			return "";
		}
		return type.getFormat().format(localDateTimeFromDate(date));
	}
	
	public static String format(LocalDateTime localDateTime,FundaDateUtil.Type type) {
		if(null == localDateTime) {
			return "";
		}
		return type.getFormat().format(localDateTime);
	}
	
	public static String format(LocalTime localTime,FundaDateUtil.Type type) {
		if(null == localTime) {
			return "";
		}
		return type.getFormat().format(localTime);
	}
	
	public static Date dateFromLocalDateTime(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static LocalDateTime localDateTimeFromDate(Date date) {
//		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	
	public static Date dateFromLocalDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

	}
	
	public static LocalDate localDateFromDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	
	/**
	 * Transfer time to today+time.
	 * @param hhmmss
	 * @param DateTimeUtil.Type
	 * @return today + hhmmss
	 * ex:If today is 2018/01/25 12點15分16秒 ,input (12:15:16,DateTimeUtil.Type.TIME_HHMMSS_COLON),output 
	 */
	public static Date timeToTodayDateTime(String hhmmss,FundaDateUtil.Type type) {
		LocalTime startTime = LocalTime.parse(hhmmss, type.getFormat());
		return timeToTodayDateTime(startTime);
    }
	public static Date timeToTodayDateTime(LocalTime startTime) {
		Instant startInstant = startTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(startInstant);
    }
	
	/**
	 * Make time become chronograph until now.
	 * @param from
	 * @return
	 */
	public static String getChronograph(LocalDateTime from) {
		final long periodSEC = ChronoUnit.SECONDS.between(from, LocalDateTime.now());
		final long day = periodSEC/SECONDS_PER_DAY;
		final long time = periodSEC%SECONDS_PER_DAY;
		return String.format("(%s) %s",day,LocalTime.ofSecondOfDay(time));
	}
	
	public static Date offWorkDate(Date date) {
		Date offWorkDate = DateUtils.truncate(date, Calendar.DATE);
		offWorkDate = DateUtils.setHours(offWorkDate, 17);
		return offWorkDate;
	}
	
	public static int minutesBetween(Date dateFrom,Date dateTo) {
		Long duration = Duration.between(dateFrom.toInstant(), dateTo.toInstant()).toMinutes();
		return duration.intValue();
	}
	
	public static boolean isWeekend(LocalDate localDate) {
		boolean result = false;
		if(EnumSet.of(DayOfWeek.SATURDAY,DayOfWeek.SUNDAY).contains(localDate.getDayOfWeek())) {
			result = true;
		}
		return result;
	}
	
	public static boolean isBetweenBroadly(Date target,Date from,Date to) {
//		logger.info("isBetweenBroadly target:{},from:{},to:{}",
//				format(target, Type.DAY_TIME_SLASH),
//				format(from, Type.DAY_TIME_SLASH),
//				format(to, Type.DAY_TIME_SLASH));
		boolean result = false;
		if(from == null && to != null) {
			return target.before(to);
		}
		if(to == null && from != null) {
			return target.after(from) || target.equals(from);
		}
		result = isBetweenNarrowly(target, from, to);
		return result;
	}
	
	public static boolean isBetweenNarrowly(Date target,Date from,Date to) {
//		logger.info("isBetweenNarrowly target:{},from:{},to:{}",
//				format(target, Type.DAY_TIME_SLASH),
//				format(from, Type.DAY_TIME_SLASH),
//				format(to, Type.DAY_TIME_SLASH));
		boolean result = false;
		if(to != null && from != null) {
			result = (target.after(from) || target.equals(from)) && target.before(to); 
		}
		return result;
	}
	
	public static void main(String ...strings) {
		
		
		System.out.println(StringUtils.containsIgnoreCase("abc", "a"));
		System.out.println(StringUtils.containsIgnoreCase("abc", "aa"));
		System.out.println(StringUtils.containsIgnoreCase("abc", "A"));
		System.out.println(StringUtils.containsIgnoreCase("abc", "AA"));
//		final Date nowDate = new Date();
//		Date anotherDate = DateUtils.addDays(nowDate,-7);
//		anotherDate = DateUtils.round(anotherDate, Calendar.DATE);
//		System.out.println(format(nowDate, Type.DAY_TIME));
//		System.out.println(format(anotherDate, Type.DAY_TIME));
		
//		LocalDateTime ldt1 = LocalDateTime.of(2018, 3, 1, 8, 0);
//		LocalDateTime ldt2 = LocalDateTime.of(2018, 3, 1, 12, 0);
//		LocalDateTime target = LocalDateTime.of(2018, 3, 1, 13, 0);
//		System.out.println(FundaDateUtil.isBetweenBroadly(FundaDateUtil.dateFromLocalDateTime(target),
//				FundaDateUtil.dateFromLocalDateTime(ldt1), 
//				FundaDateUtil.dateFromLocalDateTime(ldt2)));
		
//		System.out.println(isWeekend(LocalDate.now()));
		
//		System.out.println(parse("2018/01/01 12:00:00", FundaDateUtil.Type.DAY_TIME_SLASH));
//		System.out.println(timeToTodayDateTime("123456", FundaDateUtil.Type.TIME_HHMMSS));
//		System.out.println(format(LocalDateTime.now(), FundaDateUtil.Type.DAY_TIME_SLASH));
//		System.out.println(format(new Date(), FundaDateUtil.Type.DAY_TIME_SLASH));
//		System.out.println(offWorkDate(new Date()));
		
//		Date d1 = parse("2018/02/04 15:33:00", Type.DAY_TIME_SLASH);
//		Date d2 = parse("2018/02/04 17:00:00", Type.DAY_TIME_SLASH);
//		System.out.println(d1);
//		System.out.println(d2);
//		System.out.println(minutesBetween(d1, d2));
	}
}
