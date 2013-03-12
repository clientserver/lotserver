package com.ruyicai.lotserver.lottype.tc.pls;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 排列三-直选
 * @author Administrator
 *
 */
public class Pls_1 {

	/**
	 * 解析OrderInfo
	 * @param code(1|4,16,5_2_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code.replaceAll(",", "|");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(1|4,16,5_2_200_400)
	 * @param winCode(253)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> baiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 1), 1); //百位开奖号码
		List<String> shiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(1, 2), 1); //十位开奖号码
		List<String> geWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 3), 1); //个位开奖号码
		
		String[] codes = code.split(",");
		
		String bai = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codes[0], baiWinCodeList, 1); //百位
		String shi = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codes[1], shiWinCodeList, 1); //十位
		String ge = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codes[2], geWinCodeList, 1); //个位
		
		String parseCode = bai+"|"+shi+"|"+ge;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("4,16,5", "453");
		System.out.println(html);
	}*/
	
}
