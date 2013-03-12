package com.ruyicai.lotserver.lottype.jc.zq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.ruyicai.lotserver.consts.jc.BfEnum;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竟彩足球比分
 * @author Administrator
 *
 */
public class Bf {

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
				String win = getBfWinByScore(homeScore, guestScore);
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
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, betCode);
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
			String homeScore, String guestScore) {
		StringBuilder builder = new StringBuilder();
		String endCharacter = " "; //结束符
		if (!StringUtil.isEmpty(betCode)) {
			int l = betCode.length();
			int h = l/2;
			int n = 0;
			for (int i = 0; i < h; i++) {
				String code = betCode.substring(n, n+2);
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, betCode);
				n = n + 2;
				List<String> peiLvs = new ArrayList<String>();
				peiLvs.add(peiLv);
				//解析后的注码
				String pCode = parseSingleCode(code);
				String parseCode = JingCaiUtil.getParseCodeWithPeiLv(pCode, peiLvs);
				//中奖标红
				if (isHtml) {
					endCharacter = "<br/>";
					String win = getBfWinByScore(homeScore, guestScore);
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
	 * 解析单个注码
	 * @param code
	 * @return
	 */
	private static String parseSingleCode(String code) {
		String parseCode = ""; //解析后的注码
		if (code.equals("90")) {
			parseCode = "胜其他";
		} else if (code.equals("99")) {
			parseCode = "平其他";
		} else if (code.equals("09")) {
			parseCode = "负其他";
		} else {
			parseCode = code.substring(0, 1)+":"+code.substring(1);
		}
		return parseCode;
	}
	
	/**
	 * 获得比分的开奖号码
	 * @param homeScore
	 * @param guestScore
	 * @return
	 */
	private static String getBfWinByScore(String homeScore, String guestScore) {
		String win = "";
		if (!StringUtil.isEmpty(homeScore)&&!homeScore.equals("null")
				&&!StringUtil.isEmpty(guestScore)&&!guestScore.equals("null")) {
			Integer homeScoreInt = Integer.parseInt(homeScore);
			Integer guestScoreInt = Integer.parseInt(guestScore);
			if (homeScoreInt>guestScoreInt) { //主胜
				if ((homeScoreInt==1&&guestScoreInt==0)||(homeScoreInt==2&&guestScoreInt==0)||(homeScoreInt==2&&guestScoreInt==1)
						||(homeScoreInt==3&&guestScoreInt==0)||(homeScoreInt==3&&guestScoreInt==1)||(homeScoreInt==3&&guestScoreInt==2)
						||(homeScoreInt==4&&guestScoreInt==0)||(homeScoreInt==4&&guestScoreInt==1)||(homeScoreInt==4&&guestScoreInt==2)
						||(homeScoreInt==5&&guestScoreInt==0)||(homeScoreInt==5&&guestScoreInt==1)||(homeScoreInt==5&&guestScoreInt==2)) {
					win = homeScore+guestScore;
				} else { //胜其他
					win = BfEnum.win_others.value();
				}
			} else if (homeScoreInt==guestScoreInt) { //平
				if ((homeScoreInt==0&&guestScoreInt==0)||(homeScoreInt==1&&guestScoreInt==1)||(homeScoreInt==2&&guestScoreInt==2)
						||(homeScoreInt==3&&guestScoreInt==3)) {
					win = homeScore+guestScore;
				} else { //平其他
					win = BfEnum.standoff_others.value();
				}
			} else if (homeScoreInt<guestScoreInt) { //主负
				if ((homeScoreInt==0&&guestScoreInt==1)||(homeScoreInt==0&&guestScoreInt==2)||(homeScoreInt==1&&guestScoreInt==2)
						||(homeScoreInt==0&&guestScoreInt==3)||(homeScoreInt==1&&guestScoreInt==3)||(homeScoreInt==2&&guestScoreInt==3)
						||(homeScoreInt==0&&guestScoreInt==4)||(homeScoreInt==1&&guestScoreInt==4)||(homeScoreInt==2&&guestScoreInt==4)
						||(homeScoreInt==0&&guestScoreInt==5)||(homeScoreInt==1&&guestScoreInt==5)||(homeScoreInt==2&&guestScoreInt==5)) {
					win = homeScore+guestScore;
				} else { //负其他
					win = BfEnum.loss_others.value();
				}
			}
		}
		return win;
	}
	
}
