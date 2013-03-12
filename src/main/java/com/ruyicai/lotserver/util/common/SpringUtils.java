package com.ruyicai.lotserver.util.common;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @ClassName: SpringUtils
 * @Description: spring工具类
 * 
 */
public class SpringUtils {

	private static Logger logger = LoggerFactory.getLogger(SpringUtils.class);

	private static ApplicationContext applicationContext = null;

	/**
	 * 初始化SpringUtils
	 * 
	 * @param servletContext
	 */
	public static void initApplicationContext(ServletContext servletContext) {
		Assert.notNull(servletContext);
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = WebApplicationContextUtils
					.getRequiredWebApplicationContext(servletContext);
		}
	}

	/**
	 * 设置存储静态变量中的ApplicationContext.
	 */
	public static void setApplicationContext(ApplicationContext applicationContext) {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
		}
	}

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		logger.debug("清除SpringUtils中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		if (applicationContext == null) {
			throw new IllegalStateException("SpringUtils未在系统加载时初始化");
		}
	}

}
