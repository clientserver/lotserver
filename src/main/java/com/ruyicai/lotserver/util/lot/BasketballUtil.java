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
 * 篮球公共类
 * @author Administrator
 *
 */
public class BasketballUtil {
	
	private static Logger logger = Logger.getLogger(BasketballUtil.class);

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
			String homeTeamId = scheduleReturnObject.getString("homeTeamId"); //主队编号
			String guestTeamId = scheduleReturnObject.getString("guestTeamId"); //客队编号
			
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
				String sclassShortName = preClashSchedulesObject.getString("sclassShortName"); //联赛简称
				String matchTime = preClashSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = preClashSchedulesObject.getString("homeTeamId"); //主队编号
				String guestTeamId = preClashSchedulesObject.getString("guestTeamId"); //客队编号
				String homeTeam = preClashSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = preClashSchedulesObject.getString("guestTeam"); //客队名称
				String homeScore = preClashSchedulesObject.getString("homeScore"); //主队比分
				String guestScore = preClashSchedulesObject.getString("guestScore"); //客队比分
				String letScore = preClashSchedulesObject.getString("letScore"); //让分
				String totalScore = preClashSchedulesObject.getString("totalScore"); //总分盘
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassShortName);
				//赛果(胜负)
				String resultSf = JingCaiUtil.getMatchResult(homeScore, guestScore).getString("result");
				//赛果(让分胜负)
				String resultRfsf = JingCaiUtil.getResultRfsf(homeScore, guestScore, letScore);
				//赛果(大小)
				String resultDx = JingCaiUtil.getResultDx(homeScore, guestScore, totalScore);
				
				JSONObject object = new JSONObject();
				object.put("sclassName", sclassName); //联赛名称
				object.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
				object.put("homeTeamId", homeTeamId); //主队编号
				object.put("guestTeamId", guestTeamId); //客队编号
				object.put("homeTeam", homeTeam); //主队名称
				object.put("guestTeam", guestTeam); //客队名称
				object.put("score", homeScore+":"+guestScore); //全场比分
				object.put("resultSf", resultSf); //赛果(胜负)
				object.put("resultRfsf", resultRfsf); //赛果(让分胜负)
				object.put("resultDx", resultDx); //赛果(大小)
				
				preClashSchedulesArray.add(object);
			}
		}
		resultJson.put("preClashSchedules", preClashSchedulesArray); //历史交锋
		
		//亚赔-让分盘
		JSONArray letGoalsArray = new JSONArray();
		JSONArray letGoalsReturnArray = valueObject.getJSONArray("letGoals"); //返回数据
		if (letGoalsReturnArray!=null&&letGoalsReturnArray.size()>0) {
			for (int i = 0; i < letGoalsReturnArray.size(); i++) {
				JSONObject letGoalsObject = letGoalsReturnArray.getJSONObject(i);
				String companyName = letGoalsObject.getString("companyName"); //公司名
				String firstGoal = letGoalsObject.getString("firstGoal"); //初盘盘口
				String firstUpodds = letGoalsObject.getString("firstUpodds"); //主队初盘赔率
				String firstDownodds = letGoalsObject.getString("firstDownodds"); //客队初盘赔率
				String goal = letGoalsObject.getString("goal"); //即时盘口
				String upOdds = letGoalsObject.getString("upOdds"); //主队即时赔率
				String downOdds = letGoalsObject.getString("downOdds"); //客队即时赔率
				
				//将数据格式化成两位小数
				firstUpodds = Tools.formatTwoDecimal(firstUpodds);
				firstDownodds = Tools.formatTwoDecimal(firstDownodds);
				
				upOdds = Tools.formatTwoDecimal(upOdds);
				downOdds = Tools.formatTwoDecimal(downOdds);
				
				JSONObject object = new JSONObject();
				object.put("companyName", companyName.trim()); //公司名
				object.put("firstGoal", firstGoal); //初盘盘口
				object.put("firstUpodds", firstUpodds); //主队初盘赔率
				object.put("firstDownodds", firstDownodds); //客队初盘赔率
				object.put("goal", goal); //即时盘口
				object.put("upOdds", upOdds); //主队即时赔率
				object.put("downOdds", downOdds); //客队即时赔率
				letGoalsArray.add(object);
			}
		}
		resultJson.put("letGoals", letGoalsArray); //亚盘
		
		//亚赔-总分盘
		JSONArray totalScoresArray = new JSONArray();
		JSONArray totalScoresReturnArray = valueObject.getJSONArray("totalScores"); //返回数据
		if (totalScoresReturnArray!=null&&totalScoresReturnArray.size()>0) {
			for (int i = 0; i < totalScoresReturnArray.size(); i++) {
				JSONObject totalScoresObject = totalScoresReturnArray.getJSONObject(i);
				String companyName = totalScoresObject.getString("companyName"); //公司名
				String firstGoal = totalScoresObject.getString("firstGoal"); //初盘盘口
				String firstUpodds = totalScoresObject.getString("firstUpodds"); //主队初盘赔率
				String firstDownodds = totalScoresObject.getString("firstDownodds"); //客队初盘赔率
				String goal = totalScoresObject.getString("goal"); //即时盘口
				String upOdds = totalScoresObject.getString("upOdds"); //主队即时赔率
				String downOdds = totalScoresObject.getString("downOdds"); //客队即时赔率
				
				//将数据格式化成两位小数
				firstUpodds = Tools.formatTwoDecimal(firstUpodds);
				firstDownodds = Tools.formatTwoDecimal(firstDownodds);
				
				upOdds = Tools.formatTwoDecimal(upOdds);
				downOdds = Tools.formatTwoDecimal(downOdds);
				
				JSONObject object = new JSONObject();
				object.put("companyName", companyName.trim()); //公司名
				object.put("firstGoal", firstGoal); //初盘盘口
				object.put("firstUpodds", firstUpodds); //主队初盘赔率
				object.put("firstDownodds", firstDownodds); //客队初盘赔率
				object.put("goal", goal); //即时盘口
				object.put("upOdds", upOdds); //主队即时赔率
				object.put("downOdds", downOdds); //客队即时赔率
				totalScoresArray.add(object);
			}
		}
		resultJson.put("totalScores", totalScoresArray); //亚盘
		
		//欧赔
		JSONArray standardsArray = new JSONArray();
		JSONArray standardsReturnArray = valueObject.getJSONArray("standards"); //返回数据
		if (standardsReturnArray!=null&&standardsReturnArray.size()>0) {
			for (int i = 0; i < standardsReturnArray.size(); i++) {
				JSONObject standardsObject = standardsReturnArray.getJSONObject(i);
				String companyName = standardsObject.getString("companyName"); //公司名
				String homeWin = standardsObject.getString("homeWin"); //即时主胜
				String guestWin = standardsObject.getString("guestWin"); //即时客胜
				String homeWinLv = standardsObject.getString("homeWinLv"); //主胜率
				String guestWinLv = standardsObject.getString("guestWinLv"); //客胜率
				String k_h = standardsObject.getString("k_h"); //主凯指
				String k_g = standardsObject.getString("k_g"); //客凯指
				String fanHuanLv = standardsObject.getString("fanHuanLv"); //返还率
				
				//将数据格式化成两位小数
				homeWin = Tools.formatTwoDecimal(homeWin);
				guestWin = Tools.formatTwoDecimal(guestWin);
				
				homeWinLv = Tools.formatTwoDecimal(homeWinLv);
				guestWinLv = Tools.formatTwoDecimal(guestWinLv);
				
				k_h = Tools.formatTwoDecimal(k_h);
				k_g = Tools.formatTwoDecimal(k_g);
				
				fanHuanLv = Tools.formatTwoDecimal(fanHuanLv);
				
				JSONObject object = new JSONObject();
				object.put("companyName", companyName.trim()); //公司名
				object.put("homeWin", homeWin); //即时主胜
				object.put("guestWin", guestWin); //即时客胜
				object.put("homeWinLv", homeWinLv); //主胜率
				object.put("guestWinLv", guestWinLv); //客胜率
				object.put("k_h", k_h); //主凯指
				object.put("k_g", k_g); //客凯指
				object.put("fanHuanLv", fanHuanLv); //返还率
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
				String teamId = rankingsObject.getString("teamId"); //球队编号
				String teamName = rankingsObject.getString("teamName"); //球队名称
				String matchCount = rankingsObject.getString("matchCount"); //赛几场
				String winCount = rankingsObject.getString("winCount"); //胜次数
				String loseCount = rankingsObject.getString("loseCount"); //负次数
				String gainScore = rankingsObject.getString("gainScore"); //得分
				String loseScore = rankingsObject.getString("loseScore"); //失分
				String scoreDifference = rankingsObject.getString("scoreDifference"); //净得分
				
				JSONObject object = new JSONObject();
				object.put("ranking", ranking); //排名
				object.put("teamId", teamId); //球队编号
				object.put("teamName", teamName); //球队名称
				object.put("matchCount", matchCount); //赛几场
				object.put("winCount", winCount); //胜次数
				object.put("loseCount", loseCount); //负次数
				object.put("gainScore", gainScore); //得分
				object.put("loseScore", loseScore); //失分
				object.put("scoreDifference", scoreDifference); //净得分
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
				String homeTeamId = homePreSchedulesObject.getString("homeTeamId"); //主队编号
				String guestTeamId = homePreSchedulesObject.getString("guestTeamId"); //客队编号
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
				String homeTeamId = guestPreSchedulesObject.getString("homeTeamId"); //主队编号
				String guestTeamId = guestPreSchedulesObject.getString("guestTeamId"); //客队编号
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
				String sclassShortName = homeAfterSchedulesObject.getString("sclassShortName"); //联赛简称
				String matchTime = homeAfterSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = homeAfterSchedulesObject.getString("homeTeamId"); //主队编号
				String guestTeamId = homeAfterSchedulesObject.getString("guestTeamId"); //客队编号
				String homeTeam = homeAfterSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = homeAfterSchedulesObject.getString("guestTeam"); //客队名称
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassShortName);
				
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
				String sclassShortName = guestAfterSchedulesObject.getString("sclassShortName"); //联赛简称
				String matchTime = guestAfterSchedulesObject.getString("matchTime"); //比赛时间
				String homeTeamId = guestAfterSchedulesObject.getString("homeTeamId"); //主队编号
				String guestTeamId = guestAfterSchedulesObject.getString("guestTeamId"); //客队编号
				String homeTeam = guestAfterSchedulesObject.getString("homeTeam"); //主队名称
				String guestTeam = guestAfterSchedulesObject.getString("guestTeam"); //客队名称
				
				//如果简称不为空则返回简称
				sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassShortName);
				
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
	public static void parseImmediateScore(JSONArray resultArray, JSONObject immediateScoreObject) {
		String event = immediateScoreObject.getString("event"); //赛事信息
		String sclassName = immediateScoreObject.getString("sclassName"); //联赛名称
		String sclassShortName = immediateScoreObject.getString("sclassShortName"); //联赛简称
		String homeTeam = immediateScoreObject.getString("homeTeam"); //主队
		String guestTeam = immediateScoreObject.getString("guestTeam"); //客队
		String homeScore = immediateScoreObject.getString("homeScore"); //主队比分
		String guestScore = immediateScoreObject.getString("guestScore"); //客队比分
		String matchTime = immediateScoreObject.getString("matchTime"); //比赛时间
		String matchState = immediateScoreObject.getString("matchState"); //比赛状态
		
		//如果简称不为空则返回简称
		sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassShortName);
		//(0:未开) 未开,(1:一节,2:二节,3:三节,4:四节,-2:待定,-3:中断,-5:推迟) 比赛中,(-1:完场,-4:取消) 完场
		String stateMemo = MemoUtil.getJclImmediateScoreStateMemo(matchState); //状态描述
		
		JSONObject object = new JSONObject();
		object.put("event", event); //赛事信息
		object.put("sclassName", sclassName); //联赛名称
		object.put("homeTeam", homeTeam.trim()); //主队
		object.put("guestTeam", guestTeam.trim()); //客队
		object.put("homeScore", homeScore); //主队比分
		object.put("guestScore", guestScore); //客队比分
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
		String sclassShortName = valueObject.getString("sclassShortName"); //联赛简称
		String sclassType = valueObject.getString("sclassType"); //分几节进行(2:上下半场;4:四节)
		String homeTeam = valueObject.getString("homeTeam"); //主队
		String guestTeam = valueObject.getString("guestTeam"); //客队
		String matchTime = valueObject.getString("matchTime"); //比赛时间
		String matchState = valueObject.getString("matchState"); //比赛状态
		String homeScore = valueObject.getString("homeScore"); //主队比分
		String guestScore = valueObject.getString("guestScore"); //客队比分
		String homeOne = valueObject.getString("homeOne"); //主队一节得分(上半场)
		String guestOne = valueObject.getString("guestOne"); //客队一节得分(上半场)
		String homeTwo = valueObject.getString("homeTwo"); //主队二节得分
		String guestTwo = valueObject.getString("guestTwo"); //客队二节得分
		String homeThree = valueObject.getString("homeThree"); //主队三节得分(下半场)
		String guestThree = valueObject.getString("guestThree"); //客队三节得分(下半场)
		String homeFour = valueObject.getString("homeFour"); //主队四节得分
		String guestFour = valueObject.getString("guestFour"); //客队四节得分
		
		//如果简称不为空则返回简称
		sclassName = JingCaiUtil.getLeagueShortName(sclassName, sclassShortName);
		//(0:未开) 未开,(1:一节,2:二节,3:三节,4:四节,-2:待定,-3:中断,-5:推迟) 比赛中,(-1:完场,-4:取消) 完场
		String stateMemo = MemoUtil.getJclImmediateScoreStateMemo(matchState); //状态描述
		
		responseJson.put("sclassName", sclassName); //联赛名称
		responseJson.put("sclassType", sclassType); //分几节进行
		responseJson.put("homeTeam", homeTeam); //主队
		responseJson.put("guestTeam", guestTeam); //客队
		responseJson.put("homeScore", homeScore); //主队比分
		responseJson.put("guestScore", guestScore); //客队比分
		responseJson.put("homeOne", homeOne); //主队一节得分
		responseJson.put("guestOne", guestOne); //客队一节得分
		responseJson.put("homeTwo", homeTwo); //主队二节得分
		responseJson.put("guestTwo", guestTwo); //客队二节得分
		responseJson.put("homeThree", homeThree); //主队三节得分
		responseJson.put("guestThree", guestThree); //客队三节得分
		responseJson.put("homeFour", homeFour); //主队四节得分
		responseJson.put("guestFour", guestFour); //客队四节得分
		responseJson.put("matchTime", DateParseFormatUtil.formatY_M_d(matchTime)); //比赛时间
		responseJson.put(Constants.stateMemo, stateMemo); //状态描述
	}
	
	/**
	 * 解析竞彩赔率(篮球)
	 * 
	 * @param object
	 * @param xml
	 * @param id
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void parseXMLForJingcaiPeiLvLQ(JSONObject object, String xml, String id) {
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
					// 玩法:胜负
					Element vsElement = itemElement.element("vs");
					Element v0Element = vsElement.element("v0");
					if (v0Element != null && !v0Element.getText().equals("0000")) {
						object.put("v0", v0Element.getText());
					} else {
						object.put("v0", "");
					}
					Element v3Element = vsElement.element("v3");
					if (v3Element != null && !v3Element.getText().equals("0000")) {
						object.put("v3", v3Element.getText());
					} else {
						object.put("v3", "");
					}
					// 玩法:让分胜负
					Element letVsElement = itemElement.element("letVs");
					Element letVs_V0Element = letVsElement.element("v0");
					if (letVs_V0Element != null && !letVs_V0Element.getText().equals("0000")) {
						object.put("letVs_v0", letVs_V0Element.getText());
					} else {
						object.put("letVs_v0", "");
					}
					Element letVs_V3Element = letVsElement.element("v3");
					if (letVs_V3Element != null && !letVs_V3Element.getText().equals("0000")) {
						object.put("letVs_v3", letVs_V3Element.getText());
					} else {
						object.put("letVs_v3", "");
					}
					Element letPointElement = letVsElement.element("letPoint");
					if (letPointElement != null && !letPointElement.getText().equals("0000")) {
						object.put("letPoint", letPointElement.getText());
					} else {
						object.put("letPoint", "");
					}
					// 玩法:大小分
					Element bsElement = itemElement.element("bs");
					Element gElement = bsElement.element("g");
					if (gElement != null && !gElement.getText().equals("0000")) {
						object.put("g", gElement.getText());
					} else {
						object.put("g", "");
					}
					Element lElement = bsElement.element("l");
					if (lElement != null && !lElement.getText().equals("0000")) {
						object.put("l", lElement.getText());
					} else {
						object.put("l", "");
					}
					Element basePointElement = bsElement.element("basePoint");
					if (basePointElement != null && !basePointElement.getText().equals("0000")) {
						object.put("basePoint", basePointElement.getText());
					} else {
						object.put("basePoint", "");
					}
					// 玩法:胜分差
					Element diffElement = itemElement.element("diff");
					Element v01Element = diffElement.element("v01");
					if (v01Element != null && !v01Element.getText().equals("0000")) {
						object.put("v01", v01Element.getText());
					} else {
						object.put("v01", "");
					}
					Element v02Element = diffElement.element("v02");
					if (v02Element != null && !v02Element.getText().equals("0000")) {
						object.put("v02", v02Element.getText());
					} else {
						object.put("v02", "");
					}
					Element v03Element = diffElement.element("v03");
					if (v03Element != null && !v03Element.getText().equals("0000")) {
						object.put("v03", v03Element.getText());
					} else {
						object.put("v03", "");
					}
					Element v04Element = diffElement.element("v04");
					if (v04Element != null && !v04Element.getText().equals("0000")) {
						object.put("v04", v04Element.getText());
					} else {
						object.put("v04", "");
					}
					Element v05Element = diffElement.element("v05");
					if (v05Element != null && !v05Element.getText().equals("0000")) {
						object.put("v05", v05Element.getText());
					} else {
						object.put("v05", "");
					}
					Element v06Element = diffElement.element("v06");
					if (v06Element != null && !v06Element.getText().equals("0000")) {
						object.put("v06", v06Element.getText());
					} else {
						object.put("v06", "");
					}
					Element v11Element = diffElement.element("v11");
					if (v11Element != null && !v11Element.getText().equals("0000")) {
						object.put("v11", v11Element.getText());
					} else {
						object.put("v11", "");
					}
					Element v12Element = diffElement.element("v12");
					if (v12Element != null && !v12Element.getText().equals("0000")) {
						object.put("v12", v12Element.getText());
					} else {
						object.put("v12", "");
					}
					Element v13Element = diffElement.element("v13");
					if (v13Element != null && !v13Element.getText().equals("0000")) {
						object.put("v13", v13Element.getText());
					} else {
						object.put("v13", "");
					}
					Element v14Element = diffElement.element("v14");
					if (v14Element != null && !v14Element.getText().equals("0000")) {
						object.put("v14", v14Element.getText());
					} else {
						object.put("v14", "");
					}
					Element v15Element = diffElement.element("v15");
					if (v15Element != null && !v15Element.getText().equals("0000")) {
						object.put("v15", v15Element.getText());
					} else {
						object.put("v15", "");
					}
					Element v16Element = diffElement.element("v16");
					if (v16Element != null && !v16Element.getText().equals("0000")) {
						object.put("v16", v16Element.getText());
					} else {
						object.put("v16", "");
					}
				}
			}
			if (!isExist) {
				// 玩法:胜负
				object.put("v0", "");
				object.put("v3", "");
				// 玩法:让分胜负
				object.put("letVs_v0", "");
				object.put("letVs_v3", "");
				object.put("letPoint", "");
				// 玩法:大小分
				object.put("g", "");
				object.put("l", "");
				object.put("basePoint", "");
				// 玩法:胜分差
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
		} catch (DocumentException e) {
			logger.error("解析竞彩赔率(篮球)发生异常", e);
		}
	}
	
}
