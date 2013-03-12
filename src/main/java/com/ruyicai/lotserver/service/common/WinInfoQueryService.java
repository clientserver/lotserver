package com.ruyicai.lotserver.service.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.lottype.fc.ddd.Ddd;
import com.ruyicai.lotserver.lottype.fc.qlc.Qlc;
import com.ruyicai.lotserver.lottype.fc.ssq.Ssq;
import com.ruyicai.lotserver.lottype.tc.dlt.Dlt;
import com.ruyicai.lotserver.lottype.tc.pls.Pls;
import com.ruyicai.lotserver.lottype.tc.plw.Plw;
import com.ruyicai.lotserver.lottype.tc.qxc.Qxc;
import com.ruyicai.lotserver.lottype.tc.ttx5.Ttx5;
import com.ruyicai.lotserver.lottype.zc.bqc.Bqc;
import com.ruyicai.lotserver.lottype.zc.jqc.Jqc;
import com.ruyicai.lotserver.lottype.zc.rjc.Rjc;
import com.ruyicai.lotserver.lottype.zc.sfc.Sfc;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeInfoUtil;

/**
 * 开奖公告相关查询
 * @author Administrator
 *
 */
@Service
public class WinInfoQueryService {

	private Logger logger = Logger.getLogger(WinInfoQueryService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 开奖公告
	 * @param clientInfo
	 * @return
	 */
	public String getWinInfo(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			JSONObject qlcResultObject = new JSONObject(); //七乐彩
			JSONObject ssqResultObject = new JSONObject(); //双色球
			JSONObject dddResultObject = new JSONObject(); //福彩3D
			
			JSONObject dltResultObject = new JSONObject(); //大乐透
			JSONObject pl3ResultObject = new JSONObject(); //排列三
			JSONObject pl5ResultObject = new JSONObject(); //排列五
			JSONObject qxcResultObject = new JSONObject(); //七星彩
			JSONObject _22_5ResultObject = new JSONObject(); //22选5
			
			JSONObject sscResultObject = new JSONObject(); //时时彩
			JSONObject _11_5ResultObject = new JSONObject(); //11选5
			JSONObject _11_ydjResultObject = new JSONObject(); //十一运夺金
			JSONObject gd_11_5ResultObject = new JSONObject(); //广东11选5
			JSONObject gd_happy_10ResultObject = new JSONObject(); //广东快乐十分
			
			JSONObject sfcResultObject = new JSONObject(); //胜负彩
			JSONObject rx9ResultObject = new JSONObject(); //任选9
			JSONObject jqcResultObject = new JSONObject(); //进球彩
			JSONObject lcbResultObject = new JSONObject(); //6场半
			
			int num =1; //开奖信息返回条数
			JSONObject valueObject = cacheCommonUtil.getWinInfoValueObject(num); //获取开奖公告
			/******************************福彩*******************************/
			//七乐彩
			JSONArray qlcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.QLC.lotNo(), num);
			if (qlcArray!=null && qlcArray.size()>0) {
				JSONObject object = qlcArray.getJSONObject(0);
				String specialCode = object.getString("winspecialcode"); //特殊号码
				if (Tools.isEmpty(specialCode)||specialCode.equals("null")) {
					specialCode = "";
				}
				qlcResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")+specialCode); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(qlcResultObject, object); //期号和开奖时间
			}
			
			//福彩3D
			JSONArray dddArray = commonUtil.getWinInfoValueArray(valueObject, LotType.DDD.lotNo(), num);
			if (dddArray!=null && dddArray.size()>0) {
				JSONObject object = dddArray.getJSONObject(0);
				//获取试机号
				String tryCode = "";
				JSONObject latestTryCodeValueObject = cacheCommonUtil.getLatestTryCodeValueObjectByLotNo(LotType.DDD.lotNo());
				if (latestTryCodeValueObject!=null) {
					tryCode = latestTryCodeValueObject.getString("trycode");
				}
				dddResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				dddResultObject.put("tryCode", tryCode); //试机号
				LotTypeInfoUtil.dealWinInfoOpenTime(dddResultObject, object); //期号和开奖时间
			}
			
			//双色球
			JSONArray ssqArray = commonUtil.getWinInfoValueArray(valueObject, LotType.SSQ.lotNo(), num);
			if (ssqArray!=null && ssqArray.size()>0) {
				JSONObject object = ssqArray.getJSONObject(0);
				String specialCode = object.getString("winspecialcode"); //特殊号码
				if (Tools.isEmpty(specialCode)||specialCode.equals("null")) {
					specialCode = "";
				}
				ssqResultObject.put("winCode", object.getString("winbasecode")+specialCode); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(ssqResultObject, object); //期号和开奖时间
			}
			
			/******************************体彩*******************************/
			//大乐透
			JSONArray dltArray = commonUtil.getWinInfoValueArray(valueObject, LotType.DLT.lotNo(), num);
			if (dltArray!=null && dltArray.size()>0) {
				JSONObject object = dltArray.getJSONObject(0);
				dltResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(dltResultObject, object); //期号和开奖时间
			}
			
			//排列三
			JSONArray plsArray = commonUtil.getWinInfoValueArray(valueObject, LotType.PLS.lotNo(), num);
			if (plsArray!=null && plsArray.size()>0) {
				JSONObject object = plsArray.getJSONObject(0);
				pl3ResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(pl3ResultObject, object); //期号和开奖时间
			}
			
			//排列五
			JSONArray pl5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.PLW.lotNo(), num);
			if(pl5Array!=null && pl5Array.size()>0) {
				JSONObject object = pl5Array.getJSONObject(0);
				pl5ResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(pl5ResultObject, object); //期号和开奖时间
			}
			
			//七星彩
			JSONArray qxcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.QXC.lotNo(), num);
			if (qxcArray!=null && qxcArray.size()>0) {
				JSONObject object = qxcArray.getJSONObject(0);
				qxcResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(qxcResultObject, object); //期号和开奖时间
			}
			
			//22选5
			JSONArray _22_5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.TT_F.lotNo(), num);
			if (_22_5Array!=null && _22_5Array.size()>0) {
				JSONObject object = _22_5Array.getJSONObject(0);
				_22_5ResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(_22_5ResultObject, object); //期号和开奖时间
			}
			
			/******************************高频彩*******************************/
			//时时彩
			JSONArray sscArray = commonUtil.getWinInfoValueArray(valueObject, LotType.SSC.lotNo(), num);
			if (sscArray!=null && sscArray.size()>0) {
				JSONObject object = sscArray.getJSONObject(0);
				sscResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(sscResultObject, object); //期号和开奖时间
			}
			
			//11选5
			JSONArray _11_5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.OO_F.lotNo(), num);
			if (_11_5Array!=null && _11_5Array.size()>0) {
				JSONObject object = _11_5Array.getJSONObject(0);
				_11_5ResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(_11_5ResultObject, object); //期号和开奖时间
			}
			
			//11运夺金
			JSONArray _11_ydjArray = commonUtil.getWinInfoValueArray(valueObject, LotType.OO_YDJ.lotNo(), num);
			if (_11_ydjArray!=null && _11_ydjArray.size()>0) {
				JSONObject object = _11_ydjArray.getJSONObject(0);
				_11_ydjResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(_11_ydjResultObject, object); //期号和开奖时间
			}
			
			//广东11选5
			JSONArray gd_11_5Array = commonUtil.getWinInfoValueArray(valueObject, LotType.GDOO_F.lotNo(), num);
			if (gd_11_5Array!=null && gd_11_5Array.size()>0) {
				JSONObject object = gd_11_5Array.getJSONObject(0);
				gd_11_5ResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(gd_11_5ResultObject, object); //期号和开奖时间
			}
			
			//广东快乐十分
			JSONArray gd_happy_10Array = commonUtil.getWinInfoValueArray(valueObject, LotType.GDH_T.lotNo(), num);
			if (gd_happy_10Array!=null && gd_happy_10Array.size()>0) {
				JSONObject object = gd_happy_10Array.getJSONObject(0);
				gd_happy_10ResultObject.put("winCode", object.getString("winbasecode").replaceAll(" ", "")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(gd_happy_10ResultObject, object); //期号和开奖时间
			}
			
			/******************************足彩*******************************/
			//足彩胜负彩
			JSONArray sfcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_SFC.lotNo(), num);
			if (sfcArray!=null && sfcArray.size()>0) {
				JSONObject object = sfcArray.getJSONObject(0);
				sfcResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(sfcResultObject, object); //期号和开奖时间
			}
			
			//足彩任选9
			JSONArray rx9Array = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_RX9.lotNo(), num);
			if (rx9Array!=null && rx9Array.size()>0) {
				JSONObject object = rx9Array.getJSONObject(0);
				rx9ResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(rx9ResultObject, object); //期号和开奖时间
			}
			
			//足彩进球彩
			JSONArray jqcArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_JQC.lotNo(), num);
			if (jqcArray!=null && jqcArray.size()>0) {
				JSONObject object = jqcArray.getJSONObject(0);
				jqcResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(jqcResultObject, object); //期号和开奖时间
			}
			
			//足彩半全场
			JSONArray lcbArray = commonUtil.getWinInfoValueArray(valueObject, LotType.ZC_BQC.lotNo(), num);
			if (lcbArray!=null && lcbArray.size()>0) {
				JSONObject object = lcbArray.getJSONObject(0);
				lcbResultObject.put("winCode", object.getString("winbasecode")); //开奖号码
				LotTypeInfoUtil.dealWinInfoOpenTime(lcbResultObject, object); //期号和开奖时间
			}
			
			
			responseJson.put("qlc", qlcResultObject);
			responseJson.put("ddd", dddResultObject);
			responseJson.put("ssq", ssqResultObject);
			
			responseJson.put("dlt", dltResultObject);
			responseJson.put("pl3", pl3ResultObject);
			responseJson.put("pl5", pl5ResultObject);
			responseJson.put("qxc", qxcResultObject);
			responseJson.put("22-5", _22_5ResultObject);
			
			responseJson.put("ssc", sscResultObject);
			responseJson.put("11-5", _11_5ResultObject);
			responseJson.put("11-ydj", _11_ydjResultObject);
			responseJson.put("gd-11-5", gd_11_5ResultObject);
			responseJson.put("gd-happy-10", gd_happy_10ResultObject);
			
			responseJson.put("sfc", sfcResultObject);
			responseJson.put("rx9", rx9ResultObject);
			responseJson.put("jqc", jqcResultObject);
			responseJson.put("6cb", lcbResultObject);
		} catch (Exception e) {
			logger.error("开奖公告新(每个彩种返回上一期开奖号码)发生异常", e);
		}
		//下次通知联网时间,距现在多少秒
		responseJson.put("noticeTime", "600");
		return responseJson.toString();
	}
	
	/**
	 * 开奖公告明细查询
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String getWinInfoDetail(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String lotNo = clientInfo.getLotNo(); //彩种编号
			String batchCode = clientInfo.getBatchCode(); //期号
			if (Tools.isEmpty(lotNo)||Tools.isEmpty(batchCode)) { // 如果lotNo或batchCode为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			//获取明细
			JSONObject valueObject = cacheCommonUtil.getWinInfoDetailByLotNoAndBatchCode(lotNo, batchCode); 
			if (valueObject!=null) { // 获取成功
				String winBaseCode = valueObject.getString("winbasecode"); //基本号码
				String winSpecialCode = valueObject.getString("winspecialcode"); //特殊号码
				String openTime = valueObject.getString("opentime"); //开奖日期
				
				// 基本号码
				if (Tools.isEmpty(winBaseCode)||winBaseCode.equals("null")) {
					winBaseCode = "";
				}
				// 特殊号码
				if (Tools.isEmpty(winSpecialCode)||winSpecialCode.equals("null")) {
					winSpecialCode = "";
				}
				// 开奖号码
				String winNo = ""; 
				if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { // 大乐透
					winNo = winBaseCode + winSpecialCode;
				} else { //其他彩种
					winNo = winBaseCode.replaceAll(" ", "")+winSpecialCode.replaceAll(" ", "");
				}
				// 开奖日期
				if (Tools.isEmpty(openTime)||openTime.equals("null")) {
					openTime = "";
				} else {
					openTime = sdf.format(new Date(Long.parseLong(openTime)));
				}
				//openTime = (Tools.isEmpty(openTime)||openTime.equals("null"))?"":sdf.format(new Date(Long.parseLong(openTime)));
				
				//返回信息
				responseJson.put(Constants.error_code, ErrorCode.success.value());
				responseJson.put(Constants.message, "查询成功");
				responseJson.put("lotNo", lotNo); // 彩种
				responseJson.put("batchCode", batchCode); // 期号
				responseJson.put("winNo", winNo); //开奖号码
				responseJson.put("openTime", openTime); 

				// 开奖详情
				String info = valueObject.getString("info");
				if (lotNo!=null&&lotNo.trim().equals(LotType.SSQ.lotNo())) { // 双色球
					Ssq.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.DDD.lotNo())) { // 福彩3D
					Ddd.parseWinInfoDetail(responseJson, info);
					//试机号
					String tryCode = "";
					JSONObject tryCodeValueObject = cacheCommonUtil.getTryCodeValueObjectByLotNoAndBatchCode(LotType.DDD.lotNo(), batchCode);
					if (tryCodeValueObject!=null) {
						tryCode = tryCodeValueObject.getString("trycode");
					}
					responseJson.put("tryCode", tryCode);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.QLC.lotNo())) { // 七乐彩
					Qlc.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.PLS.lotNo())) { // 排列三
					Pls.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.DLT.lotNo())) { // 大乐透
					Dlt.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.PLW.lotNo())) { // 排列五
					Plw.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.QXC.lotNo())) { // 七星彩
					Qxc.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.TT_F.lotNo())) { // 22选5
					Ttx5.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.ZC_SFC.lotNo())) { //足彩-胜负彩
					Sfc.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.ZC_RX9.lotNo())) { //足彩-任选9
					Rjc.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.ZC_JQC.lotNo())) { //足彩-进球彩
					Jqc.parseWinInfoDetail(responseJson, info);
				} else if (lotNo!=null&&lotNo.trim().equals(LotType.ZC_BQC.lotNo())) { //足彩-半全场
					Bqc.parseWinInfoDetail(responseJson, info);
				}
			} else { // 获取失败
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("开奖公告明细查询发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 开奖公告查询（分页）
	 * @param clientInfo
	 * @return
	 */
	public String getWinInfoList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			if (Tools.isEmpty(lotNo)) { //彩种为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			String pageIndex = clientInfo.getPageindex(); //当前页数(第一页pageIndex=1)
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			//处理安卓pageIndex传参错误
			Integer newPageIndex = Integer.parseInt(pageIndex)-1<0?0:Integer.parseInt(pageIndex)-1;
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("lotno=" + lotNo);
			paramStr.append("&pageIndex=" + newPageIndex);
			paramStr.append("&maxResult=" + maxResult);
			
			String url = propertiesUtil.getLotteryUrl() + "select/getwininfolist";
			String resultString = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("开奖公告查询(分页)返回:"+resultString+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(resultString)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(resultString);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if(list != null && list.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject  winObject = list.getJSONObject(i);
							String batchCode = winObject.getJSONObject("id").getString("batchcode"); //期号
							String winBaseCode = winObject.getString("winbasecode"); //基础号码
							String winSpecialCode = winObject.getString("winspecialcode"); //特殊号码
							String openTime = winObject.getString("opentime"); //开奖时间
							
							//特殊号码
							if(Tools.isEmpty(winSpecialCode)||winSpecialCode.equals("null")) {
								winSpecialCode = "";
							}
							//中奖号码
							String winCode = ""; 
							if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //大乐透
								winCode = winBaseCode+winSpecialCode;
							} else { //其他彩种
								winCode = winBaseCode.replaceAll(" ", "")+winSpecialCode.replaceAll(" ", "");
							}
							//开奖时间
							if (Tools.isEmpty(openTime)||openTime.equals("null")) {
								openTime = ""; 
							} else {
								openTime = DateParseFormatUtil.formatY_M_d(winObject.getString("opentime"));
							}
							//试机号
							String tryCode = "";
							if (lotNo!=null&&lotNo.trim().equals(LotType.DDD.lotNo())) { // 福彩3D
								JSONObject tryCodeValueObject = cacheCommonUtil.getTryCodeValueObjectByLotNoAndBatchCode(LotType.DDD.lotNo(), batchCode);
								if (tryCodeValueObject!=null) {
									tryCode = tryCodeValueObject.getString("trycode");
								}
							}
							
							JSONObject object = new JSONObject();
							object.put("batchCode", batchCode); //期号
							object.put("winCode", winCode); //开奖号码
							object.put("tryCode", tryCode); //试机号
							object.put("openTime", openTime); //开奖时间
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("totalPage", totalPage);
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("开奖公告查询（分页）发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
}
