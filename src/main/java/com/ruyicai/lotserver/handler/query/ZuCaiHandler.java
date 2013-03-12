package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.ZuCaiService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 足彩请求
 * @author Administrator
 *
 */
@Service("zuCai")
public class ZuCaiHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private ZuCaiService zuCaiService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("duiZhen")) { //查询对阵
			responseString = zuCaiService.getDuiZhen(clientInfo);
		} else if (requestType!=null&&requestType.equals("dataAnalysis")) { //数据分析
			responseString = zuCaiService.getDataAnalysis(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScore")) { //即时比分列表
			responseString = zuCaiService.getImmediateScore(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScoreDetail")) { //即时比分详细
			responseString = zuCaiService.getImmediateScoreDetail(clientInfo);
		} else { //参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}
	
}
