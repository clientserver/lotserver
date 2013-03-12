package com.ruyicai.lotserver.util.parse;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 注码解析
 * @author Administrator
 *
 */
public class BetCodeParseUtil {

	/**
	 * 解析注码
	 * @param lotNo 彩种
	 * @param betCode 数据库的注码
	 * @return
	 */
	public static JSONArray parseBetCode(String lotNo, String betCode) {
		JSONArray resultArray = new JSONArray();
		if (lotNo!=null&&lotNo.equals(LotType.QLC.lotNo())) { //七乐彩
			JSONObject object = new JSONObject();
			object.put("lotName", "七乐彩");
			if (betCode.length() > 4) {
				String[] betCodes = new String[]{""};
				if (betCode.indexOf("!") > -1) {
					betCodes = betCode.split("!");
				} else {
					betCodes = betCode.split("\\^");
				}
				for(String code : betCodes) {
					object.put("multiple", code.substring(2, 4)); //倍数
					if (code.substring(0, 2).equals("00")) { //单式
						object.put("play", "单式");
						if (betCode.indexOf("!") > -1) {
							String bet_code = StringUtil.joinStringArrayWithCharacter(
									LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.indexOf("^")), 2), ",");
							object.put("betCode", bet_code);
						} else {
							String bet_code = StringUtil.joinStringArrayWithCharacter(
									LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4), 2), ",");
							object.put("betCode", bet_code);
						}
					} else if (code.substring(0, 2).equals("10")) { //复式
						object.put("play", "复式");
						String bet_code = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(5), 2), ",");
						object.put("betCode", bet_code);
					} else if (code.substring(0, 2).equals("20")) { //胆拖
						object.put("play", "胆拖");
						String danMa = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.indexOf("*")), 2), ","); // 胆码
						String tuoMa = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf("*")+1), 2), ","); // 拖码
						object.put("betCode", "胆码:"+danMa+"\n拖码:"+tuoMa);
					} else {
						object.put("play", "未知");
						object.put("betCode", code);
					}
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
			JSONObject object = new JSONObject();
			object.put("lotName", "福彩3D");
			String[] betCodes = betCode.split("!");
			String play ="0"; //投注类型
			for (String code : betCodes) {
				if (code.substring(0, 2).equals("20")) { //直选复式 20020102^0106^0100^
					object.put("multiple", betCode.substring(2, 4)); //倍数
					object.put("play", "直选复式");
					String[] str = code.split("\\^");
					String bai = LotteryAlgorithmUtil.removeZero3D(str[0].substring(6));
					String shi = LotteryAlgorithmUtil.removeZero3D(str[1].substring(2));
					String ge = LotteryAlgorithmUtil.removeZero3D(str[2].substring(2));
					object.put("betCode", "百位:"+bai+" 十位:"+shi+" 个位:"+ge);
					resultArray.add(object);
					continue;
				} else if (code.substring(0, 2).equals("00")) { //单选单式
					play = "DXD";
					object.put("play", "单选单式");
				} else if (code.substring(0, 2).equals("01")) { //组3单式
					play = "Z3D";
					object.put("play", "组3单式");
				} else if (code.substring(0, 2).equals("02")) { //组6单式
					play = "Z6D";
					object.put("play", "组6单式");
				} else if (code.substring(0, 2).equals("31")) { //组3复式
					play = "Z3F";
					object.put("play", "组3复式");
				} else if (code.substring(0, 2).equals("32")) { //组6复式
					play = "Z6F";
					object.put("play", "组6复式");
				} else if (code.substring(0, 2).equals("34")) { //单选单复式
					play = "DXDF";
					object.put("play", "单选单复式");
				}else if (code.substring(0, 2).equals("54")) { //胆拖
					play = "DT";
					object.put("play", "胆拖");
				} else if (code.substring(0, 2).equals("10")) { //直选和值
					play = "ZXHZ";
					object.put("play", "直选和值");
				} else if (code.substring(0, 2).equals("11")) { //组3和值
					play = "Z3HZ";
					object.put("play", "组3和值");
				} else if (code.substring(0, 2).equals("12")) { //组6和值
					play = "Z6HZ";
					object.put("play", "组6和值");
				}
				if (play.trim().equals("DXD") || play.trim().equals("Z3D") || play.trim().equals("Z6D")) { //单式
					String bet_code = StringUtil.joinStringArrayWithCharacter(
							LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.length()), 2), ",");
					object.put("betCode", bet_code);
				} else if (play.trim().equals("Z3F") || play.trim().equals("Z6F") || play.trim().equals("DXDF")) { //复式
					String bet_code = StringUtil.joinStringArrayWithCharacter(
							LotteryAlgorithmUtil.getStringArrayFromString(code.substring(6), 2), ",");
					object.put("betCode", bet_code);
				} else if (play.trim().equals("DT")) {
					String danMa = StringUtil.joinStringArrayWithCharacter(
							LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.indexOf("*")), 2), ","); //胆码
					String tuoMa = StringUtil.joinStringArrayWithCharacter(
							LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf("*")+1), 2), ","); //拖码
					object.put("betCode", "胆码:"+danMa+"拖码:"+tuoMa);
				} else if (play.trim().equals("ZXHZ") || play.trim().equals("Z3HZ") || play.trim().equals("Z6HZ")) {
					String bet_code = StringUtil.joinStringArrayWithCharacter(
							LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.length()), 2), ",");
					object.put("betCode", bet_code);
				} else {
					object.put("play", "未知");
					object.put("betCode", code);
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.SSQ.lotNo())) { //双色球
			JSONObject object = new JSONObject();
			object.put("lotName", "双色球");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split("\\^");
			}
			for(String code : betCodes) {
				if (code.length() > 4) {
					String play = "0"; //投注类型
					object.put("multiple", code.substring(2, 4)); //倍数
					if (code.trim().substring(0, 2).equals("00")) {
						play = "DS"; //单式
						object.put("play", "单式");
					} else if (code.trim().substring(0, 2).equals("10")	|| code.trim().substring(0, 2).equals("20") || code.trim().substring(0, 2).equals("30")) {
						play = "FS"; //复式
						object.put("play", "复式");
					} else if (code.trim().substring(0, 2).equals("40")	|| code.trim().substring(0, 2).equals("50")) { //胆拖
						play = "DT";
						object.put("play", "胆拖");
					}
					if (play.trim().equals("DS")) { // 单式
						String red = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.indexOf('~')), 2), ",");
						if (betCode.indexOf("!") > -1) {
							String blue = code.substring(code.indexOf('~')+1, code.indexOf("^"));
							object.put("betCode", "红球:"+red+"蓝球:"+blue); //解析后的注码
						} else {
							String blue = code.substring(code.indexOf('~')+1);
							object.put("betCode", "红球:"+red+"蓝球:"+blue); //解析后的注码
						}
					} else if(play.trim().equals("FS")) {
						String red = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf('*')+1, code.indexOf('~')), 2), ",");
						String blue = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf('~')+1), 2), ",");
						object.put("betCode", "红球:\n"+red+"蓝球:\n"+blue); //解析后的注码
					} else if (play.trim().equals("DT")) { // 胆拖
						String redDanma = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(4, code.indexOf("*")), 2), ",");
						String redTuoma = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf("*")+1, code.indexOf("~")), 2), ",");
						String blue = StringUtil.joinStringArrayWithCharacter(
								LotteryAlgorithmUtil.getStringArrayFromString(code.substring(code.indexOf("~")+1), 2), ",");
						object.put("betCode", "红球胆码:\n"+redDanma+"\n红球拖码:\n"+redTuoma+"\n蓝球:\n"+blue);
					} else {
						object.put("play", "未知");
						object.put("betCode", code);
					}
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //超级大乐透
			JSONObject object = new JSONObject();
			object.put("lotName", "超级大乐透");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (code.indexOf("-") > -1) { //单式或复式或胆拖
					if (code.indexOf("$") > -1) { //胆拖
						object.put("play", "胆拖");
						String[] qianHou = code.split("-");
						String qianQuDanMa = ""; //前驱胆码
						String qianQuTuoMa = ""; //前驱拖码
						String houQuDanMa = ""; //后驱胆码
						String houQuTuoMa = ""; //后驱拖码
						//前区
						if (qianHou[0].indexOf("$") > -1) {
							String[] qianQu = qianHou[0].split("\\$");
							qianQuDanMa = qianQu[0].replaceAll(" ", ","); //前驱胆码
							qianQuTuoMa = qianQu[1].replaceAll(" ", ","); //前驱拖码
						} else {
							qianQuTuoMa = qianHou[0].replace(" ", ",");
						}
						//后区
						if (qianHou[1].indexOf("$") > -1) {
							String[] houQu = qianHou[1].split("\\$");
							houQuDanMa = houQu[0].replaceAll(" ", ",");
							houQuTuoMa = houQu[1].replaceAll(" ", ",");
						} else {
							houQuTuoMa = qianHou[1].replaceAll(" ", ",");
						}
						object.put("betCode", "前区胆码:"+qianQuDanMa+"\n前区拖码:"+qianQuTuoMa+"\n后区胆码:"+houQuDanMa+"\n后区拖码:"+houQuTuoMa);
					} else { //单式或复式
						String[] qianHou = code.split("-");
						String[] qian = qianHou[0].split(" ");
						String[] hou = qianHou[1].split(" ");
						if (qian.length==5 && hou.length==2) {
							object.put("play", "单式");
						} else {
							object.put("play", "复式");
						}
						object.put("betCode", qianHou[0].replaceAll(" ", ",")+"+"+qianHou[1].replaceAll(" ", ","));
					}
				} else { //生肖乐
					String[] split = code.split(" ");
					if (split.length == 2) {
						object.put("play", "12选2单式");
					} else {
						object.put("play", "12选2复式");
					}
					object.put("betCode", code.replaceAll(" ", ","));
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
			JSONObject object = new JSONObject();
			object.put("lotName", "排列三");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (betCode.indexOf("|") > -1) {
					String[] split = code.split("\\|");
					if (split[0].equals("1")) { //直选
						object.put("play", "直选");
					} else if (split[0].equals("6")) { //组选
						object.put("play", "组选");
					} else if (split[0].equals("S1")) { //直选和值
						object.put("play", "直选和值");
					} else if (split[0].equals("S9")) { //组选和值
						object.put("play", "组选和值");
					} else if (split[0].equals("S3")) { //组3和值
						object.put("play", "组3和值");
					} else if (split[0].equals("S6")) { //组6和值
						object.put("play", "组6和值");
					} else if (split[0].equals("F3")) { //组3包号
						object.put("play", "组3包号");
					} else if (split[0].equals("F6")) { //组6包号
						object.put("play", "组6包号");
					} else {
						object.put("play", "未知");
					}
					object.put("betCode", split[1]);
				} else {
					object.put("play", "未知");
					object.put("betCode", code);
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLW.lotNo())) { //排列五
			JSONObject object = new JSONObject();
			object.put("lotName", "排列五");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (code.indexOf(",") > -1) {
					boolean isD = true;
					String[] split = code.split(",");
					for (int i = 0; i < split.length; i++) {
						if (split[i].length() > 1) {
							isD = false;
							break;
						}
					}
					if (isD) {
						object.put("play", "单式");
					} else {
						object.put("play", "复式");
					}
				} else {
					object.put("play", "单式");
				}
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.QXC.lotNo())) { //七星彩
			JSONObject object = new JSONObject();
			object.put("lotName", "七星彩");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (code.indexOf(",") > -1) {
					boolean isD = true;
					String[] split = code.split(",");
					for (int i = 0; i < split.length; i++) {
						if (split[i].length() > 1) {
							isD = false;
							break;
						}
					}
					if (isD) {
						object.put("play", "单式");
					} else {
						object.put("play", "复式");
					}
				} else {
					object.put("play", "单式");
				}
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
			JSONObject object = new JSONObject();
			object.put("lotName", "22选5");
			String[] split = betCode.split("!");
			for (String string : split) {
				String[] split2 = string.split("@");
				if (split2[0].equals("0")) { //单式
					object.put("play", "单式");
					String code = split2[1].substring(0, split2[1].length()-1);
					object.put("betCode", StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ","));
				} else if (split2[0].equals("1")) { //复式
					object.put("play", "复式");
					String code = split2[1].substring(0, split2[1].length()-1);
					object.put("betCode", StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(code, 2), ","));
				} else if (split2[0].equals("2")) { //胆拖
					object.put("play", "胆拖");
					String danMa = split2[1].substring(0, split2[1].indexOf("*"));
					String tuoMa = split2[1].substring(split2[1].indexOf("*")+1, split2[1].length()-1);
					object.put("betCode", "胆码:"+StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(danMa, 2), ",")
										+"\n拖码:"+StringUtil.joinStringArrayWithCharacter(LotteryAlgorithmUtil.getStringArrayFromString(tuoMa, 2), ","));
				} else {
					object.put("play", "未知");
					object.put("betCode", betCode);
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.SSC.lotNo())) { //时时彩
			JSONObject object = new JSONObject();
			object.put("lotName", "时时彩");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (betCode.indexOf("|") > -1) {
					String[] split = code.split("\\|");
					if (split[0].equals("5D")) { //五星
						object.put("play", "五星");
					} else if (split[0].equals("3D")) { //三星
						object.put("play", "三星");
					} else if (split[0].equals("2D")) { //二星
						object.put("play", "二星");
					} else if (split[0].equals("1D")) { //一星
						object.put("play", "一星");
					} else if (split[0].equals("5F")) { //五星复选
						object.put("play", "五星复选");
					} else if (split[0].equals("5T")) { //五星通选
						object.put("play", "五星通选");
					} else if (split[0].equals("3F")) { //三星复选
						object.put("play", "三星复选");
					} else if (split[0].equals("2F")) { //二星复选
						object.put("play", "二星复选");
					} else if (split[0].equals("H2")) { //二星和值
						object.put("play", "二星和值");
					} else if (split[0].equals("S2")) { //二星组选和值
						object.put("play", "二星组选和值");
					} else if (split[0].equals("DD")) { //大小单双
						object.put("play", "大小单双");
						String shi = split[1].substring(0, 1); //大小
						String ge = split[1].substring(1, 2); //单双
						String shiStr = "";
						if (shi.equals("2")) {
							shiStr = "大";
						} else if (shi.equals("1")) {
							shiStr = "小";
						} else if (shi.equals("5")) {
							shiStr = "单";
						} else if (shi.equals("4")) {
							shiStr = "双";
						}
						String geStr = "";
						if (ge.equals("2")) {
							geStr = "大";
						} else if (ge.equals("1")) {
							geStr = "小";
						} else if (ge.equals("5")) {
							geStr = "单";
						} else if (ge.equals("4")) {
							geStr = "双";
						}
						object.put("betCode", "十位:"+shiStr+"|个位:"+geStr);
						resultArray.add(object);
						continue;
					} else if (split[0].equals("Z2")) { //二星组选
						object.put("play", "二星组选");
					} else if (split[0].equals("F2")) { //二星组选复式
						object.put("play", "二星组选复式");
					} else {
						object.put("play", "未知");
					}
					object.put("betCode", split[1]);
				} else {
					object.put("play", "未知");
					object.put("betCode", code);
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_F.lotNo())) { //江西11选5
			JSONObject object = new JSONObject();
			object.put("lotName", "江西11选5");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				if (code.indexOf("$") > -1) { //胆拖
					if (code.indexOf("|") > -1) {
						String[] split = code.split("\\|");
						if (split[0].equals("R2")) { //任选二胆拖
							object.put("play", "任选二胆拖");
						} else if (split[0].equals("R3")) { //任选三胆拖
							object.put("play", "任选三胆拖");
						} else if (split[0].equals("R4")) { //任选四胆拖
							object.put("play", "任选四胆拖");
						} else if (split[0].equals("R5")) { //任选五胆拖
							object.put("play", "任选五胆拖");
						} else if (split[0].equals("R6")) { //任选六胆拖
							object.put("play", "任选六胆拖");
						} else if (split[0].equals("R7")) { //任选七胆拖
							object.put("play", "任选七胆拖");
						} else if (split[0].equals("R8")) { //任选八胆拖
							object.put("play", "任选八胆拖");
						} else if (split[0].equals("Q2")) { //选前二直选胆拖
							object.put("play", "选前二直选胆拖");
						} else if (split[0].equals("Q3")) { //选前三直选胆拖
							object.put("play", "选前三直选胆拖");
						} else if (split[0].equals("Z2")) { //选前二组选胆拖
							object.put("play", "选前二组选胆拖");
						} else if (split[0].equals("Z3")) { //选前三组选胆拖
							object.put("play", "选前三组选胆拖");
						} else {
							object.put("play", "未知");
						}
						String[] danTuo = split[1].split("\\$");
						object.put("betCode", "胆码:"+danTuo[0]+" 拖码:"+danTuo[1]);
					}
				} else {
					if (code.indexOf("|") > -1) {
						String[] split = code.split("\\|");
						if (split[0].equals("R1")) { //任选一
							object.put("play", "任选一");
						} else if (split[0].equals("R2")) { //任选二
							object.put("play", "任选二");
						} else if (split[0].equals("R3")) { //任选三
							object.put("play", "任选三");
						} else if (split[0].equals("R4")) { //任选四
							object.put("play", "任选四");
						} else if (split[0].equals("R5")) { //任选五
							object.put("play", "任选五");
						} else if (split[0].equals("R6")) { //任选六
							object.put("play", "任选六");
						} else if (split[0].equals("R7")) { //任选七
							object.put("play", "任选七");
						} else if (split[0].equals("R8")) { //任选八
							object.put("play", "任选八");
						} else if (split[0].equals("Q2")) { //选前二直选
							object.put("play", "选前二直选");
						} else if (split[0].equals("Q3")) { //选前三直选
							object.put("play", "选前三直选");
						} else if (split[0].equals("Z2")) { //选前二组选
							object.put("play", "选前二组选");
						} else if (split[0].equals("Z3")) { //选前三组选
							object.put("play", "选前三组选");
						} else {
							object.put("play", "未知");
						}
						object.put("betCode", split[1]);
					}
				}
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_YDJ.lotNo())) { //11运夺金
			JSONObject object = new JSONObject();
			object.put("lotName", "11运夺金");
			String[] split1 = betCode.split("@"); //102@*02040511^
			String wanfa = split1[0]; //玩法
			if (wanfa!=null && wanfa.trim().equals("101")) { //任选一复式
				object.put("play", "任选一复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("102")) { //任选二复式
				object.put("play", "任选二复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("103")) { //任选三复式
				object.put("play", "任选三复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("104")) { //任选四复式
				object.put("play", "任选四复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("105")) { //任选五复式
				object.put("play", "任选五复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("106")) { //任选六复式
				object.put("play", "任选六复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("107")) { //任选七复式
				object.put("play", "任选七复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("108")) { //选前二组选复式
				object.put("play", "选前二组选复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("109")) { //选前三组选复式
				object.put("play", "选前三组选复式");
				object.put("betCode", convertBetCodeFor11YDJ_FS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("111")) { //任选二单式
				object.put("play", "任选二单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("112")) { //任选三单式
				object.put("play", "任选三单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("113")) { //任选四单式
				object.put("play", "任选四单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("114")) { //任选五单式
				object.put("play", "任选五单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("115")) { //任选六单式
				object.put("play", "任选六单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("116")) { //任选七单式
				object.put("play", "任选七单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("117")) { //任选八单式
				object.put("play", "任选八单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("121")) { //任选二胆拖
				object.put("play", "任选二胆拖");
				object.put("betCode", convertBetCodeFor11YDJ_DT(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("131")) { //选前二组选单式
				object.put("play", "选前二组选单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("151")) { //选前三组选单式
				object.put("play", "选前三组选单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("141")) { //选前二直选单式
				object.put("play", "选前二直选单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("142")) { //选前二直选定位复式
				object.put("play", "选前二直选定位复式");
				object.put("betCode", convertBetCodeFor11YDJ_DWFS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("161")) { //选前三直选单式
				object.put("play", "选前三直选单式");
				object.put("betCode", convertBetCodeFor11YDJ_DS(betCode));
			} else if (wanfa!=null && wanfa.trim().equals("162")) { //选前三直选定位复式
				object.put("play", "选前三直选定位复式");
				object.put("betCode", convertBetCodeFor11YDJ_DWFS(betCode));
			} else {
				object.put("play", "未知");
				object.put("betCode", split1[1]);
			} 
			resultArray.add(object);
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_SFC.lotNo())) { //足球胜负彩
			JSONObject object = new JSONObject();
			object.put("lotName", "足球胜负彩");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
				} else {
					play = "复式";
				}
				object.put("play", play);
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_RX9.lotNo())) { //足球任选9
			JSONObject object = new JSONObject();
			object.put("lotName", "足球任九场");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				String play = ""; //玩法
				if (code.indexOf("$") > -1) {
					play = "胆拖";
				} else {
					boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
					if (isDanShi) {
						play = "单式";
					} else {
						play = "复式";
					}
				}
				object.put("play", play);
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())) { //足球进球彩
			JSONObject object = new JSONObject();
			object.put("lotName", "足球进球彩");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
				} else {
					play = "复式";
				}
				object.put("play", play);
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())) { //足球半全场
			JSONObject object = new JSONObject();
			object.put("lotName", "足球半全场");
			String[] betCodes = new String[]{""};
			if (betCode.indexOf("!") > -1) {
				betCodes = betCode.split("!");
			} else {
				betCodes = betCode.split(";");
			}
			for(String code : betCodes) {
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
				} else {
					play = "复式";
				}
				object.put("play", play);
				object.put("betCode", code);
				resultArray.add(object);
			}
		} else { //未知彩种
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("lotName", "未知");
			jsonObject.put("play", "未知");
			jsonObject.put("betCode", betCode);
			resultArray.add(jsonObject);
		}
		return resultArray;
	}
	
	/**
	 * 11运夺金注码转换(单式)
	 * @param code
	 * @return
	 */
	public static String convertBetCodeFor11YDJ_DS(String betCode) {
		StringBuffer sBuffer = new StringBuffer();
		if (betCode.indexOf("!")>-1) { //分开投注(111@0203^!111@0910^!111@0204^)
			String[] split = betCode.split("\\^");
			for (String string : split) {
				if (string!=null && !string.trim().equals("")) {
					String[] split2 = string.split("@");
					String code = split2[1].substring(split2[1].indexOf("*")+1);
					sBuffer.append(LotteryAlgorithmUtil.joinStringWithBlank(code)).append("\n");
				}
			}
		} else { //合在一起投注(111@0203^0910^0204^)
			String[] split1 = betCode.split("@"); 
			String[] split = split1[1].split("\\^");
			for (String string : split) {
				if (string!=null && !string.trim().equals("")) {
					sBuffer.append(LotteryAlgorithmUtil.joinStringWithBlank(string)).append("\n");
				}
			}
		}
		String string = sBuffer.toString();
		if (string.endsWith("\n")) {
			return string.substring(0, string.length()-1);
		} else {
			return sBuffer.toString();
		}
	}
	
	/**
	 * 11运夺金注码转换(复式)
	 * @param code
	 * @return
	 */
	public static String convertBetCodeFor11YDJ_FS(String betCode) {
		StringBuffer sBuffer = new StringBuffer();
		if (betCode.indexOf("!")>-1) { //分开投注(101@*02^!101@*03^!101@*08^!101@*04^!101@*09^!101@*05^)
			String[] split = betCode.split("\\^");
			for (String string : split) {
				if (string!=null && !string.trim().equals("")) {
					String[] split2 = string.split("@");
					String code = split2[1].substring(split2[1].indexOf("*")+1);
					sBuffer.append(LotteryAlgorithmUtil.joinStringWithBlank(code)).append("\n");
				}
			}
		} else { //合在一起投注(101@*020305^)
			String[] split = betCode.split("@");
			String string = split[1].substring(split[1].indexOf("*")+1, split[1].indexOf("^"));
			sBuffer.append(LotteryAlgorithmUtil.joinStringWithBlank(string));
		}
		String string = sBuffer.toString();
		if (string.endsWith("\n")) {
			return string.substring(0, string.length()-1);
		} else {
			return sBuffer.toString();
		}
	}
	
	/**
	 * 11运夺金注码转换(定位复式)
	 * @param betCode
	 * @return
	 */
	public static String convertBetCodeFor11YDJ_DWFS(String betCode) { 
		StringBuffer sBuffer = new StringBuffer();
		String[] split = betCode.split("@");
		String[] split2 = split[1].split("\\*");
		if (split2.length==2) { //142@04*050607^
			String wan = split2[0];
			String qian = split2[1].substring(0, split2[1].length()-1);
			sBuffer.append("万位:"+LotteryAlgorithmUtil.joinStringWithBlank(wan)
					+"千位:"+LotteryAlgorithmUtil.joinStringWithBlank(qian));
		} else if (split2.length==3) { //162@0305*04*08^
			String wan = split2[0];
			String qian = split2[1];
			String bai = split2[2].substring(0, split2[2].length()-1);
			sBuffer.append("万位:"+LotteryAlgorithmUtil.joinStringWithBlank(wan)
					+"千位:"+LotteryAlgorithmUtil.joinStringWithBlank(qian)
					+"百位:"+LotteryAlgorithmUtil.joinStringWithBlank(bai));
		}
		String string = sBuffer.toString();
		if (string.endsWith("\n")) {
			return string.substring(0, string.length()-1);
		} else {
			return sBuffer.toString();
		}
	}
	
	/**
	 * 11运夺金注码转换(胆拖)
	 * @param betCode
	 * @return
	 */
	public static String convertBetCodeFor11YDJ_DT(String betCode) { 
		StringBuffer sBuffer = new StringBuffer();
		String[] split = betCode.split("@");
		String[] split2 = split[1].split("\\*");
		String danMa = split2[0];
		String tuoMa = split2[1].substring(0, split2[1].length()-1);
		sBuffer.append("胆码:"+LotteryAlgorithmUtil.joinStringWithBlank(danMa)
				+"拖码:"+LotteryAlgorithmUtil.joinStringWithBlank(tuoMa));
		String string = sBuffer.toString();
		if (string.endsWith("\n")) {
			return string.substring(0, string.length()-1);
		} else {
			return sBuffer.toString();
		}
	}
	
}
