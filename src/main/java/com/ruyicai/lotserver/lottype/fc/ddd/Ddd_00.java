package com.ruyicai.lotserver.lottype.fc.ddd;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 福彩3D-单选单式
 * @author Administrator
 *
 */
public class Ddd_00 {

	/**
	 * 解析OrderInfo
	 * @param code(0001080203^_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = LotteryAlgorithmUtil.removeZero3D(code.substring(4)); //去掉前面的"0"
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(0001080203^_1_200_200)
	 * @param winCode(新:030105)(旧:010003|00)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = LotteryAlgorithmUtil.removeZero3D(winCode.substring(0, 6));
		List<String> baiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 1), 1); //百位开奖号码
		List<String> shiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(1, 2), 1); //十位开奖号码
		List<String> geWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 3), 1); //个位开奖号码
		
		String baiString = LotteryAlgorithmUtil.removeZero3D(code.substring(4, 6)); //百位
		String shiString = LotteryAlgorithmUtil.removeZero3D(code.substring(6, 8)); //十位
		String geString = LotteryAlgorithmUtil.removeZero3D(code.substring(8, 10)); //个位
		
		String bai = OrderInfoHtmlParseCommon.getNotPositionalBetCode(baiString, baiWinCodeList, 1);
		String shi = OrderInfoHtmlParseCommon.getNotPositionalBetCode(shiString, shiWinCodeList, 1);
		String ge = OrderInfoHtmlParseCommon.getNotPositionalBetCode(geString, geWinCodeList, 1);
		
		String parseCode = bai+"|"+shi+"|"+ge;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0001080203", "090205");
		System.out.println(html);
	}*/
	
}
