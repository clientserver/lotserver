package com.ruyicai.lotserver.service.query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.DataAnalysisService;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.FootballUtil;

/**
 * 足彩
 * @author Administrator
 *
 */
@Service
public class ZuCaiService {

	private Logger logger = Logger.getLogger(ZuCaiService.class);
	
	@Autowired
	private DataAnalysisService dataAnalysisService;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 查询对阵
	 * @param clientInfo
	 * @return
	 */
	public String getDuiZhen(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种编号
			String batchCode = clientInfo.getBatchCode(); //期号
			if (Tools.isEmpty(lotNo)||Tools.isEmpty(batchCode)) { //返回参数错误 
				return Tools.paramError(clientInfo.getImei());
			}
			
			String result = dataAnalysisService.getZuCaiDuiZhen(lotNo, batchCode);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONArray list = fromObject.getJSONArray("value");
					if (list!=null&&list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							JSONObject listObject = list.getJSONObject(i);
							String teamId = listObject.getString("teamId"); //场次编号
							String leagueName = listObject.getString("leagueName"); //联赛名称
							String homeTeam = listObject.getString("homeTeam"); //主队名称
							String guestTeam = listObject.getString("guestTeam"); //客队名称
							String matchTime = listObject.getString("matchTime"); //比赛时间
							String homeWinAverageOuPei = listObject.getString("homeWinAverageOuPei"); //主胜平均欧赔
							String standoffAverageOuPei = listObject.getString("standoffAverageOuPei"); //平局平均欧赔
							String guestWinAverageOuPei = listObject.getString("guestWinAverageOuPei"); //客胜平均欧赔
							
							JSONObject object = new JSONObject();
							object.put("teamId", teamId); //场次编号
							object.put("leagueName", leagueName); //联赛名称
							object.put("homeTeam", homeTeam); //主队名称
							object.put("guestTeam", guestTeam); //客队名称
							object.put("matchTime", matchTime); //比赛时间
							object.put("homeWinAverageOuPei", homeWinAverageOuPei); //主胜平均欧赔
							object.put("standoffAverageOuPei", standoffAverageOuPei); //平局平均欧赔
							object.put("guestWinAverageOuPei", guestWinAverageOuPei); //客胜平均欧赔
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
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询足彩对阵发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 查询数据分析
	 * @param clientInfo
	 * @return
	 */
	public String getDataAnalysis(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONObject resultJson = new JSONObject();
		try {
			String event = clientInfo.getEvent(); //赛事信息,如：T01003_2012178_1(彩种_期号_场次)
			if (Tools.isEmpty(event)) { //赛事信息为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			//如果是任选九则转成胜负彩
			if (event.startsWith(LotType.ZC_RX9.lotNo())) { 
				event = event.replaceFirst(LotType.ZC_RX9.lotNo(), LotType.ZC_SFC.lotNo());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("event="+event);
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectZc/getInfo";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取足彩数据分析返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!fromObject.getString("value").equals("null")) {
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
				JSONObject valueObject = fromObject.getJSONObject("value");
				//解析返回数据
				FootballUtil.parseDataAnalysis(resultJson, valueObject);
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询足彩数据分析发生异常", e);
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
		try {
			String lotNo = clientInfo.getLotNo(); //彩种编号
			String batchCode = clientInfo.getBatchCode(); //期号
			String type = clientInfo.getType(); //类型(0：全部 1：未开 2：比赛中 3：完场)
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("lotno="+lotNo);
			paramStr.append("&batchcode="+batchCode);
			paramStr.append("&state="+type );
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectZc/getImmediateScores";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取足彩即时比分列表返回:"+result+",paramStr:"+paramStr.toString());
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
						FootballUtil.parseImmediateScoreList(resultArray, immediateScoreObject);
					}
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("result", resultArray);
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询足彩即时比分列表发生异常", e);
		}
		responseJson.put("result", resultArray);
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
			String event = clientInfo.getEvent(); //赛事信息,如：T01003_2012178_1(彩种_期号_场次)
			if (Tools.isEmpty(event)) { //赛事信息为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			//如果是任选九则转成胜负彩
			if (event.startsWith(LotType.ZC_RX9.lotNo())) { 
				event = event.replaceFirst(LotType.ZC_RX9.lotNo(), LotType.ZC_SFC.lotNo());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("event="+event);
			
			String url = propertiesUtil.getDataAnalysisUrl() + "selectZc/getImmediateScore";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("获取竞彩足球即时比分详细返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!fromObject.getString("value").equals("null")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				if (valueObject!=null) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					//解析数据
					FootballUtil.parseImmediateScoreDetail(responseJson, valueObject);
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询足彩即时比分详细发生异常", e);
		}
		return responseJson.toString();
	}
	
}
