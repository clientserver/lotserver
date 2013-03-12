package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.CashService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 提现相关的请求
 * @author Administrator
 *
 */
@Service("getCash")
public class CashHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private CashService cashService;
    
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String cashType = clientInfo.getCashtype();
		if (cashType!=null&&cashType.equals("cash")) { //提现
			responseString = cashService.cash(clientInfo);
		} else if (cashType!=null&&cashType.equals("cancelCash")) { //取消提现
			responseString = cashService.cancleCash(clientInfo);
		} else if (cashType!=null&&cashType.equals("queryCash")) { //查询提现(最新的一条记录)
			responseString = cashService.queryCash(clientInfo);
		} else if (cashType!=null&&cashType.equals("cashRecord")) { //提现记录查询
			responseString = cashService.cashRecord(clientInfo);
		} else { //参数错误
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
