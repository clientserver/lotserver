package com.ruyicai.lotserver.util.parse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.zc.bqc.Bqc_ds;
import com.ruyicai.lotserver.lottype.zc.bqc.Bqc_fs;
import com.ruyicai.lotserver.lottype.zc.jqc.Jqc_ds;
import com.ruyicai.lotserver.lottype.zc.jqc.Jqc_fs;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_ds;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_dt;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc_fs;
import com.ruyicai.lotserver.lottype.zc.sfc.Sfc_ds;
import com.ruyicai.lotserver.lottype.zc.sfc.Sfc_fs;
import com.ruyicai.lotserver.util.lot.ZuCaiUtil;

/**
 * 足彩注码解析
 * @author Administrator
 *
 */
@Service
public class ZuCaiBetCodeParseUtil {
	
	@Autowired
	Sfc_ds sfc_ds; //胜负彩单式
	
	@Autowired
	Sfc_fs sfc_fs; //胜负彩复式
	
	@Autowired
	Rjc_ds rjc_ds; //任九场单式
	
	@Autowired
	Rjc_fs rjc_fs; //任九场复式
	
	@Autowired
	Rjc_dt rjc_dt; //任九场胆拖
	
	@Autowired
	Jqc_ds jqc_ds; //进球彩单式
	
	@Autowired
	Jqc_fs jqc_fs; //进球彩复式
	
	@Autowired
	Bqc_ds bqc_ds; //半全场单式
	
	@Autowired
	Bqc_fs bqc_fs; //半全场单式

	/**
	 * 获得注码解析数组
	 * @param lotNo
	 * @param batchCode
	 * @param betCode(3,1,0,1,3,3,1,0,1,1,3,1,0,3_2_200_200)
	 * @param winCode(31013310113101)
	 * @return
	 */
	public JSONArray getParseBetCodeArray(String lotNo, String batchCode, String betCode, String winCode) {
		JSONArray resultArray = new JSONArray();
		
		if (lotNo!=null&&lotNo.equals(LotType.ZC_SFC.lotNo())) { //足球胜负彩
			JSONObject object = new JSONObject();
			object.put("lotName", "足球胜负彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				JSONArray parseCodeArray = new JSONArray(); //解析后的注码数组
				if (isDanShi) {
					play = "单式";
					parseCodeArray = sfc_ds.getParseCodeArray(lotNo, batchCode, code, winCode);
				} else {
					play = "复式";
					parseCodeArray = sfc_fs.getParseCodeArray(lotNo, batchCode, code, winCode);
				}
				object.put("play", play);
				object.put("result", parseCodeArray);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_RX9.lotNo())) { //足球任选9
			JSONObject object = new JSONObject();
			object.put("lotName", "足球任九场");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String play = ""; //玩法
				JSONArray parseCodeArray = new JSONArray(); //解析后的注码数组
				if (code.indexOf("$") > -1) { //(0,#,#,#,#,31,#,#,#,#,#,#,#,#$#,1,0,1,#,#,1,#,3,3,03,#,1,#_1_200_6000)
					play = "胆拖";
					parseCodeArray = rjc_dt.getParseCodeArray(lotNo, batchCode, code, winCode);
				} else {
					boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
					if (isDanShi) {
						play = "单式";
						parseCodeArray = rjc_ds.getParseCodeArray(lotNo, batchCode, code, winCode);
					} else {
						play = "复式";
						parseCodeArray = rjc_fs.getParseCodeArray(lotNo, batchCode, code, winCode);
					}
				}
				object.put("play", play);
				object.put("result", parseCodeArray);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())) { //足球进球彩
			JSONObject object = new JSONObject();
			object.put("lotName", "足球进球彩");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				JSONArray parseCodeArray = new JSONArray(); //解析后的注码数组
				if (isDanShi) {
					play = "单式";
					parseCodeArray = jqc_ds.getParseCodeArray(lotNo, batchCode, code, winCode);
				} else {
					play = "复式";
					parseCodeArray = jqc_fs.getParseCodeArray(lotNo, batchCode, code, winCode);
				}
				object.put("play", play);
				object.put("result", parseCodeArray);
				resultArray.add(object);
			}
		} else if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())) { //足球半全场
			JSONObject object = new JSONObject();
			object.put("lotName", "足球半全场");
			String[] orderInfos = betCode.split("!");
			for(String orderInfo : orderInfos) {
				String[] split = orderInfo.split("_");
				String code = split[0];
				String play = ""; //玩法
				boolean isDanShi = ZuCaiUtil.isDanShiBetCode(code); //是否是单式
				JSONArray parseCodeArray = new JSONArray(); //解析后的注码数组
				if (isDanShi) {
					play = "单式";
					parseCodeArray = bqc_ds.getParseCodeArray(lotNo, batchCode, code, winCode);
				} else {
					play = "复式";
					parseCodeArray = bqc_fs.getParseCodeArray(lotNo, batchCode, code, winCode);
				}
				object.put("play", play);
				object.put("result", parseCodeArray);
				resultArray.add(object);
			}
		}
		
		return resultArray;
	}
	
}
