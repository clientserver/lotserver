package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.QueryGiftLotService;

/**
 * 赠送记录查询
 * @author Administrator
 *
 */
@Service("queryGiftLot")
public class QueryGiftLotHandler implements LotserverInterfaceHandler {

	@Autowired
	private QueryGiftLotService queryGiftLotService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = queryGiftLotService.queryGiftLot(clientInfo);
		return responseString;
	}

}
