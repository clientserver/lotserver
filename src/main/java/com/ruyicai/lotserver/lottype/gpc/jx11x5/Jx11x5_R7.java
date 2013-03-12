package com.ruyicai.lotserver.lottype.gpc.jx11x5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 江西11选5-任选七
 * @author Administrator
 *
 */
public class Jx11x5_R7 {

	/**
	 * 解析OrderInfo
	 * @param code(R7|06 04 10 07 03 02 01_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll(" ", "");
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(R7|06 04 10 07 03 02 01_1_200_200)
	 * @param winCode(06 04 09 07 01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String codeString = code.replaceAll(" ", "");
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 2);
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01 02 03 04 05 06 07 08 09 10 11", "06 04 09 07 01");
		System.out.println(html);
	}*/
	
}
