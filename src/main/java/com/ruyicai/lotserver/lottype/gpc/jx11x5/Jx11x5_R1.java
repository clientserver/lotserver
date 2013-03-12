package com.ruyicai.lotserver.lottype.gpc.jx11x5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 江西11选5-任选一
 * @author Administrator
 *
 */
public class Jx11x5_R1 {

	/**
	 * 解析OrderInfo
	 * @param code(R1|01 02 03 04 05 08_1_200_1200)
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
	 * @param code(R1|01 02 03 04 05 08_1_200_1200)
	 * @param winCode(06 04 09 07 01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 2), 2);
		
		String codeString = code.replaceAll(" ", "");
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("06 02 03 04 05 08", "06 04 09 07 01");
		System.out.println(html);
	}*/
	
}
