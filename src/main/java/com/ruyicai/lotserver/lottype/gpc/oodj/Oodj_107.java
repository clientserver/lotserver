package com.ruyicai.lotserver.lottype.gpc.oodj;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-任选七复式
 * @author Administrator
 *
 */
public class Oodj_107 {

	/**
	 * 解析OrderInfo
	 * @param code(107@*020304050607081011^_1_200_7200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String string = code.substring(code.indexOf("*")+1, code.indexOf("^"));
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(string, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(107@*020304050607081011^_1_200_7200)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String codeString = code.substring(code.indexOf("*")+1, code.indexOf("^"));
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("*010307^", "02 04 09 07 01");
		System.out.println(html);
	}*/
	
}
