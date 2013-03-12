package com.ruyicai.lotserver.lottype.tc.pls;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 排列三-组3包号
 * @author Administrator
 *
 */
public class Pls_F3 {

	/**
	 * 解析OrderInfo
	 * @param code(F3|3456_2_200_2400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 1), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(F3|3456_2_200_2400)
	 * @param winCode(253)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1); //开奖号码数组
		
		String codeString = code.replaceAll(",", "");
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 1);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("4,16,5", "453");
		System.out.println(html);
	}*/
	
}
