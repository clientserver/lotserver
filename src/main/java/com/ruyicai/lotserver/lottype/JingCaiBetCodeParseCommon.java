package com.ruyicai.lotserver.lottype;

import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.jc.lq.Dxf;
import com.ruyicai.lotserver.lottype.jc.lq.Rfsf;
import com.ruyicai.lotserver.lottype.jc.lq.Sf;
import com.ruyicai.lotserver.lottype.jc.lq.SfcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Bf;
import com.ruyicai.lotserver.lottype.jc.zq.BqcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Jqs;
import com.ruyicai.lotserver.lottype.jc.zq.Spf;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竞彩注码解析公共类
 * @author Administrator
 *
 */
public class JingCaiBetCodeParseCommon {

	/**
	 * 获取赛事信息
	 * @param matchesValueObject
	 * @return
	 */
	public static JSONObject getMatchInfoJson(JSONObject matchesValueObject) {
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
	}
	
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
	public static void putTeamMap(String lotNo, String id, String code, String peiLvString, Map<String, Object> teamMap, 
			String play, String letPoint, String basePoint) {
		if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
			Spf.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
			Bf.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
			Jqs.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
			BqcJc.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
			Sf.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
			Rfsf.putMap(lotNo, id, code, peiLvString, teamMap, play, letPoint);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
			SfcJc.putMap(lotNo, id, code, peiLvString, teamMap);
		} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
			Dxf.putMap(lotNo, id, code, peiLvString, teamMap, play, basePoint);
		}
	}
	
	/**
	 * 获取解析后的注码
	 * @param lotNo
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getBetContentJson(Map<String, Object> value, boolean addRangFen) {
		JSONObject resultObject = new JSONObject();
		StringBuilder builder = new StringBuilder(); //投注内容
		String letScore = ""; //让分(竞彩篮球让分胜负)
		String totalScore = ""; //总分盘(竞彩篮球大小分)
		String endCharacter = "<br/>"; //结束符
		Map<String, Map<String, List<String>>> lotNoMap = (Map<String, Map<String, List<String>>>)value.get("code");
		if (lotNoMap!=null) {
			String betContentHtml = "";
			for (Map.Entry<String, Map<String, List<String>>> entry : lotNoMap.entrySet()) {
				String lotNo = entry.getKey();
				Map<String, List<String>> codeMap = entry.getValue();
				
				if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
					betContentHtml = Spf.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
					betContentHtml = Bf.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
					betContentHtml = Jqs.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
					betContentHtml = BqcJc.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
					betContentHtml = Sf.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
					JSONObject jsonObject = Rfsf.getBetContentByMap(value, codeMap, addRangFen);
					betContentHtml = jsonObject.getString("betContent");
					letScore = jsonObject.getString("letScore");
				} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
					betContentHtml = SfcJc.getBetContentByMap(value, codeMap);
				} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
					JSONObject jsonObject = Dxf.getBetContentByMap(value, codeMap);
					betContentHtml = jsonObject.getString("betContent");
					totalScore = jsonObject.getString("totalScore");
				}
				builder.append(betContentHtml);
			}
		}
		String betContentHtml = StringUtil.removeEndCharacter(builder.toString(), endCharacter);
		resultObject.put("betContentHtml", betContentHtml);
		resultObject.put("letScore", letScore);
		resultObject.put("totalScore", totalScore);
		return resultObject;
	}
	
}
