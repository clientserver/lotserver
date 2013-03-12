package com.ruyicai.lotserver.service.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.BasketballUtil;
import com.ruyicai.lotserver.util.lot.FootballUtil;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竟彩
 * @author Administrator
 *
 */
@Service
public class JingCaiService {
	
	private Logger logger = Logger.getLogger(JingCaiService.class);
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 获取竞彩对阵
	 * @param clientInfo
	 * @return
	 */
	public String getJingCaiDuiZhen(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		try {
			String jingcaiType = clientInfo.getJingcaiType(); //竞彩类型(足球1;篮球0)
			String jingcaiValueType = clientInfo.getJingcaiValueType(); //( 0 单关 1 多关 )
			if (Tools.isEmpty(jingcaiType)||Tools.isEmpty(jingcaiValueType)) { //类型为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			String url = propertiesUtil.getLotteryUrl() + "select/getjingcaiduizhen?type="+jingcaiType;
			String result = HttpUtil.sendRequestByGet(url, true);
			//logger.info("获取竞彩对阵返回:"+result+",url:"+url);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					//获取赔率
					String peiLvXML = lotteryCommonService.queryJingCaiPeiLv(jingcaiType, jingcaiValueType);
					
					JSONArray valueArray = fromObject.getJSONArray("value");
					List<String> leagueList = new ArrayList<String>(); //存放联赛的List
					for (int i = 0; i < valueArray.size(); i++) {
						JSONObject valueObject = valueArray.getJSONObject(i);
						if (valueObject!=null&&valueObject.toString().equals("null")) { //防止数组里有null的情况
							continue;
						}
						String day = valueObject.getString("day"); //日期
						String dayForamt = ""; //格式化后的日期
						if (day!=null && day.trim().length()>=8) {
							dayForamt = day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6,8);
						}
						String weekid = valueObject.getString("weekid"); //星期
						String teamid = valueObject.getString("teamid"); //场次
						String team = valueObject.getString("team"); //主队:客队
						String endTime = valueObject.getString("endtime"); //停售时间
						String unsupport = valueObject.getString("unsupport"); //不支持的玩法、过关方式
						String league = valueObject.getString("league"); //联赛
						String shortName = valueObject.getString("shortname"); //联赛简称
						//如果简称不为空则返回简称
						league = JingCaiUtil.getLeagueShortName(league, shortName);
						leagueList.add(league);
						
						JSONObject object = new JSONObject();
						object.put("day", day); //日期
						object.put("dayForamt", dayForamt); //格式化后的日期
						object.put("league", league); //联赛
						object.put("team", team); //主队:客队
						object.put("teamId", teamid); //场次
						object.put("weekId", weekid); //星期
						object.put("week", JingCaiUtil.getWeekByWeekId(weekid)); //格式化后的星期
						object.put("endTime", DateParseFormatUtil.formatDateTime(endTime)); //停售时间
						object.put("unsupport", unsupport); //不支持的玩法、过关方式
						//获取赔率
						if (jingcaiType.trim().equals("1")) { //足球
							if (!Tools.isEmpty(peiLvXML)&&!peiLvXML.equals("null")) {
								String id = day+"_"+weekid+"_"+teamid;
								FootballUtil.parseXMLForJingcaiPeiLvZQ(object, peiLvXML, id);
							} else {
								//胜平负
								object.put("v3", "");
								object.put("v1", "");
								object.put("v0", "");
								object.put("letPoint", "");
								//比分
								object.put("score_v00", "");
								object.put("score_v01", "");
								object.put("score_v02", "");
								object.put("score_v03", "");
								object.put("score_v04", "");
								object.put("score_v05", "");
								object.put("score_v09", "");
								object.put("score_v10", "");
								object.put("score_v11", "");
								object.put("score_v12", "");
								object.put("score_v13", "");
								object.put("score_v14", "");
								object.put("score_v15", "");
								object.put("score_v20", "");
								object.put("score_v21", "");
								object.put("score_v22", "");
								object.put("score_v23", "");
								object.put("score_v24", "");
								object.put("score_v25", "");
								object.put("score_v30", "");
								object.put("score_v31", "");
								object.put("score_v32", "");
								object.put("score_v33", "");
								object.put("score_v40", "");
								object.put("score_v41", "");
								object.put("score_v42", "");
								object.put("score_v50", "");
								object.put("score_v51", "");
								object.put("score_v52", "");
								object.put("score_v90", "");
								object.put("score_v99", "");
								//总进球数
								object.put("goal_v0", "");
								object.put("goal_v1", "");
								object.put("goal_v2", "");
								object.put("goal_v3", "");
								object.put("goal_v4", "");
								object.put("goal_v5", "");
								object.put("goal_v6", "");
								object.put("goal_v7", "");
								//半全场
								object.put("half_v00", "");
								object.put("half_v01", "");
								object.put("half_v03", "");
								object.put("half_v10", "");
								object.put("half_v11", "");
								object.put("half_v13", "");
								object.put("half_v30", "");
								object.put("half_v31", "");
								object.put("half_v33", "");
							}
						} else if (jingcaiType.trim().equals("0")) { //篮球
							if (!Tools.isEmpty(peiLvXML)) {
								String id = day+"_"+weekid+"_"+teamid;
								BasketballUtil.parseXMLForJingcaiPeiLvLQ(object, peiLvXML, id);
							} else {
								//玩法:胜负
								object.put("v0", "");
								object.put("v3", "");
								//玩法:让分胜负
								object.put("letVs_v0", "");
								object.put("letVs_v3", "");
								object.put("letPoint", "");
								//玩法:大小分
								object.put("g", "");
								object.put("l", "");
								object.put("basePoint", "");
								//玩法:胜分差
								object.put("v01", "");
								object.put("v02", "");
								object.put("v03", "");
								object.put("v04", "");
								object.put("v05", "");
								object.put("v06", "");
								object.put("v11", "");
								object.put("v12", "");
								object.put("v13", "");
								object.put("v14", "");
								object.put("v15", "");
								object.put("v16", "");
							}
						}
						resultArray.add(object);
					}
					//所有的联赛(去除重复的)
					StringBuffer leagues = new StringBuffer();
					List<String> newLeagueList = new ArrayList<String>(new HashSet<String>(leagueList));
					for (int i = 0; i < newLeagueList.size(); i++) {
						if (i==newLeagueList.size()-1) {
							leagues.append(newLeagueList.get(i));
						} else {
							leagues.append(newLeagueList.get(i)).append(";");
						}
					}
					responseJson.put("leagues", leagues.toString());
					
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
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取竞彩对阵发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	

	/**
	 * 按日期查询获取竞彩对阵
	 * @param clientInfo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public String getJingCaiDuiZhenLimit(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		try {
			String jingcaiType = clientInfo.getJingcaiType(); //竞彩类型(足球1;篮球0)
			String jingcaiValueType = clientInfo.getJingcaiValueType(); //( 0 单关 1 多关 )
			if (Tools.isEmpty(jingcaiType)||Tools.isEmpty(jingcaiValueType)) { //类型为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			String url = propertiesUtil.getLotteryUrl() + "select/getjingcaiduizhenlimit?type="+jingcaiType;
			String result = HttpUtil.sendRequestByGet(url, true);
			//logger.info("按日期查询竞彩对阵返回:"+result+",url:"+url);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					//获取赔率
					String peiLvXML = lotteryCommonService.queryJingCaiPeiLv(jingcaiType, jingcaiValueType);
					
					JSONObject valueObject = fromObject.getJSONObject("value");
					List<String> leagueList = new ArrayList<String>(); //存放联赛的List
					Iterator keyIterator = valueObject.keys();
					while (keyIterator.hasNext()) {
						JSONArray dateJsonArray = new JSONArray();
						String key = (String)keyIterator.next();
						JSONArray dateArray = valueObject.getJSONArray(key);
						for (int i = 0; i < dateArray.size(); i++) {
							JSONObject teamObject = dateArray.getJSONObject(i);
							if (teamObject!=null&&teamObject.toString().equals("null")) { //防止数组中有null的情况
								continue;
							}
							String day = teamObject.getString("day"); //日期
							String dayForamt = ""; //格式化后的日期
							if (day!=null && day.trim().length()>=8) {
								dayForamt = day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6,8);
							}
							String weekid = teamObject.getString("weekid"); //星期
							String teamid = teamObject.getString("teamid"); //场次
							String team = teamObject.getString("team"); //主队:客队
							String endTime = teamObject.getString("endtime"); //停售时间
							String unsupport = teamObject.getString("unsupport"); //不支持的玩法、过关方式
							String league = teamObject.getString("league"); //联赛
							String shortName = teamObject.getString("shortname"); //联赛简称
							//如果简称不为空则返回简称
							league = JingCaiUtil.getLeagueShortName(league, shortName);
							leagueList.add(league);
							
							JSONObject object = new JSONObject();
							object.put("day", day); //日期
							object.put("dayForamt", dayForamt); //格式化后的日期
							object.put("league", league); //联赛
							object.put("team", team); //主队:客队
							object.put("teamId", teamid); //场次
							object.put("weekId", weekid); //星期
							object.put("week", JingCaiUtil.getWeekByWeekId(weekid)); //格式化后的星期
							object.put("endTime", DateParseFormatUtil.formatDateTime(endTime)); //停售时间
							object.put("unsupport", unsupport); //不支持的玩法、过关方式
							//获取赔率
							if (jingcaiType.trim().equals("1")) { //足球
								if (!Tools.isEmpty(peiLvXML)&&!peiLvXML.equals("null")) {
									String id = day+"_"+weekid+"_"+teamid;
									FootballUtil.parseXMLForJingcaiPeiLvZQ(object, peiLvXML, id);
								} else {
									//胜平负
									object.put("v3", "");
									object.put("v1", "");
									object.put("v0", "");
									object.put("letPoint", "");
									//比分
									object.put("score_v00", "");
									object.put("score_v01", "");
									object.put("score_v02", "");
									object.put("score_v03", "");
									object.put("score_v04", "");
									object.put("score_v05", "");
									object.put("score_v09", "");
									object.put("score_v10", "");
									object.put("score_v11", "");
									object.put("score_v12", "");
									object.put("score_v13", "");
									object.put("score_v14", "");
									object.put("score_v15", "");
									object.put("score_v20", "");
									object.put("score_v21", "");
									object.put("score_v22", "");
									object.put("score_v23", "");
									object.put("score_v24", "");
									object.put("score_v25", "");
									object.put("score_v30", "");
									object.put("score_v31", "");
									object.put("score_v32", "");
									object.put("score_v33", "");
									object.put("score_v40", "");
									object.put("score_v41", "");
									object.put("score_v42", "");
									object.put("score_v50", "");
									object.put("score_v51", "");
									object.put("score_v52", "");
									object.put("score_v90", "");
									object.put("score_v99", "");
									//总进球数
									object.put("goal_v0", "");
									object.put("goal_v1", "");
									object.put("goal_v2", "");
									object.put("goal_v3", "");
									object.put("goal_v4", "");
									object.put("goal_v5", "");
									object.put("goal_v6", "");
									object.put("goal_v7", "");
									//半全场
									object.put("half_v00", "");
									object.put("half_v01", "");
									object.put("half_v03", "");
									object.put("half_v10", "");
									object.put("half_v11", "");
									object.put("half_v13", "");
									object.put("half_v30", "");
									object.put("half_v31", "");
									object.put("half_v33", "");
								}
							} else if (jingcaiType.trim().equals("0")) { //篮球
								if (!Tools.isEmpty(peiLvXML)) {
									String id = day+"_"+weekid+"_"+teamid;
									BasketballUtil.parseXMLForJingcaiPeiLvLQ(object, peiLvXML, id);
								} else {
									//玩法:胜负
									object.put("v0", "");
									object.put("v3", "");
									//玩法:让分胜负
									object.put("letVs_v0", "");
									object.put("letVs_v3", "");
									object.put("letPoint", "");
									//玩法:大小分
									object.put("g", "");
									object.put("l", "");
									object.put("basePoint", "");
									//玩法:胜分差
									object.put("v01", "");
									object.put("v02", "");
									object.put("v03", "");
									object.put("v04", "");
									object.put("v05", "");
									object.put("v06", "");
									object.put("v11", "");
									object.put("v12", "");
									object.put("v13", "");
									object.put("v14", "");
									object.put("v15", "");
									object.put("v16", "");
								}
							}
							dateJsonArray.add(object);
						}
						resultArray.add(dateJsonArray);
					}
					//所有的联赛(去除重复的)
					StringBuffer leagues = new StringBuffer();
					List<String> newLeagueList = new ArrayList<String>(new HashSet<String>(leagueList));
					for (int i = 0; i < newLeagueList.size(); i++) {
						if (i==newLeagueList.size()-1) {
							leagues.append(newLeagueList.get(i));
						} else {
							leagues.append(newLeagueList.get(i)).append(";");
						}
					}
					responseJson.put("leagues", leagues.toString());
					
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
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("按日期查询获取竞彩对阵发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获取竞彩赛果
	 * @param clientInfo
	 * @return
	 */
	public String getJingCaiResult(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		StringBuffer dateBuffer = new StringBuffer("");
		try {
			String jingcaiType = clientInfo.getJingcaiType(); //竞彩类型
			if (Tools.isEmpty(jingcaiType)) { //类型为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			String date = clientInfo.getDate(); //日期
			Calendar calendar = Calendar.getInstance();
			if (Tools.isEmpty(date)) { //客户端传过来的日期为空
				calendar.add(Calendar.DATE, -1);
				date = DateParseFormatUtil.formatYMd(calendar.getTime().getTime()+"");
			}
			//返回给客户端的日期(前7天)
			int length = 8;
			for (int i = 1; i < length; i++) {
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, -i);
				if (i==length-1) {
					dateBuffer.append(DateParseFormatUtil.formatY_M_d(calendar.getTime().getTime()+""));
				} else {
					dateBuffer.append(DateParseFormatUtil.formatY_M_d(calendar.getTime().getTime()+"")).append(";");
				}
			}
			
			String url = propertiesUtil.getLotteryUrl() + "select/getjingcairesult?type="+jingcaiType+"&date="+date;
			String resultString = HttpUtil.sendRequestByGet(url, true);
			//logger.info("获取竞彩赛果返回:"+resultString+",url:"+url);
			if (Tools.isEmpty(resultString)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(resultString);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONArray valueArray = fromObject.getJSONArray("value");
					for (int i = 0; i < valueArray.size(); i++) {
						JSONObject valueObject = valueArray.getJSONObject(i);
						
						JSONObject resultObject = valueObject.getJSONObject("result");
						String letPoint = resultObject.getString("letpoint"); //让球
						String score = resultObject.getString("result"); //全场比分(主:客)
						if (Tools.isEmpty(score)||score.trim().equals("null")) {
							continue;
						}
						String result = JingCaiUtil.getJingCaiResultByScore(score, false);; //比赛结果
						
						String firsthalfscore = resultObject.getString("firsthalfresult"); //半场比分(主:客) 
						if (firsthalfscore==null||firsthalfscore.trim().equals("null")) {
							firsthalfscore = "";
						}
						
						if (jingcaiType!=null && jingcaiType.equals("1")) { //足球
							score = "("+firsthalfscore+")"+score; //比分(主:客)
						}
						
						JSONObject matchesObject = valueObject.getJSONObject("matches");
						String weekid = matchesObject.getString("weekid"); //星期
						String teamid = matchesObject.getString("teamid"); //场次
						String team = matchesObject.getString("team"); //主队:客队
						String time = matchesObject.getString("time"); //比赛时间
						String league = matchesObject.getString("league"); //联赛
						String day = matchesObject.getString("day");
						String dayForamt = "";
						if (day!=null && day.trim().length()>=8) {
							dayForamt = day.substring(0,4)+"-"+day.substring(4,6)+"-"+day.substring(6,8);
						}
						
						JSONObject object = new JSONObject();
						object.put("day", day); //日期
						object.put("dayForamt", dayForamt); //格式化日期
						object.put("weekId", weekid); //星期
						object.put("week", JingCaiUtil.getWeekByWeekId(weekid)); //格式化日期
						object.put("teamId", teamid); //场次
						object.put("time", DateParseFormatUtil.formatTime(time)); //比赛时间
						object.put("league", league); //联赛
						object.put("team", team); //主队:客队
						object.put("letPoint", letPoint); //让球
						object.put("score", score); //比分(主:客)
						object.put("result", result); //比赛结果
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
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取竞彩赛果发生异常", e);
		}
		responseJson.put("result", resultArray);
		responseJson.put("date", dateBuffer.toString());
		return responseJson.toString();
	}
	
}
