package com.ruyicai.lotserver.service;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.LoginCommonService;
import com.ruyicai.lotserver.service.common.RegisterCommonService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 登录
 * @author Administrator
 *
 */
@Service
public class LoginService {
	
	private Logger logger = Logger.getLogger(LoginService.class);
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private LoginCommonService loginCommonService;
	
	@Autowired
	private RegisterCommonService registerCommonService;
	
	/**
	 * 通过手机号登录
	 * @param clientInfo
	 * @return
	 */
	public String loginByMobileId(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = "";// 用户编号
			String dbPassword = ""; //密码
			String state = ""; //状态
			String password = clientInfo.getPassword(); //客户端传过来的密码
			String phonenum = clientInfo.getPhonenum(); //客户端传过来的用户名
			
			if (Tools.isEmpty(phonenum)) { //用户名为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String result = lotteryCommonService.queryUsersByMobileid(phonenum, null);
			if (Tools.isEmpty(result)) { //返回结果为空
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			JSONObject valueObject = null;
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					valueObject = fromObject.getJSONObject("value");
					userNo = valueObject.getString("userno");
					dbPassword = valueObject.getString("password");
					state = valueObject.getString("state");
				} else if (errorCode!=null&&errorCode.equals("100002")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "未注册");
					return responseJson.toString();
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "登录失败");
					return responseJson.toString();
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "登录失败");
				return responseJson.toString();
			}
			
			if (state!=null&&state.equals("0")) { //关闭用户
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "账户关闭");
			} else if (state!=null&&state.equals("2")) { //用户暂停
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "用户暂停");
			} else if (state!=null&&state.equals("1")&&password!=null
					&&dbPassword.equals(Tools.EncoderByMd5(password))) { //状态正常，密码正确
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "登录成功");
				loginCommonService.loginSuccessDispose(responseJson, valueObject, clientInfo);
				//判断是否自动登录
				autoLogin(responseJson, clientInfo.getIsAutoLogin(), userNo);
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "密码错误");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("通过手机号登录发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 登录
	 * @param clientInfo
	 * @return
	 */
	public String login(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = "";// 用户编号
			String dbPassword = ""; //密码
			String state = ""; //状态
			String password = clientInfo.getPassword(); //客户端传过来的密码
			String phoneNum = clientInfo.getPhonenum(); //客户端传过来的用户名
			
			if (Tools.isEmpty(phoneNum)) { //用户名为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String result = lotteryCommonService.queryUsersByUserName(phoneNum.trim());
			if (Tools.isEmpty(result)) { //返回结果为空
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			JSONObject valueObject = null;
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					valueObject = fromObject.getJSONObject("value");
					userNo = valueObject.getString("userno");
					dbPassword = valueObject.getString("password");
					state = valueObject.getString("state");
				} else {
					return loginByMobileId(clientInfo); //手机号码登录
				}
			} else {
				return loginByMobileId(clientInfo); //手机号码登录
			}
			
			if (state!=null&&state.equals("0")) { //关闭用户
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "账户关闭");
			} else if (state!=null&&state.equals("2")) { //用户暂停
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "用户暂停");
			} else if (state!=null&&state.equals("1")&&password!=null
					&&dbPassword.equals(Tools.EncoderByMd5(password))) { //状态正常，密码正确
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "登录成功");
				loginCommonService.loginSuccessDispose(responseJson, valueObject, clientInfo);
				//判断是否自动登录
				autoLogin(responseJson, clientInfo.getIsAutoLogin(), userNo);
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "密码错误");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("登录发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 自动登录
	 * @param responseJson
	 * @param isAutoLogin
	 * @param userNo
	 */
	public void autoLogin(JSONObject responseJson, String isAutoLogin, String userNo) {
		if (isAutoLogin!=null&&isAutoLogin.equals("1")) { //自动登录
			String autoLoginResult = lotteryCommonService.addTuserloginfo(userNo, "C");
			if (!Tools.isEmpty(autoLoginResult)) {
				JSONObject fromObject2 = JSONObject.fromObject(autoLoginResult);
				JSONObject valueObject2 = fromObject2.getJSONObject("value");
				if (valueObject2!=null) {
					String random = valueObject2.getString("random");
					if (!Tools.isEmpty(random)) {
						responseJson.put(Constants.randomNumber, random);
					}
				}
			}
		}
	}
	
	/**
	 * 联合登录
	 * @param clientInfo
	 * @return
	 */
	public String unionLogin(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		
		try {
			String source = clientInfo.getSource(); //第三方标识
			String openId = clientInfo.getOpenId(); //第三方用户标识
			String type = loginCommonService.getUnionLoginType(source); //类型
			
			String result = lotteryCommonService.getUnionLoginUser(type, openId);
			if (Tools.isEmpty(result)) { //返回结果为空
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) { //已注册
					JSONObject valueObject = fromObject.getJSONObject("value");
					
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "登录成功");
					//登录成功后的处理
					loginCommonService.loginSuccessDispose(responseJson, valueObject, clientInfo);
				} else { //未注册
					String nickName = clientInfo.getNickName(); //昵称
					String password = Tools.generateRandomNum(6); //密码
					//String channel = clientInfo.getCoopId(); //渠道号
					String imei = clientInfo.getImei(); //手机标识
					String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
					
					//生成用户名
					String phoneNum = Tools.generateRandomNum(8);
					if (nickName!=null&&nickName.length()>4) {
						phoneNum = nickName.substring(0, 4) + Tools.generateRandomNum(4);
					} else if (nickName!=null&&nickName.length()<4) {
						phoneNum = nickName + Tools.generateRandomNum(4);
					}
					
					String result2 = lotteryCommonService.registerUnionLoginUser(phoneNum, password, type, openId, newCoopId, imei);
					if (Tools.isEmpty(result2)) { //返回结果为空
						return Tools.paramError(clientInfo.getImei());
					}
					JSONObject fromObject2 = JSONObject.fromObject(result2);
					if (fromObject2!=null) {
						String errorCode2 = fromObject2.getString("errorCode");
						if (errorCode2!=null&&errorCode2.equals("0")) { //注册成功
							JSONObject valueObject = fromObject2.getJSONObject("value");
							
							responseJson.put(Constants.error_code, "0000");
							responseJson.put(Constants.message, "登录成功");
							//登录成功后的处理
							loginCommonService.loginSuccessDispose(responseJson, valueObject, clientInfo);
							//注册成功后的处理
							registerCommonService.registerSuccessDispose(phoneNum, valueObject, clientInfo);
						} else {
							responseJson.put(Constants.error_code, "9999");
							responseJson.put(Constants.message, "登录失败");
						}
					} else {
						responseJson.put(Constants.error_code, "9999");
						responseJson.put(Constants.message, "登录失败");
					}
				}
			} else { //登录失败
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "登录失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("联合登录发生异常", e);
		}
		
		return responseJson.toString();
	}
	
}
