package com.ruyicai.lotserver.service;

import java.net.URLEncoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 提现相关的Service
 * 
 * @author Administrator
 * 
 */
@Service
public class CashService {

	private Logger logger = Logger.getLogger(CashService.class);

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 提现
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String cash(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			//验证密码是否正确,防止自动登录进行提现
			String password = clientInfo.getPassword();
			if (!Tools.isEmpty(password)) {
				boolean verifyPassword = commonUtil.verifyPassword(userNo, password);
				if (!verifyPassword) { //密码错误
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "密码错误");
					return responseJson.toString();
				}
			}
			//验证提现金额(不超过5万)
			String amount = clientInfo.getAmount();
			if (!Tools.isEmpty(amount)&&Integer.parseInt(amount)>5000000) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "提现金额不能超过5万");
				return responseJson.toString();
			}
			
			String type = clientInfo.getType(); //提现类型(1:普通提现;2支付宝提现)
			if (Tools.isEmpty(type)) {
				type = "1";
			}
			
			String bankName = clientInfo.getBankname(); //开户行名称
			if (type!=null&&type.equals("2")) { //如果支付宝提现,bankName传空格，否则会报错
				bankName = " ";
			}

			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo); // 用户编号
			paramStr.append("&balance=" + clientInfo.getAmount()); // 提现金额
			paramStr.append("&bankId=" + Constants.clientBankId); // 银行标识
			paramStr.append("&bankAccount=" + clientInfo.getBankcardno()); // 银行账户
			paramStr.append("&name=" + URLEncoder.encode(clientInfo.getName(), "UTF-8")); // 用户真实姓名
			paramStr.append("&drawType=" + Constants.drawType); // 提现交易类型
			paramStr.append("&drawName=" + Constants.drawName); // 提现费率名称
			paramStr.append("&drawId=" + Constants.drawId); // 费率方案标示
			paramStr.append("&BANKNAME=" + URLEncoder.encode(bankName, "UTF-8")); // 开户行名称
			paramStr.append("&type=" + type); //提现类型(1:普通提现;2支付宝提现)
			String araeaName = clientInfo.getAraeaname(); // 城市名称
			if (!Tools.isEmpty(araeaName)) {
				paramStr.append("&AREANAME=" + URLEncoder.encode(araeaName, "UTF-8")); 
			}

			String url = propertiesUtil.getLotteryUrl() + "taccounts/drawCash";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("提现返回:" + result + ",paramStr=" + paramStr);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "您的提现申请已提交");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "提现失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "提现失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("提现发生异常", e);
		}
		return responseJson.toString();
	}

	/**
	 * 取消提现
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String cancleCash(ClientInfo clientInfo) {
		JSONObject resposeJson = new JSONObject();
		try {
			String cashDetailId = clientInfo.getCashdetailid(); //订单号
			if (Tools.isEmpty(cashDetailId)) { //订单号为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("cashdetailId=" + cashDetailId);

			String url = propertiesUtil.getLotteryUrl() + "taccounts/cancelTcashDetail";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("取消提现返回:" + result + ",paramStr:" + paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					resposeJson.put(Constants.error_code, "0000");
					resposeJson.put(Constants.message, "取消提现成功");
				} else if (errorCode!=null&&errorCode.equals("400007")) {
					resposeJson.put(Constants.error_code, "9999");
					resposeJson.put(Constants.message, "提现进入审核状态,不允许取消");
				} else if (errorCode!=null&&errorCode.equals("400006")) {
					resposeJson.put(Constants.error_code, "9999");
					resposeJson.put(Constants.message, "提现记录为空");
				} else {
					resposeJson.put(Constants.error_code, "9999");
					resposeJson.put(Constants.message, "取消提现失败");
				}
			} else {
				resposeJson.put(Constants.error_code, "9999");
				resposeJson.put(Constants.message, "取消提现失败");
			}
		} catch (Exception e) {
			resposeJson.put(Constants.error_code, "9999");
			resposeJson.put(Constants.message, "系统繁忙");
			logger.error("取消提现发生异常", e);
		}
		return resposeJson.toString();
	}

	/**
	 * 查询提现
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String queryCash(ClientInfo clientInfo) {
		JSONObject resposeJson = new JSONObject();
		try {
			logger.info("该接口已废弃  查询一条提现(queryCash),platform:"+clientInfo.getPlatform()+";softwareVersion:"+clientInfo.getSoftwareVersion()
					+";imei:"+clientInfo.getImei()+";userNo:"+clientInfo.getUserno()+";userName:"+clientInfo.getPhonenum());
			
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);

			String url = propertiesUtil.getLotteryUrl() + "taccounts/queryCashByUserNo";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("查询提现返回:" + result + ",paramStr:" + paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONArray valueArray = fromObject.getJSONArray("value");
					if (valueArray!=null&&valueArray.size()>0) {
						JSONObject cashJson = valueArray.getJSONObject(0);
						String id = cashJson.getString("id");
						String amt = cashJson.getString("amt");
						String name = cashJson.getString("name");
						String state = cashJson.getString("state");
						String bankName = cashJson.getString("bankname");
						String areaName = cashJson.getString("areaname");
						String bankAccount = cashJson.getString("bankaccount");
						
						resposeJson.put(Constants.error_code, "0000");
						resposeJson.put(Constants.message, "查询成功");
						resposeJson.put(Constants.cashDetailId, id);
						resposeJson.put(Constants.amount, amt);
						resposeJson.put(Constants.name, name);
						resposeJson.put(Constants.stat, state);
						resposeJson.put(Constants.bankName, bankName);
						resposeJson.put(Constants.areaName, areaName);
						resposeJson.put(Constants.bankCardNo, bankAccount);
					} else {
						resposeJson.put(Constants.error_code, "0047");
						resposeJson.put(Constants.message, "无记录");
					}
				} else {
					resposeJson.put(Constants.error_code, "0047");
					resposeJson.put(Constants.message, "无记录");
				}
			} else {
				resposeJson.put(Constants.error_code, "0047");
				resposeJson.put(Constants.message, "无记录");
			}
		} catch (RuntimeException e) {
			resposeJson.put(Constants.error_code, "9999");
			resposeJson.put(Constants.message, "系统繁忙");
			logger.error("查询提现发生异常", e);
		}
		resposeJson.put(Constants.allBankName, Constants.allBankName_content);
		return resposeJson.toString();
	}

	/**
	 * 提现记录查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String cashRecord(ClientInfo clientInfo) {
		JSONObject resposeJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; // 总页数
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
				
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&pageIndex=" + clientInfo.getPageindex());
			paramStr.append("&maxResult=" + clientInfo.getMaxresult());

			String url = propertiesUtil.getLotteryUrl()+"taccounts/queryCashByUsernoAndPage";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("提现记录查询返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if (list!=null&&list.size()>0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							totalPage = valueObject.getString("totalPage");
							JSONObject cashObject = list.getJSONObject(i);
							String id = cashObject.getString("id"); // 提现记录ID
							String amt = cashObject.getString("amt"); // 提现金额
							String platTime = cashObject.getString("plattime"); // 提现时间
							String rejectReason = cashObject.getString("rejectreason"); // 驳回原因
							if (Tools.isEmpty(rejectReason)||rejectReason.equals("null")) {
								rejectReason = "";
							}
							String state = cashObject.getString("state"); // 提现状态
							String stateMemo = ""; // 提现状态描述
							if (state != null && state.trim().equals("1")) {
								stateMemo = "待审核";
							} else if (state != null && state.trim().equals("103")) {
								stateMemo = "已审核";
							} else if (state != null && state.trim().equals("104")) {
								stateMemo = "驳回";
							} else if (state != null && state.trim().equals("105")) {
								stateMemo = "成功";
							} else if (state != null && state.trim().equals("106")) {
								stateMemo = "取消";
							}
							
							JSONObject object = new JSONObject();
							object.put(Constants.cashDetailId, id); //提现记录ID
							object.put(Constants.amount, amt); //提现金额
							object.put(Constants.cashTime, DateParseFormatUtil.formatDateTime(platTime)); 
							object.put(Constants.rejectReason, rejectReason); // 驳回原因
							object.put(Constants.stateMemo, stateMemo); // 提现状态描述
							object.put(Constants.state, state); // 状态
							resultArray.add(object);
						}
						resposeJson.put(Constants.error_code, "0000");
						resposeJson.put(Constants.message, "查询成功");
					} else {
						resposeJson.put(Constants.error_code, "0047");
						resposeJson.put(Constants.message, "无记录");
					}
				} else {
					resposeJson.put(Constants.error_code, "0047");
					resposeJson.put(Constants.message, "无记录");
				}
			} else {
				resposeJson.put(Constants.error_code, "0047");
				resposeJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			resposeJson.put(Constants.error_code, "9999");
			resposeJson.put(Constants.message, "系统繁忙");
			logger.error("提现记录查询发生异常", e);
		}
		resposeJson.put("totalPage", totalPage);
		resposeJson.put("result", resultArray);
		return resposeJson.toString();
	}

}
