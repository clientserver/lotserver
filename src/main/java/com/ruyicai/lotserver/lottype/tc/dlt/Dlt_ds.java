package com.ruyicai.lotserver.lottype.tc.dlt;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 大乐透-单式
 * @author Administrator
 *
 */
public class Dlt_ds {

	/**
	 * 解析OrderInfo
	 * @param code(02 10 18 24 28-02 11;_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] qianHou = code.split("-");
		String parseCode = qianHou[0].replaceAll(" ", ",")+"|"+qianHou[1].replaceAll(" ", ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(02 10 18 24 28-02 11;_1_200_200)
	 * @param winCode(新:10 13 22 27 34+04 09)(旧:03 04 06 11 13 01 02)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeQianList = new ArrayList<String>();
		List<String> winCodeHouList = new ArrayList<String>();
		if (winCode.indexOf("+")>-1) { //新格式
			String[] winCodes = winCode.split("\\+");
			winCodeQianList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[0], 2);
			winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[1], 2);
		} else {
			if (winCode.length()>10) {
				winCodeQianList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 10), 2);
				winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(10), 2);
			}
		}
		
		String[] qianHou = code.replaceAll(" ", "").split("-");
		String qian = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[0], winCodeQianList, 2); //前区
		String hou = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[1], winCodeHouList, 2); //后区
		
		String parseCode = qian+"|"+hou;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01 02 03$09 19 28-06 07 09", "01 13 22 27 34 06 09");
		System.out.println(html);
	}*/
	
}
