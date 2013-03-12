package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-复式-组选前三
 * @author Administrator
 *
 */
public class Gdkl10_MZ3 {

	/**
	 * 解析OrderInfo
	 * @param code(M|Z3|05030106_2_200_600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(M|Z3|05030106_2_200_600)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 6), 2);
		
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(code, winList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("05030106", "02 01 03 04 05 06 11 18");
		System.out.println(html);
	}*/
	
}
