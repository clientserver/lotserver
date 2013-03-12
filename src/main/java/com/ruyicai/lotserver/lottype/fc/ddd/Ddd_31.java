package com.ruyicai.lotserver.lottype.fc.ddd;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 福彩3D-组3复式
 * @author Administrator
 *
 */
public class Ddd_31 {

	/**
	 * 解析OrderInfo
	 * @param code(3101020102^_1_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = LotteryAlgorithmUtil.removeZero3D(code.substring(6)); //去掉前面的"0"
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(3101020102^_1_200_400)
	 * @param winCode(新:030105)(旧:010003|00)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = LotteryAlgorithmUtil.removeZero3D(winCode.substring(0, 6));
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1); //开奖号码数组
		
		String codeString = LotteryAlgorithmUtil.removeZero3D(code.substring(6)); //去掉前面的"0"
		String parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 1);
		return parseCode;
		
		/*
		String parseCode = "";
		String baiWinCode = winCode.substring(0, 1); //百位开奖号码
		String shiWinCode = winCode.substring(1, 2); //百位开奖号码
		String geWinCode = winCode.substring(2, 3); //个位开奖号码
		if ((baiWinCode.equals(shiWinCode)&&!shiWinCode.equals(geWinCode))||(shiWinCode.equals(geWinCode)&&!geWinCode.equals(baiWinCode))
				||(baiWinCode.equals(geWinCode)&&!geWinCode.equals(shiWinCode))) { //是组3的开奖号码
			List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1); //开奖号码数组
			List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1);
			if (codeList.contains(baiWinCode)&&codeList.contains(shiWinCode)&&codeList.contains(geWinCode)) { //已中奖
				parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, winCodeList, 1);
				return parseCode;
			}
		}
		parseCode = OrderInfoHtmlParseCommon.getNotPositionalBetCode(codeString, new ArrayList<String>(), 1);
		return parseCode;
		*/
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("310103010305", "010203");
		System.out.println(html);
	}*/
	
}
