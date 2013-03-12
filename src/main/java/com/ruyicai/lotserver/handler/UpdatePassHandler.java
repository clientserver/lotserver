package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.UpdatePassService;

/**
 * 修改密码
 * @author Administrator
 *
 */
@Service("updatePass")
public class UpdatePassHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private  UpdatePassService updatePassService;

	public String execute(ClientInfo clientInfo) {
	    String responseString = updatePassService.updatePassword(clientInfo);
		return responseString;
	}

}
