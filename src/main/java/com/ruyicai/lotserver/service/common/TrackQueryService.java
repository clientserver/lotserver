package com.ruyicai.lotserver.service.common;

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
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.parse.WinCodeParseUtil;

/**
 * 追号相关查询
 * @author Administrator
 *
 */
@Service
public class TrackQueryService {

	private Logger logger = Logger.getLogger(TrackQueryService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	/**
	 * 追号查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getTrack(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; // 总页数
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String pageIndex = clientInfo.getPageindex(); // 当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); // 每页显示的条数
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			String beginTime = "20080808"; // 开始时间
			String endTime = DateParseFormatUtil.getTodayDate();// 设置默认结束的时间
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&beginTime=" + beginTime);
			paramStr.append("&endTime=" + endTime);
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);

			String url = propertiesUtil.getLotteryUrl() + "select/getTsubscribe";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("追号查询返回:" + result + ",paramStr:" + paramStr.toString());
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
							JSONObject subscribeObject = list.getJSONObject(i);
							String id = subscribeObject.getString("flowno"); // 交易流水号
							String lotNo = subscribeObject.getString("lotno"); //彩种编号
							String multiple = subscribeObject.getString("lotmulti"); //倍数
							String amount = subscribeObject.getString("amount"); //单次投注金额(收益率追号就是平均值)
							String remainderAmount = subscribeObject.getString("totalamt"); //剩余总金额
							String betCode = subscribeObject.getString("betcode"); //注码
							String orderInfo = subscribeObject.getString("orderinfo"); //orderInfo
							String batchnum = subscribeObject.getString("batchnum"); //追号期数
							String lastnum = subscribeObject.getString("lastnum"); //剩余期数
							String desc = subscribeObject.getString("desc"); //收益率信息

							//如果betcode和orderInfo都为空
							if ((Tools.isEmpty(betCode)||betCode.equals("null"))&&(Tools.isEmpty(orderInfo)||orderInfo.equals("null"))) { 
								continue;
							}
							//倍数为空
							multiple = (Tools.isEmpty(multiple)||multiple.equals("null")) ? "" : multiple;
							//解析注码
							String parseCode = commonUtil.getParseCode("true", lotNo, orderInfo, betCode, null, false, "");
							//处理收益率追号的总金额会有小数的情况
							BigDecimal totalAmount = new BigDecimal(amount).multiply(new BigDecimal(batchnum));
							totalAmount = (totalAmount.divide(new BigDecimal(100))).setScale(0, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
							//单注金额
							String oneAmount = "200";
							if (!Tools.isEmpty(orderInfo)&&!orderInfo.equals("null")) {
								String[] orderInfos = orderInfo.split("!");
								String[] codes = orderInfos[0].split("_");
								oneAmount = codes[2];
							}
							//是否允许再追一次
							String isRepeatBuy = "true"; 
							if (Tools.isEmpty(multiple)||multiple.equals("null")||multiple.equals("0")
									||(!Tools.isEmpty(desc)&&!desc.equals("null"))) { //倍数为空或0或收益率追号不允许再追一次
								isRepeatBuy = "false";
							}
							
							JSONObject object = new JSONObject();
							object.put("id", id); // 交易ID
							object.put("lotNo", lotNo); // 彩种编号
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); // 彩种名称
							object.put("lotMulti", multiple); //倍数
							object.put("bet_code", orderInfo); //原始注码
							object.put("betCode", parseCode); // 解析后的注码
							object.put("batchNum", Integer.parseInt(batchnum)); // 追号期数
							object.put("lastNum", Integer.parseInt(batchnum)-Integer.parseInt(lastnum)); // 已追期数
							object.put("beginBatch", subscribeObject.getString("beginbatch")); // 开始期号
							object.put("lastBatch", subscribeObject.getString("lastbatch")); // 上次投注的彩票期号
							object.put("betNum", subscribeObject.getString("betnum")); // 注数
							object.put("amount", totalAmount.toString()); // 追号总金额
							object.put("remainderAmount", remainderAmount); // 剩余追号总金额
							object.put("oneAmount", oneAmount); // 单注金额
							object.put("state", subscribeObject.getString("state")); // 状态
							object.put("orderTime", DateParseFormatUtil.formatDateTime(subscribeObject.getString("ordertime"))); // 定制时间
							object.put("prizeEnd", subscribeObject.getString("prizeend")); // 中奖后是否停止追号
							object.put("isRepeatBuy", isRepeatBuy); //是否允许再追一次
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
			logger.error("追号查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 追号详情查询
	 * @param clientInfo
	 * @return
	 */
	public String getTrackDetail(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			String tsubscribeNo = clientInfo.getTsubscribeNo(); //追号订单
			if (Tools.isEmpty(tsubscribeNo)) { //订单号为空，返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String result = lotteryCommonService.getTorderByScribeno(tsubscribeNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueJsonObject = fromObject.getJSONObject("value");
					JSONArray list = valueJsonObject.getJSONArray("list");
					if (list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
							JSONObject subscribeObject = list.getJSONObject(i);
							JSONObject torderObject = subscribeObject.getJSONObject("torder");
							String lotNo = torderObject.getString("lotno"); //彩种
							String batchCode = torderObject.getString("batchcode"); //期号
							String lotMulti = torderObject.getString("lotmulti"); //倍数
							String amount = torderObject.getString("amt"); //金额
							String orderPrizeAmt = torderObject.getString("orderprizeamt"); //中奖金额
							String desc = torderObject.getString("desc"); //收益率信息
							String orderState = torderObject.getString("orderstate"); //订单状态
							
							//倍数
							if (Tools.isEmpty(lotMulti)||lotMulti.trim().equals("null")) {
								lotMulti = "";
							}
							//中奖金额
							if (Tools.isEmpty(orderPrizeAmt)||orderPrizeAmt.trim().equals("null")) {
								orderPrizeAmt = "";
							}
							//收益率信息
							if (Tools.isEmpty(desc)||desc.trim().equals("null")) {
								desc = "";
							}
							//中奖号码
							String winCode = WinCodeParseUtil.parseWinCode(lotNo, torderObject.getString("winbasecode"));
							//状态描述
							String stateMemo = MemoUtil.getOrderStateMemo(orderState);
							
							JSONObject object = new JSONObject();
							object.put("batchCode", batchCode);
							object.put("lotMulti", lotMulti);
							object.put("amount", amount);
							object.put("winCode", winCode);
							object.put("state", orderState);
							object.put(Constants.stateMemo, stateMemo);
							object.put("prizeAmt", orderPrizeAmt); //中奖金额
							object.put("desc", desc); //收益率信息
							resultArray.add(object);
						}
						
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
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
			logger.error("追号详情查询发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
}
