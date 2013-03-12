package com.ruyicai.lotserver.service;

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
import com.ruyicai.lotserver.util.lot.CaseLotUtil;

/**
 * 自动跟单相关的Service
 * @author Administrator
 *
 */
@Service
public class AutoJoinService {
	
	private Logger logger = Logger.getLogger(AutoJoinService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	/**
	 * 查询合买发起人信息
	 * @param clientInfo
	 * @return
	 */
	public String selectCaseLotStarterInfo(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String starterUserNo = clientInfo.getStarterUserNo(); //发起人用户编号
			if (Tools.isEmpty(starterUserNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String lotNo = clientInfo.getLotNo(); //彩种编号
			
			//查询战绩和发起人名称
			String starterName = ""; //发起人名称
			JSONObject displayIcon = new JSONObject();
			
			JSONObject conditionObject = new JSONObject(); //查询条件
			conditionObject.put("EQS_userno", starterUserNo);
			conditionObject.put("EQS_lotno", lotNo);
			
			StringBuffer paramStr1 = new StringBuffer();
			paramStr1.append("condition=" + conditionObject.toString());
			String result1 = lotteryCommonService.getTuserachievements(paramStr1);
			if (!Tools.isEmpty(result1)) {
				JSONObject fromObject = JSONObject.fromObject(result1);
				if (fromObject!=null) {
					String errorCode = fromObject.getString("errorCode");
					if (errorCode!=null&&errorCode.equals("0")) {
						JSONObject valueObject = fromObject.getJSONObject("value");
						JSONArray list = valueObject.getJSONArray("list");
						if (list!=null&&list.size()>0) {
							JSONObject listObject = list.getJSONObject(0);
							//发起人
							JSONObject userInfoObject = listObject.getJSONObject("userinfo");
							starterName = CaseLotUtil.getCaseLotStarterName(userInfoObject); //发起人名字
							//战绩
							JSONObject achievementObject = listObject.getJSONObject("tuserachievement");
							displayIcon = CaseLotUtil.getCaseLotDisplayIcon(achievementObject);
						}
					}
				}
			}
			
			//定制人数
			String persons = "0";
			
			StringBuffer paramStr2 = new StringBuffer();
			paramStr2.append("&userno=" + starterUserNo); //用户编号
			paramStr2.append("&lotno=" + lotNo); //彩种编号
			
			String url = propertiesUtil.getLotteryUrl() + "autojoin/selectCountByStarter?";
			String result2 = HttpUtil.sendRequestByGet(url+paramStr2.toString(), true);
			//logger.info("查询合买定制人数返回:"+result+",paramStr:"+paramStr.toString());
			if (!Tools.isEmpty(result2)) {
				JSONObject fromObject = JSONObject.fromObject(result2);
				if (fromObject!=null) {
					String errorCode = fromObject.getString("errorCode");
					if (errorCode!=null&&errorCode.equals("0")) {
						persons = fromObject.getString("value");
					}
				}
			}
			
			responseJson.put(Constants.error_code, ErrorCode.success.value());
			responseJson.put(Constants.message, "查询成功");
			responseJson.put("starter", starterName); //发起人名称
			responseJson.put("displayIcon", displayIcon); //专家战绩
			responseJson.put("persons", persons); //定制人数
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询合买发起人信息发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 创建自动跟单
	 * @param clientInfo
	 * @return
	 */
	public String createAutoJoin(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String lotNo= clientInfo.getLotNo(); //彩种
			String starterUserNo = clientInfo.getStarterUserNo(); //要定制的用户编号
			String times = clientInfo.getTimes(); //跟单次数
			String joinAmt = clientInfo.getJoinAmt(); //跟单金额
			String percentNum = clientInfo.getPercent(); //跟单百分比
			String maxNum = clientInfo.getMaxAmt(); //百分比跟单最大金额
			String joinType = clientInfo.getJoinType(); //跟单类型(0:金额跟单,1:百分比跟单)
			String safeAmt = clientInfo.getSafeAmt(); //止损金额
			String forceJoin = clientInfo.getForceJoin(); //是否强制跟单(1:强制跟单,0:不强制跟单)
			//默认不强制跟单
			if (Tools.isEmpty(forceJoin)) {
				forceJoin = "0";
			}
			//渠道号
			String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId());
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno=" + userNo);
			paramStr.append("&lotno=" + lotNo);
			paramStr.append("&starter=" + starterUserNo);
			paramStr.append("&times=" + times);
			paramStr.append("&joinAmt=" + joinAmt);
			paramStr.append("&percentNum=" + percentNum);
			paramStr.append("&maxNum=" + maxNum);
			paramStr.append("&joinType=" + joinType);
			paramStr.append("&safeAmt=" + safeAmt);
			paramStr.append("&forceJoin=" + forceJoin);
			paramStr.append("&channel=" + newCoopId);
			
			String url = propertiesUtil.getLotteryUrl() + "autojoin/createAutoJoin";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("创建自动跟单返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "提交成功");
				} else if (errorCode!=null&&errorCode.equals("1120003")) {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "不能自动跟单自己的合买");
				} else {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "提交失败");
				}
			} else {
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "提交失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("创建自动跟单发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 查询定制跟单
	 * @param clientInfo
	 * @return
	 */
	public String selectAutoJoin(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
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
			
			JSONObject conditionObject = new JSONObject(); //查询条件
			conditionObject.put("EQS_userno", userNo);
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("condition=" + conditionObject.toString());
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxresult)); //开始行数
			paramStr.append("&endLine=" + maxresult); //取多少条记录
			
			String url = propertiesUtil.getLotteryUrl() + "autojoin/selectAutoJoin";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("查询定制跟单返回:"+result+",paramStr:"+paramStr.toString());
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
							//发起人Object
							JSONObject starterObject = listObject.getJSONObject("starter");
							String starterUserNo = starterObject.getString("userno"); //用户编号
							String starterName = CaseLotUtil.getCaseLotStarterName(starterObject); //发起人名字
							//专家战绩
							JSONObject achievementObject= listObject.getJSONObject("tuserachievement");
							JSONObject displayIcon = CaseLotUtil.getCaseLotDisplayIcon(achievementObject);
							//自动跟单Object
							JSONObject autoJoinObject = listObject.getJSONObject("autoJoin");
							String id = autoJoinObject.getString("id"); //编号
							String lotNo = autoJoinObject.getString("lotno"); //彩种
							String joinAmt = autoJoinObject.getString("joinAmt"); //跟单金额
							String safeAmt = autoJoinObject.getString("safeAmt"); //止损金额
							String times = autoJoinObject.getString("times"); //跟单次数
							String percentNum = autoJoinObject.getString("percentNum"); //跟单百分比
							String maxNum = autoJoinObject.getString("maxNum"); //百分比跟单最大金额
							String joinType = autoJoinObject.getString("joinType"); //跟单类型(0:金额跟单,1:百分比跟单)
							String forceJoin = autoJoinObject.getString("forceJoin"); //是否强制跟单(1:强制跟单,0:不强制跟单)
							String createTime = autoJoinObject.getString("createTime"); //创建时间
							String autoJoinState = autoJoinObject.getString("autoJoinState"); //状态
							
							String lotName = MemoUtil.getLotNameByLotNo(lotNo); //彩种名称
							
							JSONObject object = new JSONObject();
							object.put("id", id);
							object.put("starter", starterName);
							object.put("starterUserNo", starterUserNo);
							object.put("displayIcon", displayIcon);
							object.put("lotNo", lotNo);
							object.put("lotName", lotName);
							object.put("times", times);
							object.put("joinAmt", joinAmt);
							object.put("safeAmt", safeAmt);
							object.put("maxAmt", maxNum);
							object.put("percent", percentNum);
							object.put("joinType", joinType);
							object.put("forceJoin", forceJoin);
							object.put("createTime", DateParseFormatUtil.formatDateTime(createTime));
							object.put("state", autoJoinState);
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
			logger.error("查询定制跟单发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("totalPage", totalPage);
		return responseJson.toString();
	}
	
	/**
	 * 取消自动跟单
	 * @param clientInfo
	 * @return
	 */
	public String cancelAutoJoin(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String id = clientInfo.getId(); //跟单ID
			if (Tools.isEmpty(id)) { //参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("id=" + id);
			
			String url = propertiesUtil.getLotteryUrl() + "autojoin/cancelAutoJoin";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("取消自动跟单返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "取消成功");
				} else {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "取消失败");
				}
			} else {
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "取消失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("取消自动跟单发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 更新自动跟单
	 * @param clientInfo
	 * @return
	 */
	public String updateAutoJoin(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String id = clientInfo.getId(); //跟单ID
			if (Tools.isEmpty(id)) { //参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String joinAmt = clientInfo.getJoinAmt(); //跟单金额
			String percentNum = clientInfo.getPercent(); //跟单百分比
			String maxNum = clientInfo.getMaxAmt(); //百分比跟单最大金额
			String joinType = clientInfo.getJoinType(); //跟单类型(0:金额跟单,1:百分比跟单)
			String safeAmt = clientInfo.getSafeAmt(); //止损金额
			String forceJoin = clientInfo.getForceJoin(); //是否强制跟单(1:强制跟单,0:不强制跟单)
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("id=" + id);
			paramStr.append("&joinAmt=" + joinAmt);
			paramStr.append("&percentNum=" + percentNum);
			paramStr.append("&maxNum=" + maxNum);
			paramStr.append("&joinType=" + joinType);
			paramStr.append("&safeAmt=" + safeAmt);
			paramStr.append("&forceJoin=" + forceJoin);
			
			String url = propertiesUtil.getLotteryUrl() + "autojoin/updateAutoJoin";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("更新自动跟单返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "提交成功");
				} else {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "提交失败");
				}
			} else {
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "提交失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("更新自动跟单发生异常", e);
		}
		return responseJson.toString();
	}
	
}
