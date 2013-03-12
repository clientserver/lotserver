package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.BetLotService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 彩票相关的接口
 * @author Administrator
 *
 */
@Service("betLot")
public class BetLotHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private BetLotService betLotService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String betType = clientInfo.getBetType();
		if (betType!=null&&betType.equals("bet")) { // 投注
			responseString = betLotService.bet(clientInfo);
		} else if (betType!=null&&betType.equals("saveorder")) { //保存订单
			responseString = betLotService.saveOrder(clientInfo);
		} else if (betType!=null&&betType.equals("startcase")) { // 发起合买
			responseString = betLotService.startCaseLot(clientInfo);
		} else if (betType!=null&&betType.equals("betcase")) { // 参与合买
			responseString = betLotService.betCaseLot(clientInfo);
		} else if (betType!=null&&betType.equals("cancelCaselot")) { //合买撤单
			responseString = betLotService.cancelCaselot(clientInfo);
		} else if (betType!=null&&betType.equals("cancelCaselotbuy")) { //合买撤资
			responseString = betLotService.cancelCaselotbuy(clientInfo);
		} else if (betType!=null&&betType.equals("yield")) { // 收益率
			responseString = betLotService.yield(clientInfo);
		} else if (betType!=null&&betType.equals("gift")) { // 赠送
			responseString = betLotService.gift(clientInfo);
		} else if (betType!=null&&betType.equals("receivePresent")) { // 领取赠送彩票
			responseString = betLotService.receivePresent(clientInfo);
		} else if (betType!=null&&betType.equals("receivePresentSecurityCode")) { // 领取赠送彩票时取得验证码
			responseString = betLotService.receivePresentSecurityCode(clientInfo);
		}  else { // 参数错误返回信息
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
