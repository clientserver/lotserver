package com.ruyicai.lotserver.lottype.zc.jqc;

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
 * 足球进球彩-单式
 * @author Administrator
 *
 */
@Service
public class Jqc_ds {
	
	@Autowired
	private ZuCaiUtil zuCaiUtil;

	/**
	 * 解析OrderInfo
	 * @param code(2,1,2,1,1,2,2,1_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code.replaceAll("3", "3+");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(2,1,2,1,1,2,2,1_1_200_200)
	 * @param winCode(21211201)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String codeString = code.replaceAll(",", "");
		String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeDs(codeString, winCode);
		return parseCode.replaceAll("3", "3+");
	}
	
	/**
	 * 解析OrderInfo(JSONArray)
	 * @param lotNo
	 * @param batchCode
	 * @param code(2,1,2,1,1,2,2,1_1_200_200)
	 * @param winCode(21211201)
	 * @return
	 */
	public JSONArray getParseCodeArray(String lotNo, String batchCode, String code, String winCode) {
		JSONArray resultArray = new JSONArray();
		String codeString = code.replaceAll(",", "");
		
		Map<String, Map<String, String>> tempMap = new HashMap<String, Map<String, String>>();
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
			if (isEven) { //主队
				map.put("homeTeam", homeTeam);
				map.put("betContentHome", pCode);
			} else { //客队
				map.put("guestTeam", guestTeam);
				map.put("betContentGuest", pCode);
			}
			tempMap.put(teamId, map);
		}
		resultArray = ZuCaiUtil.getCodeArrayByMap(tempMap); //根据Map得到注码数组
		return resultArray;
	}
	
	/*public static void main(String[] args) {
		System.out.println((2&1) != 0);
	}*/
	
}
