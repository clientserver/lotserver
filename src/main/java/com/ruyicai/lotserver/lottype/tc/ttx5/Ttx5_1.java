package com.ruyicai.lotserver.lottype.tc.ttx5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 22选5-复式
 * @author Administrator
 *
 */
public class Ttx5_1 {

	/**
	 * 解析OrderInfo
	 * @param code(1@060709141921^_1_200_1200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.substring(0, code.length()-1);
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(1@060709141921^_1_200_1200)
	 * @param winCode(01 02 03 04 05)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2); //开奖号码数组
		
		String codeString = code.substring(0, code.length()-1);
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("060709141921^", "01 02 03 04 06");
		System.out.println(html);
	}*/
	
}
