package com.ruyicai.lotserver.lottype.tc.dlt;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 大乐透-胆拖
 * @author Administrator
 *
 */
public class Dlt_dt {

	/**
	 * 解析OrderInfo
	 * @param code(01 02 03$09 19 28-06 07 09_1_300_2700)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] qianHou = code.split("-");
		String qianQuDanMa = ""; //前区胆码
		String qianQuTuoMa = ""; //前区拖码
		String houQuDanMa = ""; //后区胆码
		String houQuTuoMa = ""; //后区拖码
		//前区
		if (qianHou[0].indexOf("$") > -1) {
			String[] qianQu = qianHou[0].split("\\$");
			qianQuDanMa = qianQu[0].replaceAll(" ", ","); //前区胆码
			qianQuTuoMa = qianQu[1].replaceAll(" ", ","); //前区拖码
		} else {
			qianQuTuoMa = qianHou[0].replace(" ", ","); //前区拖码
		}
		//后区
		if (qianHou[1].indexOf("$") > -1) {
			String[] houQu = qianHou[1].split("\\$");
			houQuDanMa = houQu[0].replaceAll(" ", ","); //后区胆码
			houQuTuoMa = houQu[1].replaceAll(" ", ","); //后区拖码
		} else {
			houQuTuoMa = qianHou[1].replaceAll(" ", ","); //后区拖码
		}
		StringBuffer betCodeBuffer = new StringBuffer();
		if (!Tools.isEmpty(qianQuDanMa)) {
			betCodeBuffer.append(qianQuDanMa+"#");
		}
		betCodeBuffer.append(qianQuTuoMa+"|");
		if (!Tools.isEmpty(houQuDanMa)) {
			betCodeBuffer.append(houQuDanMa+"#");
		}
		betCodeBuffer.append(houQuTuoMa);
		
		return betCodeBuffer.toString();
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(01 02 03$09 19 28-06 07 09_1_300_2700)
	 * @param winCode(新:10 13 22 27 34+04 09)(旧:03 04 06 11 13 01 02)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeQianList = new ArrayList<String>();
		List<String> winCodeHouList = new ArrayList<String>();
		if (winCode.indexOf("+")>-1) { //新格式
			String[] winCodes = winCode.split("\\+");
			winCodeQianList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[0], 2);
			winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCodes[1], 2);
		} else {
			if (winCode.length()>10) {
				winCodeQianList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 10), 2);
				winCodeHouList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(10), 2);
			}
		}
		
		String[] qianHou = code.split("-");
		String qianQuDanMa = ""; //前区胆码
		String qianQuTuoMa = ""; //前区拖码
		String houQuDanMa = ""; //后区胆码
		String houQuTuoMa = ""; //后区拖码
		//前区
		if (qianHou[0].indexOf("$") > -1) {
			String[] qianQu = qianHou[0].split("\\$");
			String qianQuDanMaString = qianQu[0].replaceAll(" ", ""); //前区胆码
			qianQuDanMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianQuDanMaString, winCodeQianList, 2);
			
			String qianQuTuoMaString = qianQu[1].replaceAll(" ", ""); //前区拖码
			qianQuTuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianQuTuoMaString, winCodeQianList, 2);
		} else {
			String qianQuTuoMaString = qianHou[0].replace(" ", ""); //前区拖码
			qianQuTuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianQuTuoMaString, winCodeQianList, 2);
		}
		//后区
		if (qianHou[1].indexOf("$") > -1) {
			String[] houQu = qianHou[1].split("\\$");
			String houQuDanMaString = houQu[0].replaceAll(" ", ""); //后区胆码
			houQuDanMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(houQuDanMaString, winCodeHouList, 2);
			
			String houQuTuoMaString = houQu[1].replaceAll(" ", ""); //后区拖码
			houQuTuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(houQuTuoMaString, winCodeHouList, 2);
		} else {
			String houQuTuoMaString = qianHou[1].replaceAll(" ", ""); //后区拖码
			houQuTuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(houQuTuoMaString, winCodeHouList, 2);
		}
		StringBuilder builder = new StringBuilder();
		if (!Tools.isEmpty(qianQuDanMa)) {
			builder.append(qianQuDanMa+"#");
		}
		builder.append(qianQuTuoMa+"|");
		if (!Tools.isEmpty(houQuDanMa)) {
			builder.append(houQuDanMa+"#");
		}
		builder.append(houQuTuoMa);
		
		String parseCode = builder.toString();
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("01 02 03$09 19 28-06 07 09", "01 13 22 27 34 06 09");
		System.out.println(html);
	}*/
	
}
