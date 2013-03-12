package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;


/**
 * 广东快乐十分-复式-选二连组
 * @author Administrator
 *
 */
public class Gdkl10_MZ2 {

	/**
	 * 解析OrderInfo
	 * @param code(M|Z2|160709_2_200_600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(M|Z2|160709_2_200_600)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
		Map<String, Integer> codeMap = new HashMap<String, Integer>();
		//查询投注号码在开奖号码中的下标
		for (String c : codeList) {
			if (winCodeList.contains(c)) {
				int index = winCodeList.indexOf(c);
				codeMap.put(c, index);
			}
		}
		//查询中奖的投注号码
		List<String> winList = new ArrayList<String>();
		Object keys[] = codeMap.keySet().toArray();
		for(int i = 0; i < codeMap.size(); i++) {
			Integer iIndex = codeMap.get(keys[i]);
			for (int j = i+1; j < codeMap.size(); j++) {
				Integer jIndex = codeMap.get(keys[j]);
				if (Math.abs(iIndex-jIndex)==1) {
					winList.add((String)keys[i]);
					winList.add((String)keys[j]);
				}
			}
		}
		//解析投注号码
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(code, winList, 2);
		return parseCode;
		
		/*StringBuilder builder = new StringBuilder();
		for (String c : codeList) {
			if (winCodeList.contains(c)) { //中奖
				builder.append("<font color=\"red\">"+c+"</font>").append(",");
			} else {
				builder.append(c).append(",");
			}
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}*/
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("160709", "02 01 03 04 05 07 09 16");
		System.out.println(html);
	}*/
	
}
