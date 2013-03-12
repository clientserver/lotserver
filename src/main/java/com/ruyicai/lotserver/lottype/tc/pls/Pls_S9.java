package com.ruyicai.lotserver.lottype.tc.pls;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 排列三-组选和值
 * @author Administrator
 *
 */
public class Pls_S9 {

	/**
	 * 解析OrderInfo
	 * @param code(S9|5_1_200_1000)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = code;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S9|5_1_200_1000)
	 * @param winCode(253)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1); //开奖号码数组
		
		List<String> codeList = new ArrayList<String>();
		codeList.add(code);
		String parseCode = OrderInfoHtmlParseCommon.getHeZhiBetCode(codeList, winCodeList);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("5", "023");
		System.out.println(html);
	}*/
	
}
