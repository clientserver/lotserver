package com.ruyicai.lotserver.lottype.gpc.oodj;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-选前二直选单式
 * @author Administrator
 *
 */
public class Oodj_141 {

	/**
	 * 解析OrderInfo
	 * @param code(141@0102^_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String string = code.substring(0, code.indexOf("^"));
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(string, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(141@0102^_1_200_200)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String codeString = code.substring(0, code.indexOf("^"));
		String qianString = codeString.substring(0, 2); //前位
		String houString = codeString.substring(2, 4); //后位
		
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianErBetCode(qianString, houString, winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0102^", "01 02 08 07 01");
		System.out.println(html);
	}*/
	
}
