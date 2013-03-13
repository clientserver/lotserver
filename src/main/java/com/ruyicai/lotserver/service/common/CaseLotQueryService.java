package com.ruyicai.lotserver.service.common;

import java.math.BigDecimal;
import java.net.URLEncoder;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.CaseLotUtil;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import com.ruyicai.lotserver.util.parse.WinCodeParseUtil;

/**
 * 合买相关查询
 * @author Administrator
 *
 */
@Service
public class CaseLotQueryService {

	private Logger logger = Logger.getLogger(CaseLotQueryService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CaseLotUtil caseLotUtil;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 合买大厅查询
	 * @param clientInfo
	 * @return
	 * @throws Exception 
	 */
	public String getCaseOrder(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxresult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxresult)) {
				maxresult = "10";
			}
			String lotNo = clientInfo.getLotNo(); //彩种
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("state=" + "1"); //新发起
			paramStr.append("&state=" + "2"); //已投注
			//paramStr.append("&state=" + "3"); //完成
			//paramStr.append("&state=" + "4"); //取消
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxresult)); //开始行数
			paramStr.append("&endLine=" + maxresult); //取多少条记录
			String orderBy = clientInfo.getOrderBy(); //按什么排序
			if (orderBy!=null&&orderBy.equals("totalAmt")) { //按总额排序
				paramStr.append("&orderBy=" + "totalAmt");
			} else if (orderBy!=null&&orderBy.equals("buyAmt")) { //按认购金额排序
				paramStr.append("&orderBy=" + URLEncoder.encode("buyAmtByStarter+buyAmtByFollower", "iso-8859-1"));
			} else if (orderBy!=null&&orderBy.equals("progress")) { //按进度排序
				paramStr.append("&orderBy=" + URLEncoder.encode("(buyAmtByStarter+buyAmtByFollower+safeAmt)/totalAmt", "iso-8859-1"));
			} else if (orderBy!=null&&orderBy.equals("participantCount")) { //按参与人数排序
				paramStr.append("&orderBy=" + "participantCount");
			}
			paramStr.append("&orderDir=" + clientInfo.getOrderDir()); //排序方式
			JSONObject requestObject = new JSONObject(); //查询条件
			requestObject.put("EQS_batchcode", clientInfo.getBatchCode());
			if (lotNo!=null&&lotNo.equals("JC_Z")) { //竞彩足球
				paramStr.append("&lotno=J00001&lotno=J00002&lotno=J00003&lotno=J00004");
			} else if (lotNo!=null&&lotNo.equals("JC_L")) { //竞彩篮球
				paramStr.append("&lotno=J00005&lotno=J00006&lotno=J00007&lotno=J00008");
			} else if (lotNo!=null&&lotNo.equals("ZC")) { //足彩
				paramStr.append("&lotno=T01003&lotno=T01004&lotno=T01005&lotno=T01006");
			} else {
				requestObject.put("EQS_lotno", lotNo);
			}
			paramStr.append("&condition=" + requestObject.toString());
			
			String url = propertiesUtil.getLotteryUrl() + "select/selectCaseLots";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("根据条件查询合买方案的返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					totalPage = valueObject.getString("totalPage");
					JSONArray list = valueObject.getJSONArray("list");
					if (list!=null&&list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							JSONObject caseObject = list.getJSONObject(i);
							//发起人
							JSONObject starterObject = caseObject.getJSONObject("starter");
							String starterUserNo = starterObject.getString("userno"); //用户编号
							String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名字
							//合买Object
							JSONObject caseLotObject = caseObject.getJSONObject("caseLot");
							String id = caseLotObject.getString("id"); //方案编号
							String lotno = caseLotObject.getString("lotno"); //彩种
							String batchCode = caseLotObject.getString("batchcode"); //期号
							String totalAmt = caseLotObject.getString("totalAmt"); //方案总金额
							String safeAmt = caseLotObject.getString("safeAmt"); //保底金额
							String sortState = caseLotObject.getString("sortState"); //置顶状态
							String buyAmtByStarter = caseLotObject.getString("buyAmtByStarter"); //发起人认购金额
							String buyAmtByFollower = caseLotObject.getString("buyAmtByFollower"); //跟随者的认购金额
							//总的认购金额
							Integer buyAmt = Integer.parseInt(buyAmtByStarter) + Integer.parseInt(buyAmtByFollower); 
							//计算认购进度
							BigDecimal progress = CaseLotUtil.computePlan(Long.parseLong(buyAmtByStarter), Long.parseLong(buyAmtByFollower), 
									Long.parseLong(totalAmt));
							//计算保底进度
							BigDecimal safeRate = CaseLotUtil.computeSafeRate(Long.parseLong(safeAmt), Long.parseLong(totalAmt));
							//专家战绩
							JSONObject achievementObject= caseObject.getJSONObject("achievement");
							JSONObject displayIcon = (achievementObject!=null&&achievementObject.has("displayIcon")) ? 
									achievementObject.getJSONObject("displayIcon") : new JSONObject();
							//彩种名称
							String lotName = "";
							if (LotTypeUtil.isJingCaiZQ(lotno)) { //竞彩足球
								lotName = "竞彩足球";
							} else if (LotTypeUtil.isJingCaiLQ(lotno)) { //竞彩篮球
								lotName = "竞彩篮球";
							} else {
								lotName = MemoUtil.getLotNameByLotNo(lotno);
							}
							//是否置顶
							String isTop = CaseLotUtil.isTop(sortState);
							
							JSONObject object = new JSONObject();
							object.put("caseLotId", id); //合买编号
							object.put("lotNo", lotno); //彩种编号
							object.put("lotName", lotName); //彩种名称
							object.put("batchCode", batchCode); //期号
							object.put("starter", starterName); //发起人名字
							object.put("starterUserNo", starterUserNo); //发起人用户编号
							object.put("totalAmt", totalAmt); //方案总金额
							object.put("safeAmt", safeAmt); //保底金额
							object.put("buyAmt", buyAmt); //总的认购金额
							object.put("progress", progress); //认购进度
							object.put("safeRate", safeRate); //保底进度
							object.put("displayIcon", displayIcon); //专家战绩
							object.put("isTop", isTop); //是否置顶
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
			logger.error("合买大厅查询发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("totalPage", totalPage);
		return responseJson.toString();
	}
	
	/**
	 * 参与合买查询
	 * @param clientInfo
	 * @return
	 */
	public String getCaseLot(ClientInfo clientInfo) {
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
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo); //用户编号
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);
			paramStr.append("&orderBy=" + "buyTime");//排序(按认购时间进行排序)
			paramStr.append("&orderDir=" + "DESC"); //倒序
			
			String url = propertiesUtil.getLotteryUrl() + "select/selectCaseLotBuys";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("查询用户参与的合买订单返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
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
							//是否显示注码
							String displayTlots = caseObject.getString("displayTlots");
							//发起人
							JSONObject starterObject = caseObject.getJSONObject("starter");
							String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名称
							//合买Object
							JSONObject caseLotObject = caseObject.getJSONObject("caseLot");
							String lotNo = caseLotObject.getString("lotno"); //彩种
							String batchCode = caseLotObject.getString("batchcode"); //彩种
							String displayState = caseLotObject.getString("displayState"); //合买状态
							String displayStateMemo = caseLotObject.getString("displayStateMemo"); //合买状态memo
							String totalAmt = caseLotObject.getString("totalAmt"); //方案金额
							String safeAmt = caseLotObject.getString("safeAmt"); //总方案的保底金额
							String buyAmtByStarter = caseLotObject.getString("buyAmtByStarter"); //发起人认购金额
							String buyAmtByFollower = caseLotObject.getString("buyAmtByFollower"); //跟随者的认购金额
							String commisionRatio = caseLotObject.getString("commisionRatio"); //提成比例
							String visibility = caseLotObject.getString("visibility"); //公开状态
							//合买认购Object
							JSONObject caseLotBuy = caseObject.getJSONObject("caseLotBuy");
							String caseLotId = caseLotBuy.getString("caselotId"); //合买编号
							String amt = caseLotBuy.getString("num"); //参与人认购金额
							String buySafeAmt = caseLotBuy.getString("safeAmt"); //参与人保底金额
							String prizeAmt = caseLotBuy.getString("prizeAmt"); //参与人中奖金额
							String commisionPrizeAmt = caseLotBuy.getString("commisionPrizeAmt"); //参与人佣金金额
							String buyTime = caseLotBuy.getString("buyTime"); //认购时间
							//订单Object
							JSONObject torderObject = caseObject.getJSONObject("torder");
							if (torderObject.toString().equals("null")) { //兼容没有订单的情况
								continue;
							}
							String orderId = torderObject.getString("id"); //订单编号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String orderInfo = torderObject.getString("orderinfo"); //orderInfo
							String orderState = torderObject.getString("orderstate"); //订单状态
							String prizeState = torderObject.getString("prizestate"); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							//兼容以前的合买记录
							if (Tools.isEmpty(orderInfo)||orderInfo.trim().equals("null")) { 
								continue;
							}
							//计算认购进度
							BigDecimal progress = CaseLotUtil.computePlan(Long.parseLong(buyAmtByStarter), Long.parseLong(buyAmtByFollower), 
									Long.parseLong(totalAmt));
							//计算保底进度
							BigDecimal safeRate = CaseLotUtil.computeSafeRate(Long.parseLong(safeAmt), Long.parseLong(totalAmt));
							//解析开奖号码
							String winCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
							//解析方案内容
							JSONObject parseContentJson = caseLotUtil.getCaseLotContent(displayTlots, caseLotObject, torderObject);
							String content = parseContentJson.getString("content"); //解析后的注码
							String contentHtml = parseContentJson.getString("contentHtml"); //解析后的注码(html)
							//解析方案内容Json
							JSONObject contentJson = caseLotUtil.getCaseLotContentJson(displayTlots, visibility, orderId, lotNo, batchCode, 
									orderInfo, winBaseCode, orderState);
							
							JSONObject object = new JSONObject();
							object.put("caseLotId", caseLotId); //合买编号
							object.put("lotNo", lotNo); //彩种编号
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
							object.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
							object.put("batchCode", batchCode); //期号
							object.put("content", content); //合买内容
							object.put("contentHtml", contentHtml); //合买内容(html)
							object.put("contentJson", contentJson); //合买内容(Json)
							object.put("displayState", displayState); //合买状态
							object.put("displayStateMemo", displayStateMemo); //合买状态描述
							object.put("amt", amt); //参与人认购金额
							object.put("prizeAmt", prizeAmt); //参与人中奖金额
							object.put("commisionPrizeAmt", commisionPrizeAmt); //参与人佣金金额
							object.put("commisionRatio", commisionRatio); //提成比例
							object.put("totalAmt", totalAmt); //方案金额
							object.put("safeAmt", buySafeAmt); //参与人保底金额
							object.put("progress", progress); //认购进度
							object.put("safeRate", safeRate); //保底进度
							object.put("buyTime", DateParseFormatUtil.formatDateTime(buyTime)); //认购时间
							object.put("starter", starterName); //发起人名称
							object.put("prizeState", prizeState); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							object.put("winCode", winCode); //中奖号码
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
			logger.error("参与合买查询发生异常", e);
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
	public String getCaseOrderDetail(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = clientInfo.getUserno(); //此处不需要根据手机号查询userno,客户端会传
			String caseId = clientInfo.getCaseid(); //合买编号
			if (Tools.isEmpty(caseId)) { //合买编号为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			String result = lotteryCommonService.selectCaseLotDetail(caseId, userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					//是否显示注码
					String displayTlots = valueObject.getString("displayTlots");
					//发起人
					JSONObject starterObject = valueObject.getJSONObject("starter");
					String starterUserNo = starterObject.getString("userno"); //用户编号
					String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名字
					//合买Object
					JSONObject caseLotObject = valueObject.getJSONObject("caseLot");
					String id = caseLotObject.getString("id"); //方案编号
					String batchCode = caseLotObject.getString("batchcode"); //期号
					String totalAmt = caseLotObject.getString("totalAmt"); //方案金额
					String safeAmt = caseLotObject.getString("safeAmt"); //保底金额
					String minAmt = caseLotObject.getString("minAmt"); //最低认购金额
					String buyAmtByStarter = caseLotObject.getString("buyAmtByStarter"); //发起人认购金额
					String buyAmtByFollower = caseLotObject.getString("buyAmtByFollower"); //跟随者的认购金额
					String displayState = caseLotObject.getString("displayState"); //合买状态(1:认购中;2:满员;3:成功;4:撤单;5:流单;6:已中奖)
					String displayStateMemo = caseLotObject.getString("displayStateMemo"); //合买状态memo
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
					//认购金额
					long buyAmt = Long.parseLong(buyAmtByStarter) + Long.parseLong(buyAmtByFollower); //总的认购金额
					long remainderAmt = Long.parseLong(totalAmt) - buyAmt; //剩余认购金额
					//计算认购进度
					BigDecimal progress = CaseLotUtil.computePlan(Long.parseLong(buyAmtByStarter), Long.parseLong(buyAmtByFollower), 
							Long.parseLong(totalAmt));
					//计算保底进度
					BigDecimal safeRate = CaseLotUtil.computeSafeRate(Long.parseLong(safeAmt), Long.parseLong(totalAmt));
					BigDecimal totalRate = progress.add(safeRate);
					//解析开奖号码
					String winCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
					//专家战绩
					JSONObject achievementObject= valueObject.getJSONObject("achievement");
					JSONObject displayIcon = CaseLotUtil.getCaseLotDisplayIcon(achievementObject);
					//截止时间
					String endTime = caseLotUtil.getCaseEndTime(lotNo, batchCode);
					//解析方案内容
					JSONObject parseContentJson = caseLotUtil.getCaseLotContent(displayTlots, caseLotObject, torderObject);
					String content = parseContentJson.getString("content"); //解析后的注码
					String contentHtml = parseContentJson.getString("contentHtml"); //解析后的注码(html)
					//解析方案内容Json
					JSONObject contentJson = caseLotUtil.getCaseLotContentJson(displayTlots, visibility, orderId, lotNo, batchCode, 
							orderInfo, winBaseCode, orderState);
					//是否可以撤单
					String cancelCaselot = caseLotUtil.isCancelCaselot(userNo, starterUserNo, displayState, totalRate);
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("caseLotId", id); //彩种编号
					responseJson.put("lotNo", lotNo); //彩种编号
					responseJson.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
					responseJson.put("lotMulti", LotteryAlgorithmUtil.removeZeroMutiple(multiple)); //倍数
					responseJson.put("batchCode", batchCode); //期号
					responseJson.put("content", content); //合买内容
					responseJson.put("contentHtml", contentHtml); //合买内容(html)
					responseJson.put("contentJson", contentJson); //合买内容(Json)
					responseJson.put("starter", starterName); //发起人名字
					responseJson.put("description", description); //合买描述
					responseJson.put("totalAmt", totalAmt); //合买总金额
					responseJson.put("safeAmt", safeAmt); //保底金额
					responseJson.put("buyAmt", buyAmt); //总的认购金额
					responseJson.put("minAmt", minAmt); //最低认购金额
					responseJson.put("remainderAmt", remainderAmt); //剩余认购金额
					responseJson.put("buyAmtByStarter", buyAmtByStarter); //发起人认购金额
					responseJson.put("displayState", displayState); //合买状态
					responseJson.put("displayStateMemo", displayStateMemo); //合买状态memo
					responseJson.put("participantCount", participantCount); //参与人数
					responseJson.put("commisionRatio", commisionRatio); //提成比例
					responseJson.put("progress", progress); //认购进度
					responseJson.put("safeRate", safeRate); //保底进度
					responseJson.put("displayIcon", displayIcon); //专家战绩
					responseJson.put("winCode", winCode); //中奖号码
					responseJson.put("cancelCaselot", cancelCaselot); //是否可以撤单
					responseJson.put("url", "http://wap.ruyicai.com/w/orderhm/caseLotDetail.jspx?caseLotId="+id); //合买详情地址(用作微博分享)
					responseJson.put("endTime", DateParseFormatUtil.formatDateTime(endTime));
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
		return responseJson.toString();
	}
	
	/**
	 * 合买参与人查询
	 * @param clientInfo
	 * @return
	 */
	public String getCaseLotBuys(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //此处不需要根据手机号查询userno，客户端会传
			String caseId = clientInfo.getCaseid(); //合买编号
			if (Tools.isEmpty(caseId)) { //合买编号为空
				return Tools.paramError(clientInfo.getImei());
			}
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxresult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxresult)) {
				maxresult = "10";
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("caselotid="+caseId); //合买编号
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxresult)); //开始行数
			paramStr.append("&endLine=" + maxresult); //取多少条记录
			paramStr.append("&orderBy=" + "buyTime"); //排序字段
			paramStr.append("&orderDir=" + "ASC"); //排序方式
			
			String url = propertiesUtil.getLotteryUrl() + "select/selectCaseLotBuysSimplify";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("合买参与人查询返回:"+result+",paramStr:"+paramStr.toString());
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
						String starterUserNo = ""; //发起人
						BigDecimal totalRate = BigDecimal.ZERO; //进度(认购进度+保底进度)
						String displayState = ""; //合买状态
						String result2 = lotteryCommonService.selectCaseLotDetail(caseId, "");
						if (!Tools.isEmpty(result2)) {
							JSONObject fromObject2 = JSONObject.fromObject(result2);
							if (fromObject2!=null) {
								String errorCode2 = fromObject2.getString("errorCode");
								if (errorCode2!=null&&errorCode2.equals("0")) {
									JSONObject valueObject2 = fromObject2.getJSONObject("value");
									//发起人Object
									JSONObject starterObject = valueObject2.getJSONObject("starter");
									starterUserNo = starterObject.getString("userno"); //用户编号
									//合买Object
									JSONObject caseLotObject = valueObject2.getJSONObject("caseLot");
									String totalAmt = caseLotObject.getString("totalAmt"); //方案金额
									String safeAmt = caseLotObject.getString("safeAmt"); //保底金额
									String buyAmtByStarter = caseLotObject.getString("buyAmtByStarter"); //发起人认购金额
									String buyAmtByFollower = caseLotObject.getString("buyAmtByFollower"); //跟随者的认购金额
									displayState = caseLotObject.getString("displayState"); //合买状态(1:认购中;2:满员;3:成功;4:撤单;5:流单;6:已中奖)
									//计算认购进度
									BigDecimal progress = CaseLotUtil.computePlan(Long.parseLong(buyAmtByStarter), Long.parseLong(buyAmtByFollower), 
											Long.parseLong(totalAmt));
									//计算保底进度
									BigDecimal safeRate = CaseLotUtil.computeSafeRate(Long.parseLong(safeAmt), Long.parseLong(totalAmt));
									totalRate = progress.add(safeRate);
								}
							}
						}
						
						for (int i = 0; i < list.size(); i++) {
							JSONObject caseObject = list.getJSONObject(i);
							//参与人Object
							JSONObject userinfoObject = caseObject.getJSONObject("userinfo");
							String buyUserNo = userinfoObject.getString("userno"); //用户编号
							String nickName = CaseLotUtil.getCaseLotStarterName(userinfoObject); //名字
							//认购Object
							JSONObject caseLotBuyObject = caseObject.getJSONObject("caseLotBuy");
							String buyAmt = caseLotBuyObject.getString("num"); //认购金额
							String flag = caseLotBuyObject.getString("flag"); //正常1，撤资0
							String buyTime = caseLotBuyObject.getString("buyTime"); //认购时间
							//是否可以撤资
							String cancelCaselotbuy = CaseLotUtil.isCancelCaselotbuy(displayState, userNo, buyUserNo, starterUserNo, 
									flag, totalRate);
							
							JSONObject object = new JSONObject();
							object.put("nickName", nickName); //名字
							object.put("buyAmt", buyAmt); //认购金额
							object.put("buyTime", DateParseFormatUtil.formatDateTime(buyTime)); //认购时间
							object.put("cancelCaselotbuy", cancelCaselotbuy); //是否可以撤资
							object.put("state", flag); //参与状态(正常1，撤资0)
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
			logger.error("合买参与人查询发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("totalPage", totalPage);
		return responseJson.toString();
	}
	
}
