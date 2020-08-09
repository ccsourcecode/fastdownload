package com.yomahub.fastdownload.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {

	/**
	 * 从Map转换为bean对象。map键值要和bean一样。
	 * @param map
	 * @param t
	 */
	public static <T> void map2Bean(Map<String, Object> map, T t) {
		Object[] objKeys = map.keySet().toArray();
		String strFieldName = null;
		Field objField = null;
		try {
			for (Object objkey : objKeys) {
				strFieldName = objkey.toString();
				objField = t.getClass().getDeclaredField(strFieldName);
				objField.setAccessible(true);
				objField.set(t, map.get(strFieldName));
			}
		} catch (Exception e) {
			e.printStackTrace();
			t = null;
		}
	}


	public static <T> Map<String, Object> bean2Map(T t) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Field[] fieldArray = t.getClass().getDeclaredFields();
		for (Field f : fieldArray) {
			f.setAccessible(true);
			try {
				if (f.getName().equals("serialVersionUID")){
					continue;
				}
				returnMap.put(f.getName(), f.get(t));
			} catch (Exception e) {
				e.printStackTrace();
				returnMap = null;
			}
		}
		return returnMap;
	}

	public static Object invokeMethod(String className, String methodName, Object ... args){
	    Object returnObj = null;
	    try{
	        Class<?> c = Class.forName(className);
	        Method[] mArray = c.getMethods();
	        for (Method m : mArray){
	            if(m.getName().equals(methodName)){
	                returnObj = m.invoke(c.getInterfaces(), args);
	                break;
	            }
	        }
	    }catch(Throwable t){
	        t.printStackTrace();
	        returnObj = null;
	    }
	    return returnObj;
	}
}
