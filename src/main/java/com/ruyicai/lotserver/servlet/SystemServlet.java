package com.ruyicai.lotserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ruyicai.lotserver.util.common.SpringUtils;

/**
 * @ClassName: SystemServlet
 * @Description: 系统启动加载Servlet
 * 
 */
public class SystemServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void init() throws ServletException {
		try {
			logger.info("********************** 启动加载开始 **********************");

			logger.info("---------------------- SpringUtils 初始化开始   ----------------------");
			SpringUtils.initApplicationContext(this.getServletContext());
			logger.info("---------------------- SpringUtils 初始化结束   ----------------------");

			logger.info("********************** 启动加载结束 **********************");
		} catch (Exception e) {
			logger.error("启动加载异常", e);
		}
	}
}
