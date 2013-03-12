package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.MessageService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 消息接口
 * @author Administrator
 *
 */
@Service("message")
public class MessageHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private MessageService messageService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String type = clientInfo.getType(); //请求类型(前两个请求用type标识)
		String requestType = clientInfo.getRequestType(); //请求类型
		if (type!=null&&type.equals("messageSetting")) { //消息提醒设置
			responseString = messageService.messageSetting(clientInfo);
		} else if (type!=null&&type.equals("queryMessageSetting")) { //查询消息提醒设置
			responseString = messageService.queryMessageSetting(clientInfo);
		} else if (requestType!=null&&requestType.equals("saveToken")) { //iPhone保存token
			responseString = messageService.saveToken(clientInfo);
		} else {// 参数错误返回信息
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
