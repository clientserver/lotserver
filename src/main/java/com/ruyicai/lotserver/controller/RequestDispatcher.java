package com.ruyicai.lotserver.controller;

import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.util.common.SpringUtils;

/**
 * 请求转发
 * @author Administrator
 *
 */
public class RequestDispatcher {

	public static String dispatch(ClientInfo clientInfo) {
		LotserverInterfaceHandler handler = SpringUtils.getBean(clientInfo.getCommand());
		return handler.execute(clientInfo);
	}

}
