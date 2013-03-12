package com.ruyicai.lotserver.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.dto.SendChannel;
import com.ruyicai.lotserver.dto.UserSMSTiming;
import com.ruyicai.lotserver.dto.UserSettingDTO;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.MsgCenterService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 消息提醒相关的Service
 * @author Administrator
 *
 */
@Service
public class MessageService {
	
	private Logger logger = Logger.getLogger(MessageService.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private MsgCenterService msgCenterService;
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 消息提醒设置
	 * @param clientInfo
	 * @return
	 */
	public String messageSetting(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String info = clientInfo.getInfo(); //设置信息
			//winInfo:0:id_sms:1:id_push:0:id!win:1:id_sms:0:id_push:1:id!subscribe:1:id_sms:0:id_push:1:id
			if (Tools.isEmpty(info)) { //设置信息为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			List<UserSettingDTO> userSettingDTOList = new ArrayList<UserSettingDTO>();
			String[] messageSettings = info.split("!");
			for (String messageSetting : messageSettings) {
				UserSettingDTO userSettingDTO = new UserSettingDTO();
				UserSMSTiming userSMSTiming = new UserSMSTiming();
				List<SendChannel> sendChannels = new ArrayList<SendChannel>();
				String[] split = messageSetting.split("_");
				for (String string : split) {
					String[] split2 = string.split(":");
					if (split2[0].equals("winInfo")) { //开奖
						userSMSTiming.setNeedToSend(Integer.parseInt(split2[1]));
						userSMSTiming.setId(Integer.parseInt(split2[2]));
						continue;
					}
					if (split2[0].equals("win")) { //中奖
						userSMSTiming.setNeedToSend(Integer.parseInt(split2[1]));
						userSMSTiming.setId(Integer.parseInt(split2[2]));
						continue;
					}
					if (split2[0].equals("subscribe")) { //追号
						userSMSTiming.setNeedToSend(Integer.parseInt(split2[1]));
						userSMSTiming.setId(Integer.parseInt(split2[2]));
						continue;
					}
					if (split2[0].equals("sms")) { //短信
						SendChannel sendChannel = new SendChannel();
						sendChannel.setNeedToSend(Integer.parseInt(split2[1]));
						sendChannel.setId(Long.parseLong(split2[2]));
						sendChannels.add(sendChannel);
						continue;
					}
					if (split2[0].equals("push")) { //Push
						SendChannel sendChannel = new SendChannel();
						sendChannel.setNeedToSend(Integer.parseInt(split2[1]));
						sendChannel.setId(Long.parseLong(split2[2]));
						sendChannels.add(sendChannel);
						continue;
					}
				}
				userSettingDTO.setUserSMSTiming(userSMSTiming);
				userSettingDTO.setSendChannels(sendChannels);
				userSettingDTOList.add(userSettingDTO);
			}
			
			String url = propertiesUtil.getMsgCenterUrl()+"usersetting/updateUserSMSTimingBatch";
			String result = HttpUtil.sendRequestByPost(url, "json="+JSONArray.fromObject(userSettingDTOList), true);
			logger.info("消息提醒设置返回:"+result);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "设置成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "设置失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "设置失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("消息提醒设置发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 查询消息提醒设置
	 * @param clientInfo
	 * @return
	 */
	public String queryMessageSetting(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			//开奖
			JSONObject winInfoObject = new JSONObject();
			String result1 = msgCenterService.findMessageSetting(userNo, BigDecimal.ONE);
			getMessageSettingInfo(winInfoObject, result1);
			
			//中奖
			JSONObject winObject = new JSONObject();
			String result2 = msgCenterService.findMessageSetting(userNo, new BigDecimal(2));
			getMessageSettingInfo(winObject, result2);
			
			//追号
			JSONObject subscribeObject = new JSONObject();
			String result3 = msgCenterService.findMessageSetting(userNo, new BigDecimal(3));
			getMessageSettingInfo(subscribeObject, result3);
			
			responseJson.put(Constants.error_code, "0000");
			responseJson.put(Constants.message, "查询成功");
			responseJson.put("winInfo", winInfoObject.toString());
			responseJson.put("win", winObject.toString());
			responseJson.put("subscribe", subscribeObject.toString());
		} catch (Exception e) {
			logger.error("查询消息提醒设置发生异常", e);
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
		}
		return responseJson.toString();
	}
	
	/**
	 * 得到设置信息
	 * @param object
	 * @param result
	 */
	public void getMessageSettingInfo(JSONObject object, String result) {
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
					&&fromObject.getString("errorCode").equals("0")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				JSONObject userSMSTimingObject = valueObject.getJSONObject("userSMSTiming");
				object.put("id", userSMSTimingObject.getString("id"));
				object.put("needToSend", userSMSTimingObject.getString("needToSend"));
				
				JSONArray sendChannelsArray = valueObject.getJSONArray("sendChannels");
				JSONArray sendChannels = new JSONArray();
				for (int i = 0; i < sendChannelsArray.size(); i++) {
					JSONObject sendChannelsObject = sendChannelsArray.getJSONObject(i);
					
					JSONObject sendChannel = new JSONObject();
					sendChannel.put("id", sendChannelsObject.getString("id"));
					String sendType = sendChannelsObject.getString("sendtype");
					if (!Tools.isEmpty(sendType)&&sendType.equals("0")) { //短信
						sendChannel.put("sms", sendChannelsObject.getString("needToSend"));
					}
					if (!Tools.isEmpty(sendType)&&sendType.equals("1")) { //email
						sendChannel.put("email", sendChannelsObject.getString("needToSend"));
					}
					if (!Tools.isEmpty(sendType)&&sendType.equals("2")) { //Push
						sendChannel.put("push", sendChannelsObject.getString("needToSend"));
					}
					sendChannels.add(sendChannel);
				}
				object.put("sendChannels", sendChannels.toString());
			}
		}
	}
	
	/**
	 * iPhone保存token
	 * @param clientInfo
	 * @return
	 */
	public String saveToken(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String token = clientInfo.getToken();
			String type = clientInfo.getType();
			if (StringUtil.isEmpty(type) || StringUtil.isEmpty(token)) { //返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String result = msgCenterService.saveTk(token, type);
			if (StringUtil.isEmpty(result)) {
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "保存成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "保存失败");
		} catch (Exception e) {
			logger.error("iPhone保存token发生异常", e);
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
		}
		return responseJson.toString();
	}
	
}
