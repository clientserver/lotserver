package com.ruyicai.lotserver.lottype.gpc.ssc;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 时时彩-二星组选复式
 * @author Administrator
 *
 */
public class Ssc_F2 {

	/**
	 * 解析OrderInfo
	 * @param code(F2|0268_1_200_1200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 1), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(F2|0268_1_200_1200)
	 * @param winCode(68588)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(3), 1);
		
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(code, winCodeList, 1);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0268", "62587");
		System.out.println(html);
	}*/
	
}
