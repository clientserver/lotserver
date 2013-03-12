package com.ruyicai.lotserver.lottype.gpc.gd11x5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东11选5-胆拖-组选前三
 * @author Administrator
 *
 */
public class Gd11x5_DZ3 {

	/**
	 * 解析OrderInfo
	 * @param code(D|Z3|0102-0304_1_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] strings = code.split("-");
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(strings[0], 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(strings[1], 2), ",");
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(D|Z3|0102-0304_1_200_400)
	 * @param winCode(05 10 11 02 03)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 6), 2); //前2位中奖号码
		
		String[] danTuo = code.split("-");
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[0], winCodeList, 2); //胆码
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[1], winCodeList, 2); //拖码
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0205", "05 10 11 02 03");
		System.out.println(html);
	}*/
	
}
