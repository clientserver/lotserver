package com.ruyicai.lotserver.lottype.gpc.ssc;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 时时彩-二星组选
 * @author Administrator
 *
 */
public class Ssc_Z2 {

	/**
	 * 解析OrderInfo
	 * @param code(Z2|168,9_1_200_600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(Z2|168,9_1_200_600)
	 * @param winCode(68588)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(3), 1);
		
		String[] codes = code.split(",");
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codes[i], winCodeList, 1);
			builder.append(parseCode).append(",");
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("168,9", "62578");
		System.out.println(html);
	}*/
	
}
