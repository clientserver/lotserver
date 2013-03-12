package com.ruyicai.lotserver.lottype.gpc.ssc;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.MemoUtil;

/**
 * 时时彩-大小单双
 * @author Administrator
 *
 */
public class Ssc_DD {

	/**
	 * 解析OrderInfo
	 * @param code(DD|12;_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String shi = code.substring(0, 1); //大小
		String ge = code.substring(1, 2); //单双
		
		String shiMemo = MemoUtil.getSscDXDSMemo(shi);
		String geMemo = MemoUtil.getSscDXDSMemo(ge);
		
		String parseCode = shiMemo+geMemo;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(DD|12;_2_200_200)
	 * @param winCode(68588)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		StringBuilder builder = new StringBuilder();
		
		String shi = code.substring(0, 1); //大小
		String ge = code.substring(1, 2); //单双
		
		String shiMemo = MemoUtil.getSscDXDSMemo(shi);
		String geMemo = MemoUtil.getSscDXDSMemo(ge);
		
		String shiWinCode = winCode.substring(3, 4); //十位开奖号码
		String geWinCode = winCode.substring(4, 5); //个位开奖号码
		
		List<String> shiWinCodeList = OrderInfoHtmlParseCommon.getSscDxDsWinCodeList(shiWinCode);
		List<String> geWinCodeList = OrderInfoHtmlParseCommon.getSscDxDsWinCodeList(geWinCode);
		
		if (shiWinCodeList.contains(shi)) {
			builder.append("<font color=\"red\">"+shiMemo+"</font>");
		} else {
			builder.append(shiMemo);
		}
		
		if (geWinCodeList.contains(ge)) {
			builder.append("<font color=\"red\">"+geMemo+"</font>");
		} else {
			builder.append(geMemo);
		}
		return builder.toString();
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("5,6,14,16", "68542");
		System.out.println(html);
	}*/
	
}
