package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.ArrayList;
import java.util.List;

import com.ruyicai.lotserver.lottype.OrderInfoHtmlParseCommon;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-定位-直选前三
 * @author Administrator
 *
 */
public class Gdkl10_PQ3 {

	/**
	 * 解析OrderInfo
	 * @param code(P|Q3|0115-2005-18_1_200_800)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String[] codes = code.split("-");
		String qian = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[0], 2), ","); //前位
		String zhong = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[1], 2), ","); //中位
		String hou = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(codes[2], 2), ","); //后位
		String parseCode = qian+"|"+zhong+"|"+hou;
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(P|Q3|0115-2005-18_1_200_800)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		String qianWinCode = winCode.substring(0, 2);
		String zhongWinCode = winCode.substring(2, 4);
		String houWinCode = winCode.substring(4, 6);
		
		String[] qianHou = code.split("-");
		List<String> qianList = LotteryAlgorithmUtil.getStringArrayFromString(qianHou[0], 2);
		List<String> zhongList = LotteryAlgorithmUtil.getStringArrayFromString(qianHou[1], 2);
		List<String> houList = LotteryAlgorithmUtil.getStringArrayFromString(qianHou[2], 2);
		
		//查找中奖号码
		List<String> qianWinList = new ArrayList<String>();
		List<String> zhongWinList = new ArrayList<String>();
		List<String> houWinList = new ArrayList<String>();
		
		for (String qian : qianList) {
			if (qian.equals(qianWinCode)) {
				if (zhongList.contains(zhongWinCode)&&houList.contains(houWinCode)) {
					qianWinList.add(qian);
					zhongWinList.add(zhongWinCode);
					houWinList.add(houWinCode);
				}
			}
		}
		
		//解析投注号码
		String qian = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[0], qianWinList, 2); //前位
		String zhong = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[1], zhongWinList, 2); //中位
		String hou = OrderInfoHtmlParseCommon.getNotPositionalBetCode(qianHou[2], houWinList, 2); //后位
		String parseCode = qian+"|"+zhong+"|"+hou;
		
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0115-2005-18", "01 05 18 04 05 06 11 19");
		System.out.println(html);
	}*/
	
}
