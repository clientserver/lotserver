package com.ruyicai.lotserver.service.query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;

/**
 * 赠送查询
 * @author Administrator
 *
 */
@Service
public class QueryGiftLotService {

	private Logger logger = Logger.getLogger(QueryGiftLotService.class);
	
	/**
	 * 彩票赠送记录查询
	 * @param clientInfo
	 * @return
	 */
	public String queryGiftLot(ClientInfo clientInfo) {
		JSONArray responseArray = new JSONArray();
		try {
			logger.info("该接口已废弃  彩票赠送记录查询(queryGiftLot),platform:"+clientInfo.getPlatform()+";softwareVersion:"+clientInfo.getSoftwareVersion()
					+";imei:"+clientInfo.getImei()+";userNo:"+clientInfo.getUserno()+";userName:"+clientInfo.getPhonenum());
			
			JSONObject object = new JSONObject();
			object.put(Constants.error_code, "0047");
			responseArray.add(object);
		} catch (Exception e) {
			JSONObject object = new JSONObject();
			object.put(Constants.error_code, "9999");
			responseArray.add(object);
			logger.error("彩票赠送记录查询发生异常", e);
		}
		return responseArray.toString();
	}
	
}
