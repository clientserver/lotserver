package com.ruyicai.lotserver.util.lot;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 彩票查询公共类
 * @author Administrator
 *
 */
public class LotQueryUtil {

	/**
	 * 获得原始注码
	 * 投注查询-再买一次时使用
	 * @param isSellWays
	 * @param lotNo
	 * @param betCode
	 * @param orderInfo
	 * @return
	 */
	public static String getOriginalBetCode(String isSellWays, String lotNo, String betCode, String orderInfo) {
		String originalBetCode = "";
		if (Tools.isEmpty(isSellWays)) { //不是多玩法
			if (lotNo!=null&&(lotNo.trim().equals(LotType.QLC.lotNo())||lotNo.trim().equals(LotType.DDD.lotNo())
					||lotNo.trim().equals(LotType.SSQ.lotNo())||lotNo.trim().equals(LotType.OO_YDJ.lotNo())
					||lotNo.trim().equals(LotType.TT_F.lotNo()))) {
				originalBetCode = betCode.replaceAll("!", ""); //原始注码
			} else {
				if (betCode.indexOf(";")>-1 && betCode.indexOf("!")>-1) { //为了兼容互联网的多方案投注
					originalBetCode = betCode.replaceAll(";", "").replaceAll("!", ";"); //原始注码
				} else { //为了兼容wap和互联网的多注投注
					originalBetCode = betCode.replaceAll("!", ";"); //原始注码
				}
			}
		} else { //多玩法
			originalBetCode = orderInfo;
		}
		return originalBetCode;
	}
	
	/**
	 * 获得单注金额
	 * 投注查询-再买一次时使用
	 * @param orderInfo
	 * @return
	 */
	public static String getOneAmount(String orderInfo) {
		String oneAmount = "200";
		if (!Tools.isEmpty(orderInfo)&&!orderInfo.equals("null")) {
			String[] orderInfos = orderInfo.split("!");
			String[] codes = orderInfos[0].split("_");
			oneAmount = codes[2];
		}
		return oneAmount;
	}
	
	/**
	 * 是否可以再买一次
	 * 投注查询-再买一次时使用
	 * 足彩任九场,足彩进球彩,足彩半全场,足彩胜负彩,竞彩足球胜负平,合买不允许再买一次
	 * @param lotNo
	 * @param tlotCaseId
	 * @return
	 */
	public static String isRepeatBuy(String lotNo, String tlotCaseId) {
		String isRepeatBuy = "true";
		if ((lotNo!=null&&(LotTypeUtil.isZuCai(lotNo)||LotTypeUtil.isJingCaiZQ(lotNo)
				||LotTypeUtil.isJingCaiLQ(lotNo)||LotTypeUtil.isJingCaiHH(lotNo))
				)||(!Tools.isEmpty(tlotCaseId)&&!tlotCaseId.trim().equals("null"))) { 
			isRepeatBuy = "false";
		}
		return isRepeatBuy;
	}
	
}
