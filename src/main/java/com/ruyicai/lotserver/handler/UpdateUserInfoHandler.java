package com.ruyicai.lotserver.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.UpdateUserInfoService;
import com.ruyicai.lotserver.service.common.ScoreCommonService;

/**
 * 用户相关请求
 * @author Administrator
 *
 */
@Service("updateUserInfo")
public class UpdateUserInfoHandler implements LotserverInterfaceHandler {
	
	@Autowired
    private  UpdateUserInfoService updateUserInfoService;
	
	@Autowired
    private  ScoreCommonService scoreCommonService;

	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String type = clientInfo.getType(); //请求类型
		if (type!=null&&type.equals("userCenter")) { //用户中心信息查询(昵称,手机号码,余额,积分,身份证号)
			responseString = updateUserInfoService.userCenter(clientInfo);
		} else if (type!=null&&type.equals("retrievePassword")) { //找回密码
			responseString = updateUserInfoService.retrievePassword(clientInfo);
		} else if (type!=null&&type.equals("updateNickName")) { //修改昵称
			responseString = updateUserInfoService.updateNickName(clientInfo);
		} else if(type!=null&&type.equals("bindPhoneSecurityCode")) { //绑定手机号时发送验证码
			responseString = updateUserInfoService.sendCodeForBindPhone(clientInfo);
		} else if (type!=null&&type.equals("bindPhone")) { //绑定手机号
			responseString = updateUserInfoService.bindPhone(clientInfo);
		} else if (type!=null&&type.equals("removeBindPhone")) { //解除绑定手机号
			responseString = updateUserInfoService.removeBindPhone(clientInfo);
		} else if (type!=null&&type.equals("cancelAutoLogin")) { //取消自动登录
			responseString = updateUserInfoService.cancelAutoLogin(clientInfo);
		} else if (type!=null&&type.equals("scoreDetail")) { //积分详细查询
			responseString = scoreCommonService.queryScoreDetail(clientInfo);
		} else if (type!=null&&type.equals("transScore2Money")) { //积分兑换彩金
			responseString = scoreCommonService.transScore2Money(clientInfo);
		} else if (type!=null&&type.equals("addScore")) { //增加积分
			responseString = scoreCommonService.addScore(clientInfo);
		} else {
			responseString = updateUserInfoService.bindCertId(clientInfo); //绑定身份证
		}
		
		return responseString;
	}

}
