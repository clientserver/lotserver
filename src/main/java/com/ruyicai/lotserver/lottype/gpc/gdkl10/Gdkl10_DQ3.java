package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;



/**
 * 广东快乐十分-胆拖-直选前三
 * @author Administrator
 *
 */
public class Gdkl10_DQ3 {

	/**
	 * 解析OrderInfo
	 * @param code(D|Q3|0908-111204_1_200_3600)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] codes = code.split("-");
		String danMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ",");
		String tuoMa = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ",");
		String parseCode = danMa+"#"+tuoMa;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(D|Q3|0908-111204_1_200_3600)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 6), 2);
		
		String[] danTuo = code.split("-");
		List<String> danList = LotteryAlgorithmUtil.getStringArrayFromString(danTuo[0], 2);
		List<String> tuoList = LotteryAlgorithmUtil.getStringArrayFromString(danTuo[1], 2);
		
		//查询中奖的号码
		List<String> danWinList = new ArrayList<String>();
		List<String> tuoWinList = new ArrayList<String>();
		
		if (winCodeList.containsAll(danList)) { //胆码都在中奖号码里
			winCodeList.removeAll(danList);
			if (winCodeList.size()==1) {
				String win = winCodeList.get(0);
				if (tuoList.contains(win)) {
					danWinList.addAll(danList);
					tuoWinList.add(win);
				}
			} else if (winCodeList.size()==2) {
				String oneWin = winCodeList.get(0);
				String twoWin = winCodeList.get(1);
				if (tuoList.contains(oneWin)&&tuoList.contains(twoWin)) {
					danWinList.addAll(danList);
					tuoWinList.add(oneWin);
					tuoWinList.add(twoWin);
				}
			}
		}
		
		//解析投注号码
		String danMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[0], danWinList, 2); //胆码
		String tuoMa = OrderInfoHtmlParseCommon.getNotPositionalBetCode(danTuo[1], tuoWinList, 2); //拖码
		String parseCode = danMa+"#"+tuoMa;
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0908-111204", "09 08 11 04 05 07 12 18");
		System.out.println(html);
	}*/
	
}
