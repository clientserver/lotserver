package com.ruyicai.lotserver.util.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.lottype.JingCaiBetCodeParseCommon;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竞彩混合过关注码解析
 * @author Administrator
 *
 */
@Service
public class JingCaiHhBetCodeParseUtil {

	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 获得注码解析数组(过滤重复的比赛)
	 * @param lotNo
	 * @param orderInfo
	 * @param tlotsValueObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getParseBetCodeList(JSONObject tlotsValueObject) {
		JSONArray resultArray = new JSONArray();
		Map<String, Map<String, Object>> infoMap = new HashMap<String, Map<String, Object>>();
		List<String> plays = new ArrayList<String>();
		if (tlotsValueObject!=null) {
			JSONArray list = tlotsValueObject.getJSONArray("list");
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tlotObject = list.getJSONObject(i);
					//注码(502@20130129|2|001|J00001|3^20130129|2|002|J00002|52^)
					String betCode = tlotObject.getString("betcode");
					//赔率(20130129*2*001*|J00001|3:2.700|^20130129*2*002*|J00002|52:1000|^)
					String peiLvString = tlotObject.getString("peilu");
					
					//玩法
					String[] split1 = betCode.split("@");
					String wanfa = split1[0];
					if (!plays.contains(wanfa)) {
						plays.add(wanfa);
					}
					
					String[] split2 = split1[1].split("\\^");
					for (String string : split2) {
						String[] split3 = string.split("\\|");
						String day = split3[0]; //日期
						String weekId = split3[1]; //星期
						String teamId = split3[2]; //场次
						String lotNo = split3[3]; //彩种
						String code = split3[4]; //注码
						
						//获取比赛信息
						JSONObject matchesValueObject = cacheCommonUtil.getJingCaiMatchesValueObject(lotNo, day, weekId, teamId);
						if (matchesValueObject==null||Tools.isEmpty(matchesValueObject.getString("matches"))
								||matchesValueObject.getString("matches").equals("null")) {
							continue;
						}
						JSONObject matchInfoJson = JingCaiBetCodeParseCommon.getMatchInfoJson(matchesValueObject);
						String homeTeam = matchInfoJson.getString("homeTeam"); //主队
						String guestTeam = matchInfoJson.getString("guestTeam"); //客队
						String homeScore = matchInfoJson.getString("homeScore"); //主队全场比分
						String guestScore = matchInfoJson.getString("guestScore"); //客队全场比分
						String homeHalfScore = matchInfoJson.getString("homeHalfScore"); //主队半场比分
						String guestHalfScore = matchInfoJson.getString("guestHalfScore"); //客队半场比分
						String letPoint = matchInfoJson.getString("letPoint"); //开奖公告里的让分
						String basePoint = matchInfoJson.getString("basePoint"); //开奖公告里的预设总分
						
						String id = day+"*"+weekId+"*"+teamId+"*"; //赛事信息
						Map<String, Object> teamMap = infoMap.get(id);
						if (teamMap==null) {
							teamMap = new HashMap<String, Object>();
							teamMap.put("day", day);
							teamMap.put("weekId", weekId);
							teamMap.put("teamId", teamId);
							teamMap.put("homeTeam", homeTeam);
							teamMap.put("guestTeam", guestTeam);
							teamMap.put("homeScore", homeScore);
							teamMap.put("guestScore", guestScore);
							teamMap.put("homeHalfScore", homeHalfScore);
							teamMap.put("guestHalfScore", guestHalfScore);
						}
						JingCaiBetCodeParseCommon.putTeamMap(lotNo, id+"|"+lotNo, code, peiLvString, teamMap, wanfa, letPoint, basePoint);
						infoMap.put(id, teamMap);
					}
				}
			}
		}
		//解析玩法
		String play = JingCaiUtil.getJingCaiPlayByList(plays);
		//循环Map
		if (infoMap!=null) {
			//排序
			infoMap = Tools.sortMapByKey(infoMap, false);
			for(Map.Entry<String, Map<String, Object>> entry : infoMap.entrySet()) {
				//String key = entry.getKey();
				Map<String, Object> value = entry.getValue();
				
				JSONObject betContentJson = JingCaiBetCodeParseCommon.getBetContentJson(value, true);
				String betContentHtml = betContentJson.getString("betContentHtml"); //投注内容(html)
				String letScore = betContentJson.getString("letScore"); //让分(竞彩篮球让分胜负)
				String totalScore = betContentJson.getString("totalScore"); //总分盘(竞彩篮球大小分)
				
				JSONObject object = new JSONObject();
				object.put("play", play); //玩法
				object.put("day", (String)value.get("day")); //日期
				object.put("weekId", (String)value.get("weekId")); //星期
				object.put("teamId", (String)value.get("teamId")); //赛事编号
				object.put("homeTeam", (String)value.get("homeTeam")); //主队
				object.put("guestTeam", (String)value.get("guestTeam")); //客队
				object.put("homeScore", (String)value.get("homeScore")); //主队比分
				object.put("guestScore", (String)value.get("guestScore")); //客队比分
				object.put("letScore", letScore); //让分(竞彩篮球让分胜负)
				object.put("totalScore", totalScore); //总分盘(竞彩篮球大小分)
				object.put("betContentHtml", betContentHtml); //投注内容(html)
				object.put("isDanMa", ""); //是否是胆码
				resultArray.add(object);
			}
		}
		return resultArray;
	}
	
	/**
	 * 获取赛事信息
	 * @param matchesValueObject
	 * @return
	 */
	/*private JSONObject getMatchInfoJson(JSONObject matchesValueObject) {
		JSONObject resultJson = new JSONObject();
		//队伍名称
		JSONObject matchesObject = matchesValueObject.getJSONObject("matches");
		String team = matchesObject.getString("team");
		String homeTeam = JingCaiUtil.getTeamName(team).getString("homeTeam"); //主队
		String guestTeam = JingCaiUtil.getTeamName(team).getString("guestTeam"); //客队
		//比赛结果
		String homeScore = ""; //主队全场比分
		String guestScore = ""; //客队全场比分
		String homeHalfScore = ""; //主队半场比分
		String guestHalfScore = ""; //客队半场比分
		String letPoint = ""; //开奖公告里的让分
		String basePoint = ""; //开奖公告里的预设总分
		String resultString = matchesValueObject.getString("result");
		if (!Tools.isEmpty(resultString)&&!resultString.equals("null")) {
			JSONObject resultObject = matchesValueObject.getJSONObject("result");
			letPoint = resultObject.getString("letpoint"); //让分
			basePoint = resultObject.getString("basepoint"); //预设总分
			String result = resultObject.getString("result"); //全场比分
			homeScore = JingCaiUtil.getScoreByResult(result).getString("homeScore");
			guestScore = JingCaiUtil.getScoreByResult(result).getString("guestScore");
			
			String halfResult = resultObject.getString("firsthalfresult"); //半场比分
			homeHalfScore = JingCaiUtil.getScoreByResult(halfResult).getString("homeScore");
			guestHalfScore = JingCaiUtil.getScoreByResult(halfResult).getString("guestScore");
		}
		resultJson.put("homeTeam", homeTeam);
		resultJson.put("guestTeam", guestTeam);
		resultJson.put("homeScore", homeScore);
		resultJson.put("guestScore", guestScore);
		resultJson.put("homeHalfScore", homeHalfScore);
		resultJson.put("guestHalfScore", guestHalfScore);
		resultJson.put("letPoint", letPoint);
		resultJson.put("basePoint", basePoint);
		return resultJson;
	}*/
	
	/**
	 * 设置TeamMap
	 * @param lotNo
	 * @param id
	 * @param code
	 * @param peiLvString
	 * @param teamMap
	 * @param play
	 * @param letPoint
	 * @param basePoint
	 */
	/*private void putTeamMap(String lotNo, String id, String code, String peiLvString, Map<String, Object> teamMap, 
			String play, String letPoint, String basePoint) {
		if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
			Spf.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
			Bf.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
			Jqs.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
			BqcJc.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
			Sf.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
			Rfsf.putMap(id, code, peiLvString, teamMap, play, letPoint);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
			SfcJc.putMap(id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
			Dxf.putMap(id, code, peiLvString, teamMap, play, basePoint);
		}
	}*/
	
	/**
	 * 获取解析后的注码
	 * @param lotNo
	 * @param value
	 * @return
	 */
	/*private JSONObject getBetContentJson(String lotNo, Map<String, Object> value) {
		JSONObject resultObject = new JSONObject();
		String betContentHtml = ""; //投注内容(html)
		String letScore = ""; //让分(竞彩篮球让分胜负)
		String totalScore = ""; //总分盘(竞彩篮球大小分)
		if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
			betContentHtml = Spf.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
			betContentHtml = Bf.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
			betContentHtml = Jqs.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
			betContentHtml = BqcJc.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
			betContentHtml = Sf.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
			JSONObject jsonObject = Rfsf.getBetContentByMap(value, true);
			betContentHtml = jsonObject.getString("betContent");
			letScore = jsonObject.getString("letScore");
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
			betContentHtml = SfcJc.getBetContentByMap(value);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
			JSONObject jsonObject = Dxf.getBetContentByMap(value);
			betContentHtml = jsonObject.getString("betContent");
			totalScore = jsonObject.getString("totalScore");
		}
		resultObject.put("betContentHtml", betContentHtml);
		resultObject.put("letScore", letScore);
		resultObject.put("totalScore", totalScore);
		return resultObject;
	}*/
	
}
