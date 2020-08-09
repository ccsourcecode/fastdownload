package com.yomahub.fastdownload.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	/**
	 * 把给定的字符串用给定的字符串在前面扩展到指定长度 如果等待扩展的字符串本来就长过目标长度,截断
	 * 如果fillStr长度不是0，那么返回不超过length的填充字符串
	 */
	public static String fillString(String fillStr, String value, int length) {
		int i = length - value.length();
		if (i == 0) { // Do nothing
			return value;
		} else if (i > 0) {
			if (fillStr.length() <= 0) {
				return value;
			}
			StringBuffer sb = new StringBuffer(length);
			sb.append(repeatString(fillStr, i / fillStr.length()));
			sb.append(value);
			return sb.toString();
		} else {
			return value.substring(-1 * i);
		}
	} // End of FillString

	/**
	 * 返回给定字符串重复次数后的字符串
	 */
	public static String repeatString(String str, int repeat) {
		StringBuffer sb = new StringBuffer(str.length() * repeat);
		for (int i = 0; i < repeat; i++) {
			sb.append(str);
		}
		//
		return sb.toString();
	} // End of RepeatString

	/**
	 * 字符串替换，将字符串中的某一子串替换成另一子串
	 */

	public static String replaceString(String str, String replaceStr,
			String replaceWithStr) throws Exception {
		if (str == null || str.length() == 0) {
			return str;
		}
		if (replaceStr == null || replaceStr.length() == 0
				|| replaceWithStr == null || replaceWithStr.length() == 0) {
			throw new Exception(
					"StringHelper: failed to replace string. bad replaceStr or replaceWithStr.");
		}
		StringBuffer newStr = new StringBuffer(256);
		String leftPartStr = "";
		int leftPartPos = -1;
		int pos = str.indexOf(replaceStr);
		while (pos >= 0) {
			if (pos == 0) {
				if (leftPartPos == -1) {
					leftPartStr = "";
				}
			} else {

				leftPartStr = str.substring(leftPartPos + 1, pos);
			}

			leftPartPos = pos + replaceStr.length() - 1;
			newStr.append(leftPartStr);
			newStr.append(replaceWithStr);
			pos = str.indexOf(replaceStr, leftPartPos + 1);
		} // while
		if (leftPartPos + 1 < str.length()) {
			newStr.append(str.substring(leftPartPos + 1));
		}
		return newStr.toString();
	}

	/**
	 * 时间间隔字符串
	 */
	public final static String getBetweenStr(java.util.Date beginDate,
			java.util.Date endDate) {
		long time = endDate.getTime() - beginDate.getTime();
		long day = time / (24 * 60 * 60 * 1000);
		long hour = (time / (60 * 60 * 1000) - day * 24);
		long minute = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long second = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		return day + "天" + hour + "小时" + minute + "分" + second + "秒";
	}

	/**
	 * <p>
	 * 把字符打散转换成UTF-8的格式，用于解决网页上汉字乱码
	 *
	 * @param str
	 *            字符串
	 * @return 截取后得到的字符
	 */
	public static String getEncodeStr(String str) {
//		String strUtf8 = null;
//		try {
//			strUtf8 = new String(str.getBytes("ISO-8859-1"), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//
//		}
		return str;
	}

	/**
	 * 把字符打散转换成自定义的格式，用于解决网页上汉字乱码
	 */
	public static String getEncodeStr(String str, String from, String to) {
		String strUtf8 = null;
		try {
			strUtf8 = new String(str.getBytes(from), to);
		} catch (UnsupportedEncodingException e) {

		}
		return strUtf8;
	}

	public static String nvl(String str, String defaultStr){
		if (str == null){
			return defaultStr;
		}else{
			return str;
		}
	}

	public static Object nvl(Object str, Object defaultObject){
		if (str == null){
			return defaultObject;
		}else{
			return str;
		}
	}
	/**
	 * type:要转换的字符说明
	 * JSP:
	 * 1---------'
	 * 2---------回车
	 * 3---------1和2的合体
	 *
	 * ORACLE:
	 * 101-------'
	 */
	public static String replaceSpecialWords(int type, String str){
		String returnStr = null;
		if (str != null){
			if (type == 1){
				returnStr = str.replaceAll("'", "’");
			}else if (type == 2){
				returnStr = str.replaceAll("\n", " ");
			}else if (type == 3){
				returnStr = str.trim().replaceAll("'", "’").replaceAll("\n", " ");
			}else if (type == 101){
				returnStr = str.replaceAll("'", "' || chr(39)|| '");
			}
		}
		return returnStr;
	}

	public static String getMethodPar(String methodName){
		String parName = null;
		if (StringUtils.isNotEmpty(methodName)){
			parName = methodName.substring(3,4).toLowerCase() + methodName.substring(4);
		}
		return parName;
	}

	public static String upperFirstChar(String str){
	    return str.replaceFirst(str.substring(0, 1), str.substring(0, 1).toUpperCase());
	}

	private static String toCamelize(String str){
		StringBuffer result = new StringBuffer("");
		String[] elems = str.split("_");
		String appendStr = null;
		for (int i = 0; i < elems.length; i++){
			elems[i] = elems[i].toLowerCase();
			if (i > 0){
				appendStr = elems[i].substring(0, 1).toUpperCase() + elems[i].substring(1);
			}else{
				appendStr = elems[i];
			}
			result.append(appendStr);
		}
		return result.toString();
	}

	public static void main(String args[]) {
		String str = "adadff";
		System.out.println(upperFirstChar(str));
	}
}
