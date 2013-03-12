package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-单式-直选前三
 * @author Administrator
 *
 */
public class Gdkl10_SQ3 {

	/**
	 * 解析OrderInfo
	 * @param code(S|Q3|091011_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S|Q3|091011_2_200_200)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		
		String parseCode = "";
		String qianCode = code.substring(0, 2); //前位
		List<String> qianWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 2), 2);
		String qian = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianCode, qianWinCodeList, 2);
		
		String zhongCode = code.substring(2, 4); //中位
		List<String> zhongWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 4), 2);
		String zhong = OrderInfoHtmlParseCommon.getNotPositionalBetCode(zhongCode, zhongWinCodeList, 2);
		
		String houCode = code.substring(4, 6); //后位
		List<String> houWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(4, 6), 2);
		String hou = OrderInfoHtmlParseCommon.getNotPositionalBetCode(houCode, houWinCodeList, 2);
		
		parseCode = qian+"|"+zhong+"|"+hou;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("091011", "09 10 11 04 06 09 01 02");
		System.out.println(html);
	}*/
	
}
