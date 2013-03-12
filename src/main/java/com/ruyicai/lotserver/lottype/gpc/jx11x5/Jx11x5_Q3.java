package com.ruyicai.lotserver.lottype.gpc.jx11x5;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 江西11选5-选前三直选
 * @author Administrator
 *
 */
public class Jx11x5_Q3 {

	/**
	 * 解析OrderInfo
	 * @param code(Q3|01,02,03 05 07 08_1_200_800)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String codeString = code.replaceAll(" ", "");
		String[] split4 = codeString.split(",");
		
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(split4[0], 2), ",");
		String zhong = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(split4[1], 2), ",");
		String hou = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(split4[2], 2), ",");
		String parseCode = qian+"|"+zhong+"|"+hou;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(Q3|01,02,03 05 07 08_1_200_800)
	 * @param winCode(06 04 09 07 01)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String codeString = code.replaceAll(" ", "");
		String[] codes = codeString.split(",");
		
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianSanBetCode(codes[0], codes[1], codes[2], winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01,02,03 05 07 08", "01 06 09 07 01");
		System.out.println(html);
	}*/
	
}
