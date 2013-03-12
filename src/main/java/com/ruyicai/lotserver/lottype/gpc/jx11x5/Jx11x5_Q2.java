package com.ruyicai.lotserver.lottype.gpc.jx11x5;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 江西11选5-选前二直选
 * @author Administrator
 *
 */
public class Jx11x5_Q2 {

	/**
	 * 解析OrderInfo
	 * @param code(Q2|02 03,06_1_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll(" ", "");
		String[] codes = codeString.split(",");
		
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ",");
		String hou = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ",");
		String parseCode = qian+"|"+hou;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(Q2|02 03,06_1_200_400)
	 * @param winCode(06 04 09 07 01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String codeString = code.replaceAll(" ", "");
		String[] codes = codeString.split(",");
		
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianErBetCode(codes[0], codes[1], winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("02 03,06", "03 06 09 07 01");
		System.out.println(html);
	}*/
	
}
