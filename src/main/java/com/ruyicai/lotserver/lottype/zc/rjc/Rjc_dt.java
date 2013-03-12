package com.ruyicai.lotserver.lottype.zc.rjc;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

/**
 * 足球任九场-胆拖
 * @author Administrator
 *
 */
@Service
public class Rjc_dt {
	
	@Autowired
	private ZuCaiUtil zuCaiUtil;

	/**
	 * 解析OrderInfo
	 * @param code(0,#,#,#,#,31,#,#,#,#,#,#,#,#$#,1,0,1,#,#,1,#,3,3,03,#,1,#_1_200_6000)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code.replaceAll("#", "*");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(0,#,#,#,#,31,#,#,#,#,#,#,#,#$#,1,0,1,#,#,1,#,3,3,03,#,1,#_1_200_6000)
	 * @param winCode(10111311313113)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String[] danTuo = code.split("\\$");
		String danMaString = danTuo[0];
		//胆码
		String[] split2 = danMaString.split(",");
		StringBuilder danBuilder = new StringBuilder();
		for (int i = 0; i < split2.length; i++) {
			String pCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(split2[i], winCode.substring(i, i+1));
			danBuilder.append(pCode).append(",");
		}
		if (danBuilder.toString().endsWith(",")) {
			danBuilder = danBuilder.delete(danBuilder.length()-1, danBuilder.length());
		}
		//拖码
		String tuoMaString = danTuo[1];
		String[] split3 = tuoMaString.split(",");
		StringBuilder tuoBuilder = new StringBuilder();
		for (int i = 0; i < split3.length; i++) {
			String pCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(split3[i], winCode.substring(i, i+1));
			tuoBuilder.append(pCode).append(",");
		}
		String parseCode = danBuilder.toString()+"#"+tuoBuilder.toString();
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(JSONArray)
	 * @param lotNo
	 * @param batchCode
	 * @param code(0,#,#,#,#,31,#,#,#,#,#,#,#,#$#,1,0,1,#,#,1,#,3,3,03,#,1,#_1_200_6000)
	 * @param winCode(10111311313113)
	 * @return
	 */
	public JSONArray getParseCodeArray(String lotNo, String batchCode, String code, String winCode) {
		JSONArray resultArray = new JSONArray();
		String[] danTuo = code.split("\\$");
		String danMaString = danTuo[0];
		//胆码
		String[] split2 = danMaString.split(",");
		for (int i = 0; i < split2.length; i++) {
			if (!split2[i].equals("#")) {
				String win = "";
				if (!Tools.isEmpty(winCode)&&!winCode.equals("null")) {
					win = winCode.substring(i, i+1);
				}
				String pCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(split2[i], win);
				//获取赛事信息
				JSONObject matchInfoObject = zuCaiUtil.getMatchInfo(lotNo, batchCode, (i+1)+"");
				String homeTeam = matchInfoObject.getString("homeTeam"); //主队
				String guestTeam = matchInfoObject.getString("guestTeam"); //客队
				
				JSONObject object = new JSONObject();
				object.put("teamId", (i+1)+"");
				object.put("homeTeam", homeTeam);
				object.put("guestTeam", guestTeam);
				object.put("betContent", pCode);
				object.put("isDanMa", "true");
				resultArray.add(object);
			}
		}
		//拖码
		String tuoMaString = danTuo[1];
		String[] split3 = tuoMaString.split(",");
		for (int i = 0; i < split3.length; i++) {
			if (!split3[i].equals("#")) {
				String win = "";
				if (!Tools.isEmpty(winCode)&&!winCode.equals("null")) {
					win = winCode.substring(i, i+1);
				}
				String pCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(split3[i], win);
				//获取赛事信息
				JSONObject matchInfoObject = zuCaiUtil.getMatchInfo(lotNo, batchCode, (i+1)+"");
				String homeTeam = matchInfoObject.getString("homeTeam"); //主队
				String guestTeam = matchInfoObject.getString("guestTeam"); //客队
				
				JSONObject object = new JSONObject();
				object.put("teamId", (i+1)+"");
				object.put("homeTeam", homeTeam);
				object.put("guestTeam", guestTeam);
				object.put("betContent", pCode);
				resultArray.add(object);
			}
		}
		//根据teamId进行排序
		ZuCaiUtil.sortBetCodeArray(resultArray);
		return resultArray;
	}
	
}
