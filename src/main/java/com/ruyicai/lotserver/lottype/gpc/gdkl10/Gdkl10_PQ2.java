package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-定位-选二连直
 * @author Administrator
 *
 */
public class Gdkl10_PQ2 {

	/**
	 * 解析OrderInfo
	 * @param code(P|Q2|010203-1804_2_200_1200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] codes = code.split("-");
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ","); //前位
		String hou = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ","); //后位
		String parseCode = qian+"|"+hou;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(P|Q2|010203-1804_2_200_1200)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String[] qianHou = code.split("-");
		List<String> qianList = LotteryAlgorithmUtil.getStringArrayFromString(qianHou[0], 2);
		List<String> houList = LotteryAlgorithmUtil.getStringArrayFromString(qianHou[1], 2);
		
		//查找中奖号码
		List<String> qianWinList = new ArrayList<String>();
		List<String> houWinList = new ArrayList<String>();
		
		for (String qian : qianList) {
			if (winCodeList.contains(qian)) {
				int index = winCodeList.indexOf(qian);
				if (index==0) { //第一位
					String nextWin = winCodeList.get(index+1);
					if (houList.contains(nextWin)) {
						qianWinList.add(qian);
						houWinList.add(nextWin);
					}
				} else if (index==winCodeList.size()-1) { //最后一位
					String preWin = winCodeList.get(index-1);
					if (houList.contains(preWin)) {
						qianWinList.add(qian);
						houWinList.add(preWin);
					}
				} else {
					String nextWin = winCodeList.get(index+1);
					if (houList.contains(nextWin)) {
						qianWinList.add(qian);
						houWinList.add(nextWin);
					}
					String preWin = winCodeList.get(index-1);
					if (houList.contains(preWin)) {
						qianWinList.add(qian);
						houWinList.add(preWin);
					}
				}
			}
		}
		
		//解析投注号码
		String qian = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[0], qianWinList, 2); //前位
		String hou = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[1], houWinList, 2); //后位
		String parseCode = qian+"|"+hou;
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("010203-1804", "03 01 02 04 05 06 11 18");
		System.out.println(html);
	}*/
	
}
