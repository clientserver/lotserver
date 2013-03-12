package com.ruyicai.lotserver.lottype.tc.plw;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;

/**
 * 排列五-复式
 * @author Administrator
 *
 */
public class Plw_fs {

	/**
	 * 解析OrderInfo
	 * @param code(09,1,2,3,4689_1_200_1600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(09,1,2,3,4689_1_200_1600)
	 * @param winCode(40647)
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
		String html = parseOrderInfoHtml("09,1,2,3,4689", "40648");
		System.out.println(html);
	}*/
	
}
