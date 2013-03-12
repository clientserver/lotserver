package com.ruyicai.lotserver.lottype.tc.plw;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 排列五-单式
 * @author Administrator
 *
 */
public class Plw_ds {

	/**
	 * 解析OrderInfo
	 * @param code(3,7,8,0,7_1_200_200或37807_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll(",", "");
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(3,7,8,0,7_1_200_200或37807_1_200_200)
	 * @param winCode(40647)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String codeString = code.replaceAll(",", "");
		String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeDs(codeString, winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("37807", "40647");
		System.out.println(html);
	}*/
	
}
