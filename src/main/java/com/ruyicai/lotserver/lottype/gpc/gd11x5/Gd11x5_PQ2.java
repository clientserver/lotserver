package com.ruyicai.lotserver.lottype.gpc.gd11x5;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东11选5-定位-直选前二
 * @author Administrator
 *
 */
public class Gd11x5_PQ2 {

	/**
	 * 解析OrderInfo
	 * @param code(P|Q2|0103-08_2_200_400)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] codes = code.split("-");
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ",");
		String hou = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ",");
		String parseCode = qian+"|"+hou;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(P|Q2|0103-08_2_200_400)
	 * @param winCode(05 10 11 02 03)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String[] codes = code.split("-");
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianErBetCode(codes[0], codes[1], winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0205", "05 10 11 02 03");
		System.out.println(html);
	}*/
	
}
