package com.ruyicai.lotserver.lottype.gpc.ssc;

import java.util.Arrays;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 时时彩-二星组选和值
 * @author Administrator
 *
 */
public class Ssc_S2 {

	/**
	 * 解析OrderInfo
	 * @param code(S2|0,2,11,13,16,18_1_200_2600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S2|0,2,11,13,16,18_1_200_2600)
	 * @param winCode(68588)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(3), 1); //开奖号码数组
		
		String[] codes = code.split(",");
		List<String> codeList = Arrays.asList(codes);
		
		String parseCode = OrderInfoHtmlParseCommon.getHeZhiBetCode(codeList, winCodeList);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("5,6,14,16", "68542");
		System.out.println(html);
	}*/
	
}
