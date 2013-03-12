package com.ruyicai.lotserver.lottype.zc.sfc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

/**
 * 足球胜负彩-复式
 * @author Administrator
 *
 */
@Service
public class Sfc_fs {
	
	@Autowired
	private ZuCaiUtil zuCaiUtil;

	/**
	 * 解析OrderInfo
	 * @param code(3,1,0,1,3,3,1,0,1,1,31,1,0,3_2_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(3,1,0,1,3,3,1,0,1,1,31,1,0,3_2_200_400)
	 * @param winCode(31013310113101)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String[] codes = code.split(",");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(codes[i], winCode.substring(i, i+1));
			builder.append(parseCode).append(",");
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/**
	 * 解析OrderInfo(JSONArray)
	 * @param code(3,1,0,1,3,3,1,0,1,1,31,1,0,3_2_200_400)
	 * @param winCode(31013310113101)
	 * @return
	 */
	public JSONArray getParseCodeArray(String lotNo, String batchCode, String code, String winCode) {
		JSONArray resultArray = new JSONArray();
		String[] codes = code.split(",");
		for (int i = 0; i < codes.length; i++) {
			String win = "";
			if (!Tools.isEmpty(winCode)&&!winCode.equals("null")) {
				win = winCode.substring(i, i+1);
			}
			String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(codes[i], win);
			//获取赛事信息
			JSONObject matchInfoObject = zuCaiUtil.getMatchInfo(lotNo, batchCode, (i+1)+"");
			String homeTeam = matchInfoObject.getString("homeTeam"); //主队
			String guestTeam = matchInfoObject.getString("guestTeam"); //客队
			
			JSONObject object = new JSONObject();
			object.put("teamId", (i+1)+"");
			object.put("homeTeam", homeTeam);
			object.put("guestTeam", guestTeam);
			object.put("betContent", parseCode);
			resultArray.add(object);
		}
		return resultArray;
	}
	
}
