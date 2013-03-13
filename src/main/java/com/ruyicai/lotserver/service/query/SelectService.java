package com.ruyicai.lotserver.service.query;

import java.math.BigDecimal;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.CaseLotUtil;
import com.ruyicai.lotserver.util.lot.LotQueryUtil;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import com.ruyicai.lotserver.util.parse.WinCodeParseUtil;

/**
 * 查询请求
 * @author Administrator
 *
 */
@Service
public class SelectService {

	private Logger logger = Logger.getLogger(SelectService.class);
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CaseLotUtil caseLotUtil;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 投注列表查询
	 * @param clientInfo
	 * @return
	 */
	public String getBetList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (StringUtil.isEmpty(userNo)) { // 如果userNo为空,参数错误
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
			if (StringUtil.isEmpty(result)) { // 如果返回空,参数错误
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
							String orderAmt = userActionObject.getString("orderamt"); //金额（如果是合买就是认购的金额）
							String prizeAmt = userActionObject.getString("prizeamt"); //中奖金额
							//订单信息
							JSONObject torderObject = userActionObject.getJSONObject("torder");
							String orderId = torderObject.getString("id"); //订单Id
							String lotNo = torderObject.getString("lotno"); //彩种编号
							String batchCode = torderObject.getString("batchcode"); //期号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String betNum = torderObject.getString("betnum"); //注数
							String orderInfo = torderObject.getString("orderinfo"); //orderInfo
							String createTime = torderObject.getString("createtime"); //投注时间
							String orderState = torderObject.getString("orderstate"); //orderstate
							String tlotCaseId = torderObject.getString("tlotcaseid"); //合买编号
							String prizeState = torderObject.getString("prizestate"); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							
							//判断注码是否为空，解决以前的不是订单投注的记录
							if (StringUtil.isEmpty(orderInfo)||orderInfo.equals("null")) { 
								continue;
							}
							//判断期号是否为空
							batchCode = (StringUtil.isEmpty(batchCode)||batchCode.equals("null")) ? "" : batchCode.trim();
							//单注金额
							String oneAmount = LotQueryUtil.getOneAmount(orderInfo);
							//开奖号码
							String parseWinCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
							//是否可以再买一次(足彩任九场,足彩进球彩,足彩半全场,足彩胜负彩,竞彩足球胜负平,合买不允许再买一次)
							String isRepeatBuy = LotQueryUtil.isRepeatBuy(lotNo, tlotCaseId);
							
							JSONObject object = new JSONObject();
							object.put("orderId", orderId); //订单编号
							object.put("lotNo", lotNo); //彩种
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
							object.put("batchCode", batchCode); //期号
							object.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
							object.put(Constants.betNum, betNum); //注数
							object.put("orderInfo", orderInfo); //注码
							object.put("amount", orderAmt); //认购金额
							object.put(Constants.oneAmount, oneAmount); //单注金额
							object.put("prizeAmt", prizeAmt); //中奖金额
							object.put("prizeState", prizeState); //兑奖标识
							object.put("orderTime", DateParseFormatUtil.formatDateTime(createTime)); //投注时间
							object.put(Constants.stateMemo, MemoUtil.getOrderStateMemo(orderState)); //状态描述
							object.put("winCode", parseWinCode); //开奖号码
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
			logger.error("投注查询getBetList发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 注码解析
	 * @param clientInfo
	 * @return
	 */
	public String getBetCodeAnalysis(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			String id = clientInfo.getId(); //订单号
			if (StringUtil.isEmpty(id)) { // 如果id为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String result = lotteryService.getTorderByOrderId(id);
			if (StringUtil.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject torderObject = fromObject.getJSONObject("value"); //订单信息
					String orderId = torderObject.getString("id"); //订单Id
					String lotNo = torderObject.getString("lotno"); //彩种编号
					String batchCode = torderObject.getString("batchcode"); //期号
					String orderInfo = torderObject.getString("orderinfo"); //orderInfo
					String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
					
					//判断期号是否为空
					batchCode = (StringUtil.isEmpty(batchCode)||batchCode.equals("null")) ? "" : batchCode.trim();
					//解析注码
					JSONObject tlotsValueObject = null; //订单对应的票
					if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)||LotTypeUtil.isJingCaiHH(lotNo)) { //竞彩
						tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, true);
					}
					String parseCodeHtml = commonUtil.getBetCodeAnalysis("true", lotNo, orderInfo, "", 
							tlotsValueObject, true, winBaseCode); //除竞彩、足彩外的彩种使用
					JSONObject parseCodeJson = commonUtil.getParseCodeTable("true", "", lotNo, batchCode, orderInfo, 
							winBaseCode, tlotsValueObject); //竞彩、足彩使用
					
					resultJson.put("betCodeHtml", parseCodeHtml); //解析后的注码(中奖号码标红)
					resultJson.put("betCodeJson", parseCodeJson); //解析后的注码(Json格式,竞彩和足彩使用)
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("result", resultJson);
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("注码解析发生异常", e);
		}
		responseJson.put("result", resultJson);
		return responseJson.toString();
	}
	
	/**
	 * 中奖列表查询
	 * @param clientInfo
	 * @return
	 */
	public String getWinList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (StringUtil.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (StringUtil.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (StringUtil.isEmpty(maxResult)) {
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
			if (StringUtil.isEmpty(result)) { // 如果返回空,参数错误
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
							String orderInfo = torderObject.getString("orderinfo"); //orderInfo
							String createTime = torderObject.getString("createtime"); //投注时间
							String encashTime = torderObject.getString("encashtime"); //兑奖时间
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							//tuseraction
							String tuserActionString = listObject.getString("tuseraction");
							//判断注码是否为空，解决以前的不是订单投注的记录
							if (StringUtil.isEmpty(orderInfo)||orderInfo.equals("null")
									||StringUtil.isEmpty(tuserActionString)||tuserActionString.equals("null")) { 
								continue;
							}
							//判断期号是否为空
							batchCode = (Tools.isEmpty(batchCode)||batchCode.equals("null")) ? "" : batchCode.trim();
							//判断兑奖时间是否为空
							if (Tools.isEmpty(encashTime)||encashTime.trim().equals("null")) {
								encashTime = "";
							} else {
								encashTime = DateParseFormatUtil.formatDateTime(encashTime);
							}
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
									object.put("prizeAmt", prizeAmt); //中奖金额
									object.put("orderTime", DateParseFormatUtil.formatDateTime(createTime)); //投注时间
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
			logger.error("中奖列表查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 参与合买列表查询
	 * @param clientInfo
	 * @return
	 */
	public String getCaseLotBuyList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (StringUtil.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (StringUtil.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (StringUtil.isEmpty(maxResult)) {
				maxResult = "10";
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo); //用户编号
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);
			paramStr.append("&orderBy=" + "buyTime");//排序(按认购时间进行排序)
			paramStr.append("&orderDir=" + "DESC"); //倒序
			
			String url = propertiesUtil.getLotteryUrl() + "select/selectCaseLotBuys";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("查询用户参与的合买订单返回:"+result+",paramStr:"+paramStr.toString());
			if (StringUtil.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray listArray = valueObject.getJSONArray("list");
					if (listArray != null && listArray.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < listArray.size(); i++) {
							JSONObject caseObject = listArray.getJSONObject(i);
							//发起人
							JSONObject starterObject = caseObject.getJSONObject("starter");
							String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名称
							//合买Object
							JSONObject caseLotObject = caseObject.getJSONObject("caseLot");
							String lotNo = caseLotObject.getString("lotno"); //彩种
							String displayState = caseLotObject.getString("displayState"); //合买状态
							//合买认购Object
							JSONObject caseLotBuy = caseObject.getJSONObject("caseLotBuy");
							String caseLotId = caseLotBuy.getString("caselotId"); //合买编号
							String buyAmt = caseLotBuy.getString("num"); //参与人认购金额
							String prizeAmt = caseLotBuy.getString("prizeAmt"); //参与人中奖金额
							String buyTime = caseLotBuy.getString("buyTime"); //认购时间
							//订单Object
							JSONObject torderObject = caseObject.getJSONObject("torder");
							if (torderObject.toString().equals("null")) { //兼容没有订单的情况
								continue;
							}
							String prizeState = torderObject.getString("prizestate"); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							
							JSONObject object = new JSONObject();
							object.put("caseLotId", caseLotId); //合买编号
							object.put("starter", starterName); //发起人名称
							object.put("lotNo", lotNo); //彩种编号
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
							object.put("buyAmt", buyAmt); //参与人认购金额
							object.put("prizeAmt", prizeAmt); //参与人中奖金额
							object.put("displayState", displayState); //合买状态
							object.put("buyTime", DateParseFormatUtil.formatDateTime(buyTime)); //认购时间
							object.put("prizeState", prizeState); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							resultArray.add(object);
						}
						
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("result", resultArray);
						responseJson.put("totalPage", totalPage);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("参与合买列表查询发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("totalPage", totalPage);
		return responseJson.toString();
	}
	
	/**
	 * 合买详情查询
	 * @param clientInfo
	 * @return
	 */
	public String getCaseLotDetail(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			String userNo = clientInfo.getUserno(); //此处不需要根据手机号查询userno,客户端会传
			String id = clientInfo.getId(); //合买编号
			if (StringUtil.isEmpty(id)) { //合买编号为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			String result = lotteryCommonService.selectCaseLotDetail(id, userNo);
			if (StringUtil.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")&&valueString!=null&&!valueString.equals("null")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					//是否显示注码
					String displayTlots = valueObject.getString("displayTlots");
					//发起人
					JSONObject starterObject = valueObject.getJSONObject("starter");
					String starterUserNo = starterObject.getString("userno"); //用户编号
					String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名字
					//合买Object
					JSONObject caseLotObject = valueObject.getJSONObject("caseLot");
					String caseLotId = caseLotObject.getString("id"); //方案编号
					String batchCode = caseLotObject.getString("batchcode"); //期号
					String totalAmt = caseLotObject.getString("totalAmt"); //方案金额
					String safeAmt = caseLotObject.getString("safeAmt"); //保底金额
					String minAmt = caseLotObject.getString("minAmt"); //最低认购金额
					String buyAmtByStarter = caseLotObject.getString("buyAmtByStarter"); //发起人认购金额
					String buyAmtByFollower = caseLotObject.getString("buyAmtByFollower"); //跟随者的认购金额
					String displayState = caseLotObject.getString("displayState"); //合买状态(1:认购中;2:满员;3:成功;4:撤单;5:流单;6:已中奖)
					String participantCount = caseLotObject.getString("participantCount"); //参与人数
					String commisionRatio = caseLotObject.getString("commisionRatio"); //提成比例
					String visibility = caseLotObject.getString("visibility"); //公开状态
					String description = caseLotObject.getString("description"); //方案描述
					//订单Object
					JSONObject torderObject = valueObject.getJSONObject("torder");
					String orderId = torderObject.getString("id"); //订单id
					String lotNo = torderObject.getString("lotno"); //彩种
					String multiple = torderObject.getString("lotmulti"); //倍数
					String orderInfo = torderObject.getString("orderinfo"); //orderInfo
					String orderState = torderObject.getString("orderstate"); //订单状态
					String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
					//判断期号是否为空
					batchCode = (StringUtil.isEmpty(batchCode)||batchCode.equals("null")) ? "" : batchCode.trim();
					//方案描述
					description = (Tools.isEmpty(description)||description.equals("null")) ? "" : description;
					//已认购金额
					long hasBuyAmt = Long.parseLong(buyAmtByStarter) + Long.parseLong(buyAmtByFollower); //已认购金额
					long remainderAmt = Long.parseLong(totalAmt) - hasBuyAmt; //剩余认购金额
					//计算认购进度
					BigDecimal buyProgress = CaseLotUtil.computePlan(Long.parseLong(buyAmtByStarter), Long.parseLong(buyAmtByFollower), 
							Long.parseLong(totalAmt));
					//计算保底进度
					BigDecimal safeProgress = CaseLotUtil.computeSafeRate(Long.parseLong(safeAmt), Long.parseLong(totalAmt));
					BigDecimal totalRate = buyProgress.add(safeProgress);
					//解析开奖号码
					String winCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
					//专家战绩
					JSONObject achievementObject= valueObject.getJSONObject("achievement");
					JSONObject displayIcon = CaseLotUtil.getCaseLotDisplayIcon(achievementObject);
					//截止时间
					String endTime = caseLotUtil.getCaseEndTime(lotNo, batchCode);
					//解析方案内容
					String betCodeHtml = caseLotUtil.getCaseLotBetCodeHtml(displayTlots, caseLotObject, 
							torderObject); //除竞彩、足彩的其他彩种使用
					JSONObject betCodeJson = caseLotUtil.getCaseLotContentTable(displayTlots, visibility, orderId, lotNo, batchCode, 
							orderInfo, winBaseCode, orderState); //竞彩、足彩使用
					//是否可以撤单
					String cancelCaselot = caseLotUtil.isCancelCaselot(userNo, starterUserNo, displayState, totalRate);
					
					resultJson.put("caseLotId", caseLotId); //彩种编号
					resultJson.put("starter", starterName); //发起人名字
					resultJson.put("lotNo", lotNo); //彩种编号
					resultJson.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
					resultJson.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
					resultJson.put("batchCode", batchCode); //期号
					resultJson.put("betCodeHtml", betCodeHtml); //合买内容(html)
					resultJson.put("betCodeJson", betCodeJson); //合买内容(json)
					resultJson.put("totalAmt", totalAmt); //合买总金额
					resultJson.put("safeAmt", safeAmt); //保底金额
					resultJson.put("hasBuyAmt", hasBuyAmt); //已认购金额
					resultJson.put("remainderAmt", remainderAmt); //剩余认购金额
					resultJson.put("minAmt", minAmt); //最低认购金额
					resultJson.put("buyAmtByStarter", buyAmtByStarter); //发起人认购金额
					resultJson.put("commisionRatio", commisionRatio); //提成比例
					resultJson.put("participantCount", participantCount); //参与人数
					resultJson.put("buyProgress", buyProgress); //认购进度
					resultJson.put("safeProgress", safeProgress); //保底进度
					resultJson.put("displayIcon", displayIcon); //专家战绩
					resultJson.put("description", description); //合买描述
					resultJson.put("displayState", displayState); //合买状态
					resultJson.put("winCode", winCode); //中奖号码
					resultJson.put("endTime", DateParseFormatUtil.formatDateTime(endTime)); //截止时间
					resultJson.put("cancelCaselot", cancelCaselot); //是否可以撤单
					resultJson.put("url", "http://wap.ruyicai.com/w/orderhm/caseLotDetail.jspx?caseLotId="+id); //合买详情地址(用作微博分享)
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("result", resultJson);
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("合买详情查询发生异常", e);
		}
		responseJson.put("result", resultJson);
		return responseJson.toString();
	}
	
}
