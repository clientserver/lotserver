package com.ruyicai.lotserver.lottype.fc.qlc;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 七乐彩-胆拖
 * @author Administrator
 *
 */
public class Qlc_20 {

	/**
	 * 解析OrderInfo
	 * @param code(2001050611132830*1529^_2_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String danMaString = code.substring(4, code.indexOf("*"));
		String tuoMaString = code.substring(code.indexOf("*")+1);
		
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danMaString, 2), ","); // 胆码
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(tuoMaString, 2), ","); // 拖码
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(2001050611132830*1529^_2_200_400)
	 * @param winCode(01020304050607|01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.replaceAll("\\|", ""), 2); //开奖号码数组
		
		String danMaString = code.substring(4, code.indexOf("*"));
		String tuoMaString = code.substring(code.indexOf("*")+1);
		
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danMaString, winCodeList, 2);
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(tuoMaString, winCodeList, 2);
		String parseCode = danMa+"#"+tuoMa;
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("000101121422272829^", "01020304050628|01");
		System.out.println(html);
	}*/
	
}
