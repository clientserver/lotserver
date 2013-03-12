package com.ruyicai.lotserver.controller;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.consts.Constants;

/**
 * 系统Controller
 * @author Administrator
 *
 */
@RequestMapping("/system")
@Controller
public class SystemController {

	private Logger logger = Logger.getLogger(SystemController.class);
	
	@Autowired
	private CacheService cacheService;
	
	/**
	 * 刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/flushCache", method = RequestMethod.GET)
	public @ResponseBody String flushCache() {
		JSONObject responseJson = new JSONObject();
		logger.info("刷新缓存 start");
		try {
			cacheService.flushAll();
			
			responseJson.put(Constants.error_code, "0000");
			responseJson.put(Constants.message, "刷新成功");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "刷新失败");
			logger.error("刷新缓存发生异常", e);
		}
		logger.info("刷新缓存 end");
		return responseJson.toString();
	}
	
}
