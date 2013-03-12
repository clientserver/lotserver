package com.ruyicai.lotserver.service.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.domain.News;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.DataAnalysisService;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.common.Tools;

@Service
public class CommonService {

	private Logger logger = Logger.getLogger(CommonService.class);
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private DataAnalysisService dataAnalysisService;
	
	@Produce(uri = "jmsLotserver:topic:perfectUserInfoAddScore", context = "lotserverCamelContext")
	private ProducerTemplate perfectUserInfoAddScoreTemplate;
	
	/**
	 * 根据彩种和期数获取开奖信息
	 * @param lotNo
	 * @param num
	 * @return
	 */
	public JSONArray getWinInfoByLotNoAndNum(String lotNo, Integer num) {
		JSONArray resultArray = null;
		String result = lotteryService.getWinfolist(lotNo, num);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) { //正常返回
					resultArray = fromObject.getJSONArray("value");
				}
			}
		}
		return resultArray;
	}
	
	/**
	 * 根据期数获取所有
	 * @param num
	 * @return
	 */
	public JSONObject getAllWinInfoByNum(Integer num) {
		JSONObject resultObject = new JSONObject();
		//福彩
		String qlcLotNo= LotType.QLC.lotNo(); //七乐彩
		JSONArray qlcArray = getWinInfoByLotNoAndNum(qlcLotNo, num);
		resultObject.put(qlcLotNo, qlcArray);
		
		String dddLotNo = LotType.DDD.lotNo(); //福彩3d
		JSONArray dddArray = getWinInfoByLotNoAndNum(dddLotNo, num); 
		resultObject.put(dddLotNo, dddArray);
		
		String ssqLotNo = LotType.SSQ.lotNo(); //双色球
		JSONArray ssqArray = getWinInfoByLotNoAndNum(ssqLotNo, num);
		resultObject.put(ssqLotNo, ssqArray);
		
    	//体彩
		String dltLotNo = LotType.DLT.lotNo(); //大乐透
		JSONArray dltArray = getWinInfoByLotNoAndNum(dltLotNo, num);
		resultObject.put(dltLotNo, dltArray);
		
		String plsLotNo = LotType.PLS.lotNo(); //排列三
		JSONArray plsArray = getWinInfoByLotNoAndNum(plsLotNo, num); 
		resultObject.put(plsLotNo, plsArray);
		
		String plwLotNo = LotType.PLW.lotNo(); //排列五
		JSONArray plwArray = getWinInfoByLotNoAndNum(plwLotNo, num); 
		resultObject.put(plwLotNo, plwArray);
		
		String qxcLotNo = LotType.QXC.lotNo(); //七星彩
		JSONArray qxcArray = getWinInfoByLotNoAndNum(qxcLotNo, num); 
		resultObject.put(qxcLotNo, qxcArray);
		
		String _22_5LotNo = LotType.TT_F.lotNo(); //22选5
		JSONArray _22_5Array = getWinInfoByLotNoAndNum(_22_5LotNo, num); 
		resultObject.put(_22_5LotNo, _22_5Array);
		
    	//高频彩
		String sscLotNo = LotType.SSC.lotNo(); //时时彩
		JSONArray sscArray = getWinInfoByLotNoAndNum(sscLotNo, num); 
		resultObject.put(sscLotNo, sscArray);
		
		String _11_5LotNo = LotType.OO_F.lotNo(); //11选5
		JSONArray _11_5Array = getWinInfoByLotNoAndNum(_11_5LotNo, num); 
		resultObject.put(_11_5LotNo, _11_5Array);
		
		String _11_ydjLotNo = LotType.OO_YDJ.lotNo(); //11运夺金
		JSONArray _11_ydjArray = getWinInfoByLotNoAndNum(_11_ydjLotNo, num); 
		resultObject.put(_11_ydjLotNo, _11_ydjArray);
		
		String gd_11_5LotNo = LotType.GDOO_F.lotNo(); //广东11选5
		JSONArray gd_11_5Array = getWinInfoByLotNoAndNum(gd_11_5LotNo, num); 
		resultObject.put(gd_11_5LotNo, gd_11_5Array);
		
		String gd_happy_10LotNo = LotType.GDH_T.lotNo(); //广东快乐十分
		JSONArray gd_happy_10Array = getWinInfoByLotNoAndNum(gd_happy_10LotNo, num); 
		resultObject.put(gd_happy_10LotNo, gd_happy_10Array);
		
    	//足彩
		String sfcLotNo = LotType.ZC_SFC.lotNo(); //足彩胜负彩
		JSONArray sfcArray = getWinInfoByLotNoAndNum(sfcLotNo, num); 
		resultObject.put(sfcLotNo, sfcArray);
		
		String rx9LotNo = LotType.ZC_RX9.lotNo(); //足彩任选9
		JSONArray rx9Array = getWinInfoByLotNoAndNum(rx9LotNo, num); 
		resultObject.put(rx9LotNo, rx9Array);
		
		String jqcLotNo = LotType.ZC_JQC.lotNo(); //足彩进球彩
		JSONArray jqcArray = getWinInfoByLotNoAndNum(jqcLotNo, num); 
		resultObject.put(jqcLotNo, jqcArray);
		
		String lcbLotNo = LotType.ZC_BQC.lotNo();  //足彩半全场
		JSONArray lcbArray = getWinInfoByLotNoAndNum(lcbLotNo, num);
		resultObject.put(lcbLotNo, lcbArray);
		
		return resultObject;
	}
	
	/**
	 * 根据彩种获取当前期号
	 * @param lotNo
	 * @return
	 */
	public JSONObject getCurrentBatchCodeByLotNo(String lotNo) {
		JSONObject valueObject = null;
		String result = lotteryService.getIssue(lotNo);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")
						&&valueString!=null&&!valueString.equals("null")) { //正常返回
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取所有彩种的当前期号
	 * @return
	 */
	public JSONObject getAllCurrentBatchCode() {
		JSONObject resultObject = new JSONObject();
		
		//福彩
		String qlcLotNo = LotType.QLC.lotNo(); //七乐彩
		JSONObject qlcObject = getCurrentBatchCodeByLotNo(qlcLotNo); 
		resultObject.put(qlcLotNo, qlcObject);
		
		String dddLotNo = LotType.DDD.lotNo(); //福彩3D
		JSONObject dddObject = getCurrentBatchCodeByLotNo(dddLotNo);
		resultObject.put(dddLotNo, dddObject);
		
		String ssqLotNo = LotType.SSQ.lotNo(); //双色球
		JSONObject ssqObject = getCurrentBatchCodeByLotNo(ssqLotNo);
		resultObject.put(ssqLotNo, ssqObject);
		//体彩
		String dltLotNo = LotType.DLT.lotNo(); //大乐透
		JSONObject dltObject = getCurrentBatchCodeByLotNo(dltLotNo);
		resultObject.put(dltLotNo, dltObject);
		
		String plsLotNo = LotType.PLS.lotNo(); //排列三
		JSONObject plsObject = getCurrentBatchCodeByLotNo(plsLotNo);
		resultObject.put(plsLotNo, plsObject);
		
		String plwLotNo = LotType.PLW.lotNo(); //排列五
		JSONObject plwObject = getCurrentBatchCodeByLotNo(plwLotNo);
		resultObject.put(plwLotNo, plwObject);
		
		String qxcLotNo = LotType.QXC.lotNo(); //七星彩
		JSONObject qxcObject = getCurrentBatchCodeByLotNo(qxcLotNo);
		resultObject.put(qxcLotNo, qxcObject);
		
		String _22_5LotNo = LotType.TT_F.lotNo(); //22选5
		JSONObject _22_5Object = getCurrentBatchCodeByLotNo(_22_5LotNo);
		resultObject.put(_22_5LotNo, _22_5Object);
		//高频彩
		String sscLotNo = LotType.SSC.lotNo(); //时时彩
		JSONObject sscObject = getCurrentBatchCodeByLotNo(sscLotNo);
		resultObject.put(sscLotNo, sscObject);
		
		String _11_5LotNo = LotType.OO_F.lotNo(); //十一选五
		JSONObject _11_5Object = getCurrentBatchCodeByLotNo(_11_5LotNo);
		resultObject.put(_11_5LotNo, _11_5Object);
		
		String _11_ydjLotNo = LotType.OO_YDJ.lotNo(); //十一运夺金
		JSONObject _11_ydjObject = getCurrentBatchCodeByLotNo(_11_ydjLotNo);
		resultObject.put(_11_ydjLotNo, _11_ydjObject);
		//足彩
		String sfcLotNo = LotType.ZC_SFC.lotNo(); //足彩胜负彩
		JSONObject sfcObject = getCurrentBatchCodeByLotNo(sfcLotNo);
		resultObject.put(sfcLotNo, sfcObject);
		
		String rx9LotNo = LotType.ZC_RX9.lotNo(); //足彩任选9
		JSONObject rx9Object = getCurrentBatchCodeByLotNo(rx9LotNo);
		resultObject.put(rx9LotNo, rx9Object);
		
		String jqcLotNo = LotType.ZC_JQC.lotNo(); //足彩进球彩
		JSONObject jqcObject = getCurrentBatchCodeByLotNo(jqcLotNo);
		resultObject.put(jqcLotNo, jqcObject);
		
		String bqcLotNo = LotType.ZC_BQC.lotNo(); //足彩半全场
		JSONObject lcbObject = getCurrentBatchCodeByLotNo(bqcLotNo);
		resultObject.put(bqcLotNo, lcbObject);
		
		return resultObject;
	}
	
	/**
	 * 根据彩种获取最新试机号
	 * @param lotNo
	 * @return
	 */
	public JSONObject getLatestTryCodeValueObjectByLotNo(String lotNo) {
		JSONObject valueObject = null;
		String result = lotteryService.getLatestTryCodeByLotNo(lotNo);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")
						&&valueString!=null&&!valueString.equals("null")) { //正常返回
					valueObject = fromObject.getJSONObject("value");
				}
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
	public JSONObject getTryCodeValueObjectByLotNo(String lotNo, String batchCode) {
		JSONObject valueObject = null;
		String result = lotteryService.getTryCodeByLotnoAndBatchcode(lotNo, batchCode);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")
						&&valueString!=null&&!valueString.equals("null")) { //正常返回
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取赛事信息
	 * @param event
	 * @return
	 */
	public JSONObject getJingCaiMatchesValueObject(String lotNo, String day, String weekid, String teamid) {
		JSONObject valueObject = null;
		String result = lotteryService.getJingcaimatches(lotNo, day, weekid, teamid);
		if (!Tools.isEmpty(result)) {
			JSONObject matchObject = JSONObject.fromObject(result);
			if (matchObject!=null&&!Tools.isEmpty(matchObject.getString("errorCode"))
					&&matchObject.getString("errorCode").equals("0")) {
				valueObject = matchObject.getJSONObject("value");
			}
		}
		return valueObject;
	}
	
	/**
	 * 查询Top新闻内容
	 * @param clientInfo
	 * @return
	 */
	public List<News> queryTopNews(String productNo, ClientInfo clientInfo) {
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.productno=? and");
		params.add(productNo);
		
		builder.append(" o.vol_typeid_fk=? and");
		params.add("5");
		
		builder.append(" o.state=? ");
		params.add("1");
		
		List<News> list = News.getList(builder.toString(), "order by o.updatetime desc", params);
		return list;
	}
	
	/**
	 * 完善用户信息送积分的JMS
	 * @param userNo
	 */
	public void perfectUserInfoAddScoreJMS(String userNo) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("userNo", userNo);
		
		logger.info("perfectUserInfoAddScoreTemplate start, headers:" + header);
		perfectUserInfoAddScoreTemplate.sendBodyAndHeaders(null, header);
		//logger.info("perfectUserInfoAddScoreTemplate end, headers:" + header);
	}
	
	/**
	 * 得到UserNo
	 * @param clientInfo
	 * @return
	 */
	public String getNewUserNo(ClientInfo clientInfo) {
		String userNo = clientInfo.getUserno();
		if (Tools.isEmpty(userNo)) { // 客户端没传userNo,就根据用户名去取
			userNo = getUserNoByPhoneNum(clientInfo.getPhonenum());
		}
		return userNo;
	}
	
	/**
	 * 根据用户名获取userNo
	 * @param phoneNum
	 * @return
	 */
	public String getUserNoByPhoneNum(String phoneNum) {
		String userNo = "";
		if (!Tools.isEmpty(phoneNum)) { // 用户名不为空
			try {
				String result = lotteryService.queryUsersByUserName(phoneNum);
				if (!Tools.isEmpty(result)) {
					JSONObject fromObject = JSONObject.fromObject(result);
					if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
							&&fromObject.getString("errorCode").equals("0")) {
						JSONObject valueObject = fromObject.getJSONObject("value");
						userNo = valueObject.getString("userno");
					}
				}
			} catch (Exception e) {
				logger.error("根据用户名获取userNo发生异常", e);
			}
		}
		return userNo;
	}
	
	/**
	 * 获取开奖公告明细
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public JSONObject getWinInfoDetailValueObject(String lotNo, String batchCode) {
		JSONObject valueObject = null;
		String result = lotteryService.getWinInfoDetailByLotNoAndBatchCode(lotNo, batchCode);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) { //获取成功
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取期信息的ValueObject
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public JSONObject getBatchCodeInfoValueObject(String lotNo, String batchCode) {
		JSONObject valueObject = null;
		String result = lotteryService.getBatchCodeInfo(lotNo, batchCode);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")
						&&valueString!=null&&!valueString.equals("null")) { //正常返回
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
	/**
	 * 获取足彩赛事信息的ValueObject
	 * @param lotNo
	 * @param batchCode
	 * @param teamId
	 * @return
	 */
	public JSONObject getZuCaiMatchInfoValueObject(String lotNo, String batchCode, String teamId) {
		JSONObject valueObject = null;
		String result = dataAnalysisService.getZuCaiMatches(lotNo, batchCode, teamId);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				String valueString = fromObject.getString("value");
				if (errorCode!=null&&errorCode.equals("0")
						&&valueString!=null&&!valueString.equals("null")) { //正常返回
					valueObject = fromObject.getJSONObject("value");
				}
			}
		}
		return valueObject;
	}
	
}
