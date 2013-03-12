package com.ruyicai.lotserver.util.parse;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_00;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_01;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_02;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_10;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_11;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_12;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_20;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_31;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_32;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_34;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd_54;
import com.ruyicai.lotserver.lottype.fc.qlc.Qlc_00;
import com.ruyicai.lotserver.lottype.fc.qlc.Qlc_10;
import com.ruyicai.lotserver.lottype.fc.qlc.Qlc_20;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_00;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_10;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_20;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_30;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_40;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq_50;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR4;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR5;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR6;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DR7;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DZ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_DZ3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MQ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MQ3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR1;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR4;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR5;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR6;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MR7;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MZ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_MZ3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_PQ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_PQ3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SQ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SQ3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR3;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR4;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR5;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR6;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR7;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SR8;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SZ2;
import com.ruyicai.lotserver.lottype.gpc.gd11x5.Gd11x5_SZ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DQ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DQ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DR2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DR3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DR4;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DR5;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DZ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_DZ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MR2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MR3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MR4;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MR5;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MS1;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MZ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_MZ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_PQ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_PQ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SH1;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SQ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SQ3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SR2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SR3;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SR4;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SR5;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SS1;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SZ2;
import com.ruyicai.lotserver.lottype.gpc.gdkl10.Gdkl10_SZ3;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_Q2;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_Q3;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R1;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R2;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R3;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R4;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R5;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R6;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R7;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_R8;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_Z2;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_Z3;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R2;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R3;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R4;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R5;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R6;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R7;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_R8;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_Z2;
import com.ruyicai.lotserver.lottype.gpc.jx11x5.Jx11x5_dt_Z3;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_101;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_102;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_103;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_104;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_105;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_106;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_107;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_108;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_109;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_111;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_112;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_113;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_114;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_115;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_116;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_117;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_121;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_122;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_123;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_124;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_125;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_126;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_131;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_133;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_141;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_142;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_151;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_153;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_161;
import com.ruyicai.lotserver.lottype.gpc.oodj.Oodj_162;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_1D;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_2D;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_2F;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_3D;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_3F;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_5D;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_5F;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_5T;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_DD;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_F2;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_H2;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_S2;
import com.ruyicai.lotserver.lottype.gpc.ssc.Ssc_Z2;
import com.ruyicai.lotserver.lottype.tc.dlt.Dlt_ds;
import com.ruyicai.lotserver.lottype.tc.dlt.Dlt_dt;
import com.ruyicai.lotserver.lottype.tc.dlt.Dlt_fs;
import com.ruyicai.lotserver.lottype.tc.dlt.Dlt_sxl;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_1;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_6;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_F3;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_F6;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_S1;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_S3;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_S6;
import com.ruyicai.lotserver.lottype.tc.pls.Pls_S9;
import com.ruyicai.lotserver.lottype.tc.plw.Plw_ds;
import com.ruyicai.lotserver.lottype.tc.plw.Plw_fs;
import com.ruyicai.lotserver.lottype.tc.qxc.Qxc_ds;
import com.ruyicai.lotserver.lottype.tc.qxc.Qxc_fs;
import com.ruyicai.lotserver.lottype.tc.ttx5.Ttx5_0;
import com.ruyicai.lotserver.lottype.tc.ttx5.Ttx5_1;
import com.ruyicai.lotserver.lottype.tc.ttx5.Ttx5_2;
import com.ruyicai.lotserver.lottype.zc.bqc.Bqc_ds;
import com.ruyicai.lotserver.lottype.zc.bqc.Bqc_fs;
import com.ruyicai.lotserver.lottype.zc.jqc.Jqc_ds;
import com.ruyicai.lotserver.lottype.zc.jqc.Jqc_fs;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_ds;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_dt;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_fs;
import com.ruyicai.lotserver.lottype.zc.sfc.Sfc_ds;
import com.ruyicai.lotserver.lottype.zc.sfc.Sfc_fs;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 解析订单的orderinfo
 * @author Administrator
 *
 */
public class OrderInfoParseUtil {

	/**
	 * 解析注码
	 * @param lotNo
	 * @param betCode
	 * @return
	 */
	public static JSONArray parseOrderInfo(String lotNo, String betCode) {
		JSONArray resultArray = new JSONArray();
		
		if (lotNo!=null&&lotNo.equals(LotType.QLC.lotNo())) { //七乐彩
			//000101121418202329^_2_200_200!1001*040512131418192021222327282930^_2_200_1287000
			JSONObject object = new JSONObject();
			object.put("lotName", "七乐彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split("\\^");
				for (String string : split2) {
					String play = string.substring(0, 2); //玩法
					String bet_code = "";
					if (play.equals("00")) { //单式(000110121422272829^_1_200_200)
						object.put("play", "单式");
						bet_code = Qlc_00.parseOrderInfo(string);
					} else if (play.equals("10")) { //复式(1001*0104081722232526^_3_200_1600)
						object.put("play", "复式");
						bet_code = Qlc_10.parseOrderInfo(string);
					} else if (play.equals("20")) { //胆拖(2001050611132830*1529^_2_200_400)
						object.put("play", "胆拖");
						bet_code = Qlc_20.parseOrderInfo(string);
					} else {
						object.put("play", "未知");
						bet_code = string;
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
			//320109000102030405060708^_1_200_16800
			JSONObject object = new JSONObject();
			object.put("lotName", "福彩3D");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				if (code.substring(0, 2).equals("20")) { //直选复式(20010109^0109^0105^_1_200_200)
					object.put("play", "直选");
					String bet_code = Ddd_20.parseOrderInfo(code);
					object.put("betCode", bet_code);
					resultArray.add(object);
					continue;
				} else {
					String[] split2 = code.split("\\^");
					for (String string : split2) {
						String play = string.substring(0, 2); //玩法
						String bet_code = "";
						if (play.equals("00")) { //单选单式(0001080203^_1_200_200)
							object.put("play", "单选单式");
							bet_code = Ddd_00.parseOrderInfo(string);
						} else if (play.equals("01")) { //组3单式(0101000005^_1_200_200)
							object.put("play", "组3单式");
							bet_code = Ddd_01.parseOrderInfo(string);
						} else if (play.equals("02")) { //组6单式(0201000102^_1_200_200)
							object.put("play", "组6单式");
							bet_code = Ddd_02.parseOrderInfo(string);
						} else if (play.equals("31")) { //组3复式(3101020102^_1_200_400)
							object.put("play", "组3复式");
							bet_code = Ddd_31.parseOrderInfo(string);
						} else if (play.equals("32")) { //组6复式(32010401020304^_1_200_800)
							object.put("play", "组6复式");
							bet_code = Ddd_32.parseOrderInfo(string);
						} else if (play.equals("34")) { //单选单复式(3401050103040607^_1_200_12000)
							object.put("play", "单选单复式");
							bet_code = Ddd_34.parseOrderInfo(string);
						}else if (play.equals("54")) { //胆拖(54010102*030405^_1_200_3600)
							object.put("play", "胆拖");
							bet_code = Ddd_54.parseOrderInfo(string);
						} else if (play.equals("10")) { //直选和值(100100^_1_200_200)
							object.put("play", "直选和值");
							bet_code = Ddd_10.parseOrderInfo(string);
						} else if (play.equals("11")) { //组3和值(110126^_1_200_200)
							object.put("play", "组3和值");
							bet_code = Ddd_11.parseOrderInfo(string);
						} else if (play.equals("12")) { //组6和值(120123^_1_200_200)
							object.put("play", "组6和值");
							bet_code = Ddd_12.parseOrderInfo(string);
						} else {
							object.put("play", "未知");
							bet_code = string;
						}
						object.put("betCode", bet_code);
						resultArray.add(object);
					}
				} 
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.SSQ.lotNo())) { // 双色球
			//1001*06131521242533~14^_1_200_1400!1001*11131517253133~05^_1_200_1400
			JSONObject object = new JSONObject();
			object.put("lotName", "双色球");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split("\\^");
				for (String string : split2) {
					String bet_code = "";
					if (string.length() > 4) {
						String play = string.substring(0, 2); //投注类型
						if (play.equals("00")) { //单式(0001040613141533~12^_2_200_200)
							object.put("play", "单式");
							bet_code = Ssq_00.parseOrderInfo(string);
						} else if (play.equals("10")) { //红球复式且蓝球单式(1001*0307151926272932~14^_1_200_5600)
							object.put("play", "复式");
							bet_code = Ssq_10.parseOrderInfo(string);
						} else if (play.equals("20")) { //红球单式且蓝球复式(2001*051519212225~040607111316^_2_200_1200)
							object.put("play", "复式");
							bet_code = Ssq_20.parseOrderInfo(string);
						} else if (play.equals("30")) { //红球复式且蓝球复式(3001*04071315192526293032~0413^_1_200_84000)
							object.put("play", "复式");
							bet_code = Ssq_30.parseOrderInfo(string);
						} else if (play.equals("40")) { //红球胆拖且蓝球单式(400111131523*142533~03^_2_200_600)
							object.put("play", "胆拖");
							bet_code = Ssq_40.parseOrderInfo(string);
						} else if (play.equals("50")) { //红球胆拖且蓝球复式(500113*051214162326~0516^_1_200_2400)
							object.put("play", "胆拖");
							bet_code = Ssq_50.parseOrderInfo(string);
						} else {
							object.put("play", "未知");
							bet_code = string;
						}
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //超级大乐透
			//12 21 30$11 13 18 19 22-04$02 10 11_2_200_6000
			JSONObject object = new JSONObject();
			object.put("lotName", "超级大乐透");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				for (String string : split2) {
					String bet_code = "";
					if (string.indexOf("-") > -1) { //单式或复式或胆拖
						if (string.indexOf("$") > -1) { //胆拖(01 02 03$09 19 28-06 07 09_1_300_2700)
							object.put("play", "胆拖");
							bet_code = Dlt_dt.parseOrderInfo(string);
						} else { //单式或复式
							String[] qianHou = string.split("-");
							String[] qian = qianHou[0].split(" ");
							String[] hou = qianHou[1].split(" ");
							if (qian.length==5 && hou.length==2) { //(02 10 18 24 28-02 11;_1_200_200)
								object.put("play", "单式");
								bet_code = Dlt_ds.parseOrderInfo(string);
							} else { //(02 03 04 05 06-09 11 12_1_300_900)
								object.put("play", "复式");
								bet_code = Dlt_fs.parseOrderInfo(string);
							}
						}
					} else { //生肖乐
						String[] split3 = string.split(" ");
						if (split3.length == 2) {
							object.put("play", "12选2单式");
						} else {
							object.put("play", "12选2复式");
						}
						bet_code = Dlt_sxl.parseOrderInfo(string);
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLS.lotNo())) { //排列三
			//S1|11_2_200_13800!1|4,16,5_2_200_400
			JSONObject object = new JSONObject();
			object.put("lotName", "排列三");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				
				for (String string : split2) {
					String[] split3 = string.split("\\|");
					String play = split3[0]; //玩法
					String bet_code = "";
					if (play.equals("1")) { //直选(1|4,16,5_2_200_400)
						object.put("play", "直选");
						bet_code = Pls_1.parseOrderInfo(split3[1]);
					} else if (play.equals("6")) { //组选(6|6,0,0_2_200_200)
						object.put("play", "组选");
						bet_code = Pls_6.parseOrderInfo(split3[1]);
					} else if (play.equals("S1")) { //直选和值(S1|5_2_200_4200)
						object.put("play", "直选和值");
						bet_code = Pls_S1.parseOrderInfo(split3[1]);
					} else if (play.equals("S9")) { //组选和值(S9|5_1_200_1000)
						object.put("play", "组选和值");
						bet_code = Pls_S9.parseOrderInfo(split3[1]);
					} else if (play.equals("S3")) { //组3和值(S3|5_2_200_600)
						object.put("play", "组3和值");
						bet_code = Pls_S3.parseOrderInfo(split3[1]);
					} else if (play.equals("S6")) { //组6和值(S6|6_2_200_600)
						object.put("play", "组6和值");
						bet_code = Pls_S6.parseOrderInfo(split3[1]);
					} else if (play.equals("F3")) { //组3包号(F3|3456_2_200_2400)
						object.put("play", "组3包号");
						bet_code = Pls_F3.parseOrderInfo(split3[1]);
					} else if (play.equals("F6")) { //组6包号(F6|2345_2_200_800)
						object.put("play", "组6包号");
						bet_code = Pls_F6.parseOrderInfo(split3[1]);
					} else {
						object.put("play", "未知");
						bet_code = split3[1];
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.PLW.lotNo())) { //排列五
			//4,5,9,3,4;_1_200_200!3,8,5,1,1;_1_200_200!8,9,1,6,4;_1_200_200!1,1,6,5,6;_1_200_200!0,6,7,1,8;_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "排列五");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				for (String string : split2) {
					String bet_code = "";
					if (string.indexOf(",") > -1) {
						boolean isD = true;
						String[] split3 = string.split(",");
						for (int i = 0; i < split3.length; i++) {
							if (split3[i].length() > 1) {
								isD = false;
								break;
							}
						}
						if (isD) { //(3,7,8,0,7_1_200_200)
							object.put("play", "单式");
							bet_code = Plw_ds.parseOrderInfo(string);
						} else { //(09,1,2,3,4689_1_200_1600)
							object.put("play", "复式");
							bet_code = Plw_fs.parseOrderInfo(string);
						}
					} else { //没有用","分隔
						object.put("play", "单式");
						bet_code = Plw_ds.parseOrderInfo(string);
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.QXC.lotNo())) { //七星彩
			//3,6,6,5,7,4,7;_1_200_200!9,3,1,9,2,5,9;_1_200_200!9,9,0,4,4,1,5;_1_200_200!7,1,3,5,8,9,1;_1_200_200!5,8,9,0,9,4,7;_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "七星彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				for (String string : split2) {
					String bet_code = "";
					if (string.indexOf(",") > -1) {
						boolean isD = true;
						String[] split3 = string.split(",");
						for (int i = 0; i < split3.length; i++) {
							if (split3[i].length() > 1) {
								isD = false;
								break;
							}
						}
						if (isD) { //(8,6,3,2,0,2,2;_1_200_200)
							object.put("play", "单式");
							bet_code = Qxc_ds.parseOrderInfo(string);
						} else { //(7,7,4,1,8,6,12_1_200_200)
							object.put("play", "复式");
							bet_code = Qxc_fs.parseOrderInfo(string);
						}
					} else { //没有用","分隔
						object.put("play", "单式");
						bet_code = Qxc_ds.parseOrderInfo(string);
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.TT_F.lotNo())) { //22选5
			//0@0809131920^_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "22选5");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String[] split2 = code.split("@");
				String play = split2[0]; //玩法
				String bet_code = "";
				if (play.equals("0")) { //单式(0@0103101220^_1_200_200)
					object.put("play", "单式");
					bet_code = Ttx5_0.parseOrderInfo(split2[1]);
				} else if (play.equals("1")) { //复式(1@060709141921^_1_200_1200)
					object.put("play", "复式");
					bet_code = Ttx5_1.parseOrderInfo(split2[1]);
				} else if (play.equals("2")) { //胆拖(2@13*060711192022^_1_200_3000)
					object.put("play", "胆拖");
					bet_code = Ttx5_2.parseOrderInfo(split2[1]);
				} else {
					object.put("play", "未知");
					bet_code = split2[1];
				}
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.SSC.lotNo())) { //时时彩
			//5D|012345678,123456789,125679,01349,0126_2_200_1944000
			JSONObject object = new JSONObject();
			object.put("lotName", "时时彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				for (String string : split2) {
					String[] split3 = string.split("\\|");
					String play = split3[0]; //玩法
					String bet_code = "";
					if (play.equals("5D")) { //五星(5D|0,1,2,168,4_1_200_200)
						object.put("play", "五星");
						bet_code = Ssc_5D.parseOrderInfo(split3[1]);
					} else if (play.equals("3D")) { //三星(3D|-,-,0,1,2_1_200_200)
						object.put("play", "三星");
						bet_code = Ssc_3D.parseOrderInfo(split3[1]);
					} else if (play.equals("2D")) { //二星(2D|-,-,-,0,1_1_200_200)
						object.put("play", "二星");
						bet_code = Ssc_2D.parseOrderInfo(split3[1]);
					} else if (play.equals("1D")) { //一星(1D|-,-,-,-,67_3_200_400)
						object.put("play", "一星");
						bet_code = Ssc_1D.parseOrderInfo(split3[1]);
					} else if (play.equals("5F")) { //五星复选(5F|6,8,5,1,8_1_200_800)
						object.put("play", "五星复选");
						bet_code = Ssc_5F.parseOrderInfo(split3[1]);
					} else if (play.equals("5T")) { //五星通选(5T|8,1,8,1,7_1_200_200)
						object.put("play", "五星通选");
						bet_code = Ssc_5T.parseOrderInfo(split3[1]);
					} else if (play.equals("3F")) { //三星复选(3F|-,-,5,1,8_1_200_600)
						object.put("play", "三星复选");
						bet_code = Ssc_3F.parseOrderInfo(split3[1]);
					} else if (play.equals("2F")) { //二星复选(2F|-,-,-,1,8_1_200_400)
						object.put("play", "二星复选");
						bet_code = Ssc_2F.parseOrderInfo(split3[1]);
					} else if (play.equals("H2")) { //二星和值(H2|5,6,14,16_1_200_4200)
						object.put("play", "二星和值");
						bet_code = Ssc_H2.parseOrderInfo(split3[1]);
					} else if (play.equals("S2")) { //二星组选和值(S2|0,2,11,13,16,18_1_200_2600)
						object.put("play", "二星组选和值");
						bet_code = Ssc_S2.parseOrderInfo(split3[1]);
					} else if (play.equals("DD")) { //大小单双(DD|12;_2_200_200)
						object.put("play", "大小单双");
						bet_code = Ssc_DD.parseOrderInfo(split3[1]);
					} else if (play.equals("Z2")) { //二星组选(Z2|168,9_1_200_600)
						object.put("play", "二星组选");
						bet_code = Ssc_Z2.parseOrderInfo(split3[1]);
					} else if (play.equals("F2")) { //二星组选复式(F2|0268_1_200_1200)
						object.put("play", "二星组选复式");
						bet_code = Ssc_F2.parseOrderInfo(split3[1]);
					} else {
						object.put("play", "未知");
						bet_code = split3[1];
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_F.lotNo())) { //江西11选5
			//R8|01 02 06 07 08 09 10 11_1_200_200!R8|02 04 05 06 07 08 10 11_1_200_200!R8|01 02 03 04 05 07 08 09_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "江西11选5");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String[] split2 = code.split(";");
				
				for (String string : split2) {
					String bet_code = "";
					if (code.indexOf("$") > -1) { //胆拖
						if (code.indexOf("|") > -1) {
							String[] split3 = string.split("\\|");
							String play = split3[0]; //玩法
							if (play.equals("R2")) { //任选二胆拖(R2|02$05 07 09_2000_200_600)
								object.put("play", "任选二胆拖");
								bet_code = Jx11x5_dt_R2.parseOrderInfo(split3[1]);
							} else if (play.equals("R3")) { //任选三胆拖(R3|01 11$07 10_2000_200_400)
								object.put("play", "任选三胆拖");
								bet_code = Jx11x5_dt_R3.parseOrderInfo(split3[1]);
							} else if (play.equals("R4")) { //任选四胆拖(R4|10 11$01 02 03 04_2000_200_1200)
								object.put("play", "任选四胆拖");
								bet_code = Jx11x5_dt_R4.parseOrderInfo(split3[1]);
							} else if (play.equals("R5")) { //任选五胆拖(R5|08 09 10 11$03 04_1_200_400)
								object.put("play", "任选五胆拖");
								bet_code = Jx11x5_dt_R5.parseOrderInfo(split3[1]);
							} else if (play.equals("R6")) { //任选六胆拖(R6|07 08 09 10 11$01 02 03 04 05 06_2_200_1200)
								object.put("play", "任选六胆拖");
								bet_code = Jx11x5_dt_R6.parseOrderInfo(split3[1]);
							} else if (play.equals("R7")) { //任选七胆拖(R7|04 06 10$02 03 05 07 09_1_200_1000)
								object.put("play", "任选七胆拖");
								bet_code = Jx11x5_dt_R7.parseOrderInfo(split3[1]);
							} else if (play.equals("R8")) { //任选八胆拖(R8|01$02 03 04 05 06 07 08 09 10 11_100_200_24000)
								object.put("play", "任选八胆拖");
								bet_code = Jx11x5_dt_R8.parseOrderInfo(split3[1]);
							} else if (play.equals("Z2")) { //选前二组选胆拖(Z2|11$09 01_2_200_400)
								object.put("play", "选前二组选胆拖");
								bet_code = Jx11x5_dt_Z2.parseOrderInfo(split3[1]);
							} else if (play.equals("Z3")) { //选前三组选胆拖(Z3|10 11$01 02_2_200_400)
								object.put("play", "选前三组选胆拖");
								bet_code = Jx11x5_dt_Z3.parseOrderInfo(split3[1]);
							} else {
								object.put("play", "未知");
								bet_code = split3[1];
							}
						}
					} else {
						if (code.indexOf("|") > -1) {
							String[] split3 = string.split("\\|");
							String play = split3[0]; //玩法
							if (play.equals("R1")) { //任选一(R1|01 02 03 04 05 08_1_200_1200)
								object.put("play", "任选一");
								bet_code = Jx11x5_R1.parseOrderInfo(split3[1]);
							} else if (play.equals("R2")) { //任选二(R2|01 02 03 04 05 06 07 08 09 10 11_1_200_11000)
								object.put("play", "任选二");
								bet_code = Jx11x5_R2.parseOrderInfo(split3[1]);
							} else if (play.equals("R3")) { //任选三(R3|02 04 07 09_2000_200_800)
								object.put("play", "任选三");
								bet_code = Jx11x5_R3.parseOrderInfo(split3[1]);
							} else if (play.equals("R4")) { //任选四(R4|01 02 06 11_2000_200_200)
								object.put("play", "任选四");
								bet_code = Jx11x5_R4.parseOrderInfo(split3[1]);
							} else if (play.equals("R5")) { //任选五(R5|01 02 03 04 05_1_200_200)
								object.put("play", "任选五");
								bet_code = Jx11x5_R5.parseOrderInfo(split3[1]);
							} else if (play.equals("R6")) { //任选六(R6|01 03 04 05 06 10_10_200_200)
								object.put("play", "任选六");
								bet_code = Jx11x5_R6.parseOrderInfo(split3[1]);
							} else if (play.equals("R7")) { //任选七(R7|06 04 10 07 03 02 01_1_200_200)
								object.put("play", "任选七");
								bet_code = Jx11x5_R7.parseOrderInfo(split3[1]);
							} else if (play.equals("R8")) { //任选八(R8|01 10 04 02 06 09 05 07_1_200_200)
								object.put("play", "任选八");
								bet_code = Jx11x5_R8.parseOrderInfo(split3[1]);
							} else if (play.equals("Q2")) { //选前二直选(Q2|02 03,06_1_200_400)
								object.put("play", "选前二直选");
								bet_code = Jx11x5_Q2.parseOrderInfo(split3[1]);
							} else if (play.equals("Q3")) { //选前三直选(Q3|01,02,03 05 07 08_1_200_800)
								object.put("play", "选前三直选");
								bet_code = Jx11x5_Q3.parseOrderInfo(split3[1]);
							} else if (play.equals("Z2")) { //选前二组选(Z2|01 02 03 04 05 06 07 08 09 10 11_2_200_11000)
								object.put("play", "选前二组选");
								bet_code = Jx11x5_Z2.parseOrderInfo(split3[1]);
							} else if (play.equals("Z3")) { //选前三组选(Z3|01 02 03 04 05 06 07 08 09 10 11_2_200_33000)
								object.put("play", "选前三组选");
								bet_code = Jx11x5_Z3.parseOrderInfo(split3[1]);
							} else {
								object.put("play", "未知");
								bet_code = split3[1];
							}
						}
					}
					object.put("betCode", bet_code);
					resultArray.add(object);
				}
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.OO_YDJ.lotNo())) { //11运夺金
			//111@0211^_1_200_200!102@*020411^_1_200_600
			JSONObject object = new JSONObject();
			object.put("lotName", "11运夺金");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String[] split1 = code.split("@");
				String play = split1[0]; //玩法
				String bet_code = "";
				if (play.equals("101")) { //任选一复式(101@*0102^_1_200_400)
					object.put("play", "任选一复式");
					bet_code = Oodj_101.parseOrderInfo(split1[1]);
				} else if (play.equals("102")) { //任选二复式(102@*010307^_2_200_600)
					object.put("play", "任选二复式");
					bet_code = Oodj_102.parseOrderInfo(split1[1]);
				} else if (play.equals("103")) { //任选三复式(103@*0104060910^_2_200_2000)
					object.put("play", "任选三复式");
					bet_code = Oodj_103.parseOrderInfo(split1[1]);
				} else if (play.equals("104")) { //任选四复式(104@*0304050608^_2_200_1000)
					object.put("play", "任选四复式");
					bet_code = Oodj_104.parseOrderInfo(split1[1]);
				} else if (play.equals("105")) { //任选五复式(105@*03040506070811^_1_200_4200)
					object.put("play", "任选五复式");
					bet_code = Oodj_105.parseOrderInfo(split1[1]);
				} else if (play.equals("106")) { //任选六复式(106@*0103040506070810^_1_200_5600)
					object.put("play", "任选六复式");
					bet_code = Oodj_106.parseOrderInfo(split1[1]);
				} else if (play.equals("107")) { //任选七复式(107@*020304050607081011^_1_200_7200)
					object.put("play", "任选七复式");
					bet_code = Oodj_107.parseOrderInfo(split1[1]);
				} else if (play.equals("108")) { //选前二组选复式(108@*010203^_1_200_600)
					object.put("play", "选前二组选复式");
					bet_code = Oodj_108.parseOrderInfo(split1[1]);
				} else if (play.equals("109")) { //选前三组选复式(109@*03040507^_2_200_800)
					object.put("play", "选前三组选复式");
					bet_code = Oodj_109.parseOrderInfo(split1[1]);
				} else if (play.equals("111")) { //任选二单式(111@0407^_1_200_200)
					object.put("play", "任选二单式");
					bet_code = Oodj_111.parseOrderInfo(split1[1]);
				} else if (play.equals("112")) { //任选三单式(112@010203^_2_200_200)
					object.put("play", "任选三单式");
					bet_code = Oodj_112.parseOrderInfo(split1[1]);
				} else if (play.equals("113")) { //任选四单式(113@02040511^_1_200_200)
					object.put("play", "任选四单式");
					bet_code = Oodj_113.parseOrderInfo(split1[1]);
				} else if (play.equals("114")) { //任选五单式(114@0102060811^_1_200_200)
					object.put("play", "任选五单式");
					bet_code = Oodj_114.parseOrderInfo(split1[1]);
				} else if (play.equals("115")) { //任选六单式(115@030405080911^_2_200_200)
					object.put("play", "任选六单式");
					bet_code = Oodj_115.parseOrderInfo(split1[1]);
				} else if (play.equals("116")) { //任选七单式(116@01020304050607^_3_200_200)
					object.put("play", "任选七单式");
					bet_code = Oodj_116.parseOrderInfo(split1[1]);
				} else if (play.equals("117")) { //任选八单式(117@0102030405060708^_3_200_200)
					object.put("play", "任选八单式");
					bet_code = Oodj_117.parseOrderInfo(split1[1]);
				} else if (play.equals("121")) { //任选二胆拖(121@01*0203^_1_200_400)
					object.put("play", "任选二胆拖");
					bet_code = Oodj_121.parseOrderInfo(split1[1]);
				} else if (play.equals("122")) { //任选三胆拖(122@11*01020304050607080910^_1_200_9000)
					object.put("play", "任选三胆拖");
					bet_code = Oodj_122.parseOrderInfo(split1[1]);
				} else if (play.equals("123")) { //任选四胆拖(123@11*01020304050607080910^_2_200_24000)
					object.put("play", "任选四胆拖");
					bet_code = Oodj_123.parseOrderInfo(split1[1]);
				} else if (play.equals("124")) { //任选五胆拖(124@08091011*01020304050607^_2_200_1400)
					object.put("play", "任选五胆拖");
					bet_code = Oodj_124.parseOrderInfo(split1[1]);
				} else if (play.equals("125")) { //任选六胆拖(125@0708091011*010203040506^_2_200_1200)
					object.put("play", "任选六胆拖");
					bet_code = Oodj_125.parseOrderInfo(split1[1]);
				} else if (play.equals("126")) { //任选七胆拖(126@060708091011*0102030405^_2_200_1000)
					object.put("play", "任选七胆拖");
					bet_code = Oodj_126.parseOrderInfo(split1[1]);
				} else if (play.equals("131")) { //选前二组选单式(131@0209^_1_200_200)
					object.put("play", "选前二组选单式");
					bet_code = Oodj_131.parseOrderInfo(split1[1]);
				} else if (play.equals("133")) { //选前二组选胆拖(133@11*0102050709^_2_200_1000)
					object.put("play", "选前二组选胆拖");
					bet_code = Oodj_133.parseOrderInfo(split1[1]);
				} else if (play.equals("141")) { //选前二直选单式(141@0102^_1_200_200)
					object.put("play", "选前二直选单式");
					bet_code = Oodj_141.parseOrderInfo(split1[1]);
				} else if (play.equals("142")) { //选前二直选定位复式(142@0211*0406^_1_200_800)
					object.put("play", "选前二直选定位复式");
					bet_code = Oodj_142.parseOrderInfo(split1[1]);
				} else if (play.equals("151")) { //选前三组选单式(151@030506^_2_200_200)
					object.put("play", "选前三组选单式");
					bet_code = Oodj_151.parseOrderInfo(split1[1]);
				} else if (play.equals("153")) { //选前三组选胆拖(153@1011*010203040506070809^_2_200_1800)
					object.put("play", "选前三组选胆拖");
					bet_code = Oodj_153.parseOrderInfo(split1[1]);
				} else if (play.equals("161")) { //选前三直选单式(161@010203^_2_200_200)
					object.put("play", "选前三直选单式");
					bet_code = Oodj_161.parseOrderInfo(split1[1]);
				} else if (play.equals("162")) { //选前三直选定位复式(162@0210*11*05060708^_1_200_1600)
					object.put("play", "选前三直选定位复式");
					bet_code = Oodj_162.parseOrderInfo(split1[1]);
				} else {
					object.put("play", "未知");
					bet_code = split1[1];
				} 
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.GDOO_F.lotNo())) { //广东11选5
			//S|R2|0507_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "广东11选5");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
			
				String[] codes = code.split("\\|");
				String touZhuType = codes[0]; //投注方式
				String smallType = codes[1]; //子玩法
				String realCode = codes[2]; //实际注码
				String play = MemoUtil.getT01014Play(smallType); //玩法名称
				String bet_code = "";
				if (touZhuType.equals("S")) { //单式投注
					object.put("play", play+"单式");
					if (smallType.equals("R2")) { //任选二(S|R2|0205_1_200_200)
						bet_code = Gd11x5_SR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(S|R3|040102_1_200_200)
						bet_code = Gd11x5_SR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(S|R4|01020304_1_200_200)
						bet_code = Gd11x5_SR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(S|R5|0206071011_8_200_200)
						bet_code = Gd11x5_SR5.parseOrderInfo(realCode);
					} else if (smallType.equals("R6")) { //任选六(S|R6|010205060709_1_200_200)
						bet_code = Gd11x5_SR6.parseOrderInfo(realCode);
					} else if (smallType.equals("R7")) { //任选七(S|R7|01020304050608_1_200_200)
						bet_code = Gd11x5_SR7.parseOrderInfo(realCode);
					} else if (smallType.equals("R8")) { //任选八(S|R8|0901020304050706_2_200_200)
						bet_code = Gd11x5_SR8.parseOrderInfo(realCode);
					} else if (smallType.equals("Q2")) { //直选前二(S|Q2|0102_1_200_200)
						bet_code = Gd11x5_SQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(S|Q3|030910_2_200_200)
						bet_code = Gd11x5_SQ3.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //组选前二(S|Z2|0502_2_200_200)
						bet_code = Gd11x5_SZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(S|Z3|010405_1_200_200)
						bet_code = Gd11x5_SZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("M")) { //复式投注
					object.put("play", play+"复式");
					if (smallType.equals("R1")) { //任选一(M|R1|09_1_200_200)
						bet_code = Gd11x5_MR1.parseOrderInfo(realCode);
					} else if (smallType.equals("R2")) { //任选二(M|R2|020304_1_200_600)
						bet_code = Gd11x5_MR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(M|R3|0908070611_1_200_2000)
						bet_code = Gd11x5_MR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(M|R4|0102030411_1_200_1000)
						bet_code = Gd11x5_MR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(M|R5|010203040511_1_200_1200)
						bet_code = Gd11x5_MR5.parseOrderInfo(realCode);
					} else if (smallType.equals("R6")) { //任选六(M|R6|01020304050609_1_200_1400)
						bet_code = Gd11x5_MR6.parseOrderInfo(realCode);
					} else if (smallType.equals("R7")) { //任选七(M|R7|0102030405060811_1_200_1600)
						bet_code = Gd11x5_MR7.parseOrderInfo(realCode);
					} else if (smallType.equals("Q2")) { //直选前二(M|Q2|050306_1_200_1200)
						bet_code = Gd11x5_MQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(M|Q3|05030206_2_200_800)
						bet_code = Gd11x5_MQ3.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //组选前二(M|Z2|050308_2_200_600)
						bet_code = Gd11x5_MZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(M|Z3|05030106_1_200_800)
						bet_code = Gd11x5_MZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("D")) { //胆拖投注
					object.put("play", play+"胆拖");
					if (smallType.equals("R2")) { //任选二(D|R2|01-0302_1_200_400)
						bet_code = Gd11x5_DR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(D|R3|0901-070405_2_200_600)
						bet_code = Gd11x5_DR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(D|R4|0105-020304_1_200_600)
						bet_code = Gd11x5_DR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(D|R5|01020304-05060708091011_3_200_1400)
						bet_code = Gd11x5_DR5.parseOrderInfo(realCode);
					} else if (smallType.equals("R6")) { //任选六(D|R6|0105-0203040607_1_200_1000)
						bet_code = Gd11x5_DR6.parseOrderInfo(realCode);
					} else if (smallType.equals("R7")) { //任选七(D|R7|0105-020304060708_1_200_1200)
						bet_code = Gd11x5_DR7.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //组选前二(D|Z2|04-0102_1_200_400)
						bet_code = Gd11x5_DZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(D|Z3|0102-0304_1_200_400)
						bet_code = Gd11x5_DZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("P")) { //定位投注
					object.put("play", play+"复式");
					if (smallType.equals("Q2")) { //直选前二(P|Q2|0103-08_2_200_400)
						bet_code = Gd11x5_PQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(P|Q3|0208-01-09_1_200_400)
						bet_code = Gd11x5_PQ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else {
					object.put("play", "未知");
					bet_code = realCode;
				}
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.GDH_T.lotNo())) { //广东快乐十分
			//S|S1|01_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "广东快乐十分");
			String[] orderInfos = betCode.split("!");
			for (String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
			
				String[] codes = code.split("\\|");
				String touZhuType = codes[0]; //投注方式
				String smallType = codes[1]; //子玩法
				String realCode = codes[2]; //实际注码
				String play = MemoUtil.getT01015Play(smallType); //玩法名称
				String bet_code = "";
				if (touZhuType.equals("S")) { //单式投注
					object.put("play", play+"单式"); //S|H1|19^20_1_200_400
					if (smallType.equals("S1")) { //选一数投(S|S1|02_1_200_200)
						bet_code = Gdkl10_SS1.parseOrderInfo(realCode);
					} else if (smallType.equals("H1")) { //选一红投(S|H1|19^20_1_200_400)
						bet_code = Gdkl10_SH1.parseOrderInfo(realCode);
					} else if (smallType.equals("R2")) { //任选二 (S|R2|0914_1_200_200)
						bet_code = Gdkl10_SR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(S|R3|172006_2_200_200)
						bet_code = Gdkl10_SR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(S|R4|01101115_1_200_200)
						bet_code = Gdkl10_SR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(S|R5|0611121315_1_200_200)
						bet_code = Gdkl10_SR5.parseOrderInfo(realCode);
					} else if (smallType.equals("Q2")) { //选二连直(S|Q2|0201_2_200_200)
						bet_code = Gdkl10_SQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(S|Q3|091011_2_200_200)
						bet_code = Gdkl10_SQ3.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //选二连组(S|Z2|0713_2_200_200)
						bet_code = Gdkl10_SZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(S|Z3|091120_2_200_200)
						bet_code = Gdkl10_SZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("M")) { //复式投注
					object.put("play", play+"复式");
					if (smallType.equals("S1")) { //选一数投(M|S1|0210_1_200_400)
						bet_code = Gdkl10_MS1.parseOrderInfo(realCode);
					} else if (smallType.equals("R2")) { //任选二(M|R2|180811_2_200_600)
						bet_code = Gdkl10_MR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(M|R3|16190710_2_200_800)
						bet_code = Gdkl10_MR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(M|R4|1718041014_2_200_1000)
						bet_code = Gdkl10_MR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(M|R5|170120091015_2_200_1200)
						bet_code = Gdkl10_MR5.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //选二连组(M|Z2|160709_2_200_600)
						bet_code = Gdkl10_MZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(M|Z3|05030106_2_200_600)
						bet_code = Gdkl10_MZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("D")) { //胆拖投注(D|R4|0105-020304)
					object.put("play", play+"胆拖");
					if (smallType.equals("R2")) { //任选二(D|R2|01-0911_2_200_400)
						bet_code = Gdkl10_DR2.parseOrderInfo(realCode);
					} else if (smallType.equals("R3")) { //任选三(D|R3|0913-1112_2_200_400)
						bet_code = Gdkl10_DR3.parseOrderInfo(realCode);
					} else if (smallType.equals("R4")) { //任选四(D|R4|01-09020304_2_200_800)
						bet_code = Gdkl10_DR4.parseOrderInfo(realCode);
					} else if (smallType.equals("R5")) { //任选五(D|R5|021213-06071516_1_200_1200)
						bet_code = Gdkl10_DR5.parseOrderInfo(realCode);
					} else if (smallType.equals("Q2")) { //选二连直(D|Q2|01-0203_1_200_800)
						bet_code = Gdkl10_DQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(D|Q3|0908-111204_1_200_3600)
						bet_code = Gdkl10_DQ3.parseOrderInfo(realCode);
					} else if (smallType.equals("Z2")) { //选二连组(D|Z2|01-020304_2_200_600)
						bet_code = Gdkl10_DZ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Z3")) { //组选前三(D|Z3|09-13140807_2_200_1200)
						bet_code = Gdkl10_DZ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else if (touZhuType.equals("P")) { //定位投注(P|Q2|02-0304_100_200_400!P|Q3|01-02-0304_100_200_400)
					object.put("play", play+"定位");
					if (smallType.equals("Q2")) { //选二连直(P|Q2|010203-1804_2_200_1200)
						bet_code = Gdkl10_PQ2.parseOrderInfo(realCode);
					} else if (smallType.equals("Q3")) { //直选前三(P|Q3|0115-2005-18_1_200_800)
						bet_code = Gdkl10_PQ3.parseOrderInfo(realCode);
					} else {
						bet_code = realCode;
					}
				} else {
					object.put("play", "未知");
					bet_code = realCode;
				}
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_SFC.lotNo())) { //足球胜负彩
			//3,1,0,3,1,0,1,0,3,1,0,3,1,0_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "足球胜负彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String bet_code = ""; //解析后的注码
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
					bet_code = Sfc_ds.parseOrderInfo(code);
				} else {
					play = "复式";
					bet_code = Sfc_fs.parseOrderInfo(code);
				}
				object.put("play", play);
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_RX9.lotNo())) { //足球任选9
			//3,1,3,1,0,1,3,0,3,#,#,#,#,#_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "足球任九场");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String bet_code = ""; //解析后的注码
				String play = ""; //玩法
				if (code.indexOf("$") > -1) {
					play = "胆拖";
					bet_code = Rjc_dt.parseOrderInfo(code);
				} else {
					boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
					if (isDanShi) {
						play = "单式";
						bet_code = Rjc_ds.parseOrderInfo(code);
					} else {
						play = "复式";
						bet_code = Rjc_fs.parseOrderInfo(code);
					}
				}
				object.put("play", play);
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())) { //足球进球彩
			//0,1,2,1,2,1,1,2_1_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "足球进球彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String bet_code = ""; //解析后的注码
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
					bet_code = Jqc_ds.parseOrderInfo(code);
				} else {
					play = "复式";
					bet_code = Jqc_fs.parseOrderInfo(code);
				}
				object.put("play", play);
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())) { //足球半全场
			//3,1,3,1,0,3,1,1,3,0,1,3_2_200_200
			JSONObject object = new JSONObject();
			object.put("lotName", "足球半全场");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				
				String bet_code = ""; //解析后的注码
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				if (isDanShi) {
					play = "单式";
					bet_code = Bqc_ds.parseOrderInfo(code);
				} else {
					play = "复式";
					bet_code = Bqc_fs.parseOrderInfo(code);
				}
				object.put("play", play);
				object.put("betCode", bet_code);
				resultArray.add(object);
			}
		} else { //未知彩种
			JSONObject object = new JSONObject();
			object.put("lotName", "未知");
			object.put("play", "未知");
			object.put("betCode", betCode);
			resultArray.add(object);
		}
		
		return resultArray;
	}
	
}
