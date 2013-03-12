package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-复式-选一数投
 * @author Administrator
 *
 */
public class Gdkl10_MS1 {

	/**
	 * 解析OrderInfo
	 * @param code(M|S1|0210_1_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(M|S1|0210_1_200_400)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 2), 2);
		
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(code, winList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0210", "02 01 03 04 05 06 11 12");
		System.out.println(html);
	}*/
	
}
