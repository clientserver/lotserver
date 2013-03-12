package com.ruyicai.lotserver.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.domain.AdvertiseInfo;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 广告点击记录Controller
 * @author Administrator
 *
 */
@RequestMapping("/advertise")
@Controller
public class AdvertiseController {

	private Logger logger = Logger.getLogger(AdvertiseController.class);
	
	/**
	 * 力美广告
	 * @param mac
	 * @param advertiseId
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/limeiNotify", method = RequestMethod.GET)
	public @ResponseBody String limeiNotify(@RequestParam("mac") String mac, @RequestParam("appId") String appId,
			@RequestParam("source") String source) {
		
		JSONObject responseJson = new JSONObject();
		try {
			logger.info("力美广告点击记录 start mac="+mac+";appId="+appId+";source="+source);
			
			//将mac(28E02CE34713)地址加上":"(力美的mac格式:不加密,不带分隔符,大写)
			if (!Tools.isEmpty(mac)) {
				mac = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(mac, 2), ":");
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.mac=? and");
			params.add(mac);
			
			builder.append(" o.appid=? and");
			params.add(appId);
			
			builder.append(" o.source=? ");
			params.add(source);
			
			/*builder.append(" o.state=? ");
			params.add("1");*/
			
			List<AdvertiseInfo> list = AdvertiseInfo.getList(builder.toString(), "", params);
			if (list==null||list.size()==0) {
				AdvertiseInfo advertiseInfo = new AdvertiseInfo();
				advertiseInfo.setMac(mac);
				advertiseInfo.setAppid(appId);
				advertiseInfo.setSource(source);
				advertiseInfo.setCreatetime(new Date());
				advertiseInfo.setUpdatetime(new Date());
				advertiseInfo.setState("1");
				advertiseInfo.persist();
				
				responseJson.put("success", true);
				responseJson.put(Constants.message, "通知成功");
			} else {
				responseJson.put("success", false);
				responseJson.put(Constants.message, "重复记录");
			}
		} catch (Exception e) {
			responseJson.put("success", false);
			responseJson.put(Constants.message, "通知失败");
			logger.error("力美广告点击记录发生异常", e);
		}
		//logger.info("力美广告点击记录 end mac="+mac+";appId="+appId+";source="+source+",result="+responseJson.toString());
		return responseJson.toString();
	}
	
	/**
	 * 点乐广告
	 * @param mac
	 * @param advertiseId
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public @ResponseBody String notify(@RequestParam("mac") String mac, @RequestParam("ad_id") String advertiseId,
			@RequestParam("source") String source) {
		
		JSONObject responseJson = new JSONObject();
		try {
			logger.info("点乐广告点击记录 start mac="+mac+";advertiseId="+advertiseId+";source="+source);
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.mac=? and");
			params.add(mac);
			
			builder.append(" o.advertiseid=? and");
			params.add(advertiseId);
			
			builder.append(" o.source=? ");
			params.add(source);
			
			/*builder.append(" o.state=? ");
			params.add("1");*/
			
			List<AdvertiseInfo> list = AdvertiseInfo.getList(builder.toString(), "", params);
			if (list==null||list.size()==0) {
				AdvertiseInfo advertiseInfo = new AdvertiseInfo();
				advertiseInfo.setMac(mac);
				advertiseInfo.setAdvertiseid(advertiseId);
				advertiseInfo.setSource(source);
				advertiseInfo.setCreatetime(new Date());
				advertiseInfo.setUpdatetime(new Date());
				advertiseInfo.setState("1");
				advertiseInfo.persist();
				
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "通知成功");
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "重复记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "通知失败");
			logger.error("点乐广告点击记录发生异常", e);
		}
		//logger.info("点乐广告点击记录 end mac="+mac+";advertiseId="+advertiseId+";source="+source+",result="+responseJson.toString());
		return responseJson.toString();
	}
	
	/**
	 * 点入广告
	 * @param drkey
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/dianruNotify", method = RequestMethod.GET)
	public @ResponseBody String dianruNotify(@RequestParam("drkey") String drkey, @RequestParam("source") String source) {
		
		JSONObject responseJson = new JSONObject();
		try {
			logger.info("点入广告点击记录 start drkey="+drkey+";source="+source);
			
			if (StringUtil.isEmpty(drkey) || drkey.length()<32) {
				responseJson.put("success", false);
				responseJson.put(Constants.message, "参数错误");
				return responseJson.toString();
			}
			
			String mac = drkey.substring(32); //mac地址
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.mac=? and");
			params.add(mac);
			
			builder.append(" o.drkey=? and");
			params.add(drkey);
			
			builder.append(" o.source=? ");
			params.add(source);
			
			/*builder.append(" o.state=? ");
			params.add("1");*/
			
			List<AdvertiseInfo> list = AdvertiseInfo.getList(builder.toString(), "", params);
			if (list==null||list.size()==0) {
				AdvertiseInfo advertiseInfo = new AdvertiseInfo();
				advertiseInfo.setMac(mac);
				advertiseInfo.setDrkey(drkey);
				advertiseInfo.setSource(source);
				advertiseInfo.setCreatetime(new Date());
				advertiseInfo.setUpdatetime(new Date());
				advertiseInfo.setState("1");
				advertiseInfo.persist();
				
				responseJson.put("success", true);
				responseJson.put(Constants.message, "通知成功");
			} else {
				responseJson.put("success", false);
				responseJson.put(Constants.message, "重复记录");
			}
		} catch (Exception e) {
			responseJson.put("success", false);
			responseJson.put(Constants.message, "通知失败");
			logger.error("点入广告点击记录发生异常", e);
		}
		//logger.info("点入广告点击记录 end drkey="+drkey+";source="+source);
		return responseJson.toString();
	}
	
	/**
	 * 多盟广告
	 * @param mac
	 * @param appId
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/domobNotify", method = RequestMethod.GET)
	public @ResponseBody String domobNotify(@RequestParam("udid") String mac, @RequestParam("app") String appId,  
			@RequestParam("source") String source, @RequestParam("returnFormat") String returnFormat) {
		JSONObject responseJson = new JSONObject();
		try {
			logger.info("多盟广告点击记录 start mac="+mac+";appId="+appId+";source="+source+";returnFormat="+returnFormat);
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.mac=? and");
			params.add(mac);
			
			builder.append(" o.appid=? and");
			params.add(appId);
			
			builder.append(" o.source=? ");
			params.add(source);
			
			List<AdvertiseInfo> list = AdvertiseInfo.getList(builder.toString(), "", params);
			if (list==null||list.size()==0) {
				AdvertiseInfo advertiseInfo = new AdvertiseInfo();
				advertiseInfo.setMac(mac);
				advertiseInfo.setAppid(appId);
				advertiseInfo.setSource(source);
				advertiseInfo.setCreatetime(new Date());
				advertiseInfo.setUpdatetime(new Date());
				advertiseInfo.setState("1");
				advertiseInfo.persist();
				
				responseJson.put("success", true);
				responseJson.put(Constants.message, "通知成功");
			} else {
				responseJson.put("success", false);
				responseJson.put(Constants.message, "重复记录");
			}
		} catch (Exception e) {
			responseJson.put("success", false);
			responseJson.put(Constants.message, "通知失败");
			logger.error("多盟广告点击记录发生异常", e);
		}
		//logger.info("多盟广告点击记录 end mac="+mac+";appId="+appId+";source="+source+";returnFormat="+returnFormat);
		return responseJson.toString();
	}
	
}
