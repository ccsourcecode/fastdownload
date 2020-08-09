package com.yomahub.fastdownload.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtil {

	/**
	 * 得到当前时间long类型，适用于insert into [DB]
	 */
	public static long getLongTime() {
		return System.currentTimeMillis();
	}

	/**
	 * 格式化日期,格式化后可直接insert into [DB],格式化后的日期为：2006-10-12 14:42:47
	 * <P>
	 * 把日期转换成字符串
	 */
	public static String dateToStr(Date date) {

		if (date == null)
			return "";
		else {
			SimpleDateFormat sdFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			String str_Date = sdFormat.format(date);
			return str_Date;
		}
	}

	/**
	 * 格式化后的日期为：2006年10月12
	 * <P>
	 * 把日期转换成字符串
	 */
	public static String dateToStrCN(Date date) {

		if (date == null)
			return "";
		else {
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy年MM月dd",
					Locale.getDefault());
			String str_Date = sdFormat.format(date);
			return str_Date;
		}
	}

	/**
	 * 格式化后的日期为：2006-10-12
	 * <P>
	 * 把日期转换成字符串
	 */
	public static String dateToStrEn(Date date) {

		if (date == null)
			return "";
		else {
			SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
			String str_Date = sdFormat.format(date);
			return str_Date;
		}
	}

	public static String dateToStrEn(Date date,String format) {

		if (date == null)
			return "";
		else {
			SimpleDateFormat sdFormat = new SimpleDateFormat(format,
					Locale.getDefault());
			String str_Date = sdFormat.format(date);
			return str_Date;
		}
	}

	/**
	 * 把字符串转换成日期类型
	 */
	public static Date strToDate(String str) {
		Date date = null;

		try{
			if (StringUtils.isNotEmpty(str)){
				if (str.length() > 10) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					date = formatter.parse(str.substring(0, 18));
				}else{
					date = DateFormat.getDateInstance().parse(str);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期计算
	 *
	 * @param date
	 *            起始日期
	 * @param yearNum
	 *            年增减数
	 * @param monthNum
	 *            月增减数
	 * @param dateNum
	 *            日增减数
	 */
	public static String calDate(String date, int yearNum, int monthNum,
			int dateNum) {
		String result = "";
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sd.parse(date));
			cal.add(Calendar.MONTH, monthNum);
			cal.add(Calendar.YEAR, yearNum);
			cal.add(Calendar.DATE, dateNum);
			result = sd.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String calDate(Date date, int yearNum, int monthNum,
			int dateNum) {
		String result = "";
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH, monthNum);
			cal.add(Calendar.YEAR, yearNum);
			cal.add(Calendar.DATE, dateNum);
			result = sd.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String delFrontZero(String mord) {
		int im = -1;
		try {
			im = Integer.parseInt(mord);
			return String.valueOf(im);
		} catch (Exception e) {
			return mord;
		}
	}

	/**
	 * DateUtil.parseDate("20070423123754") 转换为：Mon Apr 23 12:37:54 CST 2007
	 *
	 * @param orlTime
	 * @return
	 */
	public static Date parseDate(String orlTime) {
		Date date = null;
		if (orlTime == null || orlTime.length() <= 0) {
			return null;
		}

		if (!(orlTime.length() == 14)) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			date = (Date) formatter.parse(orlTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * DateUtil.parseDate("20070423") 转换为：Mon Apr 23 00:00:00 CST 2007
	 *
	 * @param orlTime
	 * @return
	 */
	public static Date parseDateDB(String orlTime) {
		Date date = null;
		if (orlTime == null || orlTime.length() <= 0) {
			return null;
		}

		if (!(orlTime.length() == 8)) {
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		try {
			date = (Date) formatter.parse(orlTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * DateUtil.parseCnDate("2006-10-12") 转换为：2006年10月12日
	 *
	 * @param orlTime
	 * @return
	 */
	public static String parseCnDate(String orlTime) {
		if (orlTime == null || orlTime.length() <= 0) {
			return "";
		}

		if (orlTime.length() < 10) {
			return "";
		}
		String sYear = orlTime.substring(0, 4);
		String sMonth = delFrontZero(orlTime.substring(5, 7));
		String sDay = delFrontZero(orlTime.substring(8, 10));
		return sYear + "年" + sMonth + "月" + sDay + "日";
	}

	/**
	 * DateUtil.parsePointDate("2006-10-12") 转换为：10.12
	 *
	 * @param orlTime
	 * @return
	 */
	public static String parsePointDate(String orlTime) {
		if (orlTime == null || orlTime.length() <= 0) {
			return "";
		}
		String sMonth = delFrontZero(orlTime.substring(5, 7));
		String sDay = delFrontZero(orlTime.substring(8, 10));
		return sMonth + "." + sDay;
	}

	/**
	 * 得到当前月的最后一天
	 *
	 * @param
	 * @return
	 */
	public static Date getLastDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		final int lastDay = cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	/**
	 * 得到当前月的第一天
	 *
	 * @param
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date sDate1) {
		Calendar cDay1 = Calendar.getInstance();
		cDay1.setTime(sDate1);
		final int lastDay = cDay1.getActualMinimum(Calendar.DAY_OF_MONTH);
		Date lastDate = cDay1.getTime();
		lastDate.setDate(lastDay);
		return lastDate;
	}

	public static long calendarDayPlus(Date d1, Date d2) {
		long milis = calendarMilisPlus(d1, d2);
		milis = milis / 1000 / 60 / 60 / 24;
		return milis;
	}

	public static long calendarMilisPlus(Date d1, Date d2) {
		return d2.getTime() - d1.getTime();
	}

	public static String compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				//System.out.println("dt1 在dt2前");
				return "1";
			} else if (dt1.getTime() < dt2.getTime()) {
				//System.out.println("dt1在dt2后");
				return "-1";
			} else {
				return "0";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return "0";
	}

    public final static String getBetweenStr(Date beginDate, Date endDate) {
        if (beginDate == null || endDate == null){
            return null;
        }
        long time = endDate.getTime() - beginDate.getTime();
        long hour = (time / (60 * 60 * 1000));
        long minute =((time / (60 * 1000)) - hour * 60);
        long second = (time / 1000  - hour * 60*  60 - minute * 60);
        return hour + "\u5c0f\u65f6" + minute + "\u5206" + second + "\u79d2";
    }

	public static void main(String[] args){
		System.out.println(strToDate("2011-1-25 16:36:23"));
	}
}
