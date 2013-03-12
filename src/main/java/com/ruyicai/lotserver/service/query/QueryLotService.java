package com.ruyicai.lotserver.service.query;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.BetUtil;
import com.ruyicai.lotserver.util.lot.LotQueryUtil;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import com.ruyicai.lotserver.util.parse.WinCodeParseUtil;

/**
 * 彩票查询相关的Service
 * @author Administrator
 *
 */
@Service
public class QueryLotService {
	
	private Logger logger = Logger.getLogger(QueryLotService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 投注查询
	 * @param clientInfo
	 * @return
	 */
	public String getBetRecord(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String beginTime = "20080101";
			String endTime = DateParseFormatUtil.getTodayDate();// 设置默认结束的时间
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&beginTime=" + beginTime);
			paramStr.append("&endTime=" + endTime);
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);
			paramStr.append("&bettype=" + 2); //普通代购投注
			paramStr.append("&state=" + 1); //查询投注成功的投注
			String lotno = clientInfo.getLotNo();
			if(!Tools.isEmpty(lotno)) { //彩种
				if (lotno.equals("JC_Z")) { //竞彩足球
					paramStr.append("&lotno=J00001&lotno=J00002&lotno=J00003&lotno=J00004");
				} else if (lotno.equals("JC_L")) { //竞彩篮球
					paramStr.append("&lotno=J00005&lotno=J00006&lotno=J00007&lotno=J00008");
				} else if (lotno!=null&&lotno.equals("ZC")) { //足彩
					paramStr.append("&lotno=T01003&lotno=T01004&lotno=T01005&lotno=T01006");
				} else {
					paramStr.append("&lotno=" + lotno);
				}
			}
			
			String url = propertiesUtil.getLotteryUrl() + "select/getUseraction";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("投注查询返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if(list != null && list.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject userActionObject = list.getJSONObject(i);
							String displayTlots = userActionObject.getString("displayTlots"); //是否保密
							//订单信息
							JSONObject torderObject = userActionObject.getJSONObject("torder");
							String orderId = torderObject.getString("id"); //订单Id
							String lotNo = torderObject.getString("lotno"); //彩种编号
							String batchCode = torderObject.getString("batchcode"); //期号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String betCode = torderObject.getString("betcode"); //注码
							String betNum = torderObject.getString("betnum"); //注数
							String orderInfo = torderObject.getString("orderinfo"); //orderInfo
							String orderState = torderObject.getString("orderstate"); //orderstate
							String tlotCaseId = torderObject.getString("tlotcaseid"); //合买编号
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							
							//判断注码是否为空，解决以前的不是订单投注的记录
							if (Tools.isEmpty(orderInfo)||orderInfo.equals("null")) { 
								continue;
							}
							//判断期号是否为空
							batchCode = (Tools.isEmpty(batchCode)||batchCode.equals("null"))?"":batchCode.trim();
							//原始注码
							String isSellWays = clientInfo.getIsSellWays(); //是否多玩法
							String bet_code = LotQueryUtil.getOriginalBetCode(isSellWays, lotNo, betCode, orderInfo);
							//开奖号码
							String parseWinCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
							//解析注码
							JSONObject tlotsValueObject = null; //订单对应的票
							if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
								tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, true);
							}
							String parseCode = commonUtil.getParseCode(displayTlots, lotNo, orderInfo, betCode, tlotsValueObject, false, winBaseCode);
							String parseCodeHtml = commonUtil.getParseCode(displayTlots, lotNo, orderInfo, betCode, tlotsValueObject, true, winBaseCode);
							JSONObject parseCodeJson = commonUtil.getParseCodeJson(displayTlots, "", lotNo, batchCode, orderInfo, winBaseCode, tlotsValueObject);
							//单注金额
							String oneAmount = LotQueryUtil.getOneAmount(orderInfo);
							//是否可以再买一次(足彩任九场,足彩进球彩,足彩半全场,足彩胜负彩,竞彩足球胜负平,合买不允许再买一次)
							String isRepeatBuy = LotQueryUtil.isRepeatBuy(lotNo, tlotCaseId);
							
							JSONObject object = new JSONObject();
							object.put("orderId", orderId); //订单编号
							object.put("lotNo", lotNo); //彩种
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
							object.put("batchCode", batchCode); //期号
							object.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
							object.put(Constants.betNum, betNum); //注数
							object.put("play", ""); //玩法
							object.put("bet_code", bet_code); //原始注码
							object.put("betCode", parseCode); //解析后的注码
							object.put("betCodeHtml", parseCodeHtml); //解析后的注码(中奖号码标红)
							object.put("betCodeJson", parseCodeJson); //解析后的注码(Json)
							object.put("amount", userActionObject.get("orderamt")); //金额（如果是合买就是认购的金额）
							object.put(Constants.oneAmount, oneAmount); //单注金额
							object.put("orderTime", DateParseFormatUtil.formatDateTime(torderObject.getString("createtime"))); //投注时间
							object.put("prizeAmt", userActionObject.getString("prizeamt")); //中奖金额
							object.put("prizeState", torderObject.getString("prizestate")); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							object.put("winCode", parseWinCode); //开奖号码
							object.put(Constants.stateMemo, MemoUtil.getOrderStateMemo(orderState)); //状态描述
							object.put("isRepeatBuy", isRepeatBuy); //是否可以再买一次
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("totalPage", totalPage);
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("投注查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}

	/**
	 * 中奖查询
	 * @param clientInfo
	 * @return
	 */
	public String getWinRecord(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			String beginTime = "20080101"; //开始时间
			String endTime = DateParseFormatUtil.getTodayDate();// 设置默认结束的时间
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&beginTime=" + beginTime);
			paramStr.append("&endTime=" + endTime);
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);
			paramStr.append("&orderstate=" + 1); //查询投注成功的投注
			paramStr.append("&isprize=" + 1); //中奖标识
			
			String url = propertiesUtil.getLotteryUrl() + "select/getTorders2";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("中奖查询返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if(list != null && list.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject listObject = list.getJSONObject(i);
							//订单信息
							JSONObject torderObject = listObject.getJSONObject("torder");
							String orderId = torderObject.getString("id"); //订单编号
							String lotNo = torderObject.getString("lotno"); //彩种编号
							String batchCode = torderObject.getString("batchcode"); //期号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String betNum = torderObject.getString("betnum"); //注数
							String betCode = torderObject.getString("betcode"); //注码
							String orderInfo = torderObject.getString("orderinfo"); //orderInfo
							String encashTime = torderObject.getString("encashtime"); //兑奖时间
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							//tuseraction
							String tuserActionString = listObject.getString("tuseraction");
							//判断注码是否为空，解决以前的不是订单投注的记录
							if (Tools.isEmpty(orderInfo)||orderInfo.equals("null")
									||Tools.isEmpty(tuserActionString)||tuserActionString.equals("null")) { 
								continue;
							}
							//判断兑奖时间是否为空
							if (Tools.isEmpty(encashTime)||encashTime.trim().equals("null")) {
								encashTime = "";
							} else {
								encashTime = DateParseFormatUtil.formatDateTime(encashTime);
							}
							//判断期号是否为空
							batchCode = (Tools.isEmpty(batchCode)||batchCode.equals("null"))?"":batchCode;
							//解析注码
							JSONObject tlotsValueObject = null; //订单对应的票
							if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
								tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, true);
							}
							String parseCode = commonUtil.getParseCode("true", lotNo, orderInfo, betCode, tlotsValueObject, false, "");
							String parseCodeHtml = commonUtil.getParseCode("true", lotNo, orderInfo, betCode, tlotsValueObject, true, winBaseCode);
							//解析开奖号码
							String parseWinCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
							//循环tuserAction
							JSONArray tuserActions = listObject.getJSONArray("tuseraction");
							if (tuserActions!=null&&tuserActions.size()>0) {
								for (int j = 0; j < tuserActions.size(); j++) {
									JSONObject tuserActionObject = tuserActions.getJSONObject(j);
									String amount = tuserActionObject.getString("orderamt"); //投注金额
									String prizeAmt = tuserActionObject.getString("prizeamt"); //中奖金额
									
									JSONObject object = new JSONObject();
									object.put("orderId", orderId); //订单编号
									object.put("lotNo", lotNo); //彩种编号
									object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
									object.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
									object.put(Constants.betNum, betNum); //注数
									object.put("batchCode", batchCode); //期号
									object.put("amount", amount); //投注金额
									object.put("winAmount", prizeAmt); //中奖金额
									object.put("bet_code", ""); //原始注码(以前版本的塞班客户端会解析,之后会去掉)
									object.put("betCode", parseCode); //解析后注码
									object.put("betCodeHtml", parseCodeHtml); //解析后的注码(中奖号码标红)
									object.put("sellTime", DateParseFormatUtil.formatDateTime(torderObject.getString("createtime"))); //销售时间
									object.put("cashTime", encashTime); //兑奖时间
									object.put("winCode", parseWinCode); //开奖号码
									resultArray.add(object);
								}
							}
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("totalPage", totalPage);
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("中奖查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获取遗漏值
	 * @param clientInfo
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMissValue(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			String sellway = clientInfo.getSellway(); //玩法
			if (Tools.isEmpty(lotNo)||Tools.isEmpty(sellway)) { // 如果lotNo或sellway为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String valueString = cacheCommonUtil.getMissValueString(lotNo, sellway); //获取遗漏值
			if (!Tools.isEmpty(valueString)) {
				if (BetUtil.isZuHeSellway(sellway)) { //组合的玩法,排序后取前12条记录
					JSONObject valueObject = JSONObject.fromObject(valueString);
					JSONObject resultObject = valueObject.getJSONObject("result");
					
					Map map = new HashMap();
					for(Iterator iter = resultObject.keys(); iter.hasNext();) {
						String key = (String)iter.next();
						map.put(key, resultObject.getInt(key));
					}
					Map sortMap = Tools.sortMapByValue(map, true); //排序
					JSONObject newResultObject = new JSONObject();
					int index = 0;
					for (Object entryObject : sortMap.entrySet()) {
						Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)entryObject;
						newResultObject.put(entry.getKey(), entry.getValue());
						index++;
						if (index==12) { //取前12条记录
							break;
						}
					}
					responseJson.put("result", newResultObject.toString());
				} else { //直选
					responseJson.put("result", valueString);
				}
				
				responseJson.put(Constants.error_code, ErrorCode.success.value());
				responseJson.put(Constants.message, "查询成功");
			} else {
				responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取遗漏值发生异常", e);
		}
		return responseJson.toString();
	}
	
}
