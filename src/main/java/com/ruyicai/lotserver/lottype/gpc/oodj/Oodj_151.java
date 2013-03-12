package com.ruyicai.lotserver.lottype.gpc.oodj;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-选前三组选单式
 * @author Administrator
 *
 */
public class Oodj_151 {

	/**
	 * 解析OrderInfo
	 * @param code(151@030506^_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String string = code.substring(0, code.indexOf("^"));
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(string, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(151@030506^_2_200_200)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 6), 2);
		
		String codeString = code.substring(0, code.indexOf("^"));
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0111*0406^", "01 02 08 07 01");
		System.out.println(html);
	}*/
	
}
