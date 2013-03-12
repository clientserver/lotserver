package com.ruyicai.lotserver.service.back;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.sf.json.JSONObject;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 消息的接口
 * @author Administrator
 *
 */
@Service
public class MsgCenterService {
	
	private static Logger logger = Logger.getLogger(MsgCenterService.class);
	
	@Autowired
	PropertiesUtil propertiesUtil;

	/**
	 * 发送短信 如果用91彩票的签名,则channelName为menWangOldSMSServiceProvider;
	 * 如果用如意彩的签名,则channelName为空
	 * 
	 * @param mobileId
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String sendMessage(String mobileId, String content, String channelName) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("mobileIds=" + mobileId);
		paramStr.append("&text=" + URLEncoder.encode(content, "UTF-8"));
		if (!Tools.isEmpty(channelName)) {
			paramStr.append("&channelName=" + channelName);
		}

		String url = propertiesUtil.getMsgCenterUrl() + "sms/send";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("发送短信的返回:" + result + ",paramStr:" + paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject != null && fromObject.has("errorCode")) {
				return fromObject.getString("errorCode");
			}
		}
		return "";
	}
	
	/**
	 * iphone保存Token
	 * @param machine
	 * @param token
	 * @param userno
	 * @param needToSend
	 * @param type
	 */
	public void saveToken(String usernNo, String machine, String token, Integer needToSend, String type) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("machine="+machine);
		paramStr.append("&token="+token);
		paramStr.append("&userno="+usernNo);
		paramStr.append("&needToSend="+needToSend);
		paramStr.append("&type="+type);
		
		String url = propertiesUtil.getMsgCenterUrl() + "usersetting/saveToken";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("iphone保存Token返回:"+result);
	}
	
	/**
	 * 未登录时iphone保存Token
	 * @param token
	 * @param type
	 */
	public String saveTk(String token, String type) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("token="+token);
		paramStr.append("&type="+type);
		
		String url = propertiesUtil.getMsgCenterUrl() + "usersetting/saveTk";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("未登录时iphone保存Token返回:"+result);
		return result;
	}
	
	/**
	 * 查询消息提醒设置
	 * @param userNo
	 * @param smsType
	 */
	public String findMessageSetting(String userNo, BigDecimal smsType) {
		String url = propertiesUtil.getMsgCenterUrl()+"usersetting/findUserSetting?userno="+userNo+"&smstype="+smsType;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询消息提醒设置返回:"+result);
		return result;
	}
	
	/**
	 * 查询未读留言的数量
	 * @param userNo
	 * @return
	 */
	public String getNotReadFeedbackCount(String userNo) {
		String url = propertiesUtil.getMsgCenterUrl()+"msg/findCountUnread?userno="+userNo;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询未读留言的数量返回:"+result);
		return result;
	}
	
	/**
	 * 查询未读站内信的数量
	 * @param userNo
	 * @return
	 */
	public String getNotReadLetterCount(String userNo) {
		String url = propertiesUtil.getMsgCenterUrl()+"letter/findCountUnread?userno="+userNo;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询未读站内信的数量返回:"+result);
		return result;
	}
	
}
