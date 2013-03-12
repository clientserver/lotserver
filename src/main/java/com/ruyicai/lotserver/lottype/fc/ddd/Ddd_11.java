package com.ruyicai.lotserver.lottype.fc.ddd;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 福彩3D-组3和值
 * @author Administrator
 *
 */
public class Ddd_11 {

	/**
	 * 解析OrderInfo
	 * @param code(110126^_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = LotteryAlgorithmUtil.removeZeroMutiple(code.substring(4));
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(110126^_1_200_200)
	 * @param winCode(新:030105)(旧:010003|00)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.substring(0, 6);
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2); //开奖号码数组
		
		String codeString = code.substring(4);
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(codeString, 2);
		
		String parseCode = OrderInfoHtmlParseCommon.getHeZhiBetCode(codeList, winCodeList);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("110126", "030105");
		System.out.println(html);
	}*/
	
}
