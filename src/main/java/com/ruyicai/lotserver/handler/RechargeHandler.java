package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.RechargeService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 充值相关请求
 * @author Administrator
 *
 */
@Service("recharge")
public class RechargeHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private RechargeService rechargeService; 
    
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String rechargeType = clientInfo.getRechargetype(); //充值类型
		if(rechargeType!=null&&rechargeType.equals("01")) {//DNA充值
			responseString = rechargeService.dnaCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("02")) {//手机充值卡充值
			responseString = rechargeService.phoneCardCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("03")) {//手机银行充值
			responseString = rechargeService.phoneBankCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("04")) {//游戏点卡充值
			responseString = rechargeService.gameCardCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("05")){//支付宝充值
			responseString = rechargeService.zfbCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("06")){//联通华建(银联在线支付)
			responseString = rechargeService.lthjCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("07")){//支付宝安全支付
			responseString = rechargeService.zfbSecurityCharge(clientInfo, "", "");
		} else if(rechargeType!=null&&rechargeType.equals("08")){//银行充值
			responseString = rechargeService.bankCharge();
		} else if(rechargeType!=null&&rechargeType.equals("09")){//代理充值
			responseString = rechargeService.agencyCharge(clientInfo);
		} else if(rechargeType!=null&&rechargeType.equals("10")){//拉卡拉充值
			responseString = rechargeService.lakalaCharge(clientInfo, "", "");
		} else {
			responseString = Tools.paramError(clientInfo.getImei());
		}
		
		return responseString;
	}

}
