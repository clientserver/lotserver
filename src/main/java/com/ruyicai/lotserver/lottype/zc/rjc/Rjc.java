package com.ruyicai.lotserver.lottype.zc.rjc;

import com.ruyicai.lotserver.util.common.Tools;
import net.sf.json.JSONObject;

/**
 * 足彩-任九场
 * @author Administrator
 *
 */
public class Rjc {

	/**
	 * 解析开奖公告明细
	 * @param responseJson
	 * @param info
	 */
	public static void parseWinInfoDetail(JSONObject responseJson, String info) {
		//15047556_15047000_733974,1_115_83742
		String sellTotalAmount = ""; // 本期销售总金额
		String onePrizeNum = ""; // 一等奖注数
		String onePrizeAmt = ""; // 一等奖金额
		if (!Tools.isEmpty(info)&&!info.trim().equals("null")) { // info不为空
			String[] split1 = info.split(",");
			String[] split2 = split1[0].split("_");
			sellTotalAmount = split2[0]; // 本期销售总金额
			String[] split3 = split1[1].split(";");
			for (String string : split3) {
				String[] split4 = string.split("_");
				if (split4[0].equals("1")) { // 一等奖
					onePrizeNum = split4[1]; // 一等奖注数
					onePrizeAmt = split4[2]; // 一等奖金额
				}
			}
		}
		responseJson.put("sellTotalAmount", sellTotalAmount);
		responseJson.put("onePrizeNum", onePrizeNum);
		responseJson.put("onePrizeAmt", onePrizeAmt);
	}
	
}
