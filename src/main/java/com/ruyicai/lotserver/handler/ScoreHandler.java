package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.common.ScoreCommonService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 积分接口
 * @author Administrator
 *
 */
@Service("score")
public class ScoreHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private ScoreCommonService scoreCommonService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType();
		if (requestType!=null&&requestType.equals("transMoneyNeedScores")) { //兑换1元彩金需多少积分
			responseString = scoreCommonService.transMoneyNeedScores(clientInfo);
		} else {// 参数错误返回信息
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
