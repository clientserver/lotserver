package com.ruyicai.lotserver.lottype.tc.pls;

import com.ruyicai.lotserver.util.common.Tools;
import net.sf.json.JSONObject;

/**
 * 排列三
 * @author Administrator
 *
 */
public class Pls {

	/**
	 * 解析开奖公告明细
	 * @param responseJson
	 * @param info
	 */
	public static void parseWinInfoDetail(JSONObject responseJson, String info) {
		// 2116134200_1021780000_646849000,1_8013_100000;2_6890_32000;3_0_16000
		String sellTotalAmount = ""; // 本期销售总金额
		String onePrizeNum = ""; // 一等奖注数
		String onePrizeAmt = ""; // 一等奖金额
		String twoPrizeNum = ""; // 二等奖注数
		String twoPrizeAmt = ""; // 二等奖金额
		String threePrizeNum = ""; // 三等奖注数
		String threePrizeAmt = ""; // 三等奖金额
		
		if (!Tools.isEmpty(info)&&!info.trim().equals("null")) { // info不为空
			String[] split1 = info.split(",");
			String[] split2 = split1[0].split("_");
			sellTotalAmount = split2[0]; // 本期销售总金额
			// prizePoolTotalAmount = split2[2]; //当前奖池总金额
			String[] split3 = split1[1].split(";");
			for (String string : split3) {
				String[] split4 = string.split("_");
				if (split4[0].equals("1")) { // 一等奖
					onePrizeNum = split4[1]; // 一等奖注数
					onePrizeAmt = split4[2]; // 一等奖金额
				} else if (split4[0].equals("2")) { // 二等奖
					twoPrizeNum = split4[1]; // 一等奖注数
					twoPrizeAmt = split4[2]; // 一等奖金额
				} else if (split4[0].equals("3")) { // 三等奖
					threePrizeNum = split4[1]; // 三等奖注数
					threePrizeAmt = split4[2]; // 三等奖金额
				}
			}
		}
		responseJson.put("sellTotalAmount", sellTotalAmount);
		responseJson.put("onePrizeNum", onePrizeNum);
		responseJson.put("onePrizeAmt", onePrizeAmt);
		responseJson.put("twoPrizeNum", twoPrizeNum);
		responseJson.put("twoPrizeAmt", twoPrizeAmt);
		responseJson.put("threePrizeNum", threePrizeNum);
		responseJson.put("threePrizeAmt", threePrizeAmt);
	}
	
}
