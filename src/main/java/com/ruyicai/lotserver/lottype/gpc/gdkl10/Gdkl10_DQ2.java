package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-胆拖-选二连直
 * @author Administrator
 *
 */
public class Gdkl10_DQ2 {

	/**
	 * 解析OrderInfo
	 * @param code(D|Q2|01-0203_1_200_800)
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
	 * @param code(D|Q2|01-0203_1_200_800)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String[] danTuo = code.split("-");
		String dan = danTuo[0];
		List<String> tuoList = LotteryAlgorithmUtil.getStringArrayFromString(danTuo[1], 2);
		
		//查找中奖号码
		List<String> danWinList = new ArrayList<String>();
		List<String> tuoWinList = new ArrayList<String>();
		
		if (winCodeList.contains(dan)) {
			int index = winCodeList.indexOf(dan);
			if (index==0) { //第一位
				String nextWin = winCodeList.get(index+1);
				if (tuoList.contains(nextWin)) {
					danWinList.add(dan);
					tuoWinList.add(nextWin);
				}
			} else if (index==winCodeList.size()-1) { //最后一位
				String preWin = winCodeList.get(index-1);
				if (tuoList.contains(preWin)) {
					danWinList.add(dan);
					tuoWinList.add(preWin);
				}
			} else {
				String nextWin = winCodeList.get(index+1);
				if (tuoList.contains(nextWin)) {
					danWinList.add(dan);
					tuoWinList.add(nextWin);
				}
				String preWin = winCodeList.get(index-1);
				if (tuoList.contains(preWin)) {
					danWinList.add(dan);
					tuoWinList.add(preWin);
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
		String html = parseOrderInfoHtml("01-0203", "03 01 02 04 05 06 11 18");
		System.out.println(html);
	}*/
	
}
