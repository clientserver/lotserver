package com.ruyicai.lotserver.util.lot;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 大乐透相关公共类
 * @author Administrator
 *
 */
public class DaletouUtil {

	/**
	 * 判断大乐透是否是追加
	 * @param betCode
	 * @return
	 */
	public static boolean isSuperaddition(String betCode, String amount, String lotMulti, String isSellWays) {
		long num = 0; //根据注码取得注数
		String[] codes = null;
		if (isSellWays!=null&&isSellWays.equals("1")) { //多玩法
			//01 16 28 29 30-09 10;_1_300_300!01 16 28 29 30-09 10;_1_300_300
			String[] split = betCode.split("!");
			codes = new String[split.length];
			int index = 0;
			for (String string : split) {
				String[] split2 = string.split("_");
				if (split2[0].indexOf(";")>-1) {
					codes[index] = split2[0].substring(0, split2[0].length()-1);
				} else {
					codes[index] = split2[0];
				}
				index++;
			}
		} else {
			if (betCode.indexOf("!") > -1) { //单式
				codes = betCode.split("!");
			} else if (betCode.indexOf(";") > -1) { //单式
				codes = betCode.split(";");
			} else {
				codes = new String[]{betCode};
			}
		}
		if (codes!=null && codes.length >0) {
			for (String code : codes) {
				if (code.indexOf("-") > -1) {
					if (code.indexOf("$") > -1) { //胆拖
						int aRedBalls = 0; //前区胆码的个数
						int aRedTuoBalls = 0; //前区拖码的个数
						int aBlueBalls = 0; //后区胆码的个数
						int aBlueTuoBalls = 0; //后区拖码的个数
						String[] qianHou = code.split("-");
						//前区
						if (qianHou[0].indexOf("$") > -1) {
							String[] qianQu = qianHou[0].split("\\$");
							String[] qianQuDan = qianQu[0].split(" ");
							aRedBalls = qianQuDan.length; //前区胆码的个数
							String[] qianQuTuo = qianQu[1].split(" ");
							aRedTuoBalls = qianQuTuo.length;
						} else {
							String[] qianQuTuo = qianHou[0].split(" ");
							aRedTuoBalls = qianQuTuo.length;
						}
						//后区
						if (qianHou[1].indexOf("$") > -1) {
							String[] houQu = qianHou[1].split("\\$");
							String[] houQuDan = houQu[0].split(" ");
							aBlueBalls = houQuDan.length;
							String[] houQuTuo = houQu[1].split(" ");
							aBlueTuoBalls = houQuTuo.length;
						} else {
							String[] houQuTuo = qianHou[1].split(" ");
							aBlueTuoBalls = houQuTuo.length;
						}
						num += LotteryAlgorithmUtil.zuhe(5 - aRedBalls, aRedTuoBalls)*LotteryAlgorithmUtil.zuhe(2 - aBlueBalls, aBlueTuoBalls);
					} else { //单式或复式
						String[] qianHou = code.split("-");
						String[] qian = qianHou[0].split(" ");
						String[] hou = qianHou[1].split(" ");
						int aRedBalls = qian.length;
						int aBlueBalls = hou.length;
						num += LotteryAlgorithmUtil.zuhe(5, aRedBalls)*LotteryAlgorithmUtil.zuhe(2, aBlueBalls);
					}
				} else { //十二选二
					num ++;
				}
			}
		} else {
			num = 1;
		}
		
		if (Integer.parseInt(amount)/(Integer.parseInt(lotMulti)*Integer.parseInt(num+""))==300) { //是追加
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否大乐透12选2追加
	 * @param clientInfo
	 * @return
	 */
	public static boolean isDaLeTou12_2ZhuiJia(ClientInfo clientInfo) {
		boolean isDaLeTou12_2ZhuiJia = false;
		String lotNo = clientInfo.getLotNo(); //彩种
		String isSellWays = clientInfo.getIsSellWays(); //是否多玩法
		String oneAmount = clientInfo.getOneAmount(); //单注金额
		String betCode = clientInfo.getBetCode(); //注码
		if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())&&oneAmount!=null&&oneAmount.equals("300")) { //大乐透
			if (isSellWays!=null&&isSellWays.equals("1")) { //多玩法
				String[] split = betCode.split("!");
				for (String string : split) {
					String[] split2 = string.split("_");
					if (split2[0].indexOf("-")==-1) {
						isDaLeTou12_2ZhuiJia = true;
						break;
					}
				}
			}
		}
		return isDaLeTou12_2ZhuiJia;
	}
	
}
