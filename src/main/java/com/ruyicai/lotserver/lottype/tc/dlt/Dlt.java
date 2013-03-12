package com.ruyicai.lotserver.lottype.tc.dlt;

import com.ruyicai.lotserver.util.common.Tools;

import net.sf.json.JSONObject;

/**
 * 大乐透
 * @author Administrator
 *
 */
public class Dlt {

	/**
	 * 解析开奖公告明细
	 * @param responseJson
	 * @param info
	 */
	public static void parseWinInfoDetail(JSONObject responseJson, String info) {
		// 8703128900_2455151900_29407441000,1_0_0;2_26_16483000;3_138_806700;4_127_300000;5_4607_60000;6_19362_10000;7_184981_1000;8_2058096_500;
		// 11_0_0;12_6_9889800;13_22_484000;14_25_150000;15_1447_30000;16_5670_5000;17_54731_500;18_3373_6000
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
		String sevenPrizeNum = ""; // 七等奖注数
		String sevenPrizeAmt = ""; // 七等奖金额
		String eightPrizeNum = ""; // 八等奖注数
		String eightPrizeAmt = ""; // 八等奖金额
		
		String onePrizeZJNum = ""; // 一等奖追加注数
		String onePrizeZJAmt = ""; // 一等奖追加金额
		String twoPrizeZJNum = ""; // 二等奖追加注数
		String twoPrizeZJAmt = ""; // 二等奖追加金额
		String threePrizeZJNum = ""; // 三等奖追加注数
		String threePrizeZJAmt = ""; // 三等奖追加金额
		String fourPrizeZJNum = ""; // 四等奖追加注数
		String fourPrizeZJAmt = ""; // 四等奖追加金额
		String fivePrizeZJNum = ""; // 五等奖追加注数
		String fivePrizeZJAmt = ""; // 五等奖追加金额
		String sixPrizeZJNum = ""; // 六等奖追加注数
		String sixPrizeZJAmt = ""; // 六等奖追加金额
		String sevenPrizeZJNum = ""; // 七等奖追加注数
		String sevenPrizeZJAmt = ""; // 七等奖追加金额
		String twelveSelect2PrizeNum = ""; // 十二选二注数
		String twelveSelect2PrizeAmt = ""; // 十二选二金额
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
					twoPrizeNum = split4[1]; // 一等奖注数
					twoPrizeAmt = split4[2]; // 一等奖金额
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
				} else if (split4[0].equals("7")) { // 七等奖
					sevenPrizeNum = split4[1]; // 七等奖注数
					sevenPrizeAmt = split4[2]; // 七等奖金额
				} else if (split4[0].equals("8")) { // 八等奖
					eightPrizeNum = split4[1]; // 八等奖注数
					eightPrizeAmt = split4[2]; // 八等奖金额
				} else if (split4[0].equals("11")) { // 一等奖追加
					onePrizeZJNum = split4[1]; // 一等奖追加注数
					onePrizeZJAmt = split4[2]; // 一等奖追加金额
				} else if (split4[0].equals("12")) { // 二等奖追加
					twoPrizeZJNum = split4[1]; // 二等奖追加注数
					twoPrizeZJAmt = split4[2]; // 二等奖追加金额
				} else if (split4[0].equals("13")) { // 三等奖追加
					threePrizeZJNum = split4[1]; // 三等奖追加注数
					threePrizeZJAmt = split4[2]; // 三等奖追加金额
				} else if (split4[0].equals("14")) { // 四等奖追加
					fourPrizeZJNum = split4[1]; // 四等奖追加注数
					fourPrizeZJAmt = split4[2]; // 四等奖追加金额
				} else if (split4[0].equals("15")) { // 五等奖追加
					fivePrizeZJNum = split4[1]; // 五等奖追加注数
					fivePrizeZJAmt = split4[2]; // 五等奖追加金额
				} else if (split4[0].equals("16")) { // 六等奖追加
					sixPrizeZJNum = split4[1]; // 六等奖追加注数
					sixPrizeZJAmt = split4[2]; // 六等奖追加金额
				} else if (split4[0].equals("17")) { // 七等奖追加
					sevenPrizeZJNum = split4[1]; // 七等奖追加注数
					sevenPrizeZJAmt = split4[2]; // 七等奖追加金额
				} else if (split4[0].equals("18")) { // 十二选二
					twelveSelect2PrizeNum = split4[1]; // 十二选二注数
					twelveSelect2PrizeAmt = split4[2]; // 十二选二金额
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
		responseJson.put("sevenPrizeNum", sevenPrizeNum);
		responseJson.put("sevenPrizeAmt", sevenPrizeAmt);
		responseJson.put("eightPrizeNum", eightPrizeNum);
		responseJson.put("eightPrizeAmt", eightPrizeAmt);
		// 追加
		responseJson.put("onePrizeZJNum", onePrizeZJNum);
		responseJson.put("onePrizeZJAmt", onePrizeZJAmt);
		responseJson.put("twoPrizeZJNum", twoPrizeZJNum);
		responseJson.put("twoPrizeZJAmt", twoPrizeZJAmt);
		responseJson.put("threePrizeZJNum", threePrizeZJNum);
		responseJson.put("threePrizeZJAmt", threePrizeZJAmt);
		responseJson.put("fourPrizeZJNum", fourPrizeZJNum);
		responseJson.put("fourPrizeZJAmt", fourPrizeZJAmt);
		responseJson.put("fivePrizeZJNum", fivePrizeZJNum);
		responseJson.put("fivePrizeZJAmt", fivePrizeZJAmt);
		responseJson.put("sixPrizeZJNum", sixPrizeZJNum);
		responseJson.put("sixPrizeZJAmt", sixPrizeZJAmt);
		responseJson.put("sevenPrizeZJNum", sevenPrizeZJNum);
		responseJson.put("sevenPrizeZJAmt", sevenPrizeZJAmt);
		responseJson.put("twelveSelect2PrizeNum", twelveSelect2PrizeNum);
		responseJson.put("twelveSelect2PrizeAmt", twelveSelect2PrizeAmt);
	}
	
}
