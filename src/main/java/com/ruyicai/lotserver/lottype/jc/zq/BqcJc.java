package com.ruyicai.lotserver.lottype.jc.zq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ruyicai.lotserver.consts.jc.BqcEnum;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竟彩足球半全场
 * @author Administrator
 *
 */
public class BqcJc {

	/**
	 * 解析Map
	 * @param valueMap
	 * @param homeScore
	 * @param guestScore
	 * @return
	 */
	public static String getBetContentByMap(Map<String, Object> value, Map<String, List<String>> codeMap) {
		String homeScore = (String)value.get("homeScore"); //主队比分
		String guestScore = (String)value.get("guestScore"); //客队比分
		String homeHalfScore = (String)value.get("homeHalfScore"); //主队半场比分
		String guestHalfScore = (String)value.get("guestHalfScore"); //客队半场比分
		
		StringBuilder builder = new StringBuilder(); //投注内容
		String endCharacter = "<br/>"; //结束符
		if (codeMap!=null) {
			for(Map.Entry<String, List<String>> entry : codeMap.entrySet()) {
				String code = entry.getKey();
				List<String> peiLvs = entry.getValue();
				//解析后的注码
				String pCode = parseSingleCode(code); 
				String parseCode = JingCaiUtil.getParseCodeWithPeiLv(pCode, peiLvs);
				//中奖标红
				String win = getBqcWinByScore(homeHalfScore, guestHalfScore, homeScore, guestScore);
				if (win!=null&&win.equals(code)) {
					parseCode = "<font color=\"red\">"+parseCode+"</font>";
				}
				builder.append(parseCode).append(endCharacter);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 将赛事信息放在Map里
	 * @param info
	 * @param peiLvString
	 * @param infoMap
	 */
	public static void putMap(String lotNo, String id, String betCode, String peiLvString, Map<String, Object> teamMap) {
		if (!StringUtil.isEmpty(betCode)) {
			int l = betCode.length();
			int h = l/2;
			int n = 0;
			for (int i = 0; i < h; i++) {
				String code = betCode.substring(n, n+2);
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, code);
				n = n + 2;
				//将注码和赔率放在Map里
				JingCaiUtil.putCodeToCodeMap(lotNo, teamMap, code, peiLv);
			}
		}
	}
	
	/**
	 * 获得投注内容
	 * @param id
	 * @param peiLvString
	 * @param betCode
	 * @return
	 */
	public static String getBetContent(String id, String peiLvString, String betCode, boolean isHtml, 
			String homeHalfScore, String guestHalfScore, String homeScore, String guestScore) {
		StringBuilder builder = new StringBuilder();
		String endCharacter = " "; //结束符
		if (!StringUtil.isEmpty(betCode)) {
			int l = betCode.length();
			int h = l/2;
			int n = 0;
			for (int i = 0; i < h; i++) {
				String code = betCode.substring(n, n+2);
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, code);
				n = n + 2;
				List<String> peiLvs = new ArrayList<String>();
				peiLvs.add(peiLv);
				//解析后的注码
				String pCode = parseSingleCode(code); 
				String parseCode = JingCaiUtil.getParseCodeWithPeiLv(pCode, peiLvs);
				//中奖标红
				if (isHtml) {
					endCharacter = "<br/>";
					String win = getBqcWinByScore(homeHalfScore, guestHalfScore, homeScore, guestScore);
					if (win!=null&&win.equals(code)) {
						parseCode = "<font color=\"red\">"+parseCode+"</font>";
					}
				}
				builder.append(parseCode).append(endCharacter);
			}
		}
		return StringUtil.removeEndCharacter(builder.toString(), endCharacter);
	}
	
	/**
	 * 解析竞彩足球半场胜平负的注码
	 * @param code
	 * @return
	 */
	public static String parseSingleCode(String code) {
		String result = "";
		if (code!=null) {
			BqcEnum[] values = BqcEnum.values();
			for (BqcEnum bqcEnum : values) {
				if (code.equals(bqcEnum.value())) {
					result = bqcEnum.memo();
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 获得半全场的开奖号码
	 * @param homeHalfScore
	 * @param guestHalfScore
	 * @param homeScore
	 * @param guestScore
	 * @return
	 */
	private static String getBqcWinByScore(String homeHalfScore, String guestHalfScore, String homeScore, String guestScore) {
		String win = "";
		if (!StringUtil.isEmpty(homeHalfScore)&&!homeHalfScore.equals("null")&&!StringUtil.isEmpty(guestHalfScore)&&!guestHalfScore.equals("null")
				&&!StringUtil.isEmpty(homeScore)&&!homeScore.equals("null")&&!StringUtil.isEmpty(guestScore)&&!guestScore.equals("null")) {
			Integer homeHalfScoreInt = Integer.parseInt(homeHalfScore);
			Integer guestHalfScoreInt = Integer.parseInt(guestHalfScore);
			Integer homeScoreInt = Integer.parseInt(homeScore);
			Integer guestScoreInt = Integer.parseInt(guestScore);
			if ((homeHalfScoreInt<guestHalfScoreInt)&&(homeScoreInt<guestScoreInt)) { //负负
				win = BqcEnum.lose_lose.value();
			} else if ((homeHalfScoreInt<guestHalfScoreInt)&&(homeScoreInt==guestScoreInt)) { //负平
				win = BqcEnum.lose_standoff.value();
			} else if ((homeHalfScoreInt<guestHalfScoreInt)&&(homeScoreInt>guestScoreInt)) { //负胜
				win = BqcEnum.lose_win.value();
			} else if ((homeHalfScoreInt==guestHalfScoreInt)&&(homeScoreInt==guestScoreInt)) { //平平
				win = BqcEnum.standoff_standoff.value();
			} else if ((homeHalfScoreInt==guestHalfScoreInt)&&(homeScoreInt<guestScoreInt)) { //平负
				win = BqcEnum.standoff_lose.value();
			} else if ((homeHalfScoreInt==guestHalfScoreInt)&&(homeScoreInt>guestScoreInt)) { //平胜
				win = BqcEnum.standoff_win.value();
			} else if ((homeHalfScoreInt>guestHalfScoreInt)&&(homeScoreInt>guestScoreInt)) { //胜胜
				win = BqcEnum.win_win.value();
			} else if ((homeHalfScoreInt>guestHalfScoreInt)&&(homeScoreInt<guestScoreInt)) { //胜负
				win = BqcEnum.win_lose.value();
			} else if ((homeHalfScoreInt>guestHalfScoreInt)&&(homeScoreInt==guestScoreInt)) { //胜平
				win = BqcEnum.win_standoff.value();
			}
		}
		return win;
	}
	
}
