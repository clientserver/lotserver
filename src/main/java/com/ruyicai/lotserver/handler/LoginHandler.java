package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.LoginService;

/**
 * 登录
 * @author Administrator
 *
 */
@Service("login")
public class LoginHandler implements LotserverInterfaceHandler {
	
	//private Logger logger = Logger.getLogger(LoginHandler.class);
	
	@Autowired
    private LoginService loginService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("unionLogin")) { //联合登录
			responseString = loginService.unionLogin(clientInfo);
		} else { //普通登录
			responseString = loginService.login(clientInfo);
			//logger.info("登录返回:"+responseString+",Imei="+clientInfo.getImei());
		}
		
		return responseString;
	}

}
