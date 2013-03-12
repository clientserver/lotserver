package com.ruyicai.lotserver.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.CoopId;
import com.ruyicai.lotserver.domain.News;
import com.ruyicai.lotserver.domain.Tbroadcast;
import com.ruyicai.lotserver.domain.TgrayUpgrade;
import com.ruyicai.lotserver.domain.TimageConfig;
import com.ruyicai.lotserver.domain.TupgradeConfig;
import com.ruyicai.lotserver.domain.TversionInfo;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.BatchCodeQueryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.service.common.LoginCommonService;
import com.ruyicai.lotserver.util.ChannelUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 软件升级
 * @author Administrator
 *
 */
@Service
public class SoftwareUpdateService {
	
	private Logger logger = Logger.getLogger(SoftwareUpdateService.class);
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private BatchCodeQueryService batchCodeQueryService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LoginCommonService loginCommonService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Produce(uri = "jmsLotserver:topic:recordUserInfo", context = "lotserverCamelContext")
	private ProducerTemplate recordUserInfoTemplate;
	
	/**
	 * 开机联网
	 * @param clientInfo
	 * @return
	 */
	public String getSoftwareUpdateInfo(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			//记录用户信息和广告JMS
			recordUserInfoJMS(clientInfo);
			
			responseJson.put("title", "软件升级");
			responseJson.put("noticetime", 8640000);//下次通知联网时间,距现在多少秒
			//获取所有彩种的当前期号信息
			JSONObject batchCodeJson = batchCodeQueryService.getAllCurrentBatchCode();
			responseJson.put("currentBatchCode", batchCodeJson.toString()); //各彩种的当前期号
			//根据渠道号获得产品编号
			String coopId = clientInfo.getCoopId(); //渠道号
			String productNo = commonUtil.getProductNoByCoopId(coopId);
			//Top新闻
			responseJson.put("news", getTopNewsTitle(productNo, clientInfo));
			//广播消息
			responseJson.put("broadcastmessage", getBroadcastJson(productNo, clientInfo));
			//升级提示
			getPackageUpdateJson(productNo, clientInfo, responseJson);
			//图片下载
			JSONObject imageUpdateJson = getImageUpdateJson(productNo, clientInfo);
			responseJson.put("image", imageUpdateJson.toString());
			//是否自动登录
			JSONObject autoLoginJson = getAutoLoginJson(clientInfo);
			responseJson.put("autoLogin", autoLoginJson.toString());
			//iPhone是否通过wap页面进行投注(为了app store审核)true:通过wap页面投注,false:通过客户端投注
			String platform = clientInfo.getPlatform();
			if (platform!=null&&platform.equals("iPhone")) {
				responseJson.put("isWapPage", "false");
			}
		} catch (Exception e) {
			logger.error("软件升级发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 得到广播消息的Json
	 * @param clientInfo
	 * @return
	 */
	public JSONObject getBroadcastJson(String productNo, ClientInfo clientInfo) {
		JSONObject broadcastJson = new JSONObject();
		String platform = clientInfo.getPlatform(); //平台
		
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.productno=? and");
		params.add(productNo);
		
		builder.append(" o.platform=? ");
		params.add(platform);
		List<Tbroadcast> list = Tbroadcast.getList(builder.toString(), "", params);
		if (list!=null&&list.size()>0) {
			Tbroadcast tbroadcast = list.get(0);
			broadcastJson.put("id", tbroadcast.getNumber());
			broadcastJson.put("title", tbroadcast.getTitle());
			broadcastJson.put("message", tbroadcast.getContent());
		} else {
			broadcastJson.put("id", "");
			broadcastJson.put("title", "");
			broadcastJson.put("message", "");
		}
		return broadcastJson;
	}
	
	/**
	 * 是否是不需要升级的渠道
	 * @param coopId
	 * @return
	 */
	private boolean isNotNeedUpdateChannel(String channel) {
		boolean isNotNeedUpdate = false;
		CoopId[] values = CoopId.values();
		for (CoopId coopId : values) {
			if (channel!=null&&channel.equals(coopId.value())) {
				isNotNeedUpdate = true;
				break;
			}
		}
		return isNotNeedUpdate;
	}
	
	/**
	 * 得到客户端包升级的Json
	 * @param clientInfo
	 * @param responseJson
	 * @return
	 */
	public JSONObject getPackageUpdateJson(String productNo, ClientInfo clientInfo, JSONObject responseJson) {
		String imei = clientInfo.getImei(); //imei
		String platform = clientInfo.getPlatform(); //平台
		String softwareVersion = clientInfo.getSoftwareVersion(); //版本号
		String coopId = clientInfo.getCoopId(); //渠道号
		
		//不需要升级的渠道
		if (isNotNeedUpdateChannel(coopId)) {
			responseJson.put("errorCode", "false");
			return responseJson;
		}
		
		//灰度升级
		StringBuilder builder1 = new StringBuilder(" where");
		List<Object> params1 = new ArrayList<Object>();
		
		builder1.append(" o.imei=? and");
		params1.add(imei);
		
		builder1.append(" o.state=? ");
		params1.add("1");
		
		List<TgrayUpgrade> list1 = TgrayUpgrade.getList(builder1.toString(), "", params1);
		if (list1!=null&&list1.size()>0) {
			TgrayUpgrade tgrayUpgrade = list1.get(0);
			String version = tgrayUpgrade.getVersion();
			if (!softwareVersion.startsWith(version)) { //需要升级
				TversionInfo versionInfo = getVersionInfo(productNo, platform, version);
				if (versionInfo!=null) {
					responseJson.put("errorCode", "true");
					responseJson.put("message", versionInfo.getUpgradedescription());
					responseJson.put("updateurl", versionInfo.getUpgradeurl());
					return responseJson;
				}
			}
		}
		
		//普通升级
		StringBuilder builder2 = new StringBuilder(" where");
		List<Object> params2 = new ArrayList<Object>();
		
		builder2.append(" o.productno=? and");
		params2.add(productNo);
		
		builder2.append(" o.platform=? ");
		params2.add(platform);
		
		List<TupgradeConfig> list2 = TupgradeConfig.getList(builder2.toString(), "", params2);
		if (list2!=null&&list2.size()>0) {
			TupgradeConfig tupgradeConfig = list2.get(0);
			String state = tupgradeConfig.getState(); //升级开关
			String version = tupgradeConfig.getVersion(); //升级到版本号
			if (state!=null&&state.equals("1")) { //开
				if (!softwareVersion.startsWith(version)) { //需要升级
					TversionInfo versionInfo = getVersionInfo(productNo, platform, version);
					if (versionInfo!=null) {
						responseJson.put("errorCode", "true");
						responseJson.put("message", versionInfo.getUpgradedescription());
						responseJson.put("updateurl", versionInfo.getUpgradeurl());
						return responseJson;
					}
				}
			}
		}
		responseJson.put("errorCode", "false");
		
		return responseJson;
	}
	
	/**
	 * 获得版本信息
	 * @param productNo
	 * @param platform
	 * @param version
	 * @return
	 */
	public TversionInfo getVersionInfo(String productNo, String platform, String version) {
		TversionInfo versionInfo = null;
		
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.productno=? and");
		params.add(productNo);
		
		builder.append(" o.platform=? and");
		params.add(platform);
		
		builder.append(" o.version=? ");
		params.add(version);
		
		List<TversionInfo> list = TversionInfo.getList(builder.toString(), "", params);
		if (list!=null&&list.size()>0) {
			versionInfo = list.get(0);
		}
		
		return versionInfo;
	}
	
	/**
	 * 得到开机图片更新的Json
	 * @param clientInfo
	 * @return
	 */
	public JSONObject getImageUpdateJson(String productNo, ClientInfo clientInfo) {
		JSONObject imageUpdateJson = new JSONObject();
		String platform = clientInfo.getPlatform(); //平台
		String coopId = clientInfo.getCoopId(); //渠道号
		
		//不需要升级的渠道
		if (isNotNeedUpdateChannel(coopId)||ChannelUtil.isSuNingChannel(coopId)) { //苏宁渠道不更新开机图片
			imageUpdateJson.put("errorCode", "false");
			return imageUpdateJson;
		}
		
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.productno=? and");
		params.add(productNo);
		
		builder.append(" o.platform=? ");
		params.add(platform);
		
		List<TimageConfig> list = TimageConfig.getList(builder.toString(), "", params);
		if (list!=null&&list.size()>0) {
			TimageConfig timageConfig = list.get(0);
			String state = timageConfig.getState(); //(0:关;1:开)
			if (state!=null&&state.equals("1")) {
				imageUpdateJson.put("errorCode", "true"); //是否下载图片
				imageUpdateJson.put("id", timageConfig.getNumber());
				imageUpdateJson.put("imageUrl", timageConfig.getUrl());
			} else {
				imageUpdateJson.put("errorCode", "false");
			}
		} else {
			imageUpdateJson.put("errorCode", "false");
		}
		
		return imageUpdateJson;
	}
	
	/**
	 * 获得自动登录的Json
	 * @param clientInfo
	 * @return
	 */
	public JSONObject getAutoLoginJson(ClientInfo clientInfo) {
		JSONObject autoLoginJson = new JSONObject();
		autoLoginJson.put(Constants.isAutoLogin, "false");
		String randomNumber = clientInfo.getRandomNumber();
		if (!Tools.isEmpty(randomNumber)) {
			String result = lotteryCommonService.getTuserloginfo(randomNumber);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				if (fromObject!=null) {
					String valueString = fromObject.getString("value");
					if (!Tools.isEmpty(valueString)&&!valueString.equals("null")) {
						autoLoginJson.put(Constants.isAutoLogin, "true");
						JSONObject valueObject = fromObject.getJSONObject("value");
						JSONObject tuserinfoObject = valueObject.getJSONObject("tuserinfo");
						//登录成功后的处理
						loginCommonService.loginSuccessDispose(autoLoginJson, tuserinfoObject, clientInfo);
						return autoLoginJson;
					}
				}
			}
		}
		return autoLoginJson;
	}
	
	/**
	 * 记录用户信息和广告的JMS
	 * @param clientInfo
	 */
	private void recordUserInfoJMS(ClientInfo clientInfo) {
		String imei = clientInfo.getImei(); //手机标识
		String imsi = clientInfo.getImsi(); //SIM卡标识
		String platform = clientInfo.getPlatform(); //平台
		String machineId = clientInfo.getMachineId(); //机型
		String isEmulator = clientInfo.getIsemulator(); //是否使用模拟器
		String softwareVersion = clientInfo.getSoftwareVersion(); //版本号
		String coopId = clientInfo.getCoopId(); //渠道号
		String phoneSIM = clientInfo.getPhoneSIM(); //SIM卡手机号
		String mac = clientInfo.getMac(); //网卡地址
		
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("imei", imei);
		header.put("imsi", imsi);
		header.put("platform", platform);
		header.put("machineId", machineId);
		header.put("isEmulator", isEmulator);
		header.put("softwareVersion", softwareVersion);
		header.put("coopId", coopId);
		header.put("phoneSIM", phoneSIM);
		header.put("mac", mac);
		
		//记录用户信息的JMS
		if (!Tools.isEmpty(imei)) { //手机标识不为空
			logger.info("recordUserInfoTemplate start, headers:" + header);
			recordUserInfoTemplate.sendBodyAndHeaders(null, header);
			//logger.info("recordUserInfoTemplate end, headers:" + header);
		} else {
			logger.info("记录用户信息时Imei为空,coopId="+coopId);
		}
	}
	
	/**
	 * 获得Top新闻的标题
	 * @param clientInfo
	 * @return
	 */
	private String getTopNewsTitle(String productNo, ClientInfo clientInfo) {
		String title = "";
		List<News> list = commonService.queryTopNews(productNo, clientInfo);
		if (list!=null && list.size()>0) {
			News news = list.get(0);
			title = news.getVol_title();
		}
		return title;
	}
	
}
