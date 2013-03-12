package com.ruyicai.lotserver.lottype.jc.lq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 竟彩篮球胜负
 * @author Administrator
 *
 */
public class Sf {

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
				String pCode = JingCaiUtil.getSpfHomeBySingleCode(code, false);
				String parseCode = JingCaiUtil.getParseCodeWithPeiLv(pCode, peiLvs);
				//中奖标红
				JSONObject object = JingCaiUtil.getSpfWinByScore(homeScore, guestScore);
				String win = object.getString("win"); //开奖号码
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
			for (int i = 0; i < betCode.length(); i++) {
				String code = betCode.substring(i, i+1);
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, code);
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
		StringBuilder builder = new StringBuilder(); //投注内容
		String endCharacter = " "; //结束符
		if (!StringUtil.isEmpty(betCode)) {
			for (int i = 0; i < betCode.length(); i++) {
				String code = betCode.substring(i, i+1);
				String peiLv = JingCaiUtil.getPeiLvByCode(id, peiLvString, code);
				List<String> peiLvs = new ArrayList<String>();
				peiLvs.add(peiLv);
				//解析后的注码
				String pCode = JingCaiUtil.getSpfHomeBySingleCode(code, false);
				String parseCode = JingCaiUtil.getParseCodeWithPeiLv(pCode, peiLvs);
				//中奖标红
				if (isHtml) {
					endCharacter = "<br/>";
					JSONObject object = JingCaiUtil.getSpfWinByScore(homeScore, guestScore);
					String win = object.getString("win"); //开奖号码
					if (win!=null&&win.equals(code)) {
						parseCode = "<font color=\"red\">"+parseCode+"</font>";
					}
				}
				builder.append(parseCode).append(endCharacter);
			}
		}
		return StringUtil.removeEndCharacter(builder.toString(), endCharacter);
	}
	
}
