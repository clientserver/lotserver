package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.RegisterService;

/**
 * 注册接口
 * @author Administrator
 *
 */
@Service("register")
public class RegisterHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private RegisterService registerService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = registerService.register(clientInfo);
		return responseString;
	}
	
}
