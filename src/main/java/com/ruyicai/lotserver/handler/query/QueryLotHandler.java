package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.common.BatchCodeQueryService;
import com.ruyicai.lotserver.service.common.CaseLotQueryService;
import com.ruyicai.lotserver.service.common.WinInfoQueryService;
import com.ruyicai.lotserver.service.query.JingCaiService;
import com.ruyicai.lotserver.service.query.QueryLotService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 彩票相关的查询
 * @author Administrator
 *
 */
@Service("QueryLot")
public class QueryLotHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private QueryLotService queryLotService;
	
	@Autowired
    private CaseLotQueryService caseLotQueryService;
	
	@Autowired
    private BatchCodeQueryService batchCodeQueryService;
	
	@Autowired
    private JingCaiService jingCaiService;
	
	@Autowired
    private WinInfoQueryService winInfoQueryService;
    
	public String execute(ClientInfo clientInfo) {
		String responseString = ""; 
		
		String type = clientInfo.getType(); //请求类型
		if(type!=null&&type.equals("bet")) { //投注查询
			responseString = queryLotService.getBetRecord(clientInfo);
		} else if(type!=null&&type.equals("win")) { //中奖查询
			responseString = queryLotService.getWinRecord(clientInfo);
		} else if (type!=null&&type.equals("winInfo")) { //开奖公告
			responseString = winInfoQueryService.getWinInfo(clientInfo);
		} else if (type!=null&&type.equals("winInfoList")) { //开奖公告列表
			responseString = winInfoQueryService.getWinInfoList(clientInfo);
		} else if (type!=null&&type.equals("missValue")) { //遗漏值
			responseString = queryLotService.getMissValue(clientInfo);
		} else if(type!=null&&type.equals("highFrequency")) { //当前期号查询
			responseString = batchCodeQueryService.getCurrentBatchCodeByLotNo(clientInfo);
		} else if (type!=null&&type.equals("zcIssue")) { //足彩预售期期号查询
			responseString = batchCodeQueryService.getZCIssue(clientInfo);
		} else if (type!=null&&type.equals("afterIssue")) { //查询当前期以后的期号
			responseString = batchCodeQueryService.getAfterIssue(clientInfo);
		} else if(type!=null&&type.equals("caselot")) { //参与合买查询
			responseString = caseLotQueryService.getCaseLot(clientInfo);
		} else if(type!=null&&type.equals("querycaselot")) { //合买大厅查询
			responseString = caseLotQueryService.getCaseOrder(clientInfo);
		} else if (type!=null&&type.equals("querycaselotdetail")) {//合买详情
			responseString = caseLotQueryService.getCaseOrderDetail(clientInfo);
		} else if (type!=null&&type.equals("caseLotBuys")) { //合买参与人查询
			responseString = caseLotQueryService.getCaseLotBuys(clientInfo);
		} else if (type!=null&&type.equals("jcDuiZhen")) { //竞彩对阵
			responseString = jingCaiService.getJingCaiDuiZhen(clientInfo);
		} else if (type!=null&&type.equals("jcDuiZhenLimit")) { //按日期查询竞彩对阵
			responseString = jingCaiService.getJingCaiDuiZhenLimit(clientInfo);
		} else if (type!=null&&type.equals("jcResult")) { //竞彩赛果
			responseString = jingCaiService.getJingCaiResult(clientInfo);
		} else {//参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
