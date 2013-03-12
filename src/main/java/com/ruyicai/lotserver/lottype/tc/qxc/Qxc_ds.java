package com.ruyicai.lotserver.lottype.tc.qxc;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 七星彩-单式
 * @author Administrator
 *
 */
public class Qxc_ds {

	/**
	 * 解析OrderInfo
	 * @param code(3,6,6,5,7,4,7_1_200_200或3665747_1_200_200)
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
	 * @param code(3,6,6,5,7,4,7_1_200_200或3665747_1_200_200)
	 * @param winCode(2124269)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String codeString = code.replaceAll(",", "");
		String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeDs(codeString, winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("3,6,6,5,7,6,7", "2124269");
		System.out.println(html);
	}*/
	
}
