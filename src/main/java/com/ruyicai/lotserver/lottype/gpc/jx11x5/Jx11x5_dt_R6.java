package com.ruyicai.lotserver.lottype.gpc.jx11x5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 江西11选5-胆拖-任选六
 * @author Administrator
 *
 */
public class Jx11x5_dt_R6 {

	/**
	 * 解析OrderInfo
	 * @param code(R6|07 08 09 10 11$01 02 03 04 05 06_2_200_1200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll(" ", "");
		String[] danTuo = codeString.split("\\$");
		
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[0], 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danTuo[1], 2), ",");
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(R6|07 08 09 10 11$01 02 03 04 05 06_2_200_1200)
	 * @param winCode(06 04 09 07 01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String[] danTuo = code.replaceAll(" ", "").split("\\$");
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[0], winCodeList, 2); //胆码
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[1], winCodeList, 2); //拖码
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("02$05 07 09", "06 04 09 07 01");
		System.out.println(html);
	}*/
	
}
