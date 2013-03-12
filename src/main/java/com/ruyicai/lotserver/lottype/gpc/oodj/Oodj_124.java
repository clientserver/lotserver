package com.ruyicai.lotserver.lottype.gpc.oodj;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 11运夺金-任选五胆拖
 * @author Administrator
 *
 */
public class Oodj_124 {

	/**
	 * 解析OrderInfo
	 * @param code(124@08091011*01020304050607^_2_200_1400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.substring(0, code.indexOf("^"));
		String[] danTuo = codeString.split("\\*");
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[0], 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[1], 2), ",");
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(124@08091011*01020304050607^_2_200_1400)
	 * @param winCode(01 04 06 08 11)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String codeString = code.substring(0, code.indexOf("^"));
		String[] danTuo = codeString.split("\\*");
		
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[0], winCodeList, 2); //胆码
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[1], winCodeList, 2); //拖码
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01*0203^", "02 04 09 07 01");
		System.out.println(html);
	}*/
	
}
