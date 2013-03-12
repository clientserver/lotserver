package com.ruyicai.lotserver.lottype.gpc.gd11x5;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东11选5-单式-直选前三
 * @author Administrator
 *
 */
public class Gd11x5_SQ3 {

	/**
	 * 解析OrderInfo
	 * @param code(S|Q3|030910_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S|Q3|030910_2_200_200)
	 * @param winCode(05 10 11 02 03)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String qianString = code.substring(0, 2); //前位
		String zhongString = code.substring(2, 4); //中位
		String houString = code.substring(4, 6); //后位
		
		String parseCode = OrderInfoHtmlParseCommon.getZhiXuanQianSanBetCode(qianString, zhongString, houString, winCode);
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0205", "05 10 11 02 03");
		System.out.println(html);
	}*/
	
}
