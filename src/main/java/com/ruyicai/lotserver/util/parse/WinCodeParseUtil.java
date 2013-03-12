package com.ruyicai.lotserver.util.parse;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 投注查询里中奖号码的解析
 * @author Administrator
 *
 */
public class WinCodeParseUtil {

	public static String parseWinCode(String lotNo, String winCode) {
		StringBuilder result = new StringBuilder();
		
		if (!Tools.isEmpty(winCode)&&!winCode.equals("null")) {
			if (lotNo!=null&&lotNo.equals(LotType.QLC.lotNo())) { //七乐彩
				//04060914202630|02
				String[] split = winCode.split("\\|");
				String qian = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[0], 2), ",");
				String hou = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[1], 2), ",");
				result.append(qian+"|"+hou);
			} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
				//030302
				String codeString = LotteryAlgorithmUtil.removeZero3D(winCode); //去掉前面的"0"
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.SSQ.lotNo())) { // 双色球
				//132527282930|15
				String[] split = winCode.split("\\|");
				String qian = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[0], 2), ",");
				String hou = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[1], 2), ",");
				result.append(qian+"|"+hou);
			} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //超级大乐透
				//02 05 08 09 31+03 07(新格式)||03 04 06 11 13 01 02(旧格式)
				winCode = winCode.replaceAll(" ", "");
				if (winCode.indexOf("+")>-1) { //新格式
					String[] split = winCode.split("\\+");
					String qian = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[0], 2), ",");
					String hou = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(split[1], 2), ",");
					result.append(qian+"|"+hou);
				} else { //旧格式
					if (winCode.length()>10) {
						String qianStr = winCode.substring(0, 10);
						String houStr = winCode.substring(10);
						String qian = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(qianStr, 2), ",");
						String hou = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(houStr, 2), ",");
						result.append(qian+"|"+hou);
					}
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
				//929
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.PLW.lotNo())) { //排列五
				//92934
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.QXC.lotNo())) { //七星彩
				//3462115
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
				//01 07 14 16 21
				String code = winCode.replaceAll(" ", ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.SSC.lotNo())) { //时时彩
				//95231
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.OO_F.lotNo())) { //江西11选5
				//08 11 01 04 09
				String code = winCode.replaceAll(" ", ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.OO_YDJ.lotNo())) { //11运夺金
				//04 10 08 06 02
				String code = winCode.replaceAll(" ", ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.GDOO_F.lotNo())) { //广东11选5
				//10 05 03 07 04
				String code = winCode.replaceAll(" ", ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.GDH_T.lotNo())) { //广东快乐十分
				//01 02 03 04 05 16 17 20
				String code = winCode.replaceAll(" ", ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.ZC_SFC.lotNo())) { //足球胜负彩
				//11031103000311
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.ZC_RX9.lotNo())) { //足球任选9
				//11031103000311
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())) { //足球进球彩
				//01010303
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			} else if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())) { //足球半全场
				//003311**1133(或310133333311)
				String code = StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(winCode, 1), ",");
				result.append(code);
			}
		}
		
		return result.toString();
	}
	
	/*public static void main(String[] args) {
		String code = parseWinCode("T01001", "02 05 08 09 31+03 07");
		System.out.println(code);
	}*/
	
}
