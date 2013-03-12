package com.ruyicai.lotserver.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.back.PrizeDataService;
import com.ruyicai.lotserver.service.back.RankingService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.JingCaiUtil;

/**
 * 缓存公共类
 * @author Administrator
 *
 */
@Service
public class CacheCommonUtil {

	//private static Logger logger = Logger.getLogger(CacheCommonUtil.class);
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private PrizeDataService prizeDataService;
	
	@Autowired
	private RankingService rankingService;
	
	/**
	 * 根据彩种获取最新的试机号
	 * @param lotNo
	 * @return
	 */
	public JSONObject getLatestTryCodeValueObjectByLotNo(String lotNo) {
		JSONObject valueObject = cacheService.get("lotserver_latestTryCode_"+lotNo);
		if (valueObject==null) {
			valueObject = commonService.getLatestTryCodeValueObjectByLotNo(lotNo);
			if (valueObject!=null) {
				cacheService.set("lotserver_latestTryCode_"+lotNo, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 根据彩种和期号获取试机号
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public JSONObject getTryCodeValueObjectByLotNoAndBatchCode(String lotNo, String batchCode) {
		JSONObject valueObject = cacheService.get("lotserver_tryCode_"+lotNo+"_"+batchCode);
		if (valueObject==null) {
			valueObject = commonService.getTryCodeValueObjectByLotNo(lotNo, batchCode);
			if (valueObject!=null) {
				cacheService.set("lotserver_tryCode_"+lotNo+"_"+batchCode, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取所有彩种的当前期号
	 * @return
	 */
	public JSONObject getAllCurrentBatchCodeValueObject() {
		JSONObject valueObject = cacheService.get("lotserver_currentBatchCode");
		if (valueObject==null) {
			valueObject = commonService.getAllCurrentBatchCode();
			if (valueObject!=null) {
				cacheService.set("lotserver_currentBatchCode", valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 根据彩种获取当前期号
	 * @param lotNo
	 * @return
	 */
	public JSONObject getCurrentBatchCodeValueObjectByLotNo(String lotNo) {
		JSONObject valueObject = cacheService.get("lotserver_currentBatchCode_"+lotNo);
		if (valueObject==null) {
			valueObject = commonService.getCurrentBatchCodeByLotNo(lotNo);
			if (valueObject!=null) {
				cacheService.set("lotserver_currentBatchCode_"+lotNo, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取所有彩种的开奖公告
	 * @param num
	 * @return
	 */
	public JSONObject getWinInfoValueObject(Integer num) {
		JSONObject valueObject = cacheService.get("lotserver_winInfo_"+num); //从缓存读数据
		if (valueObject==null) {
			valueObject = commonService.getAllWinInfoByNum(num);
			cacheService.set("lotserver_winInfo_"+num, valueObject);
		}
		return valueObject;
	}
	
	/**
	 * 根据彩种和期数获取开奖公告
	 * @param lotNo
	 * @param num
	 * @return
	 */
	public JSONArray getWinInfoValueArrayByLotNoAndNum(String lotNo, Integer num) {
		JSONArray valueArray = cacheService.get("lotserver_winInfo_"+lotNo+"_"+num);
		if (valueArray==null) {
			valueArray = commonService.getWinInfoByLotNoAndNum(lotNo, num);
			if (valueArray!=null&&valueArray.size()>0) {
				cacheService.set("lotserver_winInfo_"+lotNo+"_"+num, valueArray);
			}
		}
		return valueArray;
	}
	
	/**
	 * 根据orderId获取tlots
	 * @param isCaseLot
	 * @param orderId
	 * @return
	 */
	public JSONObject getTlotsByOrderIdValueObject( String orderId, boolean isOutTicket) {
		JSONObject valueObject = null;
		if (isOutTicket) { //已出票
			valueObject = cacheService.get("lotserver_tlotsByOrderId_"+orderId);
			if (valueObject==null) {
				String result = lotteryService.getToltsByOrderId(orderId);
				if (!Tools.isEmpty(result)) {
					JSONObject fromObject = JSONObject.fromObject(result);
					if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
							&&fromObject.getString("errorCode").equals("0")) {
						valueObject = fromObject.getJSONObject("value");
						//判断是否要根据订单号缓存Tlots
						boolean isCache = isCacheTlotsByOrderId(valueObject);
						if (isCache) {
							cacheService.set("lotserver_tlotsByOrderId_"+orderId, valueObject);
						}
					}
				}
			}
		} else { //合买(如果未出票就调用拆票的接口)
			String result = lotteryService.getTlotsByOrderidWrapper(orderId);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
						&&fromObject.getString("errorCode").equals("0")) {
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
	/**
	 * 判断是否要根据订单号缓存Tlots
	 * @param valueObject
	 * @return
	 */
	private boolean isCacheTlotsByOrderId(JSONObject valueObject) {
		boolean isCache = true;
		if (valueObject!=null) {
			JSONArray list = valueObject.getJSONArray("list");
			if (list!=null&&list.size()>0) {
				for (int i = 0; i < list.size(); i++) {
					JSONObject tlotObject = list.getJSONObject(i);
					String peiLvString = tlotObject.getString("peilu"); //赔率
					if (Tools.isEmpty(peiLvString)||peiLvString.equals("null")) { //赔率为空
						isCache = false;
						break;
					}
				}
			}
		}
		return isCache;
	}
	
	/**
	 * 获取竞彩某场比赛的信息
	 * @param lotno
	 * @param day
	 * @param weekid
	 * @param teamid
	 * @return
	 */
	public JSONObject getJingCaiMatchesValueObject(String lotNo, String day, String weekId, String teamId) {
		String jingCaiType = JingCaiUtil.getJingCaiTypeByLotNo(lotNo);
		JSONObject valueObject = cacheService.get("lotserver_jingCaiMatches_"+jingCaiType+"_"+day+"_"+weekId+"_"+teamId);
		if (valueObject==null) {
			valueObject = commonService.getJingCaiMatchesValueObject(lotNo, day, weekId, teamId);
			if (valueObject!=null) {
				cacheService.set("lotserver_jingCaiMatches_"+jingCaiType+"_"+day+"_"+weekId+"_"+teamId, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取遗漏值
	 * @param lotNo
	 * @param sellway
	 * @return
	 */
	public String getMissValueString(String lotNo, String sellway) {
		String valueString = "";
		//获取上一期期号
		String batchCode = "";
		JSONArray valueArray = getWinInfoValueArrayByLotNoAndNum(lotNo, 1);
		if (valueArray!=null&&valueArray.size()>0) {
			JSONObject valueObject = valueArray.getJSONObject(0);
			batchCode = valueObject.getJSONObject("id").getString("batchcode");
		}
		if (!Tools.isEmpty(batchCode)&&!Tools.isEmpty(sellway)) {
			valueString = cacheService.get("lotserver_missValue_"+lotNo+"_"+sellway+"_"+batchCode);
			if (valueString==null) {
				String result = prizeDataService.getMissValue(lotNo, batchCode, sellway);
				if (!Tools.isEmpty(result)) {
					JSONObject fromObject = JSONObject.fromObject(result);
					if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
							&&fromObject.getString("errorCode").equals("0")) {
						valueString = fromObject.getString("value");
						cacheService.set("lotserver_missValue_"+lotNo+"_"+sellway+"_"+batchCode, valueString);
					}
				}
			}
		}
		return valueString;
	}
	
	/**
	 * 获取中奖排行
	 * @param type
	 * @return
	 */
	public JSONArray getPrizeBankValueArray(String type) {
		JSONArray valueArray = null;
		//周排行
		if (type!=null&&type.equals("week")) {
			valueArray = cacheService.get("lotserver_prizeBank_week");
			if (valueArray==null) {
				String weekResult = rankingService.queryPrizeRankWeek();
				if (!Tools.isEmpty(weekResult)) {
					JSONObject weekObject = JSONObject.fromObject(weekResult);
					if (weekObject!= null&&!Tools.isEmpty(weekObject.getString("errorCode"))
							&&weekObject.getString("errorCode").equals("0")) {
						String valueString = weekObject.getString("value");
						valueArray = JSONArray.fromObject(valueString);
						cacheService.set("lotserver_prizeBank_week", valueArray);
					}
				}
			}
		} else if (type!=null&&type.equals("month")) { //月排行
			valueArray = cacheService.get("lotserver_prizeBank_month");
			if (valueArray==null) {
				String monthResult = rankingService.queryPrizeRankMonth();
				if (!Tools.isEmpty(monthResult)) {
					JSONObject monthObject = JSONObject.fromObject(monthResult);
					if (monthObject!=null&&!Tools.isEmpty(monthObject.getString("errorCode"))
							&&monthObject.getString("errorCode").equals("0")) {
						String valueString = monthObject.getString("value");
						valueArray = JSONArray.fromObject(valueString);
						cacheService.set("lotserver_prizeBank_month", valueArray);
					}
				}
			}
		} else if (type!=null&&type.equals("total")) { //总排行
			valueArray = cacheService.get("lotserver_prizeBank_total");
			if (valueArray==null) {
				String totalResult = rankingService.queryPrizeRankTotal();
				if (!Tools.isEmpty(totalResult)) {
					JSONObject totalObject = JSONObject.fromObject(totalResult);
					if (totalObject!=null&&!Tools.isEmpty(totalObject.getString("errorCode"))
							&&totalObject.getString("errorCode").equals("0")) {
						String valueString = totalObject.getString("value");
						valueArray = JSONArray.fromObject(valueString);
						cacheService.set("lotserver_prizeBank_total", valueArray);
					}
				}
			}
		}
		
		return valueArray;
	}
	
	/**
	 * 获取开奖公告明细
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public JSONObject getWinInfoDetailByLotNoAndBatchCode(String lotNo, String batchCode) {
		JSONObject valueObject = cacheService.get("lotserver_winInfoDetail_"+lotNo+"_"+batchCode);
		if (valueObject==null) {
			valueObject = commonService.getWinInfoDetailValueObject(lotNo, batchCode);
			if (valueObject!=null) {
				cacheService.set("lotserver_winInfoDetail_"+lotNo+"_"+batchCode, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取期信息
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public JSONObject getBatchCodeInfoValueObject(String lotNo, String batchCode) {
		JSONObject valueObject = cacheService.get("lotserver_batchCodeInfo_"+lotNo+"_"+batchCode);
		if (valueObject==null) {
			valueObject = commonService.getBatchCodeInfoValueObject(lotNo, batchCode);
			if (valueObject!=null) {
				cacheService.set("lotserver_batchCodeInfo_"+lotNo+"_"+batchCode, valueObject);
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取足彩赛事信息
	 * @param lotNo
	 * @param batchCode
	 * @param teamId
	 * @return
	 */
	public JSONObject getZuCaiMatchInfoValueObject(String lotNo, String batchCode, String teamId) {
		JSONObject valueObject = cacheService.get("lotserver_zuCaiMatches_"+lotNo+"_"+batchCode+"_"+teamId);
		if (valueObject==null) {
			valueObject = commonService.getZuCaiMatchInfoValueObject(lotNo, batchCode, teamId);
			if (valueObject!=null) {
				cacheService.set("lotserver_zuCaiMatches_"+lotNo+"_"+batchCode+"_"+teamId, valueObject);
			}
		}
		return valueObject;
	}
	
}
