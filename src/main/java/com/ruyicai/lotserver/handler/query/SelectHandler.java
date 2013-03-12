package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.SelectService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 查询相关请求
 * @author Administrator
 *
 */
@Service("select")
public class SelectHandler implements LotserverInterfaceHandler {

	@Autowired
    private SelectService selectService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = ""; 
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null && requestType.equals("betList")) { //投注列表查询
			responseString = selectService.getBetList(clientInfo);
		} else if (requestType!=null && requestType.equals("betCodeAnalysis")) { //注码解析
			responseString = selectService.getBetCodeAnalysis(clientInfo);
		} else if (requestType!=null && requestType.equals("winList")) { //中奖列表查询
			responseString = selectService.getWinList(clientInfo);
		} else if (requestType!=null && requestType.equals("caseLotBuyList")) { //参与合买列表查询
			responseString = selectService.getCaseLotBuyList(clientInfo);
		} else if (requestType!=null && requestType.equals("caseLotDetail")) { //合买详情查询
			responseString = selectService.getCaseLotDetail(clientInfo);
		} else { //参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
