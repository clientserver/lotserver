package com.ruyicai.lotserver.lottype.gpc.oodj;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-选前二直选定位复式
 * @author Administrator
 *
 */
public class Oodj_142 {

	/**
	 * 解析OrderInfo
	 * @param code(142@0211*0406^_1_200_800)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.substring(0, code.indexOf("^"));
		String[] danTuo = codeString.split("\\*");
		String wan = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[0], 2), ",");
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[1], 2), ",");
		String parseCode = wan+"|"+qian;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(142@0211*0406^_1_200_800)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String codeString = code.substring(0, code.indexOf("^"));
		String[] codes = codeString.split("\\*");
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianErBetCode(codes[0], codes[1], winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0111*0406^", "01 02 08 07 01");
		System.out.println(html);
	}*/
	
}
