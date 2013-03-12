package com.ruyicai.lotserver.util.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.JingCaiBetCodeParseCommon;
import com.ruyicai.lotserver.lottype.jc.lq.Dxf;
import com.ruyicai.lotserver.lottype.jc.lq.Rfsf;
import com.ruyicai.lotserver.lottype.jc.lq.Sf;
import com.ruyicai.lotserver.lottype.jc.lq.SfcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Bf;
import com.ruyicai.lotserver.lottype.jc.zq.BqcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Jqs;
import com.ruyicai.lotserver.lottype.jc.zq.Spf;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 竞彩注码解析
 * 
 * @author Administrator
 * 
 */
@Service
public class JingCaiBetCodeParseUtil {

	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 注码解析
	 * @param lotNo
	 * @param userNo
	 * @param orderId
	 * @param multiple
	 * @param isCaseLot
	 * @return
	 */
	public JSONArray parseBetCode(String lotNo, String orderInfo, JSONObject tlotsValueObject, boolean isHtml) {
		JSONArray resultArray = new JSONArray();
		
		String lineBreak = "\n"; //换行符
		if (isHtml) { //html格式
			lineBreak = "<br/>";
		}
		if (tlotsValueObject!=null) {
			JSONArray list = tlotsValueObject.getJSONArray("list");
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tlotObject = list.getJSONObject(i);
					String betCode = tlotObject.getString("betcode"); //注码
					String peiLvString = tlotObject.getString("peilu"); //赔率(20120520*7*001*|1:12.0|3:12.0|^20120520*7*002*|1:12.0|3:12.0|^)
					
					//502@20120409|1|001|3^20120409|1|002|10^
					JSONObject object = new JSONObject();
					object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo)); //彩种名称
					String[] split1 = betCode.split("@");
					String wanfa = split1[0]; //玩法
					JSONObject wanfaObject = JingCaiUtil.getJingCaiPlay(wanfa);
					object.put("play", wanfaObject.getString("play"));
					
					StringBuilder builder = new StringBuilder();
					String[] split2 = split1[1].split("\\^");
					for (String string : split2) {
						String[] split3 = string.split("\\|");
						String day = split3[0];
						String weekid = split3[1];
						String teamid = split3[2];
						//判断是否是胆码
						String danMaString = JingCaiUtil.getDanMaString(orderInfo, string.substring(0, string.lastIndexOf("|"))).getString("result");
						//获取比赛信息
						JSONObject matchesValueObject = cacheCommonUtil.getJingCaiMatchesValueObject(lotNo, day, weekid, teamid);
						if (matchesValueObject==null||Tools.isEmpty(matchesValueObject.getString("matches"))
								||matchesValueObject.getString("matches").equals("null")) {
							continue;
						}
						JSONObject matchesObject = matchesValueObject.getJSONObject("matches");
						String team = matchesObject.getString("team");
						String zhu = JingCaiUtil.getTeamName(team).getString("homeTeam"); //主队
						String ke = JingCaiUtil.getTeamName(team).getString("guestTeam"); //客队
						
						//比赛结果
						String score = "未开";
						String result = "未开";
						String letPoint = ""; //开奖公告里的让分
						String basePoint = ""; //开奖公告里的预设总分
						if (matchesValueObject!=null) {
							String resultString = matchesValueObject.getString("result");
							if (!Tools.isEmpty(resultString)&&!resultString.equals("null")) {
								JSONObject resultObject = matchesValueObject.getJSONObject("result");
								letPoint = resultObject.getString("letpoint"); //让分
								basePoint = resultObject.getString("basepoint"); //预设总分
								String scoreString = resultObject.getString("result"); //全场比分
								if (!Tools.isEmpty(scoreString)&&!scoreString.equals("null")) {
									if (LotTypeUtil.isJingCaiZQ(lotNo)) { //竟彩足球
										score = scoreString;
									} else { //竟彩篮球
										if (!Tools.isEmpty(scoreString)&&scoreString.indexOf(":")>-1) {
											String[] scores = scoreString.split(":");
											score = scores[1]+":"+scores[0];
										}
									}
								}
								result = JingCaiUtil.getJingCaiResultByScore(scoreString, true); //赛果
							}
						}
						
						String league = matchesObject.getString("league"); //联赛
						String id = day+"*"+weekid+"*"+teamid+"*"; //赛事信息
						
						builder.append(JingCaiUtil.getWeekByWeekId(weekid)+" "+teamid+" "+league+" ");
						String betContent = ""; //投注内容
						if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
							betContent = Spf.getBetContent(id, peiLvString, split3[3], false, "", "");
							builder.append(zhu+"vs"+ke);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
							betContent = Bf.getBetContent(id, peiLvString, split3[3], false, "", "");
							builder.append(zhu+"vs"+ke);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
							betContent = Jqs.getBetContent(id, peiLvString, split3[3], false, "", "");
							builder.append(zhu+"vs"+ke);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
							betContent = BqcJc.getBetContent(id, peiLvString, split3[3], false, "", "", "", "");
							builder.append(zhu+"vs"+ke);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
							betContent = Sf.getBetContent(id, peiLvString, split3[3], false, "", "");
							builder.append(ke+"vs"+zhu);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
							JSONObject codeObject = Rfsf.getBetContent(id, peiLvString, split3[3], false, "", "", wanfa, letPoint);
							betContent = codeObject.getString("betContent");
							String letScore = codeObject.getString("letPoint"); //让分
							if (!Tools.isEmpty(letScore)) {
								builder.append(ke+"("+letScore+")"+zhu);	
							} else {
								builder.append(ke+"vs"+zhu);
							}
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
							betContent = SfcJc.getBetContent(id, peiLvString, split3[3], false, "", "");
							builder.append(ke+"vs"+zhu);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
							JSONObject codeObject = Dxf.getBetContent(id, peiLvString, split3[3], false, "", "", wanfa, basePoint);
							betContent = codeObject.getString("betContent");
							String baseScore = codeObject.getString("basePoint"); //基本分
							if (!Tools.isEmpty(baseScore)) {
								builder.append(ke+"("+baseScore+")"+zhu);
							} else {
								builder.append(ke+"vs"+zhu);
							}
						}
						builder.append(danMaString+lineBreak+"您的投注:"+betContent).append(lineBreak+"全场比分:"+score)
								.append(lineBreak+"赛果:"+result+";").append(lineBreak);
					}
					if (builder.toString().endsWith(lineBreak)) {
						builder = builder.delete(builder.length()-lineBreak.length(), builder.length());
					}
					object.put("betCode", builder.toString());
					resultArray.add(object);
				}
			}
		}
		
		return resultArray;
	}
	
	/**
	 * 获得注码解析数组
	 * @param lotNo
	 * @param orderId
	 * @param orderInfo
	 * @param isOutTicket
	 * @return
	 */
	public JSONArray getParseBetCodeArray(String lotNo, String orderInfo, JSONObject tlotsValueObject) {
		JSONArray resultArray = new JSONArray();
		//JSONObject tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, isOutTicket);
		if (tlotsValueObject!=null) {
			JSONArray list = tlotsValueObject.getJSONArray("list");
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tlotObject = list.getJSONObject(i);
					String betCode = tlotObject.getString("betcode"); //注码
					String peiLvString = tlotObject.getString("peilu"); //赔率(20120520*7*001*|1:12.0|3:12.0|^20120520*7*002*|1:12.0|3:12.0|^)
					
					//502@20120409|1|001|3^20120409|1|002|10^
					//玩法
					String[] split1 = betCode.split("@");
					String wanfa = split1[0];
					JSONObject wanfaObject = JingCaiUtil.getJingCaiPlay(wanfa);
					String play = wanfaObject.getString("play");
					
					String[] split2 = split1[1].split("\\^");
					for (String string : split2) {
						String[] split3 = string.split("\\|");
						String day = split3[0];
						String weekId = split3[1];
						String teamId = split3[2];
						//判断是否是胆码
						String isDanMa = JingCaiUtil.getDanMaString(orderInfo, string.substring(0, string.lastIndexOf("|"))).getString("isDanMa");
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
						String betContent = ""; //投注内容
						String betContentHtml = ""; //投注内容(html)
						String letScore = ""; //让分(竞彩篮球让分胜负)
						String totalScore = ""; //总分盘(竞彩篮球大小分)
						if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
							betContent = Spf.getBetContent(id, peiLvString, split3[3], false, "", "");
							betContentHtml = Spf.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
							betContent = Bf.getBetContent(id, peiLvString, split3[3], false, "", "");
							betContentHtml = Bf.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
							betContent = Jqs.getBetContent(id, peiLvString, split3[3], false, "", "");
							betContentHtml = Jqs.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
							betContent = BqcJc.getBetContent(id, peiLvString, split3[3], false, "", "", "", "");
							betContentHtml = BqcJc.getBetContent(id, peiLvString, split3[3], true, homeHalfScore, guestHalfScore, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
							betContent = Sf.getBetContent(id, peiLvString, split3[3], false, "", "");
							betContentHtml = Sf.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
							JSONObject codeObject = Rfsf.getBetContent(id, peiLvString, split3[3], false, "", "", wanfa, letPoint);
							betContent = codeObject.getString("betContent");
							letScore = codeObject.getString("letPoint"); //让分
							JSONObject codeObjectHtml = Rfsf.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore, wanfa, letPoint);
							betContentHtml = codeObjectHtml.getString("betContent");
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
							betContent = SfcJc.getBetContent(id, peiLvString, split3[3], false, "", "");
							betContentHtml = SfcJc.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore);
						} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
							JSONObject codeObject = Dxf.getBetContent(id, peiLvString, split3[3], false, "", "", wanfa, basePoint);
							betContent = codeObject.getString("betContent");
							totalScore = codeObject.getString("basePoint"); //总分盘
							JSONObject codeObjectHtml = Dxf.getBetContent(id, peiLvString, split3[3], true, homeScore, guestScore, wanfa, basePoint);
							betContentHtml = codeObjectHtml.getString("betContent");
						}
						
						JSONObject object = new JSONObject();
						object.put("play", play); //玩法
						object.put("day", day); //日期
						object.put("weekId", weekId); //星期
						object.put("teamId", teamId); //赛事编号
						object.put("homeTeam", homeTeam); //主队
						object.put("guestTeam", guestTeam); //客队
						object.put("homeScore", homeScore); //主队比分
						object.put("guestScore", guestScore); //客队比分
						object.put("letScore", letScore); //让分(竞彩篮球让分胜负)
						object.put("totalScore", totalScore); //总分盘(竞彩篮球大小分)
						object.put("betContent", betContent); //投注内容
						object.put("betContentHtml", betContentHtml); //投注内容(html)
						object.put("isDanMa", isDanMa); //是否是胆码
						resultArray.add(object);
					}
				}
			}
		}
		return resultArray;
	}
	
	/**
	 * 获得注码解析数组(过滤重复的比赛)
	 * @param lotNo
	 * @param orderInfo
	 * @param tlotsValueObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONArray getParseBetCodeList(String lotNo, String orderInfo, JSONObject tlotsValueObject) {
		JSONArray resultArray = new JSONArray();
		Map<String, Map<String, Object>> infoMap = new HashMap<String, Map<String, Object>>();
		List<String> plays = new ArrayList<String>();
		if (tlotsValueObject!=null) {
			JSONArray list = tlotsValueObject.getJSONArray("list");
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tlotObject = list.getJSONObject(i);
					String betCode = tlotObject.getString("betcode"); //注码(502@20120409|1|001|3^20120409|1|002|10^)
					String peiLvString = tlotObject.getString("peilu"); //赔率(20120520*7*001*|1:12.0|3:12.0|^20120520*7*002*|1:12.0|3:12.0|^)
					
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
						String code = split3[3]; //注码
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
						JingCaiBetCodeParseCommon.putTeamMap(lotNo, id, code, peiLvString, teamMap, wanfa, letPoint, basePoint);
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
				String key = entry.getKey();
				Map<String, Object> value = entry.getValue();
				
				//判断是否是胆码
				String isDanMa = JingCaiUtil.getDanMaString(orderInfo, key.replaceAll("\\*", "|")).getString("isDanMa");
				
				JSONObject betContentJson = JingCaiBetCodeParseCommon.getBetContentJson(value, false);
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
				object.put("isDanMa", isDanMa); //是否是胆码
				resultArray.add(object);
			}
		}
		return resultArray;
	}
	
}
