package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.MissValueQueryService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 遗漏值相关查询
 * @author Administrator
 *
 */
@Service("missValue")
public class MissValueHandler implements LotserverInterfaceHandler {

	@Autowired
	private MissValueQueryService missValueQueryService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("list")) { //遗漏值列表
			responseString = missValueQueryService.getList(clientInfo);
		} else { //参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
