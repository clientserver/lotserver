package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.common.PresentQueryService;
import com.ruyicai.lotserver.service.common.TrackQueryService;
import com.ruyicai.lotserver.service.common.WinInfoQueryService;
import com.ruyicai.lotserver.service.query.AllQueryService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 相关查询请求
 * @author Administrator
 *
 */
@Service("AllQuery")
public class AllQueryHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private AllQueryService allQueryService;
	
	@Autowired
    private TrackQueryService trackQueryService;
	
	@Autowired
    private WinInfoQueryService winInfoQueryService;
	
	@Autowired
    private PresentQueryService presentQueryService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String type = clientInfo.getType(); //请求类型
		if(type!=null&&type.equals("balance")){ //余额查询
			responseString = allQueryService.getBalance(clientInfo);
		} else if (type!=null&&type.equals("buyCenter")) { //购买大厅
			responseString = allQueryService.buyCenter(clientInfo);
		} else if(type!=null&&type.equals("track")){ //追号查询
			responseString = trackQueryService.getTrack(clientInfo);
		} else if(type!=null&&type.equals("trackDetail")){ //追号详情查询
			responseString = trackQueryService.getTrackDetail(clientInfo);
		} else if(type!=null&&type.equals("winInfoDetail")){ //开奖公告明细查询
			responseString = winInfoQueryService.getWinInfoDetail(clientInfo);
		} else if(type!=null&&type.equals("dna")){ //DNA绑定查询
			responseString = allQueryService.getDNABind(clientInfo);
		} else if(type!=null&&type.equals("prizeRank")){ //中奖排行
			responseString = allQueryService.getPrizeRank(clientInfo);
		} else if(type!=null&&type.equals("gift")){ //赠送查询
			responseString = presentQueryService.getGift(clientInfo); 
		} else if (type!=null&&type.equals("gifted")) { //被赠送查询
			responseString = presentQueryService.getGifted(clientInfo); 
		} else {
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
