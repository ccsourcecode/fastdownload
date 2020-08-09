package com.yomahub.fastdownload.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {
	public static void writerJson(HttpServletResponse response,String str){
		writerText(response,"UTF-8","application/x-json",str);
	}
	public static void writerJson(HttpServletResponse response,String characterEncoding,String str){
		writerText(response,characterEncoding,"application/x-json",str);
	}
	public static void writerXml(HttpServletResponse response,String str){
		writerText(response,"UTF-8","text/xml",str);
	}
	public static void writerXml(HttpServletResponse response,String characterEncoding,String str){
		writerText(response,characterEncoding,"text/xml",str);
	}
	public static void writerText(HttpServletResponse response,String str){
		writerText(response,"UTF-8","text/plain",str);
	}
	public static void writerText(HttpServletResponse response,String characterEncoding,String str){
		writerText(response,characterEncoding,"text/plain",str);
	}
	public static void writerText(HttpServletResponse response,String characterEncoding,String contentType,String str){
		PrintWriter writer=null;
		try{
			response.setCharacterEncoding(characterEncoding);
			response.setContentType(contentType);
			writer=response.getWriter();
			writer.print(str);
			writer.flush();
		}catch (IOException e) {
			//throw e;
			System.err.println(e.getLocalizedMessage());
		}finally{
			if(writer!=null){
				writer.close();
			}
		}
	}

	public static String decode(String str) throws UnsupportedEncodingException{
		if(str==null){
			return null;
		}else{
			return URLDecoder.decode(str,"utf-8");
		}
	}
	public static String decodeDefaultValue(String str) throws UnsupportedEncodingException{
		if(str==null){
			return "";
		}else{
			return URLDecoder.decode(str,"utf-8");
		}
	}

	public static Map<String,Cookie> getCookieMap(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		Map<String,Cookie> cookieMap=new HashMap<String,Cookie>();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
}
