package com.ruyicai.lotserver.service.query;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 开奖信息
 * 
 * @author Administrator
 * 
 */
@Service
public class LotteryInfomationService {
	
	private Logger logger = Logger.getLogger(LotteryInfomationService.class);

	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 获取开奖信息(缓存)
	 * @param clientInfo
	 * @return
	 */
	public String getLotteryInfoDataCache(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			JSONArray qlcResultArray = new JSONArray(); //七乐彩
			JSONArray dddResultArray = new JSONArray(); //福彩3D
			JSONArray ssqResultArray = new JSONArray(); //双色球
			
			JSONArray dltResultArray = new JSONArray(); //大乐透
			JSONArray pl3ResultArray = new JSONArray(); //排列三
			JSONArray pl5ResultArray = new JSONArray(); //排列五
			JSONArray qxcResultArray = new JSONArray(); //七星彩
			JSONArray _22_5ResultArray = new JSONArray(); //22选5
			
			JSONArray sscResultArray = new JSONArray(); //时时彩
			JSONArray _11_5ResultArray = new JSONArray(); //11选5
			JSONArray _11_ydjResultArray = new JSONArray(); //十一运夺金
			
			JSONArray sfcResultArray = new JSONArray(); //胜负彩
			JSONArray rx9ResultArray = new JSONArray(); //任选9
			JSONArray jqcResultArray = new JSONArray(); //进球彩
			JSONArray lcbResultArray = new JSONArray(); //6场半
			
			String isCompress = clientInfo.getIsCompress(); //是否压缩
			int num = 5; //开奖信息返回的条数
			if (isCompress!=null&&isCompress.equals("1")) { // 如果客户端传压缩字段，则开奖信息返回24条记录
				num = 24;
			}
			JSONObject valueObject = cacheCommonUtil.getWinInfoValueObject(num); //获取开奖信息
			//七乐彩
			JSONArray qlcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.QLC.lotNo(), num);
			if (qlcArray!=null && qlcArray.size()>0) {
				for (int i = 0; i < qlcArray.size(); i++) {
					JSONObject object = qlcArray.getJSONObject(i);
					JSONObject qlc = new JSONObject();
					String specialCode = object.getString("winspecialcode"); //特殊号码
					if (Tools.isEmpty(specialCode)||specialCode.equals("null")) {
						specialCode = "";
					}
					qlc.put("winno", object.getString("winbasecode").replaceAll(" ", "")+specialCode.replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(qlc, object); //期号和开奖时间
					qlcResultArray.add(qlc);
				}
			}
			
			//福彩3D
			JSONArray dddArray = commonUtil.getWinInfoValueArray(valueObject, LotType.DDD.lotNo(), num);
			if (dddArray!=null && dddArray.size()>0) {
				for (int i = 0; i < dddArray.size(); i++) {
					JSONObject object = dddArray.getJSONObject(i);
					JSONObject ddd = new JSONObject();
					ddd.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(ddd, object); //期号和开奖时间
					dddResultArray.add(ddd);
				}
			}
			
			//双色球
			JSONArray ssqArray = commonUtil.getWinInfoValueArray(valueObject, LotType.SSQ.lotNo(), num);
			if (ssqArray!=null && ssqArray.size()>0) {
				for (int i = 0; i < ssqArray.size(); i++) {
					JSONObject object = ssqArray.getJSONObject(i);
					JSONObject ssq = new JSONObject();
					String specialCode = object.getString("winspecialcode"); //特殊号码
					if (Tools.isEmpty(specialCode)||specialCode.equals("null")) {
						specialCode = "";
					}
					ssq.put("winno", object.getString("winbasecode") + specialCode); //开奖号码
					dealWinInfoOpenTime(ssq, object); //期号和开奖时间
					ssqResultArray.add(ssq);
				}
			}
			
			//大乐透
			JSONArray dltArray = commonUtil.getWinInfoValueArray(valueObject, LotType.DLT.lotNo(), num);
			if (dltArray!=null && dltArray.size()>0) {
				for (int i = 0; i < dltArray.size(); i++) {
					JSONObject object = dltArray.getJSONObject(i);
					JSONObject dlt = new JSONObject();
					dlt.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(dlt, object); //期号和开奖时间
					dltResultArray.add(dlt);
				}
			}
			
			//排列三
			JSONArray plsArray = commonUtil.getWinInfoValueArray(valueObject, LotType.PLS.lotNo(), num);
			if (plsArray!=null && plsArray.size()>0) {
				for (int i = 0; i < plsArray.size(); i++) {
					JSONObject object = plsArray.getJSONObject(i);
					JSONObject pls = new JSONObject();
					pls.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(pls, object); //期号和开奖时间
					pl3ResultArray.add(pls);
				}
			}
			
			//排列五
			JSONArray pl5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.PLW.lotNo(), num);
			if(pl5Array!=null && pl5Array.size()>0) {
				for (int i = 0; i < pl5Array.size(); i++) {
					JSONObject object = pl5Array.getJSONObject(i);
					JSONObject pl5 = new JSONObject();
					pl5.put("winno", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(pl5, object); //期号和开奖时间
					pl5ResultArray.add(pl5);
				}
			}
			
			//七星彩
			JSONArray qxcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.QXC.lotNo(), num); 
			if (qxcArray!=null && qxcArray.size()>0) {
				for (int i = 0; i < qxcArray.size(); i++) {
					JSONObject object = qxcArray.getJSONObject(i);
					JSONObject qxc = new JSONObject();
					qxc.put("winno", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(qxc, object); //期号和开奖时间
					qxcResultArray.add(qxc);
				}
			}
			
			//22选5
			JSONArray _22_5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.TT_F.lotNo(), num);
			if (_22_5Array!=null && _22_5Array.size()>0) {
				for (int i = 0; i < _22_5Array.size(); i++) {
					JSONObject object = _22_5Array.getJSONObject(i);
					JSONObject _22_5 = new JSONObject();
					_22_5.put("winno", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(_22_5, object); //期号和开奖时间
					_22_5ResultArray.add(_22_5);
				}
			}
			
			//时时彩
			JSONArray sscArray = commonUtil.getWinInfoValueArray(valueObject, LotType.SSC.lotNo(), num);
			for (int i = 0; i < sscArray.size(); i++) {
				JSONObject object = sscArray.getJSONObject(i);
				JSONObject ssc = new JSONObject();
				ssc.put("winno", object.getString("winbasecode")); //开奖号码
				dealWinInfoOpenTime(ssc, object); //期号和开奖时间
				sscResultArray.add(ssc);
			}
			
			//11选5
			JSONArray _11_5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.OO_F.lotNo(), num);
			if (_11_5Array!=null && _11_5Array.size()>0) {
				for (int i = 0; i < _11_5Array.size(); i++) {
					JSONObject object = _11_5Array.getJSONObject(i);
					JSONObject _11_5 = new JSONObject();
					_11_5.put("winno", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(_11_5, object); //期号和开奖时间
					_11_5ResultArray.add(_11_5);
				}
			}
			
			//十一运夺金
			JSONArray _11_ydjArray = commonUtil.getWinInfoValueArray(valueObject, LotType.OO_YDJ.lotNo(), num);
			if (_11_ydjArray!=null && _11_ydjArray.size()>0) {
				for (int i = 0; i < _11_ydjArray.size(); i++) {
					JSONObject object = _11_ydjArray.getJSONObject(i);
					JSONObject _11_YDJ = new JSONObject();
					_11_YDJ.put("winno", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
					dealWinInfoOpenTime(_11_YDJ, object); //期号和开奖时间
					_11_ydjResultArray.add(_11_YDJ);
				}
			}
			
			//足彩胜负彩
			JSONArray sfcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_SFC.lotNo(), num);
			if (sfcArray!=null && sfcArray.size()>0) {
				for (int i = 0; i < sfcArray.size(); i++) {
					JSONObject object = sfcArray.getJSONObject(i);
					JSONObject sfc = new JSONObject();
					sfc.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(sfc, object); //期号和开奖时间
					sfcResultArray.add(sfc);
				}
			}
			
			//足彩任选9
			JSONArray rx9Array = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_RX9.lotNo(), num);
			if (rx9Array!=null && rx9Array.size()>0) {
				for (int i = 0; i < rx9Array.size(); i++) {
					JSONObject object = rx9Array.getJSONObject(i);
					JSONObject rjc = new JSONObject();
					rjc.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(rjc, object); //期号和开奖时间
					rx9ResultArray.add(rjc);
				}
			}
			
			//足彩进球彩
			JSONArray jqcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_JQC.lotNo(), num); 
			if (jqcArray!=null && jqcArray.size()>0) {
				for (int i = 0; i < jqcArray.size(); i++) {
					JSONObject object = jqcArray.getJSONObject(i);
					JSONObject jqc = new JSONObject();
					jqc.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(jqc, object); //期号和开奖时间
					jqcResultArray.add(jqc);
				}
			}
			
			//足彩半全场
			JSONArray lcbArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_BQC.lotNo(), num);
			if (lcbArray!=null && lcbArray.size()>0) {
				for (int i = 0; i < lcbArray.size(); i++) {
					JSONObject object = lcbArray.getJSONObject(i);
					JSONObject bqc = new JSONObject();
					bqc.put("winno", object.getString("winbasecode")); //开奖号码
					dealWinInfoOpenTime(bqc, object); //期号和开奖时间
					lcbResultArray.add(bqc);
				}
			}
			
			
			responseJson.put("qlc", qlcResultArray);
			responseJson.put("ddd", dddResultArray);
			responseJson.put("ssq", ssqResultArray);
			
			responseJson.put("dlt", dltResultArray);
			responseJson.put("pl3", pl3ResultArray);
			responseJson.put("pl5", pl5ResultArray);
			responseJson.put("qxc", qxcResultArray);
			responseJson.put("22-5", _22_5ResultArray);
			
			responseJson.put("ssc", sscResultArray);
			responseJson.put("11-5", _11_5ResultArray);
			responseJson.put("11-ydj", _11_ydjResultArray);
			
			responseJson.put("sfc", sfcResultArray);
			responseJson.put("rx9", rx9ResultArray);
			responseJson.put("jqc", jqcResultArray);
			responseJson.put("6cb", lcbResultArray);
		} catch (Exception e) {
			logger.error("获取开奖信息发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 处理开奖号码的开奖时间
	 * @param responseJson
	 * @param valueObject
	 */
	public void dealWinInfoOpenTime(JSONObject responseJson, JSONObject valueObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String batchCode = valueObject.getJSONObject("id").getString("batchcode"); //期号
		String openTime = valueObject.getString("opentime"); //开奖时间
		if (Tools.isEmpty(openTime)||openTime.equals("null")) {
			openTime = "";
		} else {
			openTime = sdf.format(new Date(Long.parseLong(openTime)));
		}
		responseJson.put("lotno", batchCode);
		responseJson.put("date", openTime);
	}
	
}
