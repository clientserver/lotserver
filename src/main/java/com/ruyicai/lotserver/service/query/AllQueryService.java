package com.ruyicai.lotserver.service.query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.domain.LotTypeInfo;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.ActivityCenterUtil;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.CaseLotUtil;
import com.ruyicai.lotserver.util.lot.LotTypeInfoUtil;

/**
 * 相关查询Service
 * 
 * @author Administrator
 * 
 */
@Service
public class AllQueryService {

	private Logger logger = Logger.getLogger(AllQueryService.class);

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private ActivityCenterUtil activityCenterUtil;
	
	/**
	 * 余额查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getBalance(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String result = lotteryCommonService.queryUserBalance(userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");

					String balance = valueObject.getString("balance");// 余额
					String drawbalance = valueObject.getString("drawbalance");// 可提现余额
					String freezebalance = valueObject.getString("freezebalance");// 用户冻结金额
					String bet_balance = String.valueOf(new BigDecimal(balance).subtract(new BigDecimal(freezebalance))) ; // 投注金额=账户余额减去冻结金额
					//计算减去冻结金额后的可提现余额
					BigDecimal draw_balance = BigDecimal.ZERO;
					BigDecimal notDrawBalance = new BigDecimal(balance).subtract(new BigDecimal(drawbalance)); //不可提现余额
					if (notDrawBalance.compareTo(new BigDecimal(freezebalance))==1
							||notDrawBalance.compareTo(new BigDecimal(freezebalance))==0) { //如果不可提现余额>=冻结金额则计算后的可提现余额=数据库中的可提现余额
						draw_balance =  new BigDecimal(drawbalance);
					} else { //如果不可提现余额<冻结金额则计算后的可提现余额=数据库中的可提现余额-(冻结金额-不可提现余额)
						draw_balance = new BigDecimal(drawbalance).subtract((new BigDecimal(freezebalance).subtract(notDrawBalance)));
					}

					responseJson.put(Constants.balance, (new BigDecimal(balance).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))+"元");
					responseJson.put(Constants.drawBalance, (draw_balance.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))+"元");
					responseJson.put(Constants.freezeBalance, (new BigDecimal(freezebalance).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))+"元");
					responseJson.put(Constants.betBalance, (new BigDecimal(bet_balance).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))+"元");
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "查询失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "查询失败");
			logger.error("余额查询发生异常", e);
		}
		return responseJson.toString();
	}

	/**
	 * 中奖排行
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getPrizeRank(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();

		JSONArray weekArray = new JSONArray(); // 周排行
		JSONArray monthArray = new JSONArray(); // 月排行
		JSONArray totalArray = new JSONArray(); // 总排行
		
		try {
			// 周排行
			JSONArray weekValueArray = cacheCommonUtil.getPrizeBankValueArray("week");
			if (weekValueArray!=null&&weekValueArray.size()>0) {
				for (int i = 0; i < weekValueArray.size(); i++) {
					JSONObject object = new JSONObject();
					JSONObject weekObject = weekValueArray.getJSONObject(i);
					String nickname = weekObject.getString("nickname");
					if (Tools.isEmpty(nickname)||nickname.trim().equals("null")) { //昵称为空
						String userName = weekObject.getString("username");
						if (Tools.isEmpty(userName)||userName.trim().equals("null")) { //用户名为空
							String mobileId = weekObject.getString("mobileId");
							object.put("name", CaseLotUtil.getNickname(mobileId));
						} else {
							object.put("name", CaseLotUtil.getNickname(userName));
						}
					} else {
						object.put("name", CaseLotUtil.getNickname(nickname));
					}
					object.put("prizeAmt", weekObject.getString("prizeAmt"));
					weekArray.add(object);
				}
			}
			
			// 月排行
			JSONArray monthValueArray = cacheCommonUtil.getPrizeBankValueArray("month");
			if (monthValueArray != null && monthValueArray.size() > 0) {
				for (int i = 0; i < monthValueArray.size(); i++) {
					JSONObject object = new JSONObject();
					JSONObject monthObject = monthValueArray.getJSONObject(i);
					String nickname = monthObject.getString("nickname");
					if (Tools.isEmpty(nickname)||nickname.trim().equals("null")) { //昵称为空
						String userName = monthObject.getString("username");
						if (Tools.isEmpty(userName)||userName.trim().equals("null")) { //用户名为空
							String mobileId = monthObject.getString("mobileId");
							object.put("name", CaseLotUtil.getNickname(mobileId));
						} else {
							object.put("name", CaseLotUtil.getNickname(userName));
						}
					} else {
						object.put("name", CaseLotUtil.getNickname(nickname));
					}
					object.put("prizeAmt", monthObject.getString("prizeAmt"));
					monthArray.add(object);
				}
			}
			
			// 总排行
			JSONArray totalValueArray = cacheCommonUtil.getPrizeBankValueArray("total");
			if (totalValueArray != null && totalValueArray.size() > 0) {
				for (int i = 0; i < totalValueArray.size(); i++) {
					JSONObject object = new JSONObject();
					JSONObject totalObject = totalValueArray.getJSONObject(i);
					String nickname = totalObject.getString("nickname");
					if (Tools.isEmpty(nickname)||nickname.trim().equals("null")) { //昵称为空
						String userName = totalObject.getString("username");
						if (Tools.isEmpty(userName)||userName.trim().equals("null")) { //用户名为空
							String mobileId = totalObject.getString("mobileId");
							object.put("name", CaseLotUtil.getNickname(mobileId));
						} else {
							object.put("name", CaseLotUtil.getNickname(userName));
						}
					} else {
						object.put("name", CaseLotUtil.getNickname(nickname));
					}
					object.put("prizeAmt", totalObject.getString("prizeAmt"));
					totalArray.add(object);
				}
			}
		} catch (Exception e) {
			logger.error("中奖排行发生异常", e);
		}
		
		responseJson.put(Constants.error_code, ErrorCode.success.value());
		responseJson.put(Constants.message, "查询成功");
		responseJson.put("weekArray", weekArray);
		responseJson.put("monthArray", monthArray);
		responseJson.put("totalArray", totalArray);
		return responseJson.toString();
	}

	/**
	 * 查询DNA绑定记录
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getDNABind(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);

			String url = propertiesUtil.getLotteryUrl() + "taccounts/getDNABinding";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("查询DNA绑定记录返回:" + result + ",paramStr:" + paramStr.toString());
			if (Tools.isEmpty(result)) { // 返回结果为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}  
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					String valueString = fromObject.getString("value");
					if (!Tools.isEmpty(valueString)&&!valueString.equals("null")) {
						JSONObject valueJson = JSONObject.fromObject(valueString);
						String name = valueJson.getString("name"); //姓名
						String state = valueJson.getString("state"); //绑定状态
						String bankCardNo = valueJson.getString("bankcardno"); //银行卡号
						String certId = valueJson.getString("certid"); //身份证号
						String bindTime = valueJson.getString("bindtime"); //绑定时间
						String certIdAddress = valueJson.getString("certidaddress"); //户口所在地
						String bankAddress = valueJson.getString("bankaddress"); //开户行
						String mobileId = valueJson.getString("mobileid"); //手机号
						String bankName = valueJson.getString("bankname"); //银行名称
						if (Tools.isEmpty(bankName)||bankName.equals("null")) {
							bankName = "";
						}
						
						responseJson.put(Constants.name, name); //姓名
						responseJson.put(Constants.bindState, state); //绑定状态
						responseJson.put(Constants.bankCardNo, bankCardNo); //银行卡号
						responseJson.put(Constants.certId, certId); //身份证号
						responseJson.put(Constants.bindDate, bindTime); //绑定时间
						responseJson.put(Constants.addressName, certIdAddress); //户口所在地
						responseJson.put(Constants.bankAddress, bankAddress); //开户行
						responseJson.put(Constants.phonenum, mobileId); //手机号
						responseJson.put(Constants.bankName, bankName); // 银行名称
						
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无绑定记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询DNA绑定记录发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 购买大厅
	 * @param clientInfo
	 * @return
	 */
	public String buyCenter(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		
		try {
			JSONObject qlcResultJson = new JSONObject(); //七乐彩
			JSONObject dddResultJson = new JSONObject(); //福彩3D
			JSONObject ssqResultJson = new JSONObject(); //双色球
			
			JSONObject dltResultJson = new JSONObject(); //大乐透
			JSONObject plsResultJson = new JSONObject(); //排列三
			JSONObject plwResultJson = new JSONObject(); //排列五
			JSONObject qxcResultJson = new JSONObject(); //七星彩
			JSONObject tt_fResultJson = new JSONObject(); //22选5
			
			JSONObject sscResultJson = new JSONObject(); //时时彩
			JSONObject oo_fResultJson = new JSONObject(); //江西11选5
			JSONObject oo_ydjResultJson = new JSONObject(); //11运夺金
			JSONObject gdoo_fResultJson = new JSONObject(); //广东11选5
			JSONObject gdh_tResultJson = new JSONObject(); //广东快乐十分
			
			JSONObject zcResultJson = new JSONObject(); //足彩
			
			JSONObject jc_zResultJson = new JSONObject(); //竟彩足球
			
			JSONObject jc_lResultJson = new JSONObject(); //竟彩篮球
			
			JSONObject valueObject = cacheCommonUtil.getAllCurrentBatchCodeValueObject(); //获取所有彩种的期号
			List<LotTypeInfo> addAwardLotNos = LotTypeInfoUtil.getAddAwardLotNos(); //获取加奖的彩种
			List<LotTypeInfo> saleLotNos = LotTypeInfoUtil.getSaleLotNos(); //获取销售的彩种
			//七乐彩
			String isTodayOpenPrizeQlc = "false"; //是否今日开奖
			JSONObject qlcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.QLC.lotNo());
			if (qlcObject!=null) {
				String openTime = qlcObject.getString("opentime"); //开奖日期
				isTodayOpenPrizeQlc = LotTypeInfoUtil.isTodayOpenPrize(openTime); 
			}
			qlcResultJson.put("isTodayOpenPrize", isTodayOpenPrizeQlc); //是否今日开奖
			//是否加奖、销售
			List<String> qlcLotNos = new ArrayList<String>();
			qlcLotNos.add(LotType.QLC.lotNo());
			qlcResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, qlcLotNos));
			qlcResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, qlcLotNos));
			
			//福彩3D
			//是否加奖、销售
			List<String> dddLotNos = new ArrayList<String>();
			dddLotNos.add(LotType.DDD.lotNo());
			dddResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, dddLotNos));
			dddResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, dddLotNos));
			
			//双色球
			String isTodayOpenPrizeSsq = "false"; //是否今日开奖
			JSONObject ssqObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.SSQ.lotNo());
			if (ssqObject!=null) {
				String openTime = ssqObject.getString("opentime"); //开奖日期
				isTodayOpenPrizeSsq = LotTypeInfoUtil.isTodayOpenPrize(openTime);
			}
			ssqResultJson.put("isTodayOpenPrize", isTodayOpenPrizeSsq); //是否今日开奖
			//是否加奖、销售
			List<String> ssqLotNos = new ArrayList<String>();
			ssqLotNos.add(LotType.SSQ.lotNo());
			ssqResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, ssqLotNos));
			ssqResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, ssqLotNos));
			
			//大乐透
			String isTodayOpenPrizeDlt = "false"; //是否今日开奖
			JSONObject dltObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.DLT.lotNo());
			if (dltObject!=null) {
				String openTime = dltObject.getString("opentime"); //开奖日期
				isTodayOpenPrizeDlt = LotTypeInfoUtil.isTodayOpenPrize(openTime); //是否今日开奖
			}
			dltResultJson.put("isTodayOpenPrize", isTodayOpenPrizeDlt); //是否今日开奖
			//是否加奖
			List<String> dltLotNos = new ArrayList<String>();
			dltLotNos.add(LotType.DLT.lotNo());
			dltResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, dltLotNos));
			dltResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, dltLotNos));
			
			//排列三
			//是否加奖
			List<String> plsLotNos = new ArrayList<String>();
			plsLotNos.add(LotType.PLS.lotNo());
			plsResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, plsLotNos));
			plsResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, plsLotNos));
			
			//排列五
			//是否加奖
			List<String> plwLotNos = new ArrayList<String>();
			plwLotNos.add(LotType.PLW.lotNo());
			plwResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, plwLotNos));
			plwResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, plwLotNos));
			
			//七星彩
			//是否加奖
			List<String> qxcLotNos = new ArrayList<String>();
			qxcLotNos.add(LotType.QXC.lotNo());
			qxcResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, qxcLotNos));
			qxcResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, qxcLotNos));
			
			//22选5
			//是否加奖
			List<String> tt_fLotNos = new ArrayList<String>();
			tt_fLotNos.add(LotType.TT_F.lotNo());
			tt_fResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, tt_fLotNos));
			tt_fResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, tt_fLotNos));
			
			//时时彩
			//是否加奖
			List<String> sscLotNos = new ArrayList<String>();
			sscLotNos.add(LotType.SSC.lotNo());
			sscResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, sscLotNos));
			sscResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, sscLotNos));
			
			//江西11选5
			//是否加奖
			List<String> oo_fLotNos = new ArrayList<String>();
			oo_fLotNos.add(LotType.OO_F.lotNo());
			oo_fResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, oo_fLotNos));
			oo_fResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, oo_fLotNos));
			
			//11运夺金
			//是否加奖
			List<String> oo_ydjLotNos = new ArrayList<String>();
			oo_ydjLotNos.add(LotType.OO_YDJ.lotNo());
			oo_ydjResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, oo_ydjLotNos));
			oo_ydjResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, oo_ydjLotNos));
			
			//广东11选5
			//是否加奖
			List<String> gdoo_fLotNos = new ArrayList<String>();
			gdoo_fLotNos.add(LotType.GDOO_F.lotNo());
			gdoo_fResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, gdoo_fLotNos));
			gdoo_fResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, gdoo_fLotNos));
			
			//广东快乐十分
			//是否加奖
			List<String> gdh_tLotNos = new ArrayList<String>();
			gdh_tLotNos.add(LotType.GDH_T.lotNo());
			gdh_tResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, gdh_tLotNos));
			gdh_tResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, gdh_tLotNos));
			
			//足彩
			//是否加奖
			List<String> zcLotNos = new ArrayList<String>();
			zcLotNos.add("ZC");
			zcResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, zcLotNos));
			zcResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, zcLotNos));
			
			//竟彩足球
			//是否加奖
			List<String> jc_zLotNos = new ArrayList<String>();
			jc_zLotNos.add("JC_Z");
			jc_zResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, jc_zLotNos));
			jc_zResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, jc_zLotNos));
			
			//竟彩篮球
			//是否加奖
			List<String> jc_lLotNos = new ArrayList<String>();
			jc_lLotNos.add("JC_L");
			jc_lResultJson.put("isAddAward", LotTypeInfoUtil.isLotNosExistList(addAwardLotNos, jc_lLotNos));
			jc_lResultJson.put("isSale", LotTypeInfoUtil.isLotNosExistList(saleLotNos, jc_lLotNos));
			
			//查询活动中心正在进行中的活动数量
			Integer inProgressActivityCount = activityCenterUtil.getInProgressActivityCount(clientInfo.getCoopId());
				
			
			responseJson.put(LotType.QLC.lotNo(), qlcResultJson.toString()); //七乐彩
			responseJson.put(LotType.DDD.lotNo(), dddResultJson.toString()); //福彩3D
			responseJson.put(LotType.SSQ.lotNo(), ssqResultJson.toString()); //双色球
			
			responseJson.put(LotType.DLT.lotNo(), dltResultJson.toString()); //大乐透
			responseJson.put(LotType.PLS.lotNo(), plsResultJson.toString()); //排列三
			responseJson.put(LotType.PLW.lotNo(), plwResultJson.toString()); //排列五
			responseJson.put(LotType.QXC.lotNo(), qxcResultJson.toString()); //七星彩
			responseJson.put(LotType.TT_F.lotNo(), tt_fResultJson.toString()); //22选5
			
			responseJson.put(LotType.SSC.lotNo(), sscResultJson.toString()); //时时彩
			responseJson.put(LotType.OO_F.lotNo(), oo_fResultJson.toString()); //江西11选5
			responseJson.put(LotType.OO_YDJ.lotNo(), oo_ydjResultJson.toString()); //11运夺金
			responseJson.put(LotType.GDOO_F.lotNo(), gdoo_fResultJson.toString()); //广东11选5
			responseJson.put(LotType.GDH_T.lotNo(), gdh_tResultJson.toString()); //广东快乐十分
			
			responseJson.put("ZC", zcResultJson.toString()); //足彩
			
			responseJson.put("JC_Z", jc_zResultJson.toString()); //竟彩足球
			responseJson.put("JC_L", jc_lResultJson.toString()); //竟彩篮球
			
			responseJson.put("inProgressActivityCount", inProgressActivityCount); //正在进行的活动数量
		} catch (Exception e) {
			logger.error("购买大厅查询发生异常", e);
		}
		
		return responseJson.toString();
	}
	
}
