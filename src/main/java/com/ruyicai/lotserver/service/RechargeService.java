package com.ruyicai.lotserver.service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.domain.ChargeConfig;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.VerifyUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 充值相关的Service
 * @author Administrator
 *
 */
@Service
public class RechargeService {

	private Logger logger = Logger.getLogger(RechargeService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonService commonService;

	/**
	 * DNA充值
	 * @param clientInfo
	 * @return
	 */
	public String dnaCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			String cardNo = clientInfo.getCardno(); //卡号
			if (Tools.isEmpty(cardNo)) {
				logger.info("DNA充值卡号为空"+clientInfo.getPhonenum());
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "卡号为空");
				return responseJson.toString();
			}
			
			String isWhite = clientInfo.getIswhite(); //是否是白名单(true:是;false:否)
			String certId = clientInfo.getCertid(); //身份证
			if (isWhite!=null&&isWhite.equals("false")
					&&(Tools.isEmpty(certId)||!VerifyUtil.isCertId(certId))) { //黑名单时身份证验证格式是否正确
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "身份证格式错误");
				return responseJson.toString();
			}
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "dna001");
			paramJson.put("expand", getExpand(clientInfo));
			
			String url = propertiesUtil.getChargeCenterUrl()+"dnacharge!dnaBankCharge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("DNA充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("00A3")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "充值处理中,电话确认密码");
				} else if (errorCode!=null&&errorCode.equals("000019")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "银行卡号与绑定记录不一致");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, fromObject.has("remark")?fromObject.getString("remark"):"充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("DNA充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 手机充值卡充值
	 * @param clientInfo
	 * @return
	 */
	public String phoneCardCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "gyj001");
			String cardNo = clientInfo.getCardno().replaceAll(" ", ""); //卡号
			paramJson.put("card_no", cardNo);
			String cardPwd = clientInfo.getCardpwd().replaceAll(" ", ""); //密码
			paramJson.put("card_pwd", cardPwd);
			paramJson.put("totalAmount", clientInfo.getAmount().trim());
			
			String url = propertiesUtil.getChargeCenterUrl()+"charge!nineteenpayCharge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("手机充值卡充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "您的充值已提交,请耐心等待到账");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("手机充值卡充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 手机银行充值
	 * @param clientInfo
	 * @return
	 */
	public String phoneBankCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "y00003");
			
			String url = propertiesUtil.getChargeCenterUrl() + "charge!yeepayWapBankCharge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("手机银行充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "请求成功");
					
					responseJson.put("transactionId", fromObject.getString("transation_id")); //交易号
					responseJson.put("reqestUrl", fromObject.getString("requrl")); //请求地址
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "请求失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "请求失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("手机银行充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 游戏点卡充值
	 * @param clientInfo
	 * @return
	 */
	public String gameCardCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "y00004");
			paramJson.put("card_no", clientInfo.getCardno().trim());
			paramJson.put("card_pwd", clientInfo.getCardpwd().trim());
			paramJson.put("totalAmount", clientInfo.getAmount().trim());
			
			String url = propertiesUtil.getChargeCenterUrl()+"charge!yeePayWebCardCharge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("游戏点卡充值返回结果:"+result+",userNo:"+userNo);
			JSONObject fromObject = JSONObject.fromObject(result);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "充值受理成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("游戏点卡充值发生异常", e);
		}
		return responseJson.toString();
	}

	/**
	 * 支付宝充值
	 * @param clientInfo
	 * @return
	 */
	public String zfbCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "zfb001");
			//支付宝类型(1：支付宝web支付2：支付宝语音支付3、支付宝wap浏览器支付4、支付宝wap非浏览器支付5、支付宝手机安全支付)
			String bankAccount = clientInfo.getBankAccount();
			if (Tools.isEmpty(bankAccount)) {
				bankAccount = "4";
			}
			paramJson.put("bankaccount", bankAccount);
			
			String url = propertiesUtil.getChargeCenterUrl() + "charge!zfbWapCharge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("支付宝充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "生成交易记录成功");
					
					responseJson.put(Constants.transation_id, fromObject.getString(Constants.transation_id));
					responseJson.put("return_url", fromObject.getString("requrl"));
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("支付宝充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 联通华建(银联在线支付)
	 * @param clientInfo
	 * @return
	 */
	public String lthjCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "lhj001");
			
			String url = propertiesUtil.getChargeCenterUrl()+"lthjcharge!charge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("银联在线支付充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "充值受理成功");
					
					responseJson.put("value", fromObject.getString("value"));
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("联通华建(银联在线支付)发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 支付宝安全支付
	 * @param clientInfo
	 * @return
	 */
	public String zfbSecurityCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "zfb001");
			//支付宝类型(1：支付宝web支付2：支付宝语音支付3、支付宝wap浏览器支付4、支付宝wap非浏览器支付5、支付宝手机安全支付)
			paramJson.put("bankaccount", "5");
			
			String url = propertiesUtil.getChargeCenterUrl() + "alipaysecuritycharge!charge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("支付宝安全支付返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "充值受理成功");
					
					responseJson.put("value", fromObject.getString("sign_string"));
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("支付宝安全支付发生异常", e);
		}
		return responseJson.toString();
	}

	/**
	 * 银行充值
	 * @return
	 */
	public String bankCharge() {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		try {
			StringBuilder builder1 = new StringBuilder(" where");
			List<Object> params1 = new ArrayList<Object>();
			
			builder1.append(" o.state='1' group by o.bankname ");
			List<ChargeConfig> list1 = ChargeConfig.getList(builder1.toString(), "", params1);
			if (list1!=null&&list1.size()>0) {
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
				for (ChargeConfig chargeConfig : list1) {
					JSONObject object = new JSONObject();
					object.put(Constants.bankName, chargeConfig.getBankname()); //银行名称
					
					StringBuilder builder2 = new StringBuilder(" where");
					List<Object> params2 = new ArrayList<Object>();
					
					builder2.append(" o.bankname=? and");
					params2.add(chargeConfig.getBankname());
					
					builder2.append(" o.state='1' group by o.cardtype");
					List<ChargeConfig> list2 = ChargeConfig.getList(builder2.toString(), "", params2);
					if (list2!=null&&list2.size()>0) {
						for (ChargeConfig chargeConfig2 : list2) {
							object.put(chargeConfig2.getCardtype(), chargeConfig2.getSupport());
						}
					}
					resultArray.add(object);
				}
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
			
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("银行充值发生异常", e);
		}
		
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 代理充值
	 * @param clientInfo
	 * @return
	 */
	public String agencyCharge(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			// 验证密码是否正确
			String password = clientInfo.getPassword();
			boolean verifyPassword = commonUtil.verifyPassword(userNo, password);
			if (!verifyPassword) { //密码错误
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "密码错误");
				return responseJson.toString();
			}
			//获取对方的userNo
			String to_mobile_code = clientInfo.getTo_mobile_code();
			String toUserNo = commonService.getUserNoByPhoneNum(to_mobile_code);
			if (Tools.isEmpty(toUserNo)) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "转账用户不存在");
				return responseJson.toString();
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("fromuserno=" + userNo);
			paramStr.append("&touserno=" + toUserNo);
			paramStr.append("&amt=" + clientInfo.getAmount());
			
			String url = propertiesUtil.getLotteryUrl()+"taccounts/transfercharge";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("代理充值返回结果:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "转账成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "转账失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "转账失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("代理充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 拉卡拉充值
	 * @param clientInfo
	 * @return
	 */
	public String lakalaCharge(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			JSONObject paramJson = getParamJson(clientInfo, continueBetType, orderId);
			paramJson.put("userno", userNo); // 用户编号
			paramJson.put("bankid", "lkl001");
			
			String url = propertiesUtil.getChargeCenterUrl()+"lakala!charge?jsonString=";
			String result = HttpUtil.sendRequestByGet(url + URLEncoder.encode(paramJson.toString(), "UTF-8"), true);
			logger.info("拉卡拉充值返回结果:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { //返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString(Constants.error_code);
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "充值受理成功");
					
					responseJson.put("url", fromObject.getString("url"));
					responseJson.put("notifyUrl", fromObject.getString("notifyurl"));
					responseJson.put("merId", fromObject.getString("MERID"));
					responseJson.put("amount", fromObject.getString("AMOUNT"));
					responseJson.put("productName", fromObject.getString("PRODUCTNAME"));
					responseJson.put("orderId", fromObject.getString("ORDERID"));
					responseJson.put("randNum", fromObject.getString("RANDNUM"));
					responseJson.put("desc", fromObject.getString("DESC"));
					responseJson.put("reqType", fromObject.getString("reqType"));
					responseJson.put("ver", fromObject.getString("VER"));
					responseJson.put("minCode", fromObject.getString("MINCODE"));
					responseJson.put("macType", fromObject.getString("MACTYPE"));
					responseJson.put("expiredTime", fromObject.getString("EXPIREDTIME"));
					responseJson.put("mac", fromObject.getString("MAC"));
					responseJson.put("chargeTime", fromObject.getString("chargetime"));
					responseJson.put("androidDownloadUrl", fromObject.getString("androidurl"));
					responseJson.put("iphoneDownloadUrl", fromObject.getString("iphoneurl"));
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "充值失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "充值失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("拉卡拉充值发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 获取扩展参数
	 * @param clientInfo
	 * @return
	 */
	public String getExpand(ClientInfo clientInfo) {
		StringBuffer expandBuffer = new StringBuffer("");
		try {
			String phonenum = clientInfo.getPhonenum(); //接听电话(为了处理塞班客户端DNA充值时如果已绑定没有传接听电话)
			if (Tools.isEmpty(phonenum)) { //如果客户端传过来的phonenum为空就从绑定信息里取
				String queryDNABind = lotteryCommonService.queryDNABind(clientInfo.getUserno());
				if (!Tools.isEmpty(queryDNABind)) {
					JSONObject fromObject = JSONObject.fromObject(queryDNABind);
					if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
							&&fromObject.getString("errorCode").equals("0")&&!fromObject.getString("value").equals("null")) {
						JSONObject valueJson = fromObject.getJSONObject("value");
						if (valueJson!=null&&!Tools.isEmpty(valueJson.getString("mobileid"))
								&&!valueJson.getString("mobileid").trim().equals("null")) {
							phonenum = valueJson.getString("mobileid");
						}
					}
				}
			}
			if (Tools.isEmpty(phonenum)||phonenum.trim().equals("null")) { //如果未绑定就取用户的mobileid，再取userName
				String queryUsersByUserNo = lotteryCommonService.queryUsersByUserNo(clientInfo.getUserno());
				if (!Tools.isEmpty(queryUsersByUserNo)) {
					JSONObject fromObject = JSONObject.fromObject(queryUsersByUserNo);
					if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
							&&fromObject.getString("errorCode").equals("0")) {
						JSONObject valueObject = fromObject.getJSONObject("value");
						if (valueObject!=null&&!Tools.isEmpty(valueObject.getString("mobileid"))
								&&!valueObject.getString("mobileid").trim().equals("null")) {
							phonenum = valueObject.getString("mobileid");
						} else {
							if (valueObject!=null&&valueObject.has("userName")) {
								phonenum = valueObject.getString("userName");
							}
						}
					}
				}
			}
			
			expandBuffer.append(Tools.replaceSpecialCharacter(clientInfo.getName())).append(",");
			expandBuffer.append(Tools.replaceSpecialCharacter(clientInfo.getCertid())).append(",");
			expandBuffer.append(Tools.replaceSpecialCharacter(clientInfo.getBankaddress())).append(",");
			expandBuffer.append(Tools.replaceSpecialCharacter(clientInfo.getAddressname())).append(",");
			expandBuffer.append(phonenum.trim()).append(",");
			expandBuffer.append(clientInfo.getIswhite().trim()).append(",");
			expandBuffer.append(clientInfo.getBankname());
			
		} catch (Exception e) {
			logger.error("获取DNA扩展参数发生异常", e);
		}
		return expandBuffer.toString();
	}

	/**
	 * 获取充值公共参数
	 * @param clientInfo
	 * @return
	 */
	public JSONObject getParamJson(ClientInfo clientInfo, String continueBetType, String orderId) {
		JSONObject paramJson = new JSONObject();
		String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
		
		paramJson.put("amt", clientInfo.getAmount().trim()); // 金额
		paramJson.put("cardno", clientInfo.getCardno().trim()); // 银行卡号
		paramJson.put("accesstype", Constants.accessType); // C 客户端
		paramJson.put("cardtype", clientInfo.getCardtype().trim()); //卡类型
		paramJson.put("paytype", clientInfo.getCardtype().trim());
		paramJson.put("continuebettype", continueBetType); //交易方式(如投注、追号、合买)
		paramJson.put("orderid", orderId); //订单号
		//paramJson.put("channel", clientInfo.getCoopId().trim()); // 渠道号
		paramJson.put("channel", newCoopId); // 渠道号
		
		return paramJson;
	}
	
}
