package com.ruyicai.lotserver.service.query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 账户明细
 * @author Administrator
 *
 */
@Service
public class AccountDetailService {

	private Logger logger = Logger.getLogger(AccountDetailService.class); 
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 账户明细查询新
	 * @param clientInfo
	 * @return
	 */
	public String getAccountDetailNew(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; // 总页数
		String totalAmt = "0"; //交易总金额
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			String beginTime = clientInfo.getStartDate(); //起始时间
			if (Tools.isEmpty(beginTime)) {
				beginTime = "20080808";
			}
			String endTime = clientInfo.getEndDate(); //结束时间
			if (Tools.isEmpty(endTime)) {
				endTime = DateParseFormatUtil.getTodayDate();
			}
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			String transactionType = clientInfo.getTransactionType(); // 交易类型
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("json");
			paramStr.append("&get=Taccountdetail");
			paramStr.append("&userno="+userNo);
			paramStr.append("&beginTime="+beginTime);
			paramStr.append("&endTime="+endTime);
			paramStr.append("&pageIndex="+pageIndex);
			paramStr.append("&maxResult="+maxResult);
			if (transactionType!=null&&transactionType.equals("1")) { //充值
				paramStr.append("&type=" + 2); //银行卡充值
				paramStr.append("&type=" + 3); //平台卡充值
				paramStr.append("&type=" + 10); //点卡充值
			} else if (transactionType!=null&&transactionType.equals("2")) { //支付
				paramStr.append("&type=" + 1); // 投注
			} else if (transactionType!=null&&transactionType.equals("3")) { //派奖
				paramStr.append("&type=" + 6); // 兑奖划款
			} else if (transactionType!=null&&transactionType.equals("4")) { //提现
				paramStr.append("&type=" + 5); // 提现
			}
			
			String url = propertiesUtil.getLotteryUrl() + "taccountdetails?";
			String result = HttpUtil.sendRequestByGet(url+paramStr.toString(), true);
			//logger.info("根据用户编号查询账户明细(新)返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					totalAmt = valueObject.getString("value"); //交易总金额
					JSONArray list = valueObject.getJSONArray("list");
					if (list!=null&&list.size()>0) {
						totalPage = valueObject.getString("totalPage"); //总页数
						for (int i = 0; i < list.size(); i++) {
							JSONObject accountDetailObject = list.getJSONObject(i);
							String amt = accountDetailObject.getString("amt"); //变动金额
							String freezeAmt = accountDetailObject.getString("freezeamt"); //变动的冻结金额
							String drawAmt = accountDetailObject.getString("drawamt"); //可提现变动金额
							String blsign = accountDetailObject.getString("blsign"); //进出帐标识
							String ttransactionType = accountDetailObject.getString("ttransactiontype"); //交易类型
							String memo = accountDetailObject.getString("memo"); //描述
							String balance = accountDetailObject.getString("balance"); //变动后账户余额
							String drawBalance = accountDetailObject.getString("drawbalance"); //变动后可提现余额
							String platTime = accountDetailObject.getString("plattime"); //变动时间
							
							JSONObject object = new JSONObject();
							object.put("amt", (amt!=null&&amt.equals("0"))?freezeAmt:amt); // 变动金额
							object.put("drawAmt", drawAmt); // 可提现变动金额
							object.put("blsign", blsign); // 进出帐标识(-1：出帐，1：进账)
							object.put("ttransactionType", ttransactionType); // 交易类型
							object.put("memo", memo); // 描述
							object.put("balance", balance); // 变动后账户余额
							object.put("drawamtBalance", drawBalance); // 变动后可提现余额
							object.put("platTime", DateParseFormatUtil.formatDateTime(platTime)); // 变动时间
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, "0000");
						responseJson.put(Constants.message, "查询成功");
					} else {
						responseJson.put(Constants.error_code, "0047");
						responseJson.put(Constants.message, "无记录");
					}
				} else {
					responseJson.put(Constants.error_code, "0047");
					responseJson.put(Constants.message, "无记录");
				}
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("账户明细查询新发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("totalAmt", totalAmt);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 账户明细查询旧
	 * @param clientInfo
	 * @return
	 */
	public String getAccountDetail(ClientInfo clientInfo) {
		JSONArray responseArray = new JSONArray();
		try {
			logger.info("该接口已废弃  账户明细查询旧(getAccountDetail),platform:"+clientInfo.getPlatform()+";softwareVersion:"+clientInfo.getSoftwareVersion()
					+";imei:"+clientInfo.getImei()+";userNo:"+clientInfo.getUserno()+";userName:"+clientInfo.getPhonenum());
			
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String beginTime = clientInfo.getStartDate(); //起始时间
			if (Tools.isEmpty(beginTime)) {
				beginTime = DateParseFormatUtil.getPreOneMonthDate();
			}
			String endTime = clientInfo.getEndDate(); //结束时间
			if (Tools.isEmpty(endTime)) {
				beginTime = DateParseFormatUtil.getTodayDate();
			}
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("json");
			paramStr.append("&get=Taccountdetail");
			paramStr.append("&userno=" + userNo);
			paramStr.append("&beginTime=" + beginTime);
			paramStr.append("&endTime=" + endTime);
			paramStr.append("&pageIndex=" + pageIndex);
			
			String urlStr = propertiesUtil.getLotteryUrl() + "taccountdetails?";
			String result = HttpUtil.sendRequestByGet(urlStr + paramStr.toString(), true);
			//logger.info("账户明细查询旧返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
					&&fromObject.getString("errorCode").equals("0")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				JSONArray acountDetailArray = valueObject.getJSONArray("list");
				if (acountDetailArray!=null&&acountDetailArray.size()>0) {
					String totalPage = valueObject.getString("totalPage");
					for (int i = 0; i < acountDetailArray.size(); i++) {
						JSONObject aountJson = acountDetailArray.getJSONObject(i);
						
						JSONObject object = new JSONObject();
						object.put(Constants.error_code, "0000");
						String memo = aountJson.getString("memo").trim();
						object.put("type", getTypeByMemo(memo)); //交易类型
						//type中返回汉字，是支付，还是充值
						if (clientInfo.getType().equals("0")&&(!"合买金额冻结".equals(memo)&&!"null".equals(memo)
										&&!"用户投注冻结".equals(memo)&&!memo.equals("投注冻结")&&!memo.equals("投注解冻")
										&&!"用户投注解冻".equals(memo)&&!"合买金额解冻".equals(memo))) { //查询所有
							object.put("amt", aountJson.getString("amt")); //变动金额
							object.put("drawamt", aountJson.getString("drawamt")); //可提现变动金额
							object.put("blsign", aountJson.getString("blsign")); //进出帐标识
							object.put("memo", aountJson.getString("memo")); //账户科目明细描述
							object.put("balance", aountJson.getString("balance")); //变动后账户余额
							object.put("drawamtBalance", aountJson.getString("drawbalance")); //变动后可提现余额
							object.put("plattime", DateParseFormatUtil.formatDateTime(aountJson.getString("plattime"))); //变动时间
							object.put("allNum", String.valueOf(totalPage)); //分页记录数
							responseArray.add(object);
						} else if (clientInfo.getType().equals("1")&&(memo.equals("银行卡充值")||memo.equals("平台卡充值")
										||memo.equals("大客户充值")||memo.equals("点卡充值")||memo.equals("充值")||memo.equals("意彩卡充值")
										||memo.equals("如意彩卡充值"))) {
							object.put("amt", aountJson.getString("amt")); //变动金额
							object.put("drawamt", aountJson.getString("drawamt")); //可提现变动金额
							object.put("blsign", aountJson.getString("blsign")); //进出帐标识
							object.put("memo", aountJson.getString("memo")); //账户科目明细描述
							object.put("balance", aountJson.getString("balance")); //变动后账户余额
							object.put("drawamtBalance", aountJson.getString("drawbalance")); //变动后可提现余额
							object.put("plattime", DateParseFormatUtil.formatDateTime(aountJson.getString("plattime"))); //变动时间
							object.put("allNum", String.valueOf(totalPage)); // 分页记录数
							responseArray.add(object);
						} else if (clientInfo.getType().equals("2")&&(memo.equals("用户提现扣手续费")||memo.equals("用户追号投注扣款")
										||memo.equals("点卡充值扣除手续费")||memo.equals("彩票赠送扣款")||memo.equals("用户投注扣款")
										||memo.equals("提现手续费")||memo.equals("账户扣款"))) {
							object.put("amt", aountJson.getString("amt")); // 变动金额
							object.put("drawamt", aountJson.getString("drawamt")); // 可提现变动金额
							object.put("blsign", aountJson.getString("blsign")); // 进出帐标识
							object.put("memo", aountJson.getString("memo")); // 账户科目明细描述
							object.put("balance", aountJson.getString("balance")); // 变动后账户余额
							object.put("drawamtBalance", aountJson.getString("drawbalance"));// 变动后可提现余额
							object.put("plattime", DateParseFormatUtil.formatDateTime(aountJson.getString("plattime")));// 变动时间
							object.put("allNum", String.valueOf(totalPage)); // 分页记录数
							responseArray.add(object);
						} else if (clientInfo.getType().equals("3")&&(memo.equals("用户兑奖划款")||memo.equals("体彩兑奖划款") 
								||memo.equals("奖金"))) {
							object.put("amt", aountJson.getString("amt")); //变动金额
							object.put("drawamt", aountJson.getString("drawamt")); //可提现变动金额
							object.put("blsign", aountJson.getString("blsign")); //进出帐标识
							object.put("memo", aountJson.getString("memo")); //账户科目明细描述
							object.put("balance", aountJson.getString("balance")); //变动后账户余额
							object.put("drawamtBalance", aountJson.getString("drawbalance")); //变动后可提现余额
							object.put("plattime", DateParseFormatUtil.formatDateTime(aountJson.getString("plattime"))); //变动时间
							object.put("allNum", String.valueOf(totalPage)); //分页记录数
							responseArray.add(object);
						} else if (clientInfo.getType().equals("4")&&(memo.equals("提现")||memo.equals("用户提现扣提现金额")
										||memo.equals("用户提现扣提现金额")||memo.equals("提现取消退款"))) {
							object.put("amt", aountJson.getString("amt")); //变动金额
							object.put("drawamt", aountJson.getString("drawamt")); //可提现变动金额
							object.put("blsign", aountJson.getString("blsign")); //进出帐标识
							object.put("memo", aountJson.getString("memo")); //账户科目明细描述
							object.put("balance", aountJson.getString("balance")); //变动后账户余额
							object.put("drawamtBalance", aountJson.getString("drawbalance")); //变动后可提现余额
							object.put("plattime", DateParseFormatUtil.formatDateTime(aountJson.getString("plattime"))); //变动时间
							object.put("allNum", String.valueOf(totalPage)); //分页记录数
							responseArray.add(object);
						}
					}
					if (responseArray.size() <= 0) {
						JSONObject object = new JSONObject();
						object.put("error_code", "0047");
						object.put(Constants.message, "无记录");
						responseArray.add(object);
					}
				} else {
					JSONObject object = new JSONObject();
					object.put(Constants.error_code, "0047");
					object.put(Constants.message, "无记录");
					responseArray.add(object);
				}
			} else {
				JSONObject object = new JSONObject();
				object.put(Constants.error_code, "0047");
				object.put(Constants.message, "无记录");
				responseArray.add(object);
			}
		} catch (Exception e) {
			JSONObject object = new JSONObject();
			object.put(Constants.error_code, "9999");
			object.put(Constants.message, "系统繁忙");
			responseArray.add(object);
			logger.error("账户明细查询旧发生异常", e);
		}
		return responseArray.toString();
	}
	
	/**
	 * 根据备注获取type
	 * @param str
	 * @return
	 */
	public String getTypeByMemo(String str) {
		String strTemp = "";
		if (str.equals("点卡充值")||str.equals("银行卡充值")||str.equals("充值")||str.equals("意彩卡充值")||str.equals("如意彩卡充值")) {
			strTemp = "充值";
		} else if (str.equals("用户提现扣手续费")||str.equals("用户追号投注扣款")||str.equals("点卡充值扣除手续费")||str.equals("彩票赠送扣款")
				||str.equals("用户投注扣款")||str.equals("提现手续费")|| str.equals("账户扣款")) {
			strTemp = "支付";
		} else if (str.equals("用户兑奖划款")||str.equals("体彩兑奖划款")||str.equals("奖金")) {
			strTemp = "派奖";
		} else if (str.equals("用户提现扣提现金额")||str.equals("提现")||str.equals("用户提现扣提现金额")||str.equals("提现取消退款")) {
			strTemp = "提现";
		} else if (str.equals("用户账户解冻")||str.indexOf("投注解冻")!=-1||!"用户投注解冻".equals(str)) {
			strTemp = "账户解冻";
		} else if (str.equals("用户账户冻结")||"用户投注冻结".equals(str)||str.indexOf("投注冻结") != -1) {
			strTemp = "账户冻结";
		}
		return strTemp;
	}
	
}
