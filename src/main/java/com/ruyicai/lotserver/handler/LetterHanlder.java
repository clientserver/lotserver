package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.LetterService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 站内信
 * @author Administrator
 *
 */
@Service("letter")
public class LetterHanlder implements LotserverInterfaceHandler {

	@Autowired
	private LetterService letterService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("list")) { //列表
			responseString = letterService.findLetterList(clientInfo);
		} else if (requestType!=null&&requestType.equals("updateReadState")) { //更新已读状态
			responseString = letterService.updateReadState(clientInfo);
		} else if (requestType!=null&&requestType.equals("notReadCount")) { //未读数量
			responseString = letterService.findNotReadCount(clientInfo);
		} else { // 参数错误返回信息
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}
	
}
