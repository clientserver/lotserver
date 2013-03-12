package com.ruyicai.lotserver.lottype.tc.ttx5;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 22选5-胆拖
 * @author Administrator
 *
 */
public class Ttx5_2 {

	/**
	 * 解析OrderInfo
	 * @param code(2@13*060711192022^_1_200_3000)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String danMaString = code.substring(0, code.indexOf("*"));
		String tuoMaString = code.substring(code.indexOf("*")+1, code.length()-1);
		
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danMaString, 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(tuoMaString, 2), ",");
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(2@13*060711192022^_1_200_3000)
	 * @param winCode(01 02 03 04 05)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2); //开奖号码数组
		
		String danMaString = code.substring(0, code.indexOf("*"));
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danMaString, winCodeList, 2);
		
		String tuoMaString = code.substring(code.indexOf("*")+1, code.length()-1);
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(tuoMaString, winCodeList, 2);
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("13*060711192022^", "01 02 03 04 06");
		System.out.println(html);
	}*/
	
}
