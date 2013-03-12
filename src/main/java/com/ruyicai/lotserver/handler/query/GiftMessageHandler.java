package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.GiftMessageService;

/**
 * 赠送短信
 * @author Administrator
 *
 */
@Service("giftmessage")
public class GiftMessageHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private GiftMessageService giftMessageService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("new")) { //新赠送短信查询
			responseString = giftMessageService.getGiftMessageNew(clientInfo);
		} else { //旧赠送短信查询
			responseString = giftMessageService.getGiftMessage();
		}
		
		return responseString;
	}
    
}
