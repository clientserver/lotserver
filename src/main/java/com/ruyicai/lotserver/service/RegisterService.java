package com.ruyicai.lotserver.service;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.RegisterCommonService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.VerifyUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 注册
 * 
 * @author Administrator
 * 
 */
@Service
public class RegisterService {

	private Logger logger = Logger.getLogger(RegisterService.class);
	
	@Autowired
	private RegisterCommonService registerCommonService;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	/**
	 * 注册
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String register(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String password = clientInfo.getPassword();// 密码
			if (Tools.isEmpty(password)) { //密码不允许为空
				responseJson.put("errorCode", "9999");
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "密码为空");
				return responseJson.toString();
			}
			String certId = clientInfo.getCertid();// 身份证号
			if (!Tools.isEmpty(certId)&&!VerifyUtil.isCertId(certId)) { //身份证不为空要验证格式是否正确
				responseJson.put("errorCode", "9999");
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "身份证格式错误");
				return responseJson.toString();
			}
			String name = clientInfo.getName();// 姓名
			if (!Tools.isEmpty(name)&&name.length()>16) { //验证姓名的长度
				responseJson.put("errorCode", "9999");
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "姓名超过长度限制");
				return responseJson.toString();
			}
			
			String phoneNum = clientInfo.getPhonenum().trim();// 用户名
			//String coopId = clientInfo.getCoopId();// 渠道号
			String imei = clientInfo.getImei();// 手机串号
			String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
			
			String result = lotteryCommonService.register(phoneNum, password, certId.trim(), name.trim(), newCoopId, imei); //注册
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put("errorCode", "0000");
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "注册成功,为了您的账户安全,请您到用户中心绑定手机号");
					//注册成功后的处理
					JSONObject valueObject = fromObject.getJSONObject("value");
					registerCommonService.registerSuccessDispose(phoneNum, valueObject, clientInfo);
				} else if (errorCode!=null&&errorCode.equals("0013")) {
					responseJson.put("errorCode", "0013");
					responseJson.put(Constants.error_code, "0013");
					responseJson.put(Constants.message, "用户已注册");
				} else if (errorCode!=null&&errorCode.equals("000012")) {
					responseJson.put("errorCode", "000012");
					responseJson.put(Constants.error_code, "0012");
					responseJson.put(Constants.message, "该号被暂停,请联系客服");
				} else if (errorCode!=null&&errorCode.equals("0019")) {
					responseJson.put("errorCode", "0019");
					responseJson.put(Constants.error_code, "0019");
					responseJson.put(Constants.message, "手机号错误");
				} else if (errorCode!=null&&errorCode.equals("500")) {
					responseJson.put("errorCode", "500");
					responseJson.put(Constants.error_code, "500");
					responseJson.put(Constants.message, "注册失败");
				} else {
					responseJson.put("errorCode", "9999");
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "注册失败");
				}
			} else {
				responseJson.put("errorCode", "9999");
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "注册失败");
			}
		} catch (Exception e) {
			responseJson.put("errorCode", "9999");
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("注册发生异常", e);
		}
		return responseJson.toString();
	}
	
}
