package com.ruyicai.lotserver.service;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 消息提醒相关的Service
 * @author Administrator
 *
 */
@Service
public class UpdatePassService {
	
	private Logger logger = Logger.getLogger(UpdatePassService.class);
    
	@Autowired
	private LotteryService lotteryCommonService;
    
    @Autowired
	private CommonService commonService;
	
	/**
     * 重置密码
     * @param clientInfo
     * @return
     */
    public String updatePassword(ClientInfo clientInfo){
    	JSONObject responseJson = new JSONObject();
    	try {
    		String userNo = commonService.getNewUserNo(clientInfo);
    		if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		} 
    		String result = lotteryCommonService.queryUsersByUserNo(userNo);
    		if (Tools.isEmpty(result)) { // 如果返回空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					if (valueObject!=null) {
						String dbPassword = valueObject.getString("password");
						String pwd = Tools.EncoderByMd5(clientInfo.getOldPass());
						if (dbPassword!=null&&dbPassword.equals(pwd)) { //原密码正确
							String resetPwdResult = lotteryCommonService.resetPassword(userNo, clientInfo.getNewPass());
							if (resetPwdResult!=null&&resetPwdResult.equals("0")) {
								responseJson.put(Constants.error_code, "0000");
								responseJson.put(Constants.message, "修改成功");
							} else {
								responseJson.put(Constants.error_code, "9999");
								responseJson.put(Constants.message, "修改失败");
							}
						} else {
							responseJson.put(Constants.error_code, "9999");
							responseJson.put(Constants.message, "原密码错误");
						}
					} else {
						responseJson.put(Constants.error_code, "9999");
						responseJson.put(Constants.message, "修改失败");
					}
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "修改失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "修改失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("重置密码发生异常", e);
		} 
		return responseJson.toString(); 
    }
	
}
