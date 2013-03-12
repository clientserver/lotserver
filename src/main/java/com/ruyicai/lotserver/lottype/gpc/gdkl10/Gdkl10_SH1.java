package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-单式-选一红投
 * @author Administrator
 *
 */
public class Gdkl10_SH1 {

	/**
	 * 解析OrderInfo
	 * @param code(S|H1|19^20_1_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll("\\^", "");
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S|H1|19^20_1_200_400)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		List<String> winList = new ArrayList<String>();
		String win = winCode.substring(0, 2); //开奖号码的第一位
		if (win.equals("19")||win.equals("20")) { //红色号码
			winList = LotteryAlgorithmUtil.getStringArrayFromString("1920", 2);
		}
		
		String codeString = code.replaceAll("\\^", "");
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winList, 2);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("19^20", "19 01 03 04 05 06 11 12");
		System.out.println(html);
	}*/
	
}
