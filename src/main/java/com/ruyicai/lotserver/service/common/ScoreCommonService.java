package com.ruyicai.lotserver.service.common;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.consts.ScoreType;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.back.ScoreCenterService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.MemoUtil;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 积分
 * @author Administrator
 *
 */
@Service
public class ScoreCommonService {

	private Logger logger = Logger.getLogger(ScoreCommonService.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private ScoreCenterService scoreCenterService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Produce(uri = "jmsLotserver:topic:recordWeiboShare", context = "lotserverCamelContext")
	private ProducerTemplate recordWeiboShareTemplate;
	
	/**
     * 增加积分
     * @param clientInfo
     * @return
     */
    public String addScore(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
			String userNo = commonService.getNewUserNo(clientInfo);
			String scoreType = clientInfo.getScoreType(); //积分类型
			String description = clientInfo.getDescription(); //描述
			//记录微博分享
			if (scoreType!=null&&scoreType.equals(ScoreType.weiboShare.value())) { //微博分享
				String source = clientInfo.getSource(); //分享类型
				if (!Tools.isEmpty(source)) {
					String imei = clientInfo.getImei(); //Imei
					weiboShareRecordJMS(imei, source); //记录微博分享的JMS
				}
			}
			// 如果userNo或scoreType为空,参数错误
			if (Tools.isEmpty(userNo)||Tools.isEmpty(scoreType)) { 
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "参数错误");
				return responseJson.toString();
			}
			String result = scoreCenterService.addTuserinfoScore(userNo, Integer.parseInt(scoreType), description);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "增加积分成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "增加积分失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail);
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("增加积分发生异常", e);
		}
    	return responseJson.toString();
    }
    
    /**
     * 记录微博分享的JMS
     * @param imei
     * @param type
     */
    private void weiboShareRecordJMS(String imei, String type) {
    	Map<String, Object> header = new HashMap<String, Object>();
		header.put("imei", imei);
		header.put("type", type);
		
		logger.info("recordWeiboShareTemplate start, headers:" + header);
		recordWeiboShareTemplate.sendBodyAndHeaders(null, header);
		//logger.info("recordWeiboShareTemplate end, headers:" + header);
    }
    
    /**
     * 积分兑换彩金
     * @param clientInfo
     * @return
     */
    public String transScore2Money(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
			String userNo = commonService.getNewUserNo(clientInfo);
			String score = clientInfo.getScore();
			if (Tools.isEmpty(userNo)||Tools.isEmpty(score)) { // 如果userNo或score为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String result = scoreCenterService.transScore2Money(userNo, score);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "兑换成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "兑换失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("积分兑换彩金发生异常", e);
		}
    	return responseJson.toString();
    }
    
    /**
     * 查询用户积分详细
     * @param clientInfo
     * @return
     */
    public String queryScoreDetail(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	JSONArray resultArray = new JSONArray();
    	
    	String totalPage = "0"; //总页数
    	try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
				
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			String result = scoreCenterService.findScoreDetailByUserno(userNo, pageIndex, maxResult);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if(list != null && list.size() > 0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject scoreJson = list.getJSONObject(i);
							
							String score = scoreJson.getString("score");
							String scoreType = scoreJson.getString("scoreType");
							String createTime = scoreJson.getString("createTime");
							if (Tools.isEmpty(createTime)||createTime.trim().equals("null")) { //创建时间为空
								createTime = "";
							} else {
								createTime = DateParseFormatUtil.formatDateTime(createTime);
							}
							
							String blsign = ""; //进出帐标识
							if (!Tools.isEmpty(scoreType)&&!scoreType.equals("null")) {
								int scoreTypeInt = Integer.parseInt(scoreType); //进出帐标识(-1：出帐，1：进账)
								if (scoreTypeInt>0) { //进账
									blsign = "1";
								} else { //出帐
									blsign = "-1";
								}
							}
							
							JSONObject object = new JSONObject();
							object.put("score", score); //积分
							object.put("scoreSource", MemoUtil.getScoreSourceByScoreType(scoreType)); //积分来源
							object.put("blsign", blsign); //进出帐标识
							object.put("createTime", createTime); //创建时间
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
			logger.error("查询用户积分详细发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
    	return responseJson.toString();
    }
    
    /**
     * 兑换1元彩金需多少积分
     * @param clientInfo
     * @return
     */
    public String transMoneyNeedScores(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
    		//String needScores = "1000"; //兑换积分需积分数
    		String needScores = "500"; //兑换积分需积分数
    		String description = commonUtil.getMessageContentByKeyStr("transScoreNewDescription"); //描述
    		
    		String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo或score为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			//查询用户的注册时间
			String result = lotteryService.queryUsersByUserNo(userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					String regtime = valueObject.getString("regtime"); //注册时间
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(Long.parseLong(regtime));
					Date regtimeDate = calendar.getTime();
					
					Date baseDate = DateParseFormatUtil.parseY_M_d("2012-11-6");
					if (baseDate.compareTo(regtimeDate) > 0) { //2012-11-6之前注册的
						needScores = "500";
						description = commonUtil.getMessageContentByKeyStr("transScoreOldDescription");
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.success.value());
			responseJson.put(Constants.message, "查询成功");
			responseJson.put("needScores", needScores);
			responseJson.put("description", description);
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("兑换1元彩金需多少积分发生异常", e);
		}
    	return responseJson.toString();
    }
	
}
