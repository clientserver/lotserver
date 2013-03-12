package com.ruyicai.lotserver.lottype.tc.qxc;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;

/**
 * 七星彩-复式
 * @author Administrator
 *
 */
public class Qxc_fs {

	/**
	 * 解析OrderInfo
	 * @param code(7,7,4,1,8,6,12_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(7,7,4,1,8,6,12_1_200_200)
	 * @param winCode(2124269)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String[] codes = code.split(",");
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			String parseCode = OrderInfoHtmlParseCommon.getPositionalBetCodeFs(codes[i], winCode.substring(i, i+1));
			builder.append(parseCode).append(",");
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("7,7,4,1,8,6,12", "2124262");
		System.out.println(html);
	}*/
	
}
