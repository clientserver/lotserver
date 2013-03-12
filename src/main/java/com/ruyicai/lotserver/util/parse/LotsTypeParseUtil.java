package com.ruyicai.lotserver.util.parse;

import org.apache.log4j.Logger;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;

/**
 * 订单类型解析(供合买使用)
 *  0-单式上传，1-复式，2-胆拖，3-多方案
 * @author Administrator
 *
 */
public class LotsTypeParseUtil {
	
	private static Logger logger = Logger.getLogger(LotsTypeParseUtil.class);

	/**
	 * 解析订单类型
	 * @param lotNo
	 * @param betCode
	 * @param isSellWays
	 * @return
	 */
	public static String parse(String lotNo, String betCode, String isSellWays) {
		String lotsType = "0";
		try {
			if (!Tools.isEmpty(isSellWays)&&isSellWays.trim().equals("1")) { //多玩法
				lotsType = parseByOrderInfo(lotNo, betCode);
			} else { //单玩法
				lotsType = parseByBetCode(lotNo, betCode);	
			}
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
		return lotsType;
	}
	
	/**
	 * 根据betCode解析订单类型
	 * @param lotNo
	 * @param betCode
	 * @return
	 */
	public static String parseByBetCode(String lotNo, String betCode) {
		String lotsType = "0";
		if (lotNo!=null&&lotNo.equals(LotType.QLC.lotNo())) { //七乐彩
			String wanfa = betCode.substring(0, 2); //玩法
			if (wanfa.equals("00")) { //单式
				lotsType = "0";
			} else if (wanfa.equals("10")) { //复式
				lotsType = "1";
			} else if (wanfa.equals("20")) { //胆拖
				lotsType = "2";
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
			String wanfa = betCode.substring(0, 2); //玩法
			if (wanfa.equals("00")||wanfa.equals("01")||wanfa.equals("02")) { //直选单式、 组3单式、 组6单式 
				lotsType = "0";
			} else if (wanfa.equals("10")||wanfa.equals("11")||wanfa.equals("12")
					||wanfa.equals("20")||wanfa.equals("31")||wanfa.equals("32")||wanfa.equals("34")) {
				//直选和值（单选全包点）、组3和值（组3全包点）、组六和值（组6全包点）、位选投注（3D直选复式）、单选按位包号
				//组3复式、 组6复式、 单选单复式(3D直选包号)
				lotsType = "1";
			} else if (wanfa.equals("54")) { //胆拖
				lotsType = "2";
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.SSQ.lotNo())) { //双色球
			String wanfa = betCode.substring(0, 2); //玩法
			if (wanfa.equals("00")) { //红单蓝单
				lotsType = "0";
			} else if (wanfa.equals("10")||wanfa.equals("20")||wanfa.equals("30")) { //红复蓝单 红单蓝复 红复蓝复
				lotsType = "1";
			} else if (wanfa.equals("40")||wanfa.equals("50")) { //红拖蓝单、红拖蓝复
				lotsType = "2";
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //大乐透
			if (betCode.indexOf(";") > -1) {
				String[] split = betCode.split(";");
				betCode = split[0];
			}
			if (betCode.indexOf("-") > -1) { //单式或复式或胆拖
				if (betCode.indexOf("$") > -1) { //胆拖
					lotsType = "2";
				} else {
					String[] qianHou = betCode.split("-");
					String[] qian = qianHou[0].split(" ");
					String[] hou = qianHou[1].split(" ");
					if (qian.length==5 && hou.length==2) { //单式
						lotsType = "0";
					} else { //复式
						lotsType = "1";
					}
				}
			} else { //生肖乐
				String[] split = betCode.split(" ");
				if (split.length == 2) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
			String[] split = betCode.split("\\|"); //玩法
			String wanfa = split[0];
			if (wanfa.equals("1")) { //直选
				boolean isD = true;
				String[] split2 = split[1].split(",");
				for (String code : split2) {
					if (code.length()>1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			} else if (wanfa.equals("6")) { //组3、组6
				lotsType = "0";
			} else if (wanfa.equals("S1")||wanfa.equals("S9")||wanfa.equals("S3")
					||wanfa.equals("S6")||wanfa.equals("F3")||wanfa.equals("F6")) { 
				//直选和值、组选和值、组3和值、组6和值、组3包号、组6包号
				lotsType = "1";
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLW.lotNo())) { //排列五
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length()>1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.QXC.lotNo())) { //七星彩
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length() > 1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
			String wanfa = betCode.substring(0, betCode.indexOf("@"));
			if (wanfa.equals("0")) { //单式
				lotsType = "0";
			} else if (wanfa.equals("1")) { //复式
				lotsType = "1";
			} else if (wanfa.equals("2")) { //胆拖
				lotsType = "2";
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_SFC.lotNo())) { //足彩胜负彩
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length() > 1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_RX9.lotNo())) { //足彩任选9
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length() > 1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())) { //足彩进球彩
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length() > 1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())) { //足彩半全场
			if (betCode.indexOf(",") > -1) {
				boolean isD = true;
				String[] split = betCode.split(",");
				for (String code : split) {
					if (code.length() > 1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					lotsType = "0";
				} else { //复式
					lotsType = "1";
				}
			}
		} else if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
			String wanfa = betCode.substring(0, betCode.indexOf("@"));
			if (wanfa.equals("500")) { //单关
				lotsType = "0";
			} else { //过关
				lotsType = "1";
			}
		}
		return lotsType;
	}
	
	/**
	 * 根据orderInfo解析订单类型
	 * @param lotNo
	 * @param orderInfo
	 * @return
	 */
	public static String parseByOrderInfo(String lotNo, String orderInfo) {
		String lotsType = "0";
		
		JSONObject duoWanFaObject = isDuoWanFa(lotNo, orderInfo);
		boolean isDuoWanFa = duoWanFaObject.getBoolean("isDuoWanFa"); //是否多玩法
		String wanfa = duoWanFaObject.getString("play"); //玩法
		
		if (isDuoWanFa) { //多玩法
			lotsType = "3";
		} else {
			if (lotNo!=null&&lotNo.equals(LotType.QLC.lotNo())) { //七乐彩
				if (wanfa.equals("00")) { //单式
					lotsType = "0";
				} else if (wanfa.equals("10")) { //复式
					lotsType = "1";
				} else if (wanfa.equals("20")) { //胆拖
					lotsType = "2";
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
				if (wanfa.equals("00")||wanfa.equals("01")||wanfa.equals("02")) { //直选单式、 组3单式、 组6单式 
					lotsType = "0";
				} else if (wanfa.equals("10")||wanfa.equals("11")||wanfa.equals("12")
						||wanfa.equals("20")||wanfa.equals("31")||wanfa.equals("32")||wanfa.equals("34")) {
					//直选和值（单选全包点）、组3和值（组3全包点）、组六和值（组6全包点）、位选投注（3D直选复式）、单选按位包号
					//组3复式、 组6复式、 单选单复式(3D直选包号)
					lotsType = "1";
				} else if (wanfa.equals("54")) { //胆拖
					lotsType = "2";
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.SSQ.lotNo())) { //双色球
				if (wanfa.equals("00")) { //红单蓝单
					lotsType = "0";
				} else if (wanfa.equals("10")||wanfa.equals("20")||wanfa.equals("30")) { //红复蓝单 红单蓝复 红复蓝复
					lotsType = "1";
				} else if (wanfa.equals("40")||wanfa.equals("50")) { //红拖蓝单、红拖蓝复
					lotsType = "2";
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //大乐透
				if (wanfa.equals("DS")||wanfa.equals("SXL_DS")) { //单式
					lotsType = "0";
				} else if (wanfa.equals("FS")||wanfa.equals("SXL_FS")) { //复式
					lotsType = "1";
				} else if (wanfa.equals("DT")) { //胆拖
					lotsType = "2";
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
				if (wanfa.equals("1")) { //直选
					boolean hasDS = false;
					boolean hasFS = false;
					String[] orderInfos = orderInfo.split("!");
					for (String info : orderInfos) {
						String[] infos = info.split("_");
						String code = infos[0].split("\\|")[1];
					
						String[] codes = code.split(",");
						if (codes!=null&&codes.length==3) {
							if (codes[0].length()==1&&codes[1].length()==1&&codes[2].length()==1) {
								hasDS = true;
							} else {
								hasFS = true;
							}
						}
					}
					if (hasDS&&hasFS) { //多玩法
						lotsType = "3";
					} else if (!hasDS&&hasFS) { //复式
						lotsType = "1";
					} else { //单式
						lotsType = "0";
					}
				} else if (wanfa.equals("6")) { //组3、组6
					lotsType = "0";
				} else if (wanfa.equals("S1")||wanfa.equals("S9")||wanfa.equals("S3")
						||wanfa.equals("S6")||wanfa.equals("F3")||wanfa.equals("F6")) { 
					//直选和值、组选和值、组3和值、组6和值、组3包号、组6包号
					lotsType = "1";
				}
			} else if (lotNo!=null&&(lotNo.equals(LotType.PLW.lotNo())
					||lotNo.equals(LotType.QXC.lotNo())||LotTypeUtil.isZuCai(lotNo))) { //排列五、七星彩、足彩
				if (wanfa.equals("DS")) { //单式
					lotsType = "0";
				} else if (wanfa.equals("FS")) { //复式
					lotsType = "1";
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
				if (wanfa.equals("0")) { //单式
					lotsType = "0";
				} else if (wanfa.equals("1")) { //复式
					lotsType = "1";
				} else if (wanfa.equals("2")) { //胆拖
					lotsType = "2";
				}
			} else if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
				if (wanfa.equals("500")) { //单关
					lotsType = "0";
				} else { //过关
					lotsType = "1";
				}
			}
		}
		
		return lotsType;
	}
	
	/**
	 * 判断是否多玩法
	 * @param lotNo
	 * @param orderInfo
	 * @return
	 */
	public static JSONObject isDuoWanFa(String lotNo, String orderInfo) {
		JSONObject resultObject = new JSONObject();
		
		boolean isDuoWanFa = false; //是否多玩法
		String firstWanfa = ""; //第一个玩法
		if (lotNo!=null&&(lotNo.equals(LotType.QLC.lotNo())||lotNo.equals(LotType.DDD.lotNo())
				||lotNo.equals(LotType.SSQ.lotNo()))) { //福彩(七乐彩、福彩3D、双色球)
			JSONObject duoWanFaForFCObject = isDuoWanFaForFC(orderInfo);
			isDuoWanFa = duoWanFaForFCObject.getBoolean("isDuoWanFa");
			firstWanfa = duoWanFaForFCObject.getString("play");
		} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //大乐透
			int index = 0;
			String[] orderInfos = orderInfo.split("!");
			for (String info : orderInfos) {
				String[] infos = info.split("_");
				String code = infos[0];
				
				String wanfa = "";
				if (code.indexOf("-") > -1) { //单式或复式或胆拖
					if (code.indexOf("$") > -1) { //胆拖
						wanfa = "DT";
					} else {
						String[] qianHou = code.split("-");
						String[] qian = qianHou[0].split(" ");
						String[] hou = qianHou[1].split(" ");
						if (qian.length==5 && hou.length==2) { //单式
							wanfa = "DS";
						} else { //复式
							wanfa = "FS";
						}
					}
				} else { //生肖乐
					String[] str = code.split(" ");
					if (str.length == 2) { //单式
						wanfa = "SXL_DS";
					} else { //复式
						wanfa = "SXL_FS";
					}
				}
				if (index==0) {
					firstWanfa = wanfa;
				} else {
					if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
						isDuoWanFa = true;
					}
				}
				index++;
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
			int index = 0;
			String[] orderInfos = orderInfo.split("!");
			for (String info : orderInfos) {
				String[] infos = info.split("_");
				String code = infos[0];
				
				String[] codes = code.split("\\|");
				String wanfa = codes[0]; //玩法
				if (index==0) {
					firstWanfa = wanfa;
				} else {
					if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
						isDuoWanFa = true;
					}
				}
				index++;
			}
		} else if (lotNo!=null&&(lotNo.equals(LotType.PLW.lotNo())
				||lotNo.equals(LotType.QXC.lotNo())||LotTypeUtil.isZuCai(lotNo))) { //排列五、七星彩、足彩
			JSONObject duoWanFaForZCObject = isDuoWanFaForZC(orderInfo);
			isDuoWanFa = duoWanFaForZCObject.getBoolean("isDuoWanFa");
			firstWanfa = duoWanFaForZCObject.getString("play");
		} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
			int index = 0;
			String[] orderInfos = orderInfo.split("!");
			for (String info : orderInfos) {
				String[] infos = info.split("_");
				String code = infos[0];
				
				String wanfa = code.substring(0, code.indexOf("@"));
				if (index==0) {
					firstWanfa = wanfa;
				} else {
					if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
						isDuoWanFa = true;
					}
				}
				index++;
			}
		} else if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
			int index = 0;
			String[] orderInfos = orderInfo.split("!");
			for (String info : orderInfos) {
				String[] infos = info.split("_");
				String code = infos[0];
			
				String wanfa = code.substring(0, code.indexOf("@"));
				if (index==0) {
					firstWanfa = wanfa;
				} else {
					if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
						isDuoWanFa = true;
					}
				}
				index++;
			}
		}
		
		resultObject.put("isDuoWanFa", isDuoWanFa);
		resultObject.put("play", firstWanfa);
		return resultObject;
	}
	
	/**
	 * 判断福彩是否多玩法
	 * @param orderInfo
	 * @return
	 */
	public static JSONObject isDuoWanFaForFC(String orderInfo) {
		JSONObject resultObject = new JSONObject();
		
		boolean isDuoWanFa = false; //是否多玩法
		String firstWanfa = ""; //第一个玩法
		
		int index = 0;
		String[] orderInfos = orderInfo.split("!");
		for (String info : orderInfos) {
			String[] infos = info.split("_");
			String code = infos[0];
			String wanfa = code.substring(0, 2); //玩法
			if (index==0) {
				firstWanfa = wanfa;
			} else {
				if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
					isDuoWanFa = true;
				}
			}
			index++;
		}
		
		resultObject.put("isDuoWanFa", isDuoWanFa);
		resultObject.put("play", firstWanfa);
		return resultObject;
	}
	
	/**
	 * 判断足彩是否多玩法
	 * @param orderInfo
	 * @return
	 */
	public static JSONObject isDuoWanFaForZC(String orderInfo) {
		JSONObject resultObject = new JSONObject();
		
		boolean isDuoWanFa = false; //是否多玩法
		String firstWanfa = ""; //第一个玩法
		
		int index = 0;
		String[] orderInfos = orderInfo.split("!");
		for (String info : orderInfos) {
			String[] infos = info.split("_");
			String code = infos[0];
		
			String wanfa = "";
			if (code.indexOf(",") > -1) {
				boolean isD = true;
				String[] codes = code.split(",");
				for (String str : codes) {
					if (str.length()>1) {
						isD = false;
						break;
					}
				}
				if (isD) { //单式
					wanfa = "DS";
				} else { //复式
					wanfa = "FS";
				}
			}
			if (index==0) {
				firstWanfa = wanfa;
			} else {
				if (!firstWanfa.equals(wanfa)) { //如果其中一个玩法与第一个玩法不一样就是多玩法
					isDuoWanFa = true;
				}
			}
			index++;
		}
		
		resultObject.put("isDuoWanFa", isDuoWanFa);
		resultObject.put("play", firstWanfa);
		return resultObject;
	}
	
}
