package com.yomahub.fastdownload.controller;

import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class BaseController{
	public static Object getBean(ServletContext servletContext,String beanName) {
		return getApplicationContext(servletContext).getBean(beanName);
	}
	private static ApplicationContext getApplicationContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
	}
}
