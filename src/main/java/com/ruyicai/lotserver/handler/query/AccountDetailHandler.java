package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.AccountDetailService;

/**
 * 账户明细查询
 * 
 * @author Administrator
 * 
 */
@Service("accountdetail")
public class AccountDetailHandler implements LotserverInterfaceHandler {

	@Autowired
	private AccountDetailService accountDetailService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String type = clientInfo.getType(); //请求类型
		if (type!=null&&type.equals("new")) { // 新账户明细查询
			responseString = accountDetailService.getAccountDetailNew(clientInfo);
		} else { // 旧账户明细查询
			responseString = accountDetailService.getAccountDetail(clientInfo);
		}
		
		return responseString;
	}

}
