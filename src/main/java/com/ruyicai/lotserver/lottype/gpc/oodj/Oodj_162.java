package com.ruyicai.lotserver.lottype.gpc.oodj;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-选前三直选定位复式
 * @author Administrator
 *
 */
public class Oodj_162 {

	/**
	 * 解析OrderInfo
	 * @param code(162@0210*11*05060708^_1_200_1600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.substring(0, code.indexOf("^"));
		String[] codes = codeString.split("\\*");
		String wan = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ",");
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ",");
		String bai = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[2], 2), ",");
		String parseCode = wan+"|"+qian+"|"+bai;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(162@0210*11*05060708^_1_200_1600)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String codeString = code.substring(0, code.indexOf("^"));
		String[] codes = codeString.split("\\*");
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianSanBetCode(codes[0], codes[1], codes[2], winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0210*11*05060708^", "01 02 05 07 01");
		System.out.println(html);
	}*/
	
}
