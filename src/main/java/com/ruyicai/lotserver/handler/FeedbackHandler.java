package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.FeedBackService;

/**
 * 用户反馈
 * @author Administrator
 *
 */
@Service("feedback")
public class FeedbackHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private FeedBackService feedBackService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = ""; 
		
		String type = clientInfo.getType(); //请求类型
		if (type!=null&&type.equals("feedBack")) { //用户反馈查询
			responseString = feedBackService.findFeedBackList(clientInfo);
		} else if (type!=null&&type.equals("updateReadState")) { //更新已读状态
			responseString = feedBackService.updateReadState(clientInfo);
		} else if (type!=null&&type.equals("exceptionSubmit")) { //异常信息提交
			responseString = feedBackService.saveExceptionMessage(clientInfo);
		} else { //用户反馈
			responseString = feedBackService.feedBack(clientInfo);
		}
		
		return responseString;
	}
	
}
