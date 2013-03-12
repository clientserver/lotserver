package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.AutoJoinService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 自动跟单的接口
 * @author Administrator
 *
 */
@Service("autoJoin")
public class AutoJoinHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private AutoJoinService autoJoinService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("selectCaseLotStarterInfo")) { //查询合买发起人信息
			responseString = autoJoinService.selectCaseLotStarterInfo(clientInfo);
		} else if (requestType!=null&&requestType.equals("createAutoJoin")) { //创建自动跟单
			responseString = autoJoinService.createAutoJoin(clientInfo);
		} else if (requestType!=null&&requestType.equals("selectAutoJoin")) { //查询定制跟单
			responseString = autoJoinService.selectAutoJoin(clientInfo);
		} else if (requestType!=null&&requestType.equals("cancelAutoJoin")) { //取消自动跟单
			responseString = autoJoinService.cancelAutoJoin(clientInfo);
		} else if (requestType!=null&&requestType.equals("updateAutoJoin")) { //更新自动跟单
			responseString = autoJoinService.updateAutoJoin(clientInfo);
		} else { // 参数错误返回信息
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
