package com.ruyicai.lotserver.util.lot;

import com.ruyicai.lotserver.consts.LotType;

/**
 * 彩种公共类
 * @author Administrator
 *
 */
public class LotTypeUtil {

	/**
	 * 判断是否是竞彩足球
	 * @param lotNo
	 * @return
	 */
	public static boolean isJingCaiZQ(String lotNo) {
		boolean isJingCaiZQ = false;
		if (lotNo!=null&&(lotNo.equals(LotType.JCZ_SPF.lotNo())||lotNo.equals(LotType.JCZ_BF.lotNo())
				||lotNo.equals(LotType.JCZ_JQS.lotNo())||lotNo.equals(LotType.JCZ_BQC.lotNo()))) {
			isJingCaiZQ = true;
		}
		return isJingCaiZQ;
	}
	
	/**
	 * 判断是否是竞彩篮球
	 * @param lotNo
	 * @return
	 */
	public static boolean isJingCaiLQ(String lotNo) {
		boolean isJingCaiLQ = false;
		if (lotNo!=null&&(lotNo.equals(LotType.JCL_SF.lotNo())||lotNo.equals(LotType.JCL_RFSF.lotNo())
				||lotNo.equals(LotType.JCL_SFC.lotNo())||lotNo.equals(LotType.JCL_DXF.lotNo()))) {
			isJingCaiLQ = true;
		}
		return isJingCaiLQ;
	}
	
	/**
	 * 判断是否是竞彩混合过关
	 * @param lotNo
	 * @return
	 */
	public static boolean isJingCaiHH(String lotNo) {
		boolean isJingCaiHH = false;
		if (lotNo!=null&&(lotNo.equals(LotType.JCHH_ZQ.lotNo())||lotNo.equals(LotType.JCHH_LQ.lotNo()))) {
			isJingCaiHH = true;
		}
		return isJingCaiHH;
	}
	
	/**
	 * 判断是否是足彩
	 * @param lotNo
	 * @return
	 */
	public static boolean isZuCai(String lotNo) {
		boolean isZuCai = false;
		if (lotNo!=null&&(lotNo.equals(LotType.ZC_SFC.lotNo())||lotNo.equals(LotType.ZC_RX9.lotNo())
				||lotNo.equals(LotType.ZC_JQC.lotNo())||lotNo.equals(LotType.ZC_BQC.lotNo()))) {
			isZuCai = true;
		}
		return isZuCai;
	}
	
	/**
	 * 判断是否高频彩
	 * @param lotNo
	 * @return
	 */
	public static boolean isHighFrequency(String lotNo) {
		boolean isHighFrequency = false;
		if (lotNo!=null&&(lotNo.equals(LotType.SSC.lotNo())||lotNo.equals(LotType.OO_F.lotNo())
				||lotNo.equals(LotType.OO_YDJ.lotNo())||lotNo.equals(LotType.GDOO_F.lotNo())
				||lotNo.equals(LotType.GDH_T.lotNo()))) {
			isHighFrequency = true;
		}
		return isHighFrequency;
	}
	
}
