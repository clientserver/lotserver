package com.ruyicai.lotserver.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.domain.ExceptionMessage;
import com.ruyicai.lotserver.dto.MessageRequest;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.ScoreCenterService;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 用户反馈
 * @author Administrator
 *
 */
@Service
public class FeedBackService {

	private Logger logger = Logger.getLogger(FeedBackService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private ScoreCenterService scoreCenterService;
	
	/**
	 * 用户反馈
	 * @param clientInfo
	 */
	public String feedBack(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			
			MessageRequest messageRequest = new MessageRequest();
			messageRequest.setUserno(userNo); //用户编号
			messageRequest.setContent(clientInfo.getContent().replaceAll("\n", "")); //反馈内容
			messageRequest.setImsi(clientInfo.getImsi()); //手机标识
			String detail = "Imei="+clientInfo.getImei()+";Platform="+clientInfo.getPlatform()
							+";Machine="+clientInfo.getMachineId()+";ContactWay="+clientInfo.getContactWay();
			messageRequest.setDetail(detail); //其他信息
			
			//String url = propertiesUtil.getMessageUrl() + "saveMsg?";
			String url = propertiesUtil.getMsgCenterUrl() + "msg/saveMsg?";
			String result = HttpUtil.sendRequestByPost(url, "body="+URLEncoder.encode(JSONObject.fromObject(messageRequest).toString(), "UTF-8"), true);
			logger.info("用户反馈返回:"+result+",paramStr:"+JSONObject.fromObject(messageRequest));
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put("errorCode", "success");
					responseJson.put(Constants.message, "提交成功");
					//反馈成功,调用赠送积分的方法
					if (!Tools.isEmpty(userNo)) {
						scoreCenterService.addTuserinfoScore(userNo, 7, "留言建议");
					}
					return responseJson.toString();
				} else if (errorCode!=null&&errorCode.equals("000002")) {
					responseJson.put("errorCode", "fail");
					responseJson.put(Constants.message, "10分钟内不可重复反馈，请稍后重试");
					return responseJson.toString();
				}
			}
			responseJson.put("errorCode", "fail");
			responseJson.put(Constants.message, "提交失败");
		} catch (UnsupportedEncodingException e) {
			responseJson.put("errorCode", "fail");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("用户反馈发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 用户反馈查询
	 * @param clientInfo
	 * @return
	 */
	public String findFeedBackList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) { 
				pageIndex = "0";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			
			JSONObject requestJson = new JSONObject(); //查询条件
			requestJson.put("EQS_userno", clientInfo.getUserno());
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("condition=" + URLEncoder.encode(requestJson.toString(), "UTF-8"));
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxResult)); //开始行数
			paramStr.append("&endLine=" + maxResult); //取多少条记录
			
			//String url = propertiesUtil.getMessageUrl() + "selectMsgs?";
			String url = propertiesUtil.getMsgCenterUrl() + "msg/selectMsgs?";
			String result = HttpUtil.sendRequestByGet(url+paramStr.toString(), true);
			//logger.info("用户反馈查询返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					JSONArray list = valueObject.getJSONArray("list");
					if (list!=null && list.size()>0) {
						totalPage = valueObject.getString("totalPage");
						for (int i = 0; i < list.size(); i++) {
							JSONObject messageJson = list.getJSONObject(i);
							String id = messageJson.getString("id"); //留言编号
							String content = messageJson.getString("content"); //反馈内容
							String reply = messageJson.getString("reply"); //回复内容
							String createTime = messageJson.getString("createtime"); //反馈时间
							String readState = messageJson.getString("hasRead"); //已读状态(0:未读;1:已读)
							
							reply = (Tools.isEmpty(reply)||reply.equals("null")) ? "" : reply;
							createTime = sdf.format(Long.parseLong(createTime));
							
							JSONObject object = new JSONObject();
							object.put("id", id); //留言编号
							object.put("content", content); //反馈内容
							object.put("reply", reply); //回复内容
							object.put("createTime", createTime); //反馈时间
							object.put("readState", readState); //已读状态
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, "0000");
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("totalPage", totalPage);
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, "0047");
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("用户反馈查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 更新已读状态
	 * @param clientInfo
	 * @return
	 */
	public String updateReadState(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String id = clientInfo.getId(); //站内信编号
			if (Tools.isEmpty(id)) { //返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("id=" + id);
			
			String url = propertiesUtil.getMsgCenterUrl() + "msg/update2Read";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("留言更新已读状态返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "更新成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "更新失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("留言更新已读状态发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 异常信息提交
	 * @param clientInfo
	 * @return
	 */
	public String saveExceptionMessage(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			ExceptionMessage exceptionMessage = new ExceptionMessage();
			exceptionMessage.setPlatform(clientInfo.getPlatform());
			exceptionMessage.setSoftwareversion(clientInfo.getSoftwareVersion());
			exceptionMessage.setCoopid(clientInfo.getCoopId());
			exceptionMessage.setContent(clientInfo.getContent());
			exceptionMessage.setUserno(clientInfo.getUserno());
			exceptionMessage.setMobileid(clientInfo.getPhonenum());
			exceptionMessage.setCreatetime(new Date());
			exceptionMessage.persist();
			
			responseJson.put(Constants.error_code, "0000");
			responseJson.put(Constants.message, "提交成功");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("异常信息提交发生异常", e);
		}
		return responseJson.toString();
	}
	
}
