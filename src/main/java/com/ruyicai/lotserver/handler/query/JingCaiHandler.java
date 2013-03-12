package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.JingCaiBasketballService;
import com.ruyicai.lotserver.service.query.JingCaiFootballService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 竞彩相关查询
 * @author Administrator
 *
 */
@Service("jingCai")
public class JingCaiHandler implements LotserverInterfaceHandler {

	@Autowired
	private JingCaiFootballService jingCaiFootballService;
	
	@Autowired
	private JingCaiBasketballService jingCaiBasketballService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String requestType = clientInfo.getRequestType(); //请求类型
		if (requestType!=null&&requestType.equals("dataAnalysis")) { //竟彩足球数据分析
			responseString = jingCaiFootballService.getDataAnalysis(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScore")) { //竟彩足球即时比分列表
			responseString = jingCaiFootballService.getImmediateScore(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScoreDetail")) { //竟彩足球即时比分详细
			responseString = jingCaiFootballService.getImmediateScoreDetail(clientInfo);
		} else if (requestType!=null&&requestType.equals("dataAnalysisJcl")) { //竟彩篮球数据分析
			responseString = jingCaiBasketballService.getDataAnalysis(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScoreJcl")) { //竟彩篮球即时比分列表
			responseString = jingCaiBasketballService.getImmediateScore(clientInfo);
		} else if (requestType!=null&&requestType.equals("immediateScoreDetailJcl")) { //竟彩篮球即时比分详细
			responseString = jingCaiBasketballService.getImmediateScoreDetail(clientInfo);
		} else { //参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
