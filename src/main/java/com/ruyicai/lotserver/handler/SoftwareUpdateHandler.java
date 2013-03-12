package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.SoftwareUpdateService;

/**
 * 软件升级
 * @author Administrator
 *
 */
@Service("softwareupdate")
public class SoftwareUpdateHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private SoftwareUpdateService softwareUpdateService;

	public String execute(ClientInfo clientInfo) {
		String responseString = softwareUpdateService.getSoftwareUpdateInfo(clientInfo);
		return responseString;
	}
	
}
