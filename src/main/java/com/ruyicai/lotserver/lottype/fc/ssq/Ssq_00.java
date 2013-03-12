package com.ruyicai.lotserver.lottype.fc.ssq;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 双色球-单式
 * @author Administrator
 *
 */
public class Ssq_00 {

	/**
	 * 解析OrderInfo
	 * @param code(0001040613141533~12^_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String redString = code.substring(4, code.indexOf('~'));
		String red = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(redString, 2), ",");
		String blue = code.substring(code.indexOf('~')+1);
		String parseCode = red+"|"+blue;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(0001040613141533~12^_2_200_200)
	 * @param winCode(010203040506|09)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String[] winCodes = winCode.split("\\|");
		List<String> winCodeRedList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[0], 2);
		List<String> winCodeBlueList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[1], 2);
		
		String redString = code.substring(4, code.indexOf('~'));
		String red = OrderInfoHtmlParseCommon.getNotPositionalBetCode(redString, winCodeRedList, 2);
		
		String blueString = code.substring(code.indexOf('~')+1);
		String blue = OrderInfoHtmlParseCommon.getNotPositionalBetCode(blueString, winCodeBlueList, 2);
		
		String parseCode = red+"|"+blue;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0001040613141533~09", "010203040506|09");
		System.out.println(html);
	}*/
	
}
