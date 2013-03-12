package com.ruyicai.lotserver.lottype.tc.dlt;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 大乐透-生肖乐
 * @author Administrator
 *
 */
public class Dlt_sxl {

	/**
	 * 解析OrderInfo
	 * @param code(01 02 03 04 05 09_2_200_3000)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code.replaceAll(" ", ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(01 02 03 04 05 09_2_200_3000)
	 * @param winCode(新:10 13 22 27 34+04 09)(旧:03 04 06 11 13 01 02)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeHouList = new ArrayList<String>();
		if (winCode.indexOf("+")>-1) { //新格式
			String[] winCodes = winCode.split("\\+");
			winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[1], 2);
		} else {
			if (winCode.length()>10) {
				winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(10), 2);
			}
		}
		
		String codeString = code.replaceAll(" ", "");
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeHouList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01 02 03$09 19 28-06 07 09", "01 13 22 27 34 06 09");
		System.out.println(html);
	}*/
	
}
