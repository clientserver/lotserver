package com.ruyicai.lotserver.lottype.fc.ddd;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 福彩3D-单选单胆拖
 * @author Administrator
 *
 */
public class Ddd_54 {

	/**
	 * 解析OrderInfo
	 * @param code(54010102*030405^_1_200_3600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String danMaString = LotteryAlgorithmUtil.removeZero3D(code.substring(4, code.indexOf("*"))); //去掉前面的"0"
		String tuoMaString = LotteryAlgorithmUtil.removeZero3D(code.substring(code.indexOf("*")+1));
		
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(danMaString, 1), ","); //胆码
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(tuoMaString, 1), ","); //拖码
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(54010102*030405^_1_200_3600)
	 * @param winCode(新:030105)(旧:010003|00)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = LotteryAlgorithmUtil.removeZero3D(winCode.substring(0, 6));
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1); //开奖号码数组
		
		String danMaString = LotteryAlgorithmUtil.removeZero3D(code.substring(4, code.indexOf("*")));
		String tuoMaString = LotteryAlgorithmUtil.removeZero3D(code.substring(code.indexOf("*")+1));
		
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danMaString, winCodeList, 1);
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(tuoMaString, winCodeList, 1);
		
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("54010102*030405", "030405");
		System.out.println(html);
	}*/
	
}
