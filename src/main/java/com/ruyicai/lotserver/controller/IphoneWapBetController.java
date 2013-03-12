package com.ruyicai.lotserver.controller;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.ToolsAesCrypt;
import com.ruyicai.lotserver.util.parse.RequestParamUtil;

/**
 * iphone wap页面投注
 * @author Administrator
 *
 */
@Controller
public class IphoneWapBetController {

	private Logger businessLogger = Logger.getLogger("business");
	private Logger logger = Logger.getLogger(IphoneWapBetController.class);
	
	@RequestMapping(value = "/iphoneWapBet", method = RequestMethod.POST)
	public ModelAndView list(@RequestParam(value="jsonString") String jsonString, 
			HttpServletRequest request, ModelAndView view) {
		
		String deCrypt = null;
		try {
			deCrypt = ToolsAesCrypt.Decrypt(jsonString, Constants.key); //解密
		} catch (Exception e) {
			logger.error("解密发生异常", e);
		}
		
		//logger.info("====================IphoneWapBetController-request:begin=================");
		businessLogger.info(deCrypt);
		//logger.info("====================IphoneWapBetController-request:end===================");
		
		//解析请求参数
		ClientInfo clientInfo = new ClientInfo();
		JSONObject requestObject = JSONObject.fromObject(deCrypt);
		RequestParamUtil.parseRequestParam(request, requestObject, clientInfo);
		
		//请求lotserver
		String responseString = RequestDispatcher.dispatch(clientInfo); 
		JSONObject responseObject = JSONObject.fromObject(responseString);
		if (responseObject!=null) {
			String message = responseObject.getString(Constants.message);
			view.addObject("message", message);
		} else {
			view.addObject("message", "投注失败");
		}
		view.setViewName("bet/betResult");
		return view;
	}
	
}
