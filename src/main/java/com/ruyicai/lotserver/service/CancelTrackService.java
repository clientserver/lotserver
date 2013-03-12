package com.ruyicai.lotserver.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

import net.sf.json.JSONObject;

/**
 * 取消追号
 * @author Administrator
 *
 */
@Service
public class CancelTrackService {
	
	private Logger logger = Logger.getLogger(CancelTrackService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 取消追号
	 * @param clientInfo
	 * @return
	 */
	public String cancelTrack(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String tsubscribeNo = clientInfo.getTsubscribeNo(); //追号订单
			if (Tools.isEmpty(tsubscribeNo)) { //订单号为空
				return Tools.paramError(clientInfo.getImei());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("tsubscribeNo="+clientInfo.getTsubscribeNo()); 
			
			String urlStr = propertiesUtil.getLotteryUrl() + "bet/giveupSubscribe";
			String result = HttpUtil.sendRequestByPost(urlStr, paramStr.toString(), true);
			logger.info("取消追号返回:"+result+",paramStr:"+paramStr.toString());
			if(Tools.isEmpty(result)) { //如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "取消成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "取消失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "取消失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("取消追号发生异常", e);
		}
		return responseJson.toString();
	}

}
