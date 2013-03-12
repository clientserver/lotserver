package com.ruyicai.lotserver.jms.listener;

import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.service.back.MsgCenterService;

/**
 *  iphone保存token的jms
 * @author Administrator
 *
 */
@Service
public class IphoneSaveTokenListener {

	private Logger logger = Logger.getLogger(IphoneSaveTokenListener.class);
	
	@Autowired
	private MsgCenterService msgCenterService;
	
	public void save(@Header("userNo") String userNo, @Header("type") String type, 
			@Header("machineId") String machineId, @Header("token") String token) {
		logger.info("iphone保存token的jms start "+"userNo="+userNo+";token="+token);
		
		try {
			msgCenterService.saveToken(userNo, machineId, token, 1, type);
		} catch (Exception e) {
			logger.error("iphone保存token发生异常", e);
		}
		
		//logger.info("iphone保存token的jms end "+"userNo="+userNo+";token="+token);
	}
	
}
