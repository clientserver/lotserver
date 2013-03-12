package com.ruyicai.lotserver.util.lot;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 足球公共类
 * @author Administrator
 *
 */
public class FootballUtil {
	
	private static Logger logger = Logger.getLogger(FootballUtil.class);

	/**
	 * 解析数据分析
	 * @param resultJson
	 * @param valueObject
	 */
	public static void parseDataAnalysis(JSONObject resultJson, JSONObject valueObject) {
		//赛事信息
		JSONObject scheduleObject = new JSONObject();
		JSONObject scheduleReturnObject = valueObject.getJSONObject("schedule"); //返回数据
		if (scheduleReturnObject!=null&&!scheduleReturnObject.toString().equals("null")) {
			String homeTeamId = scheduleReturnObject.getString("homeTeamID"); //主队编号
			String guestTeamId = scheduleReturnObject.getString("guestTeamID"); //客队编号
			
			scheduleObject.put("homeTeamId", homeTeamId); //主队编号
			scheduleObject.put("guestTeamId", guestTeamId); //客队编号
		}
		resultJson.put("schedule", scheduleObject); //赛事信息
		
		//历史交锋
		JSONArray preClashSchedulesArray = new JSONArray();
		JSONArray preClashSchedulesReturnArray = valueObject.getJSONArray("preClashSchedules"); //返回数据
		if (preClashSchedulesReturnArray!=null&&preClashSchedulesReturnArray.size()>0) {
			for (int i = 0; i < preClashSchedulesReturnArray.size(); i++) {
				JSONObject preClashSchedulesObject = preClashSchedulesReturnArray.getJSONObject(i);
				String sclassName = preClashSchedulesObject.getString("sclassName"); //联赛名称
				String sclassName_j = preClashSchedulesObject.getString("sclassName_j"); //联赛简称
				String matchTime = preClashSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = preClashSchedulesObject.getString("homeTeamID"); //主队编号
				String guestTeamId = preClashSchedulesObject.getString("guestTeamID"); //客队编号
				String homeTeam = preClashSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = preClashSchedulesObject.getString("guestTeam"); //客队名称
				String homeScore = preClashSchedulesObject.getString("homeScore"); //主队比分
				String guestScore = preClashSchedulesObject.getString("guestScore"); //客队比分
				String homeHalfScore = preClashSchedulesObject.getString("homeHalfScore"); //主队半场比分
				String guestHalfScore = preClashSchedulesObject.getString("guestHalfScore"); //客队半场比分
				
				String halfResult = JingCaiUtil.getMatchResult(homeHalfScore, guestHalfScore).getString("result"); //半场赛果
				String allResult = JingCaiUtil.getMatchResult(homeScore, guestScore).getString("result"); //全场赛果
				String goals = JingCaiUtil.getMatchResult(homeScore, guestScore).getString("goals"); //总进球数
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassName_j);
				
				JSONObject object = new JSONObject();
				object.put("sclassName", sclassName); //联赛名称
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam); //主队名称
				object.put("guestTeam", guestTeam); //客队名称
				object.put("halfScore", homeHalfScore+":"+guestHalfScore); //半场比分
				object.put("score", homeScore+":"+guestScore); //全场比分
				object.put("halfResult", halfResult); //半场赛果
				object.put("result", allResult); //全场赛果
				object.put("goals", goals); //总进球数
				preClashSchedulesArray.add(object);
			}
		}
		resultJson.put("preClashSchedules", preClashSchedulesArray); //历史交锋
		
		//亚盘
		JSONArray letGoalsArray = new JSONArray();
		JSONArray letGoalsReturnArray = valueObject.getJSONArray("letGoals"); //返回数据
		if (letGoalsReturnArray!=null&&letGoalsReturnArray.size()>0) {
			for (int i = 0; i < letGoalsReturnArray.size(); i++) {
				JSONObject letGoalsObject = letGoalsReturnArray.getJSONObject(i);
				String companyName = letGoalsObject.getString("companyName"); //公司名
				String firstGoalName = letGoalsObject.getString("firstGoal_name"); //初盘盘口
				String firstUpodds = letGoalsObject.getString("firstUpodds"); //主队初盘
				String firstDownodds = letGoalsObject.getString("firstDownodds"); //客队初盘
				String goalName = letGoalsObject.getString("goal_name"); //即时盘口
				String upOdds = letGoalsObject.getString("upOdds"); //主队即时
				String downOdds = letGoalsObject.getString("downOdds"); //客队即时
				
				//将数据格式化成两位小数
				firstUpodds = Tools.formatTwoDecimal(firstUpodds);
				firstDownodds = Tools.formatTwoDecimal(firstDownodds);
				
				upOdds = Tools.formatTwoDecimal(upOdds);
				downOdds = Tools.formatTwoDecimal(downOdds);
				
				JSONObject object = new JSONObject();
				object.put("companyName", companyName.trim()); //公司名
				object.put("firstGoalName", firstGoalName); //实盘盘口
				object.put("firstUpodds", firstUpodds); //主队初盘
				object.put("firstDownodds", firstDownodds); //客队初盘
				object.put("goalName", goalName); //即时盘口
				object.put("upOdds", upOdds); //主队即时
				object.put("downOdds", downOdds); //客队即时
				letGoalsArray.add(object);
			}
		}
		resultJson.put("letGoals", letGoalsArray); //亚盘
		
		//欧赔
		JSONArray standardsArray = new JSONArray();
		JSONArray standardsReturnArray = valueObject.getJSONArray("standards"); //返回数据
		if (standardsReturnArray!=null&&standardsReturnArray.size()>0) {
			for (int i = 0; i < standardsReturnArray.size(); i++) {
				JSONObject standardsObject = standardsReturnArray.getJSONObject(i);
				String companyName = standardsObject.getString("companyName"); //公司名
				String homeWin = standardsObject.getString("homeWin"); //即时主胜
				String standoff = standardsObject.getString("standoff"); //即时平局
				String guestWin = standardsObject.getString("guestWin"); //即时客胜
				String homeWinLu = standardsObject.getString("homeWinLu"); //主胜率
				String standoffLu = standardsObject.getString("standoffLu"); //平局率
				String guestWinLu = standardsObject.getString("guestWinLu"); //客胜率
				String k_h = standardsObject.getString("k_h"); //主凯指
				String k_s = standardsObject.getString("k_s"); //平凯指
				String k_g = standardsObject.getString("k_g"); //客凯指
				String fanHuanLu = standardsObject.getString("fanHuanLu"); //返还率
				
				//将数据格式化成两位小数
				homeWin = Tools.formatTwoDecimal(homeWin);
				standoff = Tools.formatTwoDecimal(standoff);
				guestWin = Tools.formatTwoDecimal(guestWin);
				
				homeWinLu = Tools.formatTwoDecimal(homeWinLu);
				standoffLu = Tools.formatTwoDecimal(standoffLu);
				guestWinLu = Tools.formatTwoDecimal(guestWinLu);
				
				k_h = Tools.formatTwoDecimal(k_h);
				k_s = Tools.formatTwoDecimal(k_s);
				k_g = Tools.formatTwoDecimal(k_g);
				
				fanHuanLu = Tools.formatTwoDecimal(fanHuanLu);
				
				JSONObject object = new JSONObject();
				object.put("companyName", companyName.trim()); //公司名
				object.put("homeWin", homeWin); //即时主胜
				object.put("standoff", standoff); //即时平局
				object.put("guestWin", guestWin); //即时客胜
				object.put("homeWinLu", homeWinLu); //主胜率
				object.put("standoffLu", standoffLu); //平局率
				object.put("guestWinLu", guestWinLu); //客胜率
				object.put("k_h", k_h); //主凯指
				object.put("k_s", k_s); //平凯指
				object.put("k_g", k_g); //客凯指
				object.put("fanHuanLu", fanHuanLu); //返还率
				standardsArray.add(object);
			}
		}
		resultJson.put("standards", standardsArray); //欧赔
		
		//联赛排名
		JSONArray rankingsArray = new JSONArray();
		JSONArray rankingsReturnArray = valueObject.getJSONArray("rankings"); //返回数据
		if (rankingsReturnArray!=null&&rankingsReturnArray.size()>0) {
			for (int i = 0; i < rankingsReturnArray.size(); i++) {
				JSONObject rankingsObject = rankingsReturnArray.getJSONObject(i);
				String ranking = rankingsObject.getString("ranking"); //排名
				String teamId = rankingsObject.getString("teamID"); //球队编号
				String teamName = rankingsObject.getString("teamName"); //球队名称
				String win = rankingsObject.getString("win"); //胜次数
				String standoff = rankingsObject.getString("standoff"); //平次数
				String lose = rankingsObject.getString("lose"); //负次数
				String goalDifference = rankingsObject.getString("goalDifference"); //净胜球
				String integral = rankingsObject.getString("integral"); //积分
				String matchCount = rankingsObject.getString("matchcount"); //赛几场
				
				JSONObject object = new JSONObject();
				object.put("ranking", ranking); //排名
				object.put("teamId", teamId); //球队编号
				object.put("teamName", teamName); //球队名称
				object.put("win", win); //胜次数
				object.put("standoff", standoff); //平次数
				object.put("lose", lose); //负次数
				object.put("goalDifference", goalDifference); //净胜球
				object.put("integral", integral); //积分
				object.put("matchCount", matchCount); //赛几场
				rankingsArray.add(object);
			}
		}
		resultJson.put("rankings", rankingsArray); //联赛排名 
		
		//主队近期(10场)战绩
		JSONArray homePreSchedulesArray = new JSONArray();
		JSONArray homePreSchedulesReturnArray = valueObject.getJSONArray("homePreSchedules"); //返回数据
		if (homePreSchedulesReturnArray!=null&&homePreSchedulesReturnArray.size()>0) {
			for (int i = 0; i < homePreSchedulesReturnArray.size(); i++) {
				JSONObject homePreSchedulesObject = homePreSchedulesReturnArray.getJSONObject(i);
				String matchTime = homePreSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = homePreSchedulesObject.getString("homeTeamID"); //主队编号
				String guestTeamId = homePreSchedulesObject.getString("guestTeamID"); //客队编号
				String homeTeam = homePreSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = homePreSchedulesObject.getString("guestTeam"); //客队名称
				String homeScore = homePreSchedulesObject.getString("homeScore"); //主队比分
				String guestScore = homePreSchedulesObject.getString("guestScore"); //客队比分
				
				JSONObject object = new JSONObject();
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam.trim()); //主队名称
				object.put("guestTeam", guestTeam.trim()); //客队名称
				object.put("score", homeScore+":"+guestScore); //比分(主:客)
				homePreSchedulesArray.add(object);
			}
		}
		resultJson.put("homePreSchedules", homePreSchedulesArray); //主队近期(10场)战绩
		
		//客队近期(10场)战绩
		JSONArray guestPreSchedulesArray = new JSONArray();
		JSONArray guestPreSchedulesReturnArray = valueObject.getJSONArray("guestPreSchedules"); //返回数据
		if (guestPreSchedulesReturnArray!=null&&guestPreSchedulesReturnArray.size()>0) {
			for (int i = 0; i < guestPreSchedulesReturnArray.size(); i++) {
				JSONObject guestPreSchedulesObject = guestPreSchedulesReturnArray.getJSONObject(i);
				String matchTime = guestPreSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = guestPreSchedulesObject.getString("homeTeamID"); //主队编号
				String guestTeamId = guestPreSchedulesObject.getString("guestTeamID"); //客队编号
				String homeTeam = guestPreSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = guestPreSchedulesObject.getString("guestTeam"); //客队名称
				String homeScore = guestPreSchedulesObject.getString("homeScore"); //主队比分
				String guestScore = guestPreSchedulesObject.getString("guestScore"); //客队比分
				
				JSONObject object = new JSONObject();
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam.trim()); //主队名称
				object.put("guestTeam", guestTeam.trim()); //客队名称
				object.put("score", homeScore+":"+guestScore); //比分(主:客)
				guestPreSchedulesArray.add(object);
			}
		}
		resultJson.put("guestPreSchedules", guestPreSchedulesArray); //主近10场赛事
		
		//主队未来5场赛事
		JSONArray homeAfterSchedulesArray = new JSONArray();
		JSONArray homeAfterSchedulesReturnArray = valueObject.getJSONArray("homeAfterSchedules"); //返回数据
		if (homeAfterSchedulesReturnArray!=null&&homeAfterSchedulesReturnArray.size()>0) {
			for (int i = 0; i < homeAfterSchedulesReturnArray.size(); i++) {
				JSONObject homeAfterSchedulesObject = homeAfterSchedulesReturnArray.getJSONObject(i);
				String sclassName = homeAfterSchedulesObject.getString("sclassName"); //联赛名称
				String sclassName_j = homeAfterSchedulesObject.getString("sclassName_j"); //联赛简称
				String matchTime = homeAfterSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = homeAfterSchedulesObject.getString("homeTeamID"); //主队编号
				String guestTeamId = homeAfterSchedulesObject.getString("guestTeamID"); //客队编号
				String homeTeam = homeAfterSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = homeAfterSchedulesObject.getString("guestTeam"); //客队名称
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassName_j);
				
				JSONObject object = new JSONObject();
				object.put("sclassName", sclassName); //联赛名称
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam.trim()); //主队名称
				object.put("guestTeam", guestTeam.trim()); //客队名称
				homeAfterSchedulesArray.add(object);
			}
		}
		resultJson.put("homeAfterSchedules", homeAfterSchedulesArray); //主未来5场赛事
		
		//客队未来5场赛事
		JSONArray guestAfterSchedulesArray = new JSONArray();
		JSONArray guestAfterSchedulesReturnArray = valueObject.getJSONArray("guestAfterSchedules"); //返回数据
		if (guestAfterSchedulesReturnArray!=null&&guestAfterSchedulesReturnArray.size()>0) {
			for (int i = 0; i < guestAfterSchedulesReturnArray.size(); i++) {
				JSONObject guestAfterSchedulesObject = guestAfterSchedulesReturnArray.getJSONObject(i);
				String sclassName = guestAfterSchedulesObject.getString("sclassName"); //联赛名称
				String sclassName_j = guestAfterSchedulesObject.getString("sclassName_j"); //联赛简称
				String matchTime = guestAfterSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = guestAfterSchedulesObject.getString("homeTeamID"); //主队编号
				String guestTeamId = guestAfterSchedulesObject.getString("guestTeamID"); //客队编号
				String homeTeam = guestAfterSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = guestAfterSchedulesObject.getString("guestTeam"); //客队名称
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassName_j);
				
				JSONObject object = new JSONObject();
				object.put("sclassName", sclassName); //联赛名称
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam.trim()); //主队名称
				object.put("guestTeam", guestTeam.trim()); //客队名称
				guestAfterSchedulesArray.add(object);
			}
		}
		resultJson.put("guestAfterSchedules", guestAfterSchedulesArray); //客未来5场赛事
	}
	
	/**
	 * 解析即时比分列表
	 * @param resultArray
	 * @param immediateScoreObject
	 */
	public static void parseImmediateScoreList(JSONArray resultArray, JSONObject immediateScoreObject) {
		String event = immediateScoreObject.getString("event"); //赛事信息
		String sclassName = immediateScoreObject.getString("sclassName"); //联赛名称
		String sclassName_j = immediateScoreObject.getString("sclassName_j"); //联赛简称
		String homeTeam = immediateScoreObject.getString("homeTeam"); //主队
		String guestTeam = immediateScoreObject.getString("guestTeam"); //客队
		String homeScore = immediateScoreObject.getString("homeScore"); //主队全场比分
		String guestScore = immediateScoreObject.getString("guestScore"); //客队全场比分
		String homeHalfScore = immediateScoreObject.getString("homeHalfScore"); //主队半场比分
		String guestHalfScore = immediateScoreObject.getString("guestHalfScore"); //客队半场比分
		String matchTime = immediateScoreObject.getString("matchTime"); //比赛时间
		String matchState = immediateScoreObject.getString("matchState"); //比赛状态
		String home_Red = immediateScoreObject.getString("home_Red"); //主队红牌
		String guest_Red = immediateScoreObject.getString("guest_Red"); //客队红牌
		String home_Yellow = immediateScoreObject.getString("home_Yellow"); //主队黄牌
		String guest_Yellow = immediateScoreObject.getString("guest_Yellow"); //客队黄牌
		
		//如果简称不为空则返回简称
		sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassName_j);
		//（0:未开）未开,（1:上半场,2:中场,3:下半场,-11:待定,-12:腰斩,-13:中断,-14:推迟）比赛中,(-1:完场，-10取消) 完场
		String stateMemo = MemoUtil.getJczImmediateScoreStateMemo(matchState); //状态描述
		//红牌总数
		String red = (Integer.parseInt(home_Red)+Integer.parseInt(guest_Red))+"";
		//黄牌总数
		String yellow = (Integer.parseInt(home_Yellow)+Integer.parseInt(guest_Yellow))+"";
		
		JSONObject object = new JSONObject();
		object.put("event", event); //赛事信息
		object.put("sclassName", sclassName); //联赛名称
		object.put("homeTeam", homeTeam.trim()); //主队
		object.put("guestTeam", guestTeam.trim()); //客队
		object.put("homeScore", homeScore); //主队全场比分
		object.put("guestScore", guestScore); //客队全场比分
		object.put("homeHalfScore", homeHalfScore); //主队半场比分
		object.put("guestHalfScore", guestHalfScore); //客队半场比分
		object.put("red", red); //红牌总数
		object.put("yellow", yellow); //黄牌总数
		object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
		object.put(Constants.stateMemo, stateMemo); //状态描述
		resultArray.add(object);
	}
	
	/**
	 * 解析即时比分详细
	 * @param responseJson
	 * @param valueObject
	 */
	public static void parseImmediateScoreDetail(JSONObject responseJson, JSONObject valueObject) {
		String sclassName = valueObject.getString("sclassName"); //联赛名称
		String sclassName_j = valueObject.getString("sclassName_j"); //联赛简称
		String homeTeam = valueObject.getString("homeTeam"); //主队
		String guestTeam = valueObject.getString("guestTeam"); //客队
		String homeScore = valueObject.getString("homeScore"); //主队比分
		String guestScore = valueObject.getString("guestScore"); //客队比分
		String matchTime = valueObject.getString("matchTime"); //比赛时间
		String matchState = valueObject.getString("matchState"); //比赛状态
		
		//如果简称不为空则返回简称
		sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassName_j);
		//（0:未开）未开,（1:上半场,2:中场,3:下半场,-11:待定,-12:腰斩,-13:中断,-14:推迟）比赛中,(-1:完场，-10取消) 完场
		String stateMemo = MemoUtil.getJczImmediateScoreStateMemo(matchState); //状态描述
		
		JSONArray detailResults = new JSONArray(); //即时比分详细
		JSONArray detailResultsArray = valueObject.getJSONArray("detailResults");
		if (detailResultsArray!=null&&detailResultsArray.size()>0) {
			for (int i = 0; i < detailResultsArray.size(); i++) {
				JSONObject detailResultsObject = detailResultsArray.getJSONObject(i);
				String kind = detailResultsObject.getString("kind"); //事件类型(1、入球 2、红牌  3、黄牌 4、换进 5换出 7、点球  8、乌龙  9、两黄变红 11、换人)
				String teamID = detailResultsObject.getString("teamID"); //主队客队 1、主队 0、客队
				String happenTime = detailResultsObject.getString("happenTime"); //发生时间分钟数  相对比赛时间
				String playerName = detailResultsObject.getString("playername"); //球员名称
				
				JSONObject object = new JSONObject();
				object.put("kind", kind); //事件类型
				object.put("teamID", teamID); //主队客队 1、主队 0、客队
				object.put("happenTime", happenTime.trim()); //发生时间分钟数
				object.put("playerName", playerName.trim()); //球员名称
				detailResults.add(object);
			}
		}
		
		responseJson.put("sclassName", sclassName); //联赛名称
		responseJson.put("homeTeam", homeTeam); //主队
		responseJson.put("guestTeam", guestTeam); //客队
		responseJson.put("homeScore", homeScore); //主队比分
		responseJson.put("guestScore", guestScore); //客队比分
		responseJson.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
		responseJson.put(Constants.stateMemo, stateMemo); //状态描述
		responseJson.put("detailResults", detailResults);//即时比分详细
	}
	
	/**
	 * 解析竞彩赔率(足球)
	 * 
	 * @param object
	 * @param xml
	 * @param id
	 */
	@SuppressWarnings("rawtypes")
	public static void parseXMLForJingcaiPeiLvZQ(JSONObject object, String xml,
			String id) {
		try {
			Document document = DocumentHelper.parseText(xml);
			Element root = document.getRootElement();
			Element bodyElement = root.element("body");
			Element matchListElement = bodyElement.element("matchList");
			List items = matchListElement.elements("item");
			boolean isExist = false;
			for (Iterator it = items.iterator(); it.hasNext();) {
				Element itemElement = (Element) it.next();
				Element idElement = itemElement.element("id");
				String idXML = idElement.getText();
				if (idXML.equals(id)) {
					isExist = true;
					// 胜平负
					Element vsElement = itemElement.element("vs");
					Element v0Element = vsElement.element("v0");
					if (v0Element != null && !v0Element.getText().equals("0000")) {
						object.put("v0", v0Element.getText());
					} else {
						object.put("v0", "");
					}
					Element v1Element = vsElement.element("v1");
					if (v1Element != null && !v1Element.getText().equals("0000")) {
						object.put("v1", v1Element.getText());
					} else {
						object.put("v1", "");
					}
					Element v3Element = vsElement.element("v3");
					if (v3Element != null && !v3Element.getText().equals("0000")) {
						object.put("v3", v3Element.getText());
					} else {
						object.put("v3", "");
					}
					Element letPointElement = vsElement.element("letPoint");
					if (letPointElement != null && !letPointElement.getText().equals("0000")) {
						object.put("letPoint", letPointElement.getText());
					} else {
						object.put("letPoint", "");
					}
					// 比分
					Element scoreElement = itemElement.element("score");
					Element v00Element = scoreElement.element("v00");
					if (v00Element != null && !v00Element.getText().equals("0000")) {
						object.put("score_v00", v00Element.getText());
					} else {
						object.put("score_v00", "");
					}
					Element v01Element = scoreElement.element("v01");
					if (v01Element != null && !v01Element.getText().equals("0000")) {
						object.put("score_v01", v01Element.getText());
					} else {
						object.put("score_v01", "");
					}
					Element v02Element = scoreElement.element("v02");
					if (v02Element != null && !v02Element.getText().equals("0000")) {
						object.put("score_v02", v02Element.getText());
					} else {
						object.put("score_v02", "");
					}
					Element v03Element = scoreElement.element("v03");
					if (v03Element != null && !v03Element.getText().equals("0000")) {
						object.put("score_v03", v03Element.getText());
					} else {
						object.put("score_v03", "");
					}
					Element v04Element = scoreElement.element("v04");
					if (v04Element != null && !v04Element.getText().equals("0000")) {
						object.put("score_v04", v04Element.getText());
					} else {
						object.put("score_v04", "");
					}
					Element v05Element = scoreElement.element("v05");
					if (v04Element != null && !v05Element.getText().equals("0000")) {
						object.put("score_v05", v05Element.getText());
					} else {
						object.put("score_v05", "");
					}
					Element v09Element = scoreElement.element("v09");
					if (v09Element != null && !v09Element.getText().equals("0000")) {
						object.put("score_v09", v09Element.getText());
					} else {
						object.put("score_v09", "");
					}
					Element v10Element = scoreElement.element("v10");
					if (v10Element != null && !v10Element.getText().equals("0000")) {
						object.put("score_v10", v10Element.getText());
					} else {
						object.put("score_v10", "");
					}
					Element v11Element = scoreElement.element("v11");
					if (v11Element != null && !v11Element.getText().equals("0000")) {
						object.put("score_v11", v11Element.getText());
					} else {
						object.put("score_v11", "");
					}
					Element v12Element = scoreElement.element("v12");
					if (v12Element != null && !v12Element.getText().equals("0000")) {
						object.put("score_v12", v12Element.getText());
					} else {
						object.put("score_v12", "");
					}
					Element v13Element = scoreElement.element("v13");
					if (v13Element != null && !v13Element.getText().equals("0000")) {
						object.put("score_v13", v13Element.getText());
					} else {
						object.put("score_v13", "");
					}
					Element v14Element = scoreElement.element("v14");
					if (v14Element != null && !v14Element.getText().equals("0000")) {
						object.put("score_v14", v14Element.getText());
					} else {
						object.put("score_v14", "");
					}
					Element v15Element = scoreElement.element("v15");
					if (v15Element != null && !v15Element.getText().equals("0000")) {
						object.put("score_v15", v15Element.getText());
					} else {
						object.put("score_v15", "");
					}
					Element v20Element = scoreElement.element("v20");
					if (v20Element != null && !v20Element.getText().equals("0000")) {
						object.put("score_v20", v20Element.getText());
					} else {
						object.put("score_v20", "");
					}
					Element v21Element = scoreElement.element("v21");
					if (v21Element != null && !v21Element.getText().equals("0000")) {
						object.put("score_v21", v21Element.getText());
					} else {
						object.put("score_v21", "");
					}
					Element v22Element = scoreElement.element("v22");
					if (v22Element != null && !v22Element.getText().equals("0000")) {
						object.put("score_v22", v22Element.getText());
					} else {
						object.put("score_v22", "");
					}
					Element v23Element = scoreElement.element("v23");
					if (v23Element != null && !v23Element.getText().equals("0000")) {
						object.put("score_v23", v23Element.getText());
					} else {
						object.put("score_v23", "");
					}
					Element v24Element = scoreElement.element("v24");
					if (v24Element != null && !v24Element.getText().equals("0000")) {
						object.put("score_v24", v24Element.getText());
					} else {
						object.put("score_v24", "");
					}
					Element v25Element = scoreElement.element("v25");
					if (v25Element != null && !v25Element.getText().equals("0000")) {
						object.put("score_v25", v25Element.getText());
					} else {
						object.put("score_v25", "");
					}
					Element v30Element = scoreElement.element("v30");
					if (v30Element != null && !v30Element.getText().equals("0000")) {
						object.put("score_v30", v30Element.getText());
					} else {
						object.put("score_v30", "");
					}
					Element v31Element = scoreElement.element("v31");
					if (v31Element != null && !v31Element.getText().equals("0000")) {
						object.put("score_v31", v31Element.getText());
					} else {
						object.put("score_v31", "");
					}
					Element v32Element = scoreElement.element("v32");
					if (v32Element != null && !v32Element.getText().equals("0000")) {
						object.put("score_v32", v32Element.getText());
					} else {
						object.put("score_v32", "");
					}
					Element v33Element = scoreElement.element("v33");
					if (v33Element != null && !v33Element.getText().equals("0000")) {
						object.put("score_v33", v33Element.getText());
					} else {
						object.put("score_v33", "");
					}
					Element v40Element = scoreElement.element("v40");
					if (v40Element != null && !v40Element.getText().equals("0000")) {
						object.put("score_v40", v40Element.getText());
					} else {
						object.put("score_v40", "");
					}
					Element v41Element = scoreElement.element("v41");
					if (v41Element != null && !v41Element.getText().equals("0000")) {
						object.put("score_v41", v41Element.getText());
					} else {
						object.put("score_v41", "");
					}
					Element v42Element = scoreElement.element("v42");
					if (v42Element != null && !v42Element.getText().equals("0000")) {
						object.put("score_v42", v42Element.getText());
					} else {
						object.put("score_v42", "");
					}
					Element v50Element = scoreElement.element("v50");
					if (v50Element != null && !v50Element.getText().equals("0000")) {
						object.put("score_v50", v50Element.getText());
					} else {
						object.put("score_v50", "");
					}
					Element v51Element = scoreElement.element("v51");
					if (v51Element != null && !v51Element.getText().equals("0000")) {
						object.put("score_v51", v51Element.getText());
					} else {
						object.put("score_v51", "");
					}
					Element v52Element = scoreElement.element("v52");
					if (v52Element != null && !v52Element.getText().equals("0000")) {
						object.put("score_v52", v52Element.getText());
					} else {
						object.put("score_v52", "");
					}
					Element v90Element = scoreElement.element("v90");
					if (v90Element != null && !v90Element.getText().equals("0000")) {
						object.put("score_v90", v90Element.getText());
					} else {
						object.put("score_v90", "");
					}
					Element v99Element = scoreElement.element("v99");
					if (v99Element != null && !v99Element.getText().equals("0000")) {
						object.put("score_v99", v99Element.getText());
					} else {
						object.put("score_v99", "");
					}
					//总进球数
					Element goalElement = itemElement.element("goal");
					Element goal_v0Element = goalElement.element("v0");
					if (goal_v0Element != null && !goal_v0Element.getText().equals("0000")) {
						object.put("goal_v0", goal_v0Element.getText());
					} else {
						object.put("goal_v0", "");
					}
					Element goal_v1Element = goalElement.element("v1");
					if (goal_v1Element != null && !goal_v1Element.getText().equals("0000")) {
						object.put("goal_v1", goal_v1Element.getText());
					} else {
						object.put("goal_v1", "");
					}
					Element goal_v2Element = goalElement.element("v2");
					if (goal_v2Element != null && !goal_v2Element.getText().equals("0000")) {
						object.put("goal_v2", goal_v2Element.getText());
					} else {
						object.put("goal_v2", "");
					}
					Element goal_v3Element = goalElement.element("v3");
					if (goal_v3Element != null && !goal_v3Element.getText().equals("0000")) {
						object.put("goal_v3", goal_v3Element.getText());
					} else {
						object.put("goal_v3", "");
					}
					Element goal_v4Element = goalElement.element("v4");
					if (goal_v4Element != null && !goal_v4Element.getText().equals("0000")) {
						object.put("goal_v4", goal_v4Element.getText());
					} else {
						object.put("goal_v4", "");
					}
					Element goal_v5Element = goalElement.element("v5");
					if (goal_v5Element != null && !goal_v5Element.getText().equals("0000")) {
						object.put("goal_v5", goal_v5Element.getText());
					} else {
						object.put("goal_v5", "");
					}
					Element goal_v6Element = goalElement.element("v6");
					if (goal_v6Element != null && !goal_v6Element.getText().equals("0000")) {
						object.put("goal_v6", goal_v6Element.getText());
					} else {
						object.put("goal_v6", "");
					}
					Element goal_v7Element = goalElement.element("v7");
					if (goal_v7Element != null && !goal_v7Element.getText().equals("0000")) {
						object.put("goal_v7", goal_v7Element.getText());
					} else {
						object.put("goal_v7", "");
					}
					//半全场
					Element halfElement = itemElement.element("half");
					Element half_v00Element = halfElement.element("v00");
					if (half_v00Element != null && !half_v00Element.getText().equals("0000")) {
						object.put("half_v00", half_v00Element.getText());
					} else {
						object.put("half_v00", "");
					}
					Element half_v01Element = halfElement.element("v01");
					if (half_v01Element != null && !half_v01Element.getText().equals("0000")) {
						object.put("half_v01", half_v01Element.getText());
					} else {
						object.put("half_v01", "");
					}
					Element half_v03Element = halfElement.element("v03");
					if (half_v03Element != null && !half_v03Element.getText().equals("0000")) {
						object.put("half_v03", half_v03Element.getText());
					} else {
						object.put("half_v03", "");
					}
					Element half_v10Element = halfElement.element("v10");
					if (half_v10Element != null && !half_v10Element.getText().equals("0000")) {
						object.put("half_v10", half_v10Element.getText());
					} else {
						object.put("half_v10", "");
					}
					Element half_v11Element = halfElement.element("v11");
					if (half_v11Element != null && !half_v11Element.getText().equals("0000")) {
						object.put("half_v11", half_v11Element.getText());
					} else {
						object.put("half_v11", "");
					}
					Element half_v13Element = halfElement.element("v13");
					if (half_v13Element != null && !half_v13Element.getText().equals("0000")) {
						object.put("half_v13", half_v13Element.getText());
					} else {
						object.put("half_v13", "");
					}
					Element half_v30Element = halfElement.element("v30");
					if (half_v30Element != null && !half_v30Element.getText().equals("0000")) {
						object.put("half_v30", half_v30Element.getText());
					} else {
						object.put("half_v30", "");
					}
					Element half_v31Element = halfElement.element("v31");
					if (half_v31Element != null && !half_v31Element.getText().equals("0000")) {
						object.put("half_v31", half_v31Element.getText());
					} else {
						object.put("half_v31", "");
					}
					Element half_v33Element = halfElement.element("v33");
					if (half_v33Element != null && !half_v33Element.getText().equals("0000")) {
						object.put("half_v33", half_v33Element.getText());
					} else {
						object.put("half_v33", "");
					}
				}
			}
			if (!isExist) {
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
		} catch (DocumentException e) {
			logger.error("解析竞彩赔率(足球)发生异常", e);
		}
	}
	
}
