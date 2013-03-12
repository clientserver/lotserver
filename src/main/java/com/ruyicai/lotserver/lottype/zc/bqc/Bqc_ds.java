package com.ruyicai.lotserver.lottype.zc.bqc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

/**
 * 足球半全场-单式
 * @author Administrator
 *
 */
@Service
public class Bqc_ds {
	
	@Autowired
	private ZuCaiUtil zuCaiUtil;

	/**
	 * 解析OrderInfo
	 * @param code(3,1,0,3,1,0,1,3,1,0,0,3_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(3,1,0,3,1,0,1,3,1,0,0,3_1_200_200)
	 * @param winCode(101113113131)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String codeString = code.replaceAll(",", "");
		String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeDs(codeString, winCode);
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(JSONArray)
	 * @param lotNo
	 * @param batchCode
	 * @param code(3,1,0,3,1,0,1,3,1,0,0,3_1_200_200)
	 * @param winCode(101113113131)
	 * @return
	 */
	public JSONArray getParseCodeArray(String lotNo, String batchCode, String code, String winCode) {
		JSONArray resultArray = new JSONArray();
		Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
		String codeString = code.replaceAll(",", "");
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1);
		for (int i = 0; i < codeList.size(); i++) {
			String c = codeList.get(i);
			String win = "";
			if (!Tools.isEmpty(winCode)&&!winCode.equals("null")) {
				win = winCode.substring(i, i+1);
			}
			String pCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(c, win);
			//获取赛事信息
			boolean isEven = Tools.isEvenNumber(i); //i是否是偶数
			String teamId = ZuCaiUtil.getJqcOrBqcTeamId(isEven, i);
			
			JSONObject matchInfoObject = zuCaiUtil.getMatchInfo(lotNo, batchCode, teamId);
			String homeTeam = matchInfoObject.getString("homeTeam"); //主队
			String guestTeam = matchInfoObject.getString("guestTeam"); //客队
			
			Map<String, String> map = tempMap.get(teamId);
			if (map==null) {
				map = new HashMap<String, String>();
			}
			map.put("homeTeam", homeTeam);
			map.put("guestTeam", guestTeam);
			if (isEven) { //半场
				map.put("betContentHalf", pCode);
			} else { //全场
				map.put("betContentAll", pCode);
			}
			tempMap.put(teamId, map);
		}
		resultArray = ZuCaiUtil.getCodeArrayByMap(tempMap); //根据Map得到注码数组
		return resultArray;
	}
	
}
