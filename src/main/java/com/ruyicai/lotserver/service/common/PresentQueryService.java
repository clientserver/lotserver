package com.ruyicai.lotserver.service.common;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import com.ruyicai.lotserver.util.parse.WinCodeParseUtil;

/**
 * 赠送查询Service
 * @author Administrator
 *
 */
@Service
public class PresentQueryService {

	private Logger logger = Logger.getLogger(PresentQueryService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 赠送查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getGift(ClientInfo clientInfo) {
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
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}

			StringBuffer paramStr = new StringBuffer();// 参数的字符串
			paramStr.append("userno=" + userNo);
			paramStr.append("&startLine=" + (Integer.parseInt(pageIndex)-1)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);

			String url = propertiesUtil.getLotteryUrl()+"present/selectSenderPresentDetails?";
			String result = HttpUtil.sendRequestByGet(url + paramStr.toString(), true);
			//logger.info("赠送查询返回:" + result + ",paramStr:" + paramStr.toString());
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
							JSONObject listObject = list.getJSONObject(i);
							//订单信息
							JSONObject torderObject = listObject.getJSONObject("torder");
							if (torderObject.toString().equals("null")) { //兼容没有订单的情况
								continue;
							}
							String orderId = torderObject.getString("id"); //订单号
							String lotNo = torderObject.getString("lotno"); //彩种
							String batchCode = torderObject.getString("batchcode"); //期号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String betCode = torderObject.getString("betcode"); //注码
							String orderInfo = torderObject.getString("orderinfo"); //orderinfo
							String betNum = torderObject.getString("betnum"); //注数
							String amount = torderObject.getString("amt"); //金额
							String orderState = torderObject.getString("orderstate"); //orderstate
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							//赠送信息
							JSONObject presentDetailsObject = listObject.getJSONObject("presentDetails");
							String reciverMobile = presentDetailsObject.getString("reciverMobile"); //被赠送人
							String createTime = presentDetailsObject.getString("createTime"); //赠送时间
							//如果betcode和orderInfo都为空
							if ((Tools.isEmpty(betCode)||betCode.trim().equals("null"))
									&&(Tools.isEmpty(orderInfo)||orderInfo.equals("null"))) {
								continue;
							}
							//判断期号是否为空
							batchCode = (Tools.isEmpty(batchCode)||batchCode.equals("null"))?"":batchCode; 
							// 注码解析
							JSONObject tlotsValueObject = null; //订单对应的票
							if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
								tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, true);
							}
							String parseCode = commonUtil.getParseCode("true", lotNo, orderInfo, betCode, tlotsValueObject, false, "");
							String parseCodeHtml = commonUtil.getParseCode("true", lotNo, orderInfo, betCode, tlotsValueObject, true, winBaseCode);
							//开奖号码
							String winCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);

							JSONObject object = new JSONObject();
							object.put("orderId", orderId); //订单编号
							object.put("toMobileId", reciverMobile); // 被赠送人
							object.put("lotNo", lotNo); // 彩种编号
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); // 彩种名称
							object.put("play", ""); // 玩法
							object.put("betCode", parseCode); // 解析后的注码
							object.put("betCodeHtml", parseCodeHtml); // 解析后的注码(Html)
							object.put("batchCode", batchCode); // 赠送期号
							object.put("lotMulti", multiple); // 赠送倍数
							object.put(Constants.betNum, betNum); //注数
							object.put("amount", amount); // 赠送金额
							object.put("orderTime", DateParseFormatUtil.formatDateTime(createTime)); // 交易时间
							object.put("reciveState", "1"); // 是否已领取
							object.put(Constants.stateMemo, MemoUtil.getOrderStateMemo(orderState)); //状态描述
							object.put("prizeState", torderObject.getString("prizestate")); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							object.put("winCode", winCode); //中奖号码
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
			logger.error("赠送查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}

	/**
	 * 被赠送查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getGifted(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; // 总页数
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}

			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&startLine=" + (Integer.parseInt(pageIndex)-1)*Integer.parseInt(maxResult));
			paramStr.append("&endLine=" + maxResult);

			String url = propertiesUtil.getLotteryUrl()+"present/selectReciverPresentDetails?";
			String result = HttpUtil.sendRequestByGet(url + paramStr.toString(), true);
			//logger.info("被赠送查询返回:" + result + ",paramStr:" + paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if (list != null && list.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject listObject = list.getJSONObject(i);
							//用户信息
							JSONObject tuserInfoObject = listObject.getJSONObject("tuserinfo");
							String userName = tuserInfoObject.getString("userName"); //用户名
							//订单信息
							JSONObject torderObject = listObject.getJSONObject("torder");
							if (torderObject.toString().equals("null")) { //兼容没有订单的情况
								continue;
							}
							String orderId = torderObject.getString("id"); //订单号
							String lotNo = torderObject.getString("lotno"); //彩种编号
							String batchCode = torderObject.getString("batchcode"); //期号
							String multiple = torderObject.getString("lotmulti"); //倍数
							String betCode = torderObject.getString("betcode"); //注码
							String orderInfo = torderObject.getString("orderinfo"); //orderinfo
							String betNum = torderObject.getString("betnum"); //注数
							String amount = torderObject.getString("amt"); //金额
							String orderState = torderObject.getString("orderstate"); //orderstate
							String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
							//赠送信息
							JSONObject presentDetailsObject = listObject.getJSONObject("presentDetails");
							String presentId = presentDetailsObject.getString("id"); //赠送订单号
							String createTime = presentDetailsObject.getString("createTime"); //赠送时间
							//如果betcode和orderInfo都为空
							if ((Tools.isEmpty(betCode)||betCode.equals("null"))
									&&(Tools.isEmpty(orderInfo)||orderInfo.equals("null"))) { 
								continue;
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
							//中奖号码
							String winCode = WinCodeParseUtil.parseWinCode(lotNo, winBaseCode);
							
							JSONObject object = new JSONObject();
							object.put("orderId", orderId); //订单编号
							object.put("presentId", presentId); // 赠送订单号
							object.put("giftMobileId", userName); // 赠送人
							object.put("lotNo", lotNo); // 彩种编号
							object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); // 彩种名称
							object.put("play", ""); // 玩法
							object.put("betCode", parseCode); // 解析后的注码
							object.put("betCodeHtml", parseCodeHtml); // 解析后的注码(html)
							object.put("batchCode", batchCode); // 赠送期号
							object.put("amount", amount); // 赠送金额
							object.put("lotMulti", multiple); // 赠送倍数
							object.put(Constants.betNum, betNum); //注数
							object.put("orderTime", DateParseFormatUtil.formatDateTime(createTime)); // 交易时间
							object.put("reciveState", "1"); // 是否已领取
							object.put(Constants.stateMemo, MemoUtil.getOrderStateMemo(orderState)); //状态描述
							object.put("prizeState", torderObject.getString("prizestate")); //兑奖标识(0 未开奖， 3 未中奖， 4 中大奖， 5 中小奖)
							object.put("winCode", winCode); //中奖号码
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
			logger.error("被赠送查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
}
