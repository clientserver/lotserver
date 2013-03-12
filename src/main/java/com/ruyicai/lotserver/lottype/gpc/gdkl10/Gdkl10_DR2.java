package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-胆拖-任选二
 * @author Administrator
 *
 */
public class Gdkl10_DR2 {

	/**
	 * 解析OrderInfo
	 * @param code(D|R2|01-0911_2_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] codes = code.split("-");
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ",");
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(D|R2|01-0911_2_200_400)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String[] danTuo = code.split("-");
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[0], winCodeList, 2); //胆码
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[1], winCodeList, 2); //拖码
		String parseCode = danMa+"#"+tuoMa;
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01-0911", "02 01 03 04 05 06 11 18");
		System.out.println(html);
	}*/
	
}
