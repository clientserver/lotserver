package com.ruyicai.lotserver.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.util.common.ToolsAesCrypt;

/**
 * 测试Controller
 * @author Administrator
 *
 */
@RequestMapping("/test")
@Controller
public class LotserverTestController {
	
	private Logger logger = Logger.getLogger(LotserverTestController.class);

	@RequestMapping("/index")
	public ModelAndView list(ModelAndView view) {
		view.setViewName("test/index");
		return view;
	}
	
	@RequestMapping(value = "/sendRequest", method = RequestMethod.POST)
	public @ResponseBody
	String execute(HttpServletRequest request) {
		String content = request.getParameter("content") ;
		String sURL = request.getParameter("urlAddress");
		//参数加密
		try {
			content = ToolsAesCrypt.Encrypt(content, Constants.key);
		} catch (Exception e) {
			logger.error("error", e);
		}
		String result = ""; //返回结果
		//请求
		try {
			HttpClient client = new HttpClient();
			PostMethod postMethod = new PostMethod(sURL);
			RequestEntity requestEntity = new StringRequestEntity(content, null, null); 
			postMethod.setRequestEntity(requestEntity);
			client.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		} catch (Exception e) {
			logger.error("error", e);
		}
		//返回结果解密
		try {
			result = ToolsAesCrypt.Decrypt(result, Constants.key);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return result;
	}
	
}
