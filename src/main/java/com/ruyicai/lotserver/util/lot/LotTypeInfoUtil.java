package com.ruyicai.lotserver.util.lot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.domain.LotTypeInfo;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 彩种信息公共类
 * @author Administrator
 *
 */
public class LotTypeInfoUtil {

	/**
	 * 判断是否今日开奖
	 * @param opentime
	 * @return
	 */
	public static String isTodayOpenPrize(String openTime) {
		String isTodayOpenPrize = "false";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (!Tools.isEmpty(openTime)&&!openTime.trim().equals("null")) {
			String opentimeString = sdf.format(new Date(Long.parseLong(openTime)));
			String todayString = sdf.format(new Date());
			if (opentimeString.equals(todayString)) {
				isTodayOpenPrize = "true";
			}
		}
		return isTodayOpenPrize;
	}
	
	/**
	 * 处理当前期号的截止时间
	 * @param resultJson
	 * @param endBetTime
	 */
	public static void dealCurrentBatchCodeEndTime(JSONObject responseJson, String endBetTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
		long currentTime = System.currentTimeMillis();
		if (!Tools.isEmpty(endBetTime)&&!endBetTime.equals("null")) {
			try {
				long endSecond = (Long.parseLong(endBetTime)-currentTime)/1000;
				if (endSecond < 0) {
					endSecond = 0;
				}
				responseJson.put("endSecond", endSecond);
			} catch (Exception e) {
				e.printStackTrace();
			}
			endBetTime = sdf.format(new Date(Long.parseLong(endBetTime)));
		} else {
			endBetTime = "";
		}
		responseJson.put("endTime", endBetTime); //截止时间
	}
	
	/**
	 * 处理开奖公告的开奖时间
	 * @param responseJson
	 * @param valueObject
	 */
	public static void dealWinInfoOpenTime(JSONObject responseJson, JSONObject valueObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String batchCode = valueObject.getJSONObject("id").getString("batchcode"); //期号
		String openTime = valueObject.getString("opentime"); //开奖时间
		if (Tools.isEmpty(openTime)||openTime.equals("null")) {
			openTime = "";
		} else {
			openTime = sdf.format(new Date(Long.parseLong(openTime)));
		}
		responseJson.put("batchCode", batchCode);
		responseJson.put("openTime", openTime);
	}
	
	/**
	 * 获得加奖的彩种
	 * @return
	 */
	public static List<LotTypeInfo> getAddAwardLotNos() {
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.addawardstate=? ");
		params.add("1");
		
		List<LotTypeInfo> list = LotTypeInfo.getList(builder.toString(), "", params);
		return list;
	}
	
	/**
	 * 获得正在销售的彩种
	 * @return
	 */
	public static List<LotTypeInfo> getSaleLotNos() {
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.salestate=? ");
		params.add("1");
		
		List<LotTypeInfo> list = LotTypeInfo.getList(builder.toString(), "", params);
		return list;
	}
	
	/**
	 * 判断彩种是否加奖、销售
	 * @param lotNo
	 */
	public static String isLotNosExistList(List<LotTypeInfo> lotTypeInfos, List<String> lotNos) {
		String isExist = "false";
		if (lotTypeInfos!=null&&lotTypeInfos.size()>0&&lotNos!=null&&lotNos.size()>0) {
			for (LotTypeInfo lotTypeInfo : lotTypeInfos) {
				String lot_no = lotTypeInfo.getLotno();
				for (String lotNo : lotNos) {
					if (lotNo.equals(lot_no)) {
						isExist = "true";
						break;
					}
				}
			}
		}
		return isExist;
	}
	
}
