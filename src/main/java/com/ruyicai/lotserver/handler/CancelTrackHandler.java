package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.CancelTrackService;

/**
 * 取消追号
 * @author Administrator
 *
 */
@Service("cancelTrack")
public class CancelTrackHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private CancelTrackService cancelTrackService;
    
	public String execute(ClientInfo clientInfo) {
		String responseString = cancelTrackService.cancelTrack(clientInfo);
		return responseString;
	}
	

}
