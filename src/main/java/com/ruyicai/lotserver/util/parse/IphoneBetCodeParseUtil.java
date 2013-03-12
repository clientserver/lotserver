package com.ruyicai.lotserver.util.parse;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.jc.lq.Dxf;
import com.ruyicai.lotserver.lottype.jc.lq.Rfsf;
import com.ruyicai.lotserver.lottype.jc.lq.Sf;
import com.ruyicai.lotserver.lottype.jc.lq.SfcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Bf;
import com.ruyicai.lotserver.lottype.jc.zq.BqcJc;
import com.ruyicai.lotserver.lottype.jc.zq.Jqs;
import com.ruyicai.lotserver.lottype.jc.zq.Spf;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * iPhone wap页面投注时的注码解析
 * @author Administrator
 *
 */
public class IphoneBetCodeParseUtil {
	
	/**
	 * 注码解析
	 * @param lotNo
	 * @param betCode
	 * @param isSellWays
	 * @return
	 */
	public static JSONArray parseBetCode(String lotNo, String betCode, String isSellWays) {
		JSONArray resultArray = new JSONArray();
		if (Tools.isEmpty(isSellWays)) { //旧的注码格式
			resultArray = BetCodeParseUtil.parseBetCode(lotNo, betCode);
		} else { //新的注码格式
			if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竟彩
				String[] codes = betCode.split("!");
				for (String code : codes) {
					JSONObject object = new JSONObject();
					object.put("lotName", MemoUtil.getLotNameByLotNo(lotNo));
					String[] split = code.split("_");
					String[] split2 = split[0].split("@");
					
					StringBuffer sBuffer = new StringBuffer();
					if (split2[1].indexOf("$")>-1) { //设胆
						String[] split3 = split2[1].split("\\$");
						String danContent = getContent(split3[0], lotNo, "(胆)"); //胆
						sBuffer.append(danContent);
						
						String tuoContent = getContent(split3[1], lotNo, ""); //拖
						sBuffer.append(tuoContent);
					} else {
						String content = getContent(split2[1], lotNo, "");
						sBuffer.append(content);
					}
					if (sBuffer.toString().endsWith("\n")) {
						object.put("betCode", sBuffer.toString().substring(0, sBuffer.toString().length()-1));
					} else {
						object.put("betCode", sBuffer.toString());
					}
					resultArray.add(object);
				}
			} else {
				resultArray = OrderInfoParseUtil.parseOrderInfo(lotNo, betCode);
			}
		}
		return resultArray;
	}
	
	/**
	 * 获得投注内容
	 * @param code
	 * @param lotNo
	 * @param isDanMa
	 * @return
	 */
	private static String getContent(String code, String lotNo, String isDanMa) {
		StringBuffer sBuffer = new StringBuffer();
		String[] split = code.split("\\^");
		for (String string : split) {
			String[] split2 = string.split("\\|");
			String day = split2[0];
			String weekid = split2[1];
			String teamid = split2[2];
			
			String id = day+"*"+weekid+"*"+teamid+"*";
			String betContent = "";
			if (lotNo!=null&&lotNo.equals(LotType.JCZ_SPF.lotNo())) { //竞彩足球胜平负
				betContent = Spf.getBetContent(id, "", split2[3], false, "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BF.lotNo())) { //竞彩足球比分
				betContent = Bf.getBetContent(id, "", split2[3], false, "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_JQS.lotNo())) { //竞彩足球总进球数
				betContent = Jqs.getBetContent(id, "", split2[3], false, "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCZ_BQC.lotNo())) { //竞彩足球半全场
				betContent = BqcJc.getBetContent(id, "", split2[3], false, "", "", "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SF.lotNo())) { //竞彩篮球胜负
				betContent = Sf.getBetContent(id, "", split2[3], false, "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCL_RFSF.lotNo())) { //竞彩篮球让分胜负
				JSONObject codeObject = Rfsf.getBetContent(id, "", split2[3], false, "", "", "", ""); //投注内容
				betContent = codeObject.getString("betContent");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCL_SFC.lotNo())) { //竞彩篮球胜分差
				betContent = SfcJc.getBetContent(id, "", split2[3], false, "", "");
			} else if (lotNo!=null&&lotNo.equals(LotType.JCL_DXF.lotNo())) { //竞彩篮球大小分
				JSONObject codeObject = Dxf.getBetContent(id, "", split2[3], false, "", "", "", ""); //投注内容
				betContent = codeObject.getString("betContent");
			} 
			sBuffer.append(JingCaiUtil.getWeekByWeekId(weekid)+" "+teamid+" "+"\n您的投注:"+betContent+isDanMa+";").append("\n");
		}
		return sBuffer.toString();
	}
	
	/*public static void main(String[] args) {
		JSONArray parseBetCode = BetCodeParseIphoneUtil.parseBetCode("J00001", "500@20120822|3|016|3^20120822|3|017|1^20120822|3|018|1^$20120822|3|019|3^20120822|3|020|1^20120822|3|021|0^20120822|3|022|31^_1_200_1600", "1");
		for (int i = 0; i < parseBetCode.size(); i++) {
			JSONObject jsonObject = parseBetCode.getJSONObject(i);
			System.out.println(jsonObject.getString("betCode"));
		}
	}*/
	
}
