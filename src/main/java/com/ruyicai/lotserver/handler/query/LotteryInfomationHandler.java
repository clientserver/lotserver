package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.LotteryInfomationService;

/**
 * 开奖公告
 * @author Administrator
 *
 */
@Service("lotteryinfomation")
public class LotteryInfomationHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private LotteryInfomationService lotteryInfomationService;

	public String execute(ClientInfo clientInfo) {
		String responseString = lotteryInfomationService.getLotteryInfoDataCache(clientInfo);
		return responseString;
    }
   
}
