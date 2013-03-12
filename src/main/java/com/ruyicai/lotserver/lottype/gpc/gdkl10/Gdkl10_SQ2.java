package com.ruyicai.lotserver.lottype.gpc.gdkl10;

import java.util.List;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;

/**
 * 广东快乐十分-单式-选二连直
 * @author Administrator
 *
 */
public class Gdkl10_SQ2 {

	/**
	 * 解析OrderInfo
	 * @param code(S|Q2|0201_2_200_200)
	 * @return
	 */
	public static String parseOrderInfo(String code) {
		String parseCode = StringUtil.joinStringArrayWithCharacter(
				LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ",");
		return parseCode;
	}
	
	/**
	 * 解析OrderInfo(html)
	 * @param code(S|Q2|0201_2_200_200)
	 * @param winCode(02 01 03 04 05 06 11 12)
	 * @return
	 */
	public static String parseOrderInfoHtml(String code, String winCode) {
		winCode = winCode.replaceAll(" ", "");
		List<String> winCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode, 2);
		
		String parseCode = "";
		String qianString = code.substring(0, 2); //前位
		String houString = code.substring(2, 4); //后位
		boolean isWin = false; //是否中奖
		if (winCodeList.contains(qianString)) {
			int index = winCodeList.indexOf(qianString);
			if (index!=winCodeList.size()-1) {
				String nextWin = winCodeList.get(index+1);
				if (nextWin.equals(houString)) {
					isWin = true;
				}
			}
		}
		if (isWin) { //中奖
			parseCode = "<font color=\"red\">"+qianString+"</font>"+","+"<font color=\"red\">"+houString+"</font>";
		} else {
			parseCode = qianString+","+houString;
		}
		return parseCode;
	}
	
	/*public static void main(String[] args) {
		String html = parseOrderInfoHtml("0201", "08 03 13 04 06 09 01 02");
		System.out.println(html);
	}*/
	
}
