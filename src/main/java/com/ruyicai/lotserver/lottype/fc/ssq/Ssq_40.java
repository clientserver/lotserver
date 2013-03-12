package com.ruyicai.lotserver.lottype.fc.ssq;

import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 双色球-红球胆拖且蓝球单式
 * @author Administrator
 *
 */
public class Ssq_40 {

	/**
	 * 解析OrderInfo
	 * @param code(400111131523*142533~03^_2_200_600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String redDanMaString = code.substring(4, code.indexOf("*"));
		String redTuoMaString = code.substring(code.indexOf("*")+1, code.indexOf("~"));
		String blueString = code.substring(code.indexOf("~")+1);
		
		String redDanMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(redDanMaString, 2), ",");
		String redTuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(redTuoMaString, 2), ",");
		String blue = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(blueString, 2), ",");
		String parseCode = redDanMa+"#"+redTuoMa+"|"+blue;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(400111131523*142533~03^_2_200_600)
	 * @param winCode(010203040506|09)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		String[] winCodes = winCode.split("\\|");
		List<String> winCodeRedList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[0], 2);
		List<String> winCodeBlueList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[1], 2);
		
		String redDanMaString = code.substring(4, code.indexOf("*"));
		String redDanMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(redDanMaString, winCodeRedList, 2);
		
		String redTuoMaString = code.substring(code.indexOf("*")+1, code.indexOf("~"));
		String redTuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(redTuoMaString, winCodeRedList, 2);
		
		String blueString = code.substring(code.indexOf("~")+1);
		String blue = OrderInfoHtmlParseCommon.getNotPositionalBetCode(blueString, winCodeBlueList, 2);
		
		String parseCode = redDanMa+"#"+redTuoMa+"|"+blue;
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0001040613141533~09", "010203040506|09");
		System.out.println(html);
	}*/
	
}
