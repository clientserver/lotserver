package com.ruyicai.lotserver.util.lot;

import java.util.List;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 收益率
 * @author Administrator
 *
 */
public class YieldUtil {

	/**
	 * 计算收益率
	 * 
	 * @param lotMulti
	 * @param betNum
	 * @param accumulatedInput
	 * @param yieldRate
	 * @param prix
	 * @param lotMultiArray
	 * @param accumulatedInputArray
	 * @param i
	 */
	public static String calculateYield(String lotMulti, String betNum,String accumulatedInput, String yieldRate, 
			int prix, String[] lotMultiArray, String[] accumulatedInputArray, int index) {
		String num = "";
		
		int lotMultiInt = Integer.parseInt(lotMulti);
		int betNumInt = Integer.parseInt(betNum);
		int accumulatedInputInt = Integer.parseInt(accumulatedInput);
		int yieldRateInt = Integer.parseInt(yieldRate);
		if (((prix*lotMultiInt-(betNumInt*2*lotMultiInt+accumulatedInputInt))*100)
				/ (betNumInt*2*lotMultiInt+accumulatedInputInt)>=yieldRateInt) {
			if (index != 0) {
				accumulatedInput = (betNumInt*2*lotMultiInt+accumulatedInputInt)+"";
			}
			lotMultiArray[index] = lotMulti;
			accumulatedInputArray[index] = accumulatedInput;
		} else {
			if (9999-lotMultiInt>1) { //比互联网多加了一个判断，否则会出现异常
				for (int j = 1; j < 9999-lotMultiInt; j++) {
					if ((prix*(lotMultiInt+j)-(betNumInt*2*(lotMultiInt+j)+accumulatedInputInt))*100
							/ (betNumInt*2*(lotMultiInt+j)+accumulatedInputInt)>=yieldRateInt) {
						if (index != 0) {
							accumulatedInput = (betNumInt*2*(lotMultiInt+j)+accumulatedInputInt)+"";
						}
						lotMultiArray[index] = (lotMultiInt+j)+"";
						accumulatedInputArray[index] = accumulatedInput;
						break;
					}
					if (j >= 9999-lotMultiInt-1) { //方案上限限制9999倍，你的方案不适合投入
						num = (index+1)+"";
					}
				}
			} else {
				num = (index+1)+"";
			}
		}
		return num;
	}

	/**
	 * 计算收益率时获取该注码的奖金
	 * 
	 * @param codes
	 * @param lotno
	 * @return
	 */
	public static Integer getPrix(String lotNo, String orderInfo) {
		Integer prix = 0;
		String wanfa = "";
		String code = "";
		String[] betCodes = orderInfo.split("_");
		String betCode = betCodes[0];
		if (lotNo!=null&&lotNo.equals(LotType.SSC.lotNo())) { //时时彩
			wanfa = betCode.substring(0, 2);
			//code = betCode.substring(2, betCode.length() - 1);

			if (wanfa.equals("DD")) {
				prix = 4;
			} else if (wanfa.equals("1D")) {
				prix = 10;
			} else if (wanfa.equals("2D")) {
				prix = 100;
			} else if (wanfa.equals("F2")) {
				prix = 50;
			} else if (wanfa.equals("S2")) {
				prix = 50;
			} else if (wanfa.equals("3D")) {
				prix = 1000;
			} else if (wanfa.equals("5D")) {
				prix = 100000;
			} else if (wanfa.equals("5T")) {
				prix = 20440;
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_F.lotNo())) { //江西11选5
			wanfa = betCode.substring(0, 2);
			code = betCode.substring(3, betCode.length());

			if (wanfa.equals("R1")) {
				prix = 13;
			} else if (wanfa.equals("R2")) {
				if (code.indexOf("$") > -1) {
					if (code.split("\\$")[1].split(" ").length < 5) {
						prix = 6 * code.split("\\$")[1].split(" ").length;
					} else {
						prix = 6 * 4;
					}
				} else {
					if (code.split(" ").length == 3) {
						prix = 6 * 3;
					} else {
						prix = 6;
					}
				}
			} else if (wanfa.equals("R3")) {
				if (code.indexOf("$") > -1) {
					Integer tmCount = code.split("\\$")[1].split(" ").length;
					Integer dmCount = code.split("\\$")[0].split(" ").length;
					if (dmCount == 1) {
						tmCount = tmCount > 4 ? 4 : tmCount;
					} else {
						tmCount = tmCount > 3 ? 3 : tmCount;
					}
					long zhushu = getDanTuoZhushu(tmCount,3,dmCount);
					prix = 19 * (Integer.valueOf(zhushu+""));
				} else {
					if (code.split(" ").length == 4) {
						prix = 19 * 4;
					} else {
						prix = 19;
					}
				}
			} else if (wanfa.equals("R4")) {
				if (code.indexOf("$") > -1) {
					Integer tmCount = code.split("\\$")[1].split(" ").length;
					Integer dmCount = code.split("\\$")[0].split(" ").length;
					if (dmCount == 1) {
						tmCount = tmCount > 4 ? 4 : tmCount;
					} else if (dmCount == 2) {
						tmCount = tmCount > 3 ? 3 : tmCount;
					} else {
						tmCount = tmCount > 2 ? 2 : tmCount;
					}
					long zhushu = getDanTuoZhushu(tmCount,4,dmCount);
					prix = 78 * (Integer.valueOf(zhushu+""));
				} else {
					if (code.split(" ").length > 4) {
						prix = 78 * 5;
					} else {
						prix = 78;
					}
				}
			} else if (wanfa.equals("R5")) {
				prix = 540;
			} else if (wanfa.equals("R6")) {
				if (code.indexOf("$") > -1) {
					Integer dmCount = code.split("\\$")[0].split(" ").length;
					Integer tmCount = code.split("\\$")[1].split(" ").length;
					prix = 90 * (tmCount - (5 - dmCount));
				} else {
					prix = 90 * (code.split(" ").length - 5);
				}
			} else if (wanfa.equals("R7")) {
				if (code.indexOf("$") > -1) {
					Integer dmCount = code.split("\\$")[0].split(" ").length;
					Integer tmCount = code.split("\\$")[1].split(" ").length;
					if (dmCount <= 5) {
						if (((tmCount + dmCount) - 5) == 6) {
							prix = 26 * 15;
						}
						if (((tmCount + dmCount) - 5) == 5) {
							prix = 26 * 10;
						}
						if (((tmCount + dmCount) - 5) == 4) {
							prix = 26 * 6;
						}
						if (((tmCount + dmCount) - 5) == 3) {
							prix = 26 * 3;
						}
					} else {
						prix = 26 * (tmCount);
					}
				} else {
					if ((code.split(" ").length - 5) == 4) {
						prix = 26 * 6;
					} else if ((code.split(" ").length - 5) == 3) {
						prix = 26 * 3;
					} else {
						prix = 26;
					}
				}
			} else if (wanfa.equals("R8")) {
				if (code.indexOf("$") > -1) {
					Integer dmCount = code.split("\\$")[0].split(" ").length;
					Integer tmCount = code.split("\\$")[1].split(" ").length;
					if ((dmCount) <= 5) {
						prix = 9 * (tmCount - (5 - dmCount));
					} else {
						prix = 9 * (tmCount);
					}
				} else {
					prix = 9;
				}
			} else if (wanfa.equals("Q2")) {
				prix = 130;
			} else if (wanfa.equals("Q3")) {
				prix = 1170;
			} else if (wanfa.equals("Z2")) {
				prix = 65;
			} else if (wanfa.equals("Z3")) {
				prix = 195;
			} 
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_YDJ.lotNo())) { //11运夺金
			wanfa = betCode.substring(0, 3);
			code = betCode.substring(4, betCode.length() - 1);

			if (wanfa.equals("101")) {
				prix = 13;
			} else if (wanfa.equals("141")||wanfa.equals("142")) {
				prix = 130;
			} else if (wanfa.equals("131")||wanfa.equals("108")||wanfa.equals("133")) {
				prix = 65;
			} else if (wanfa.equals("161")||wanfa.equals("162")) {
				prix = 1170;
			} else if (wanfa.equals("151")||wanfa.equals("109")||wanfa.equals("153")) {
				prix = 195;
			} else if (wanfa.equals("121")) {
				int length = code.split("\\*")[1].length() / 2;
				if (length < 5) {
					prix = 6 * length;
				} else {
					prix = 6 * 4;
				}
			} else if (wanfa.equals("102")) {
				int length = code.substring(code.indexOf("*")).length() / 2;
				if (length == 3) {
					prix = 6 * 3;
				} else {
					prix = 6;
				}
			} else if (wanfa.equals("111")) {
				int length = code.length() / 2;
				if (length == 3) {
					prix = 6 * 3;
				} else {
					prix = 6;
				}
			} else if (wanfa.equals("122")) {
				Integer dmCount = code.split("\\*")[0].length() / 2;
				Integer tmCount = code.split("\\*")[1].length() / 2;
				if (dmCount == 1) {
					tmCount = tmCount > 4 ? 4 : tmCount;
				} else {
					tmCount = tmCount > 3 ? 3 : tmCount;
				}
				long zhushu = getDanTuoZhushu(tmCount,3,dmCount);
				prix = 19 * (Integer.parseInt(zhushu+""));
			} else if (wanfa.equals("103")) {
				if (code.substring(code.indexOf("*")).length() / 2 == 4) {
					prix = 19 * 4;
				} else {
					prix = 19;
				}
			} else if (wanfa.equals("112")) {
				if (code.length() / 2 == 4) {
					prix = 19 * 4;
				} else {
					prix = 19;
				}
			} else if (wanfa.equals("123")) {
				Integer dmCount = code.split("\\*")[0].length() / 2;
				Integer tmCount = code.split("\\*")[1].length() / 2;
				if (dmCount == 1) {
					tmCount = tmCount > 4 ? 4 : tmCount;
				} else if (dmCount == 2) {
					tmCount = tmCount > 3 ? 3 : tmCount;
				} else {
					tmCount = tmCount > 2 ? 2 : tmCount;
				}
				long zhushu = getDanTuoZhushu(tmCount,4,dmCount);
				prix = 78 * (Integer.parseInt(zhushu+""));
			} else if (wanfa.equals("104")) {
				int length = code.substring(code.indexOf("*")).length() / 2;
				if (length > 4) {
					prix = 78 * 5;
				} else {
					prix = 78;
				}
			} else if (wanfa.equals("113")) {
				int length = code.length() / 2;
				if (length > 4) {
					prix = 78 * 5;
				} else {
					prix = 78;
				}
			} else if (wanfa.equals("114")||wanfa.equals("105")||wanfa.equals("124")) {
				prix = 540;
			} else if (wanfa.equals("125")) {
				Integer dmCount = code.split("\\*")[0].length() / 2;
				Integer tmCount = code.split("\\*")[1].length() / 2;
				prix = 90 * (tmCount - (5 - dmCount));
			} else if (wanfa.equals("106")) {
				int length = code.substring(code.indexOf("*")).length() / 2;
				prix = 90 * (length - 5);
			} else if (wanfa.equals("115")) {
				int length = code.length() / 2;
				prix = 90 * (length - 5);
			} else if (wanfa.equals("126")) {
				Integer dmCount = code.split("\\*")[0].length() / 2;
				Integer tmCount = code.split("\\*")[1].length() / 2;
				if (dmCount <= 5) {
					if (((tmCount + dmCount) - 5) == 6) {
						prix = 26 * 15;
					}
					if (((tmCount + dmCount) - 5) == 5) {
						prix = 26 * 10;
					}
					if (((tmCount + dmCount) - 5) == 4) {
						prix = 26 * 6;
					}
					if (((tmCount + dmCount) - 5) == 3) {
						prix = 26 * 3;
					}
				} else {
					prix = 26 * (tmCount);
				}
			} else if (wanfa.equals("107")) {
				int length = code.substring(code.indexOf("*")).length() / 2;
				if ((length - 5) == 4) {
					prix = 26 * 6;
				} else if ((length - 5) == 3) {
					prix = 26 * 3;
				} else {
					prix = 26;
				}
			} else if (wanfa.equals("116")) {
				int length = code.length() / 2;
				if ((length - 5) == 4) {
					prix = 26 * 6;
				} else if ((length - 5) == 3) {
					prix = 26 * 3;
				} else {
					prix = 26;
				}
			} else if (wanfa.equals("117")) {
				if (code.indexOf("*") > -1) {
					Integer dmCount = code.split("\\*")[0].length() / 2;
					Integer tmCount = code.split("\\*")[1].length() / 2;
					if ((dmCount) <= 5) {
						prix = 9 * (tmCount - (5 - dmCount));
					} else {
						prix = 9 * (tmCount);
					}
				} else {
					prix = 9;
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.GDOO_F.lotNo())) { //广东11选五
			//S|R2|0507_1_200_200
			String[] strings = betCode.split("\\|");
			wanfa = strings[1]; //子玩法
			code = strings[2]; //实际注码
			if (wanfa.equals("R1")) {
				prix = 13;
			} else if (wanfa.equals("R2")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String tuoMa = split[1]; //拖码
					List<String> tuoMaList = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2);
					if (tuoMaList.size()<5) {
						prix = 6 * tuoMaList.size();
					} else {
						prix = 6 * 4;
					}
				} else {
					List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
					if (codeList.size() == 3) {
						prix = 6 * 3;
					} else {
						prix = 6;
					}
				}
			} else if (wanfa.equals("R3")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String danMa = split[0]; //胆码
					String tuoMa = split[1]; //拖码
					Integer dmCount = LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2).size();
					Integer tmCount = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2).size();
					
					if (dmCount == 1) {
						tmCount = tmCount > 4 ? 4 : tmCount;
					} else {
						tmCount = tmCount > 3 ? 3 : tmCount;
					}
					long zhushu = getDanTuoZhushu(tmCount,3,dmCount);
					prix = 19 * (Integer.valueOf(zhushu+""));
				} else {
					List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
					if (codeList.size() == 4) {
						prix = 19 * 4;
					} else {
						prix = 19;
					}
				}
			} else if (wanfa.equals("R4")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String danMa = split[0]; //胆码
					String tuoMa = split[1]; //拖码
					Integer dmCount = LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2).size();
					Integer tmCount = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2).size();
					
					if (dmCount == 1) {
						tmCount = tmCount > 4 ? 4 : tmCount;
					} else if (dmCount == 2) {
						tmCount = tmCount > 3 ? 3 : tmCount;
					} else {
						tmCount = tmCount > 2 ? 2 : tmCount;
					}
					long zhushu = getDanTuoZhushu(tmCount,4,dmCount);
					prix = 78 * (Integer.valueOf(zhushu+""));
				} else {
					List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
					if (codeList.size() > 4) {
						prix = 78 * 5;
					} else {
						prix = 78;
					}
				}
			} else if (wanfa.equals("R5")) {
				prix = 540;
			} else if (wanfa.equals("R6")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String danMa = split[0]; //胆码
					String tuoMa = split[1]; //拖码
					Integer dmCount = LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2).size();
					Integer tmCount = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2).size();
					
					prix = 90 * (tmCount - (5 - dmCount));
				} else {
					List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
					prix = 90 * (codeList.size() - 5);
				}
			} else if (wanfa.equals("R7")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String danMa = split[0]; //胆码
					String tuoMa = split[1]; //拖码
					Integer dmCount = LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2).size();
					Integer tmCount = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2).size();
					
					if (dmCount <= 5) {
						if (((tmCount + dmCount) - 5) == 6) {
							prix = 26 * 15;
						}
						if (((tmCount + dmCount) - 5) == 5) {
							prix = 26 * 10;
						}
						if (((tmCount + dmCount) - 5) == 4) {
							prix = 26 * 6;
						}
						if (((tmCount + dmCount) - 5) == 3) {
							prix = 26 * 3;
						}
					} else {
						prix = 26 * (tmCount);
					}
				} else {
					List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(code, 2);
					if ((codeList.size() - 5) == 4) {
						prix = 26 * 6;
					} else if ((codeList.size() - 5) == 3) {
						prix = 26 * 3;
					} else {
						prix = 26;
					}
				}
			} else if (wanfa.equals("R8")) {
				if (code.indexOf("-") > -1) {
					String[] split = code.split("-");
					String danMa = split[0]; //胆码
					String tuoMa = split[1]; //拖码
					Integer dmCount = LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2).size();
					Integer tmCount = LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2).size();
					
					if ((dmCount) <= 5) {
						prix = 9 * (tmCount - (5 - dmCount));
					} else {
						prix = 9 * (tmCount);
					}
				} else {
					prix = 9;
				}
			} else if (wanfa.equals("Q2")) {
				prix = 130;
			} else if (wanfa.equals("Q3")) {
				prix = 1170;
			} else if (wanfa.equals("Z2")) {
				prix = 65;
			} else if (wanfa.equals("Z3")) {
				prix = 195;
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.GDH_T.lotNo())) { //广东快乐十分
			//S|R5|0309101218_1_200_200
			String[] strings = betCode.split("\\|");
			//String touZhuType = strings[0]; //投注方式
			wanfa = strings[1]; //子玩法
			code = strings[2]; //实际注码
			
			if (wanfa.equals("S1")) { //选一数投
				prix = 25;
			} else if (wanfa.equals("H1")) { //选一红投
				prix = 5;
			} else if (wanfa.equals("R2")) { //任选二
				prix = 8;
			} else if (wanfa.equals("R3")) { //任选三
				prix = 24;
			} else if (wanfa.equals("R4")) { //任选四
				prix = 80;
			} else if (wanfa.equals("R5")) { //任选五
				prix = 320;
			} else if (wanfa.equals("Q2")) { //选二连直
				prix = 62;
			} else if (wanfa.equals("Q3")) { //直选前三
				prix = 8000;
			} else if (wanfa.equals("Z2")) { //选二连组
				prix = 31;
			} else if (wanfa.equals("Z3")) { //组选前三
				prix = 1300;
			}
		}
		return prix;
	}
	
	/**
	 * 计算胆拖注数
	 * @param tmCount
	 * @param wanfa
	 * @param dmCount
	 * @return
	 */
	public static long getDanTuoZhushu(int tmCount, int wanfa, int dmCount) {
		if (dmCount == 0 || tmCount == 0) {
			return 0;
		}
		return LotteryAlgorithmUtil.zuhe(wanfa-dmCount, tmCount);
	} 
	
}
