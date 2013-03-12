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
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeInfoUtil;

/**
 * 期号相关查询
 * @author Administrator
 *
 */
@Service
public class BatchCodeQueryService {

	private Logger logger = Logger.getLogger(BatchCodeQueryService.class);
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	/**
	 * 当前期号查询
	 * @param clientInfo
	 * @return
	 */
	public String getCurrentBatchCodeByLotNo(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			if (Tools.isEmpty(lotNo)) { //彩种为空
				return Tools.paramError(clientInfo.getImei());
			}
			String result = lotteryCommonService.getIssue(lotNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")&&valueString!=null&&!valueString.equals("null")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					String endBetTime = valueObject.getString("endbettime"); //投注截止时间
					
					JSONObject idObject = valueObject.getJSONObject("id");
					String batchcode = idObject.getString("batchcode"); // 期号
					
					long startTime = System.currentTimeMillis(); //系统当期时间
					
					String timeRemaining = ""; // 剩余时间
					if (Tools.isEmpty(endBetTime)||endBetTime.equals("null")) {
						endBetTime = "";
					} else {
						timeRemaining = (Long.parseLong(endBetTime)-startTime)/1000+""; 
						endBetTime = DateParseFormatUtil.formatYY_M_d_H_m(endBetTime+"");
					}
					
					responseJson.put(Constants.batchCode, batchcode); //期号
					responseJson.put(Constants.endTime, endBetTime); //投注截止时间
					responseJson.put(Constants.sysCurrentTime, System.currentTimeMillis()+""); //系统时间 
					responseJson.put(Constants.startTime, DateParseFormatUtil.formatYMd_Hms(startTime+"")); //格式化后的系统当前时间
					responseJson.put(Constants.timeRemaining, timeRemaining); //离截止时间剩余的秒数
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "获取成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "查询失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("当前期号查询发生异常", e);
		}
		return responseJson.toString();
	}

	/**
	 * 足彩预售期号查询
	 * @param clientInfo
	 * @return
	 */
	public String getZCIssue(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");
			String lotNo = clientInfo.getLotNo(); //彩种编号
			if (Tools.isEmpty(lotNo)) { //彩种编号为空
				return Tools.paramError(clientInfo.getImei());
			}
			String result = lotteryCommonService.getZCIssue(clientInfo.getLotNo());
			if (Tools.isEmpty(result)) { //返回结果为空
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")&&valueString!=null&&!valueString.equals("null")) {
					JSONArray valueArray = fromObject.getJSONArray("value");
					for (int i = 0; i < valueArray.size(); i++) {
						JSONObject valueObject = valueArray.getJSONObject(i);
						String endTime = valueObject.getString("endbettime"); //截止时间
						
						long endSecond = 0;
						String endTimeString = "";
						if (!Tools.isEmpty(endTime)&&!endTime.trim().equals("null")) {
							endTimeString = sdf.format(new Date(Long.parseLong(endTime)));
							endSecond = (Long.parseLong(endTime)-System.currentTimeMillis())/1000;
							if (endSecond < 0) {
								endSecond = 0;
							}
						}
						
						JSONObject idObject = valueObject.getJSONObject("id");
						String batchCode = idObject.getString("batchcode"); //期号
						
						JSONObject object = new JSONObject();
						object.put("batchCode", batchCode); //期号
						object.put("endTime", endTimeString); //期结时间
						object.put("endSecond", endSecond); //离期结时间还有多少秒
						resultArray.add(object);
					}
					
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "查询成功");
					responseJson.put("result", resultArray);
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("足彩预售期号查询发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 查询当前期以后的期号
	 * @param clientInfo
	 * @return
	 */
	public String getAfterIssue(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			String batchNum = clientInfo.getBatchnum(); //期数
			if (Tools.isEmpty(lotNo)||Tools.isEmpty(batchNum)) { //彩种和期数为空
				return Tools.paramError(clientInfo.getImei());
			}
			//String currentBatchCode = lotteryCommonService.getCurrentBatchCodeByLotNo(lotNo); //获取当前期号
			String currentBatchCode = commonUtil.getCurrentBatchCodeByLotNo(lotNo); //获取当前期号
			if (!Tools.isEmpty(currentBatchCode)) {
				String result = lotteryCommonService.getAfterIssue(lotNo, currentBatchCode, (Integer.parseInt(batchNum)-1)+"");
				if (!Tools.isEmpty(result)&&!result.equals("null")) {
					JSONArray valueArray = JSONArray.fromObject(result);
					if (valueArray!=null&&valueArray.size()>0) {
						for (int i = 0; i < valueArray.size(); i++) {
							JSONObject valueObject = valueArray.getJSONObject(i);
							JSONObject idObject = valueObject.getJSONObject("id");
							String batchCode = idObject.getString("batchcode"); //期号
							
							JSONObject object = new JSONObject();
							object.put("batchCode", batchCode); //期号
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
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
			logger.error("查询当前期以后的期号发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获取所有彩种的当前期号
	 * @return
	 */
	public JSONObject getAllCurrentBatchCode() {
		JSONObject resultJson = new JSONObject();
		
		try {
			JSONObject qlcResultJson = new JSONObject(); //七乐彩
			JSONObject dddResultJson = new JSONObject(); //福彩3D
			JSONObject ssqResultJson = new JSONObject(); //双色球
			JSONObject dltResultJson = new JSONObject(); //大乐透
			JSONObject plsResultJson = new JSONObject(); //排列三
			JSONObject plwResultJson = new JSONObject(); //排列五
			JSONObject qxcResultJson = new JSONObject(); //七星彩
			JSONObject tt_fResultJson = new JSONObject(); //22选5
			JSONObject sscResultJson = new JSONObject(); //时时彩
			JSONObject oo_fResultJson = new JSONObject(); //江西11选五
			JSONObject oo_ydjResultJson = new JSONObject(); //11运夺金
			JSONObject zc_sfcResultJson = new JSONObject(); //足彩胜负彩
			JSONObject zc_rx9ResultJson = new JSONObject(); //足彩任选九
			JSONObject zc_jqcResultJson = new JSONObject(); //足彩进球彩
			JSONObject zc_bqcResultJson = new JSONObject(); //足彩半全场
			
			JSONObject valueObject = cacheCommonUtil.getAllCurrentBatchCodeValueObject(); //获取期号
			//七乐彩
			JSONObject qlcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.QLC.lotNo());
			if (qlcObject!=null) {
				qlcResultJson.put("batchCode", qlcObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = qlcObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(qlcResultJson, endBetTime);
			}
			
			//福彩3D
			JSONObject dddObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.DDD.lotNo());
			if (dddObject!=null) {
				dddResultJson.put("batchCode", dddObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = dddObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(dddResultJson, endBetTime);
			}
			
			//双色球
			JSONObject ssqObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.SSQ.lotNo());
			if (ssqObject!=null) {
				ssqResultJson.put("batchCode", ssqObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = ssqObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(ssqResultJson, endBetTime);
			}
			
			//大乐透
			JSONObject dltObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.DLT.lotNo());
			if (dltObject!=null) {
				dltResultJson.put("batchCode", dltObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = dltObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(dltResultJson, endBetTime);
			}
			
			//排列三
			JSONObject plsObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.PLS.lotNo());
			if (plsObject!=null) {
				plsResultJson.put("batchCode", plsObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = plsObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(plsResultJson, endBetTime);
			}
			
			//排列五
			JSONObject plwObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.PLW.lotNo());
			if (plwObject!=null) {
				plwResultJson.put("batchCode", plwObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = plwObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(plwResultJson, endBetTime);
			}
			
			//七星彩
			JSONObject qxcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.QXC.lotNo());
			if (qxcObject!=null) {
				qxcResultJson.put("batchCode", qxcObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = qxcObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(qxcResultJson, endBetTime);
			}
			
			//22选5
			JSONObject tt_fObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.TT_F.lotNo());
			if (tt_fObject!=null) {
				tt_fResultJson.put("batchCode", tt_fObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = tt_fObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(tt_fResultJson, endBetTime);
			}
			
			//时时彩
			JSONObject sscObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.SSC.lotNo());
			if (sscObject!=null) {
				sscResultJson.put("batchCode", sscObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = sscObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(sscResultJson, endBetTime);
			}
			
			//江西11选五
			JSONObject oo_fObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.OO_F.lotNo());
			if (oo_fObject!=null) {
				oo_fResultJson.put("batchCode", oo_fObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = oo_fObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(oo_fResultJson, endBetTime);
			}
			
			//11运夺金
			JSONObject oo_ydjObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.OO_YDJ.lotNo());
			if (oo_ydjObject!=null) {
				oo_ydjResultJson.put("batchCode", oo_ydjObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = oo_ydjObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(oo_ydjResultJson, endBetTime);
			}
			
			//足彩胜负彩
			JSONObject sfcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.ZC_SFC.lotNo());
			if (sfcObject!=null) {
				zc_sfcResultJson.put("batchCode", sfcObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = sfcObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(zc_sfcResultJson, endBetTime);
			}
			
			//足彩任选9
			JSONObject rx9Object = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.ZC_RX9.lotNo());
			if (rx9Object!=null) {
				zc_rx9ResultJson.put("batchCode", rx9Object.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = rx9Object.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(zc_rx9ResultJson, endBetTime);
			}
			
			//足彩进球彩
			JSONObject jqcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.ZC_JQC.lotNo());
			if (jqcObject!=null) {
				zc_jqcResultJson.put("batchCode", jqcObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = jqcObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(zc_jqcResultJson, endBetTime);
			}
			
			//足彩半全场
			JSONObject bqcObject = commonUtil.getCurrentBatchCodeValueObject(valueObject, LotType.ZC_BQC.lotNo());
			if (bqcObject!=null) {
				zc_bqcResultJson.put("batchCode", bqcObject.getJSONObject("id").getString("batchcode")); //期号
				String endBetTime = bqcObject.getString("endbettime"); //投注截止时间
				LotTypeInfoUtil.dealCurrentBatchCodeEndTime(zc_bqcResultJson, endBetTime);
			}

			
			resultJson.put(LotType.QLC.lotNo(), qlcResultJson.toString()); //七乐彩
			resultJson.put(LotType.DDD.lotNo(), dddResultJson.toString()); //福彩3D
			resultJson.put(LotType.SSQ.lotNo(), ssqResultJson.toString()); //双色球
			
			resultJson.put(LotType.DLT.lotNo(), dltResultJson.toString()); //大乐透
			resultJson.put(LotType.PLS.lotNo(), plsResultJson.toString()); //排列三
			resultJson.put(LotType.PLW.lotNo(), plwResultJson.toString()); //排列五
			resultJson.put(LotType.QXC.lotNo(), qxcResultJson.toString()); //七星彩
			resultJson.put(LotType.TT_F.lotNo(), tt_fResultJson.toString()); //22选5
			
			resultJson.put(LotType.SSC.lotNo(), sscResultJson.toString()); //时时彩
			resultJson.put(LotType.OO_F.lotNo(), oo_fResultJson.toString()); //十一选五
			resultJson.put(LotType.OO_YDJ.lotNo(), oo_ydjResultJson.toString()); //十一运夺金
			
			resultJson.put(LotType.ZC_SFC.lotNo(), zc_sfcResultJson.toString()); //足彩胜负彩
			resultJson.put(LotType.ZC_RX9.lotNo(), zc_rx9ResultJson.toString()); //足彩任选9
			resultJson.put(LotType.ZC_JQC.lotNo(), zc_jqcResultJson.toString()); //足彩进球彩
			resultJson.put(LotType.ZC_BQC.lotNo(), zc_bqcResultJson.toString()); //足彩半全场
		} catch (Exception e) {
			logger.error("获取所有彩种的当前期号发生异常", e);
		}
		
		return resultJson;
	}
	
}
