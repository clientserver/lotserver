package com.ruyicai.lotserver.service.common;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.Platform;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 登录
 * @author Administrator
 *
 */
@Service
public class LoginCommonService {

	private Logger logger = Logger.getLogger(LoginCommonService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	LotteryService lotteryCommonService;
	
	@Produce(uri = "jmsLotserver:topic:loginSuccessAddScore", context = "lotserverCamelContext")
	private ProducerTemplate loginSuccessAddScoreTemplate;
	
	@Produce(uri = "jmsLotserver:topic:iphoneSaveToken", context = "lotserverCamelContext")
	private ProducerTemplate iphoneSaveTokenTemplate;
	
	/**
	 * 登录成功后增加积分的JMS
	 * @param userNo
	 */
	public void loginSuccessAddScoreJMS(String userNo) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("userNo", userNo);
		
		logger.info("loginSuccessAddScoreTemplate start, headers:" + header);
		loginSuccessAddScoreTemplate.sendBodyAndHeaders(null, header);
		//logger.info("loginSuccessAddScoreTemplate end, headers:" + header);
	}
	
	/**
	 * iphone登录的特殊处理
	 * @param responseJson
	 * @param userNo
	 * @param clientInfo
	 */
	public void iphoneLoginSpecialDispose(JSONObject responseJson, String userNo, ClientInfo clientInfo) {
		//iPhone是否通过wap页面进行投注(为了app store审核)true:通过wap页面投注,false:通过客户端投注
		if (userNo!=null&&userNo.equals(propertiesUtil.getIphone_isWapPage_userno())) {
			responseJson.put("isWapPage", "true");
		}
		//iphone设置Token
		iphoneSaveTokenJMS(userNo, clientInfo);
	}
	
	/**
	 * iphone保存token的JMS
	 * @param userNo
	 * @param machineId
	 * @param token
	 * @param type
	 */
	public void iphoneSaveTokenJMS(String userNo, ClientInfo clientInfo) {
		String token = clientInfo.getToken();
		String type = clientInfo.getType();
		String machineId = clientInfo.getMachineId();
		
		if (!StringUtil.isEmpty(token)&&!StringUtil.isEmpty(type)) {
			Map<String, Object> header = new HashMap<String, Object>();
			header.put("userNo", userNo);
			header.put("type", type);
			header.put("machineId", machineId);
			header.put("token", token);
			
			logger.info("iphoneSaveTokenTemplate start, headers:" + header);
			iphoneSaveTokenTemplate.sendBodyAndHeaders(null, header);
			//logger.info("iphoneSaveTokenTemplate end, headers:" + header);
		}
	}
	
	/**
	 * 登录成后的处理
	 * @param responseJson
	 * @param userNo
	 * @param certId
	 * @param mobileId
	 * @param name
	 * @param userName
	 * @param clientInfo
	 */
	public void loginSuccessDispose(JSONObject responseJson, JSONObject valueObject, ClientInfo clientInfo) {
		String userNo = valueObject.getString("userno");
		String certId = valueObject.getString("certid");
		if (Tools.isEmpty(certId)||certId.trim().equals("null")
				||certId.trim().equals("111111111111111111")) { //如果身份证为18个1，则未绑定
			certId = "";
		}
		String mobileId = valueObject.getString("mobileid");
		if (Tools.isEmpty(mobileId)||mobileId.trim().equals("null")) { //绑定的手机号码为空
			mobileId = "";
		}
		String name = valueObject.getString("name");
		if (Tools.isEmpty(name)||name.trim().equals("null")) { //真是姓名为空
			name = "";
		}
		String userName = valueObject.getString("userName");
		if (Tools.isEmpty(userName)||userName.trim().equals("null")) { //用户名为空
			userName = "";
		}
		
		responseJson.put(Constants.userNo, userNo);
		responseJson.put(Constants.certId, certId);
		responseJson.put(Constants.mobileId, mobileId);
		responseJson.put(Constants.name, name);
		responseJson.put(Constants.userName, userName);
		responseJson.put(Constants.sessionId, clientInfo.getSysSessionid());
		
		//登录成功,调用赠送积分的方法
		loginSuccessAddScoreJMS(userNo);
		
		String platform = clientInfo.getPlatform(); //平台
		if (platform!=null&&platform.equals(Platform.iPhone.value())) { //iPhone的特殊处理
			iphoneLoginSpecialDispose(responseJson, userNo, clientInfo);
		}
	}
	
	/**
	 * 根据来源获得联合登录的type
	 * @param source
	 * @return
	 */
	public String getUnionLoginType(String source) {
		String type = "";
		if (source!=null&&source.equals("qq")) { //腾讯
			type = "000003";
		} else if (source!=null&&source.equals("sina")) { //新浪
			type = "000004";
		}
		return type;
	}
	
}
