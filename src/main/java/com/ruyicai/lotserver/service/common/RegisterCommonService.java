package com.ruyicai.lotserver.service.common;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;

/**
 * 注册
 * @author Administrator
 *
 */
@Service
public class RegisterCommonService {

	private Logger logger = Logger.getLogger(RegisterCommonService.class);
	
	@Autowired
	private CommonService commonService;
	
	@Produce(uri = "jmsLotserver:topic:registerUpdateUserInfo", context = "lotserverCamelContext")
	private ProducerTemplate registerUpdateUserInfoTemplate;
	
	@Produce(uri = "jmsLotserver:topic:registerAgency", context = "lotserverCamelContext")
	private ProducerTemplate registerAgencyTemplate;
	
	/**
	 * 注册成功后的处理
	 * @param phoneNum
	 * @param valueObject
	 * @param clientInfo
	 */
	public void registerSuccessDispose(String phoneNum, JSONObject valueObject, ClientInfo clientInfo) {
		String imei = clientInfo.getImei(); //手机标识
		String userNo = ""; //用户编号
		if (valueObject!=null&&valueObject.has("userno")) {
			userNo = valueObject.getString("userno");
		}
		//注册后更新用户信息
		updateUserInfoJMS(imei, phoneNum, userNo, clientInfo.getPlatform(), clientInfo.getMac());
		commonService.perfectUserInfoAddScoreJMS(userNo); //判断用户信息是否完整,如果完整则送积分
		//代理
		String recommender = clientInfo.getRecommender(); //推荐人的用户名
		String agencyNo = clientInfo.getAgencyNo(); //默认代理编号
		registerAgentJMS(userNo, recommender, agencyNo);
	}
	
	/**
	 * 注册完后更新用户信息
	 * @param imei
	 * @param phoneNum
	 * @param userNo
	 */
	private void updateUserInfoJMS(String imei, String phoneNum, String userNo, String platform, String mac) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("imei", imei);
		header.put("phoneNum", phoneNum);
		header.put("userNo", userNo);
		header.put("platform", platform);
		header.put("mac", mac);
		
		logger.info("registerUpdateUserInfoTemplate start, headers:" + header);
		registerUpdateUserInfoTemplate.sendBodyAndHeaders(null, header);
		//logger.info("registerUpdateUserInfoTemplate end, headers:" + header);
	}
	
	/**
	 * 注册代理
	 * @param userNo
	 * @param recommender
	 */
	private void registerAgentJMS(String userNo, String recommender, String agencyNo) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("userNo", userNo);
		header.put("recommender", recommender);
		header.put("agencyNo", agencyNo);
		
		logger.info("registerAgencyTemplate start, headers:" + header);
		registerAgencyTemplate.sendBodyAndHeaders(null, header);
		//logger.info("registerAgencyTemplate end, headers:" + header);
	}
	
}
