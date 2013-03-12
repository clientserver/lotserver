package com.ruyicai.lotserver.util.lot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ruyicai.lotserver.consts.jc.DxfEnum;
import com.ruyicai.lotserver.consts.jc.JcPlayEnum;
import com.ruyicai.lotserver.consts.jc.SpfEnum;
import com.ruyicai.lotserver.consts.jc.WeekEnum;
import com.ruyicai.lotserver.lottype.jc.lq.Dxf;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;
import net.sf.json.JSONObject;

/**
 * 竞彩公共类
 * 
 * @author Administrator
 * 
 */
public class JingCaiUtil {
	
	/**
	 * 根据weekId获得星期(供竞彩星期转换使用)
	 * 
	 * @param weekId
	 * @return
	 */
	public static String getWeekByWeekId(String weekId) {
		String week = "";
		if (weekId!=null) {
			WeekEnum[] values = WeekEnum.values();
			for (WeekEnum weekEnum : values) {
				if (weekId.equals(weekEnum.value())) {
					week = weekEnum.memo();
					break;
				}
			}
		}
		return week;
	}

	/**
	 * 获取竞彩玩法
	 * 
	 * @param code
	 * @return
	 */
	public static JSONObject getJingCaiPlay(String wanfa) {
		JSONObject object = new JSONObject();
		String play = "未知";
		String valueType = "1";
		if (wanfa!=null) {
			if (wanfa.equals(JcPlayEnum.danGuan.value())) {
				valueType = "0";
			}
			JcPlayEnum[] values = JcPlayEnum.values();
			for (JcPlayEnum jcPlayEnum : values) {
				if (wanfa.equals(jcPlayEnum.value())) {
					play = jcPlayEnum.memo();
					break;
				}
			}
		}
		object.put("play", play);
		object.put("valueType", valueType);
		return object;
	}
	
	/**
	 * 根据列表获得玩法
	 * @param list
	 * @return
	 */
	public static String getJingCaiPlayByList(List<String> list) {
		StringBuilder builder = new StringBuilder();
		if (list!=null&&list.size()>0) {
			for (String wanfa : list) {
				JSONObject wanfaObject = getJingCaiPlay(wanfa);
				String play = wanfaObject.getString("play");
				builder.append(play).append(",");
			}
		}
		return StringUtil.removeEndCharacter(builder.toString(), ",");
	}
	
	/**
	 * 根据注码获得赔率
	 * @param id
	 * @param peiLvString
	 * @param code
	 * @return
	 */
	public static String getPeiLvByCode(String id, String peiLvString, String code) {
		//20120520*7*001*|1:12.0|3:12.0|^20120520*7*002*|1:12.0|3:12.0|^
		//20130129*2*001*|J00001|3:2.780|^20130129*2*002*|J00004|00:1.900|^(混合过关)
		String peiLv = ""; //赔率
		if (!Tools.isEmpty(peiLvString)) {
			String[] split = peiLvString.split("\\^");
			for (String string : split) {
				if (string.startsWith(id)) {
					String[] split2 = string.substring(id.length()).split("\\|");
					for (int i = 1; i < split2.length; i++) {
						String[] split3 = split2[i].split(":");
						if (split3[0].equals(code)) {
							peiLv = split3[1];
							break;
						}
					}
				}
			}
		}
		return peiLv;
	}
	
	/**
	 * 根据注码获得赔率(竞彩篮球让分胜负,竞彩篮球大小分)
	 * @param id
	 * @param peiLvString
	 * @param code
	 * @return
	 */
	public static JSONObject getPeiLvByCodeForJ00006OrJ00008(String id, String peiLvString, String code) {
		//20120529*2*301*|2(202.5):1.750|^20120529*2*302*|2(153.5):1.700|1(153.5):1.700|^20120530*3*303*|2(180.5):1.750|^
		//20130130*3*306*|J00005|0:1.350|^20130130*3*307*|J00006|0(-0.50):1.700|^20130130*3*308*|J00008|1(180.5):1.750|^20130130*3*309*|J00007|01:3.800|^
		JSONObject resultObject = new JSONObject();
		
		String peiLv = ""; //赔率
		String letPoint = ""; //让分胜负
		String basePoint = ""; //基本分
		if (!Tools.isEmpty(peiLvString)) {
			String[] split = peiLvString.split("\\^");
			for (String string : split) {
				if (string.startsWith(id)) {
					String[] split2 = string.substring(id.length()).split("\\|");
					for (int i = 1; i < split2.length; i++) {
						String[] split3 = split2[i].split(":");
						if (split3.length==2&&split3[0].substring(0, split3[0].indexOf("(")).equals(code)) {
							peiLv = split3[1];
							Pattern pattern = Pattern.compile(".*\\((.*)\\).*");
							Matcher matcher = pattern.matcher(split3[0]);
							if(matcher.find()) {
								letPoint = matcher.group(1);
								basePoint = matcher.group(1);
							}
							break;
						}
					}
				}
			}
		}
		resultObject.put("peiLv", peiLv);
		resultObject.put("letPoint", letPoint);
		resultObject.put("basePoint", basePoint);
		return resultObject;
	}
	
	/**
	 * 根据单个注码获取竞彩胜平负(带"主")
	 * @param code
	 * @return
	 */
	public static String getSpfHomeBySingleCode(String code, boolean addRangFen) {
		String result = getSpfBySingleCode(code);
		if (!StringUtil.isEmpty(result)) {
			if (!result.equals("平")) {
				result = "主" + result;
			}
			if (addRangFen) {
				result = "让分" + result;
			}
		}
		return result;
	}
	
	/**
	 * 根据单个注码获取竞彩胜平负
	 * @param code
	 * @return
	 */
	public static String getSpfBySingleCode(String code) {
		String result = "";
		if (code!=null) {
			SpfEnum[] values = SpfEnum.values();
			for (SpfEnum spfEnum : values) {
				if (code.equals(spfEnum.value())) {
					result = spfEnum.memo();
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 判断是否胆码
	 * @param orderInfo //602@20120815|3|022|10^$20120815|3|023|1^20120815|3|024|1^
	 * @param info
	 * @return
	 */
	public static JSONObject getDanMaString(String orderInfo, String info) {
		JSONObject returnJson = new JSONObject();
		String result = "";
		String isDanMa = "false";
		List<String> danMaList = new ArrayList<String>(); //胆码列表
		if (orderInfo!=null&&orderInfo.indexOf("$")>-1) { //设胆
			String[] split = orderInfo.split("@");
			String[] split2 = split[1].split("\\$");
			String[] split3 = split2[0].split("\\^");
			for (String string : split3) {
				danMaList.add(string.substring(0, string.lastIndexOf("|")));
			}
		}
		if (danMaList!=null&&danMaList.size()>0) {
			for (String string : danMaList) {
				if (string.equals(info)) {
					result = "(胆)";
					isDanMa = "true";
					break;
				}
			}
		}
		returnJson.put("result", result);
		returnJson.put("isDanMa", isDanMa);
		return returnJson;
	}
	
	/**
	 * 根据比分获得竟彩比赛结果
	 * @param score
	 * @return
	 */
	public static String getJingCaiResultByScore(String score, boolean isAddZhu) {
		String result = "未开"; //比赛结果
		if (!Tools.isEmpty(score)&&score.indexOf(":")>-1) {
			String[] scores = score.split(":");
			Integer zhuScore = Integer.parseInt(scores[0]);
			Integer keScore = Integer.parseInt(scores[1]);
			if (zhuScore > keScore) {
				result = isAddZhu ? "主胜":"胜";
			} else if (zhuScore == keScore) {
				result = "平";
			} else {
				result = isAddZhu ? "主负":"负";
			}
		}
		return result;
	}
	
	/**
	 * 获得联赛的简称
	 * @param sclassName
	 * @param sclassName_j
	 * @return
	 */
	public static String getLeagueShortName(String sclassName, String sclassName_j) {
		//如果简称不为空则返回简称
		if (!Tools.isEmpty(sclassName_j)&&!sclassName_j.equals("null")) { 
			sclassName = sclassName_j;
		}
		return sclassName.trim();
	}
	
	/**
	 * 根据彩种编号获取竟彩类型
	 * @param lotNo
	 * @return
	 */
	public static String getJingCaiTypeByLotNo(String lotNo) {
		String jingCaiType = "";
		if (LotTypeUtil.isJingCaiZQ(lotNo)) { //竟彩足球
			jingCaiType = "1";
		} else if (LotTypeUtil.isJingCaiLQ(lotNo)) { //竟彩篮球
			jingCaiType = "0";
		}
		return jingCaiType;
	}
	
	/**
	 * 获得胜平负的开奖号码和进球数
	 * @param homeScore
	 * @param guestScore
	 * @return
	 */
	public static JSONObject getSpfWinByScore(String homeScore, String guestScore) {
		JSONObject resultObject = new JSONObject();
		String win = ""; //开奖号码
		String goals = ""; //总进球数
		if (!Tools.isEmpty(homeScore)&&!homeScore.equals("null")
				&&!Tools.isEmpty(guestScore)&&!guestScore.equals("null")) {
			BigDecimal homeScoreB = new BigDecimal(homeScore);
			BigDecimal guestScoreB = new BigDecimal(guestScore);
			if (homeScoreB.compareTo(guestScoreB)==1) { //胜
				win = SpfEnum.win.value();
			} else if (homeScoreB.compareTo(guestScoreB)==0) { //平
				win = SpfEnum.standoff.value();
			} else if (homeScoreB.compareTo(guestScoreB)==-1) { //负
				win = SpfEnum.loss.value();
			}
			goals = homeScoreB.add(guestScoreB).toString();
		}
		resultObject.put("win", win);
		resultObject.put("goals", goals);
		return resultObject;
	}
	
	/**
	 * 获得胜平负的赛果
	 * @param homeScore
	 * @param guestScore
	 * @return
	 */
	public static JSONObject getMatchResult(String homeScore, String guestScore) {
		JSONObject resultObject = new JSONObject();
		JSONObject object = getSpfWinByScore(homeScore, guestScore);
		String win = object.getString("win"); //开奖号码
		String goals = object.getString("goals"); //进球数
		String result = getSpfBySingleCode(win); //赛果
		resultObject.put("result", result);
		resultObject.put("goals", goals);
		return resultObject;
	}
	
	/**
	 * 获得让分胜负的赛果
	 * @param homeScore
	 * @param guestScore
	 * @param letScore
	 * @return
	 */
	public static String getResultRfsf(String homeScore, String guestScore, String letScore) {
		String win = getRfsfWinByScore(homeScore, guestScore, letScore);
		String result = getSpfBySingleCode(win); //赛果
		return result;
	}
	
	/**
	 * 获得让分胜负的开奖号码
	 * @param homeScore
	 * @param guestScore
	 * @param letScore
	 * @return
	 */
	public static String getRfsfWinByScore(String homeScore, String guestScore, String letScore) {
		String win = "";
		if (!Tools.isEmpty(homeScore)&&!homeScore.equals("null")&&!Tools.isEmpty(guestScore)&&!guestScore.equals("null")
				&&!Tools.isEmpty(letScore)&&!letScore.equals("null")) {
			BigDecimal homeScoreB = new BigDecimal(homeScore);
			BigDecimal guestScoreB = new BigDecimal(guestScore);
			if (letScore.startsWith("+")) {
				BigDecimal letScoreB = new BigDecimal(letScore.replaceFirst("\\+", ""));
				homeScoreB = homeScoreB.add(letScoreB);
			}
			if (letScore.startsWith("-")) {
				BigDecimal letScoreB = new BigDecimal(letScore.replaceFirst("\\-", ""));
				homeScoreB = homeScoreB.subtract(letScoreB);
			}
			if (homeScoreB.compareTo(guestScoreB)==1) { //胜
				win = SpfEnum.win.value();
			} else if (homeScoreB.compareTo(guestScoreB)==0) { //平
				win = SpfEnum.standoff.value();
			} else if (homeScoreB.compareTo(guestScoreB)==-1) { //负
				win = SpfEnum.loss.value();
			}
		}
		return win;
	}
	
	/**
	 * 获得大小分的赛果
	 * @param homeScore
	 * @param guestScore
	 * @param totalScore
	 * @return
	 */
	public static String getResultDx(String homeScore, String guestScore, String totalScore) {
		String win = getDxfWinByScore(homeScore, guestScore, totalScore);
		String result = Dxf.parseSingleCode(win);
		return result;
	}
	
	/**
	 * 获得大小分的开奖号码
	 * @param homeScore
	 * @param guestScore
	 * @param totalScore
	 * @return
	 */
	public static String getDxfWinByScore(String homeScore, String guestScore, String totalScore) {
		String win = "";
		if (!Tools.isEmpty(homeScore)&&!homeScore.equals("null")&&!Tools.isEmpty(guestScore)&&!guestScore.equals("null")
				&&!Tools.isEmpty(totalScore)&&!totalScore.equals("null")) {
			BigDecimal homeScoreB = new BigDecimal(homeScore);
			BigDecimal guestScoreB = new BigDecimal(guestScore);
			BigDecimal totalScoreB = new BigDecimal(totalScore);
			BigDecimal total = homeScoreB.add(guestScoreB);
			if (total.compareTo(totalScoreB)==1) { //大
				win = DxfEnum.big.value();
			} else if (total.compareTo(totalScoreB)==-1) { //小
				win = DxfEnum.small.value();
			}
		}
		return win;
	}
	
	/**
	 * 获得球队名称
	 * @param teamString
	 * @return
	 */
	public static JSONObject getTeamName(String teamString) {
		JSONObject object = new JSONObject();
		String homeTeam = ""; //主队
		String guestTeam = ""; //客队
		if (!Tools.isEmpty(teamString)&&!teamString.trim().equals("null")&&teamString.indexOf(":")>-1) {
			String[] split = teamString.split(":");
			homeTeam = split[0];
			guestTeam = split[1];
		}
		object.put("homeTeam", homeTeam);
		object.put("guestTeam", guestTeam);
		return object;
	}
	
	/**
	 * 根据结果获得比分
	 * @param result
	 * @return
	 */
	public static JSONObject getScoreByResult(String result) {
		JSONObject object = new JSONObject();
		String homeScore = ""; //主队比分
		String guestScore = ""; //客队比分
		if (!Tools.isEmpty(result)&&!result.equals("null")) {
			if (!Tools.isEmpty(result)&&result.indexOf(":")>-1) {
				String[] scores = result.split(":");
				homeScore = scores[0];
				guestScore = scores[1];
			}
		}
		object.put("homeScore", homeScore);
		object.put("guestScore", guestScore);
		return object;
	}
	
	/**
	 * 获得解析注码
	 * @param code
	 * @param peiLv
	 * @param isHtml
	 * @return
	 */
	public static String getParseCodeWithPeiLv(String parseCode, List<String> peiLvs) {
		StringBuilder builder = new StringBuilder();
		builder.append(parseCode);
		//将多个赔率连起来
		String peiLv = StringUtil.joinStringArrayWithCharacter(peiLvs, ",");
		if (!StringUtil.isEmpty(peiLv)) {
			builder.append("("+peiLv+")");
		}
		return builder.toString();
	}
	
	/**
	 * 将注码和赔率放在Map里
	 * @param teamMap
	 * @param code
	 * @param peiLv
	 */
	@SuppressWarnings("unchecked")
	public static void putCodeToCodeMap(String lotNo, Map<String, Object> teamMap, String code, String peiLv) {
		Object object = teamMap.get("code");
		Map<String, Map<String, List<String>>> lotNoMap = null;
		Map<String, List<String>> codeMap = null;
		List<String> peiLvs = null;
		if (object==null) {
			lotNoMap = new HashMap<String, Map<String, List<String>>>();
			codeMap = new HashMap<String, List<String>>();
			peiLvs = new ArrayList<String>();
		} else {
			lotNoMap = (Map<String, Map<String, List<String>>>)object;
			codeMap = lotNoMap.get(lotNo);
			if (codeMap==null) {
				codeMap = new HashMap<String, List<String>>();
				peiLvs = new ArrayList<String>();
			} else {
				peiLvs = codeMap.get(code);
				peiLvs = (peiLvs==null ? new ArrayList<String>() : peiLvs);
			}
		}
		if (!peiLvs.contains(peiLv)) {
			peiLvs.add(peiLv);
		}
		codeMap.put(code, peiLvs);
		lotNoMap.put(lotNo, codeMap);
		teamMap.put("code", lotNoMap);
	} 
	
}
