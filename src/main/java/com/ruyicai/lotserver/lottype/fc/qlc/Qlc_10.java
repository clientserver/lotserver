package com.ruyicai.lotserver.lottype.fc.qlc;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 七乐彩-复式
 * @author Administrator
 *
 */
public class Qlc_10 {

	/**
	 * 解析OrderInfo
	 * @param code(1001*0104081722232526^_3_200_1600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.substring(5);
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(1001*0104081722232526^_3_200_1600)
	 * @param winCode(01020304050607|01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.replaceAll("\\|", ""), 2); //开奖号码数组
		
		String codeString = code.substring(5);
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("000101121422272829^", "01020304050628|01");
		System.out.println(html);
	}*/
	
}
