package com.ruyicai.lotserver.lottype.fc.ddd;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 福彩3D-直选
 * @author Administrator
 *
 */
public class Ddd_20 {

	/**
	 * 解析OrderInfo
	 * @param code(20010109^0109^0105^_1_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] str = code.split("\\^");
		String bai = LotteryAlgorithmUtil.removeZero3D(str[0].substring(6));
		String shi = LotteryAlgorithmUtil.removeZero3D(str[1].substring(2));
		String ge = LotteryAlgorithmUtil.removeZero3D(str[2].substring(2));
		String parseCode = bai+"|"+shi+"|"+ge;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(20010109^0109^0105^_1_200_200)
	 * @param winCode(新:030105)(旧:010003|00)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = LotteryAlgorithmUtil.removeZero3D(winCode.substring(0, 6));
		List<String> baiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 1), 1); //百位开奖号码
		List<String> shiWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(1, 2), 1); //十位开奖号码
		List<String> geWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 3), 1); //个位开奖号码
		
		String[] codes = code.split("\\^");
		String baiString = LotteryAlgorithmUtil.removeZero3D(codes[0].substring(6)); //百位
		String shiString = LotteryAlgorithmUtil.removeZero3D(codes[1].substring(2)); //十位
		String geString = LotteryAlgorithmUtil.removeZero3D(codes[2].substring(2)); //个位
		
		String bai = OrderInfoHtmlParseCommon.getNotPositionalBetCode(baiString, baiWinCodeList, 1);
		String shi = OrderInfoHtmlParseCommon.getNotPositionalBetCode(shiString, shiWinCodeList, 1);
		String ge = OrderInfoHtmlParseCommon.getNotPositionalBetCode(geString, geWinCodeList, 1);
		
		String parseCode = bai+"|"+shi+"|"+ge;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("20010109^0109^0105^", "090105");
		System.out.println(html);
	}*/
	
}
