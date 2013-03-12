package com.ruyicai.lotserver.service.query;

import java.util.Calendar;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.BasketballUtil;

/**
 * 竟彩篮球
 * @author Administrator
 *
 */
@Service
public class JingCaiBasketballService {

	private Logger logger = Logger.getLogger(JingCaiBasketballService.class);
	
	@Autowired
	PropertiesUtil propertiesUtil;
	
	/**
	 * 查询数据分析
	 * @param clientInfo
	 * @return
	 */
	public String getDataAnalysis(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			String event = clientInfo.getEvent(); //赛事信息,如：1_20120806_1_001(类型_day_weekid_teamid)类型：1足球  0篮球
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("event="+event); //如：1_20120806_1_001(类型_day_weekid_teamid)类型：1足球  0篮球
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectJcl/getInfo";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取竞彩篮球数据分析返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!fromObject.getString("value").equals("null")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				if (valueObject!=null) {
					//解析返回数据
					BasketballUtil.parseDataAnalysis(resultJson, valueObject);
					
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
			logger.error("查询竟彩篮球数据分析发生异常", e);
		}
		responseJson.put("result", resultJson);
		return responseJson.toString();
	}
	
	/**
	 * 即时比分列表
	 * @param clientInfo
	 * @return
	 */
	public String getImmediateScore(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		StringBuffer dateBuffer = new StringBuffer(""); //日期
		String todayIndex = "8"; //当前日期的索引
		try {
			String type = clientInfo.getType(); //类型(0：全部 1：未开 2：比赛中 3：完场)
			String date = clientInfo.getDate(); //日期
			
			Calendar calendar = Calendar.getInstance();
			if (Tools.isEmpty(date)) { //客户端传过来的日期为空
				date = DateParseFormatUtil.formatYMd(calendar.getTimeInMillis()+"");
			}
			//返回给客户端的日期(前7天和后7天)
			int length = 7;
			//后7天
			for (int i = length; i > 0; i--) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, i);
				dateBuffer.append(DateParseFormatUtil.formatY_M_d(calendar.getTimeInMillis()+"")).append(";");
			}
			//前7天
			for (int i = 0; i < length; i++) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, -i);
				if (i==length-1) {
					dateBuffer.append(DateParseFormatUtil.formatY_M_d(calendar.getTimeInMillis()+""));
				} else {
					dateBuffer.append(DateParseFormatUtil.formatY_M_d(calendar.getTimeInMillis()+"")).append(";");
				}
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("day="+date); //日期
			paramStr.append("&state="+type ); //状态
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectJcl/getImmediateScores";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取竟彩篮球即时比分列表返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!fromObject.getString("value").equals("null")) {
				JSONArray valueArray = fromObject.getJSONArray("value");
				if (valueArray!=null&&valueArray.size()>0) {
					for (int i = 0; i < valueArray.size(); i++) {
						JSONObject immediateScoreObject = valueArray.getJSONObject(i);
						//解析数据
						BasketballUtil.parseImmediateScore(resultArray, immediateScoreObject);
					}
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("result", resultArray);
					responseJson.put("date", dateBuffer.toString());
					responseJson.put("todayIndex", todayIndex);
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询竞彩篮球即时比分列表发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("date", dateBuffer.toString());
		responseJson.put("todayIndex", todayIndex);
		return responseJson.toString();
	}
	
	/**
	 * 即时比分详细
	 * @param clientInfo
	 * @return
	 */
	public String getImmediateScoreDetail(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String event = clientInfo.getEvent(); //赛事信息,如：1_20120806_1_001(类型_day_weekid_teamid)类型：1足球  0篮球
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("event="+event);
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectJcl/getImmediateScore";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取竞彩篮球即时比分详细返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!fromObject.getString("value").equals("null")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				if (valueObject!=null) {
					//解析返回数据
					BasketballUtil.parseImmediateScoreDetail(responseJson, valueObject);
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询竞彩篮球即时比分详细发生异常", e);
		}
		return responseJson.toString();
	}
	
}
