package com.ruyicai.lotserver.util.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期公共类
 * @author Administrator
 *
 */
public class DateParseFormatUtil {
	
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * 获得今日日期
	 * @return
	 */
	public static String getTodayDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}
	
	/**
	 * 获得前1个月的日期
	 * @return
	 */
	public static String getPreOneMonthDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -30);
		return sdf.format(calendar.getTime());
	}

	/**
	 * 解析日期字符串(yyyy-MM-dd)
	 * @param dateString
	 * @return
	 */
	public static Date parseY_M_d(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!Tools.isEmpty(dateString)) {
				return sdf.parse(dateString);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	/**
	 * 格式化日期时间(yyyy-MM-dd HH:mm:ss)
	 * @param timeString
	 * @return
	 */
	public static String formatDateTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (!Tools.isEmpty(timeString)) {
			return sdf.format(Long.parseLong(timeString));
		}
		return "";
	}
	
	/**
	 * 格式化日期(yyyy-MM-dd)
	 * @param dateString
	 * @return
	 */
	public static String formatY_M_d(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (!Tools.isEmpty(dateString)) {
			return sdf.format(Long.parseLong(dateString));
		}
		return "";
	}
	
	/**
	 * 格式化日期(yyyyMMdd)
	 * @param dateString
	 * @return
	 */
	public static String formatYMd(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if (!Tools.isEmpty(dateString)) {
			return sdf.format(Long.parseLong(dateString));
		}
		return "";
	}
	
	/**
	 * 格式化时间(HH:mm:ss)
	 * @param timeString
	 * @return
	 */
	public static String formatTime(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		if (!Tools.isEmpty(timeString)) {
			return sdf.format(Long.parseLong(timeString));
		}
		return "";
	}
	
	/**
	 * 格式化日期时间(yyyy年MM月dd日 HH时mm分ss秒)
	 * @param timeString
	 * @return
	 */
	public static String formatYMd_Hms(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
		if (!Tools.isEmpty(timeString)) {
			return sdf.format(Long.parseLong(timeString));
		}
		return "";
	}
	
	/**
	 * 格式化日期时间(yy-MM-dd HH:mm)
	 * @param timeString
	 * @return
	 */
	public static String formatYY_M_d_H_m(String timeString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		if (!Tools.isEmpty(timeString)) {
			return sdf.format(Long.parseLong(timeString));
		}
		return "";
	}
	
}
