package com.ruyicai.lotserver.lottype.fc.ssq;

import com.ruyicai.lotserver.util.common.Tools;
import net.sf.json.JSONObject;

/**
 * 双色球
 * @author Administrator
 *
 */
public class Ssq {

	/**
	 * 解析开奖公告明细
	 * @param responseJson
	 * @param info
	 */
	public static void parseWinInfoDetail(JSONObject responseJson, String info) {
		// 33068233400_33068233400_70506941200,1_2_1000000000;2_75_33750800;3_1106_300000;4_64091_20000;5_1306549_1000;6_9691094_500;7_0_0;8_0_0;9_0_0;10_0_0
		String sellTotalAmount = ""; // 本期销售总金额
		String prizePoolTotalAmount = ""; // 当前奖池总金额
		String onePrizeNum = ""; // 一等奖注数
		String onePrizeAmt = ""; // 一等奖金额
		String twoPrizeNum = ""; // 二等奖注数
		String twoPrizeAmt = ""; // 二等奖金额
		String threePrizeNum = ""; // 三等奖注数
		String threePrizeAmt = ""; // 三等奖金额
		String fourPrizeNum = ""; // 四等奖注数
		String fourPrizeAmt = ""; // 四等奖金额
		String fivePrizeNum = ""; // 五等奖注数
		String fivePrizeAmt = ""; // 五等奖金额
		String sixPrizeNum = ""; // 六等奖注数
		String sixPrizeAmt = ""; // 六等奖金额
		
		if (!Tools.isEmpty(info)&&!info.trim().equals("null")) { // info不为空
			String[] split1 = info.split(",");
			String[] split2 = split1[0].split("_");
			sellTotalAmount = split2[0]; // 本期销售总金额
			prizePoolTotalAmount = split2[2]; // 当前奖池总金额
			String[] split3 = split1[1].split(";");
			for (String string : split3) {
				String[] split4 = string.split("_");
				if (split4[0].equals("1")) { // 一等奖
					onePrizeNum = split4[1]; // 一等奖注数
					onePrizeAmt = split4[2]; // 一等奖金额
				} else if (split4[0].equals("2")) { // 二等奖
					twoPrizeNum = split4[1]; // 二等奖注数
					twoPrizeAmt = split4[2]; // 二等奖金额
				} else if (split4[0].equals("3")) { // 三等奖
					threePrizeNum = split4[1]; // 三等奖注数
					threePrizeAmt = split4[2]; // 三等奖金额
				} else if (split4[0].equals("4")) { // 四等奖
					fourPrizeNum = split4[1]; // 四等奖注数
					fourPrizeAmt = split4[2]; // 四等奖金额
				} else if (split4[0].equals("5")) { // 五等奖
					fivePrizeNum = split4[1]; // 五等奖注数
					fivePrizeAmt = split4[2]; // 五等奖金额
				} else if (split4[0].equals("6")) { // 六等奖
					sixPrizeNum = split4[1]; // 六等奖注数
					sixPrizeAmt = split4[2]; // 六等奖金额
				}
			}
		}
		responseJson.put("sellTotalAmount", sellTotalAmount);
		responseJson.put("prizePoolTotalAmount", prizePoolTotalAmount);
		responseJson.put("onePrizeNum", onePrizeNum);
		responseJson.put("onePrizeAmt", onePrizeAmt);
		responseJson.put("twoPrizeNum", twoPrizeNum);
		responseJson.put("twoPrizeAmt", twoPrizeAmt);
		responseJson.put("threePrizeNum", threePrizeNum);
		responseJson.put("threePrizeAmt", threePrizeAmt);
		responseJson.put("fourPrizeNum", fourPrizeNum);
		responseJson.put("fourPrizeAmt", fourPrizeAmt);
		responseJson.put("fivePrizeNum", fivePrizeNum);
		responseJson.put("fivePrizeAmt", fivePrizeAmt);
		responseJson.put("sixPrizeNum", sixPrizeNum);
		responseJson.put("sixPrizeAmt", sixPrizeAmt);
	}
	
}
