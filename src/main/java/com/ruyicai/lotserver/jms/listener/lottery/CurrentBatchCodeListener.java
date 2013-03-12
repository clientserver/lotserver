package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.common.CommonService;

/**
 * 监听新期的jms
 * @author Administrator
 *
 */
@Service
public class CurrentBatchCodeListener {

	private Logger logger = Logger.getLogger(CurrentBatchCodeListener.class);
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	private CommonService commonService;
	
	public void updateCurrentBatchCodeCache(@Header("LOTNO") String lotNo, @Header("BATCHCODE") String batchCode,
			@Header("AGENCYNO") String agencyNo, @Header("STARTTIME") String startTime, 
			@Header("ENDTIME") String endTime) {
		logger.info("新期jms start "+"lotNo="+lotNo+";batchCode="+batchCode+";agencyNo="+agencyNo+";startTime="+startTime+";endTime="+endTime);
		
		JSONObject valueObject = cacheService.get("lotserver_currentBatchCode");
		if (valueObject==null) {
			valueObject = commonService.getAllCurrentBatchCode();
			if (valueObject!=null) {
				cacheService.set("lotserver_currentBatchCode", valueObject);
			}
		} else {
			JSONObject object = commonService.getCurrentBatchCodeByLotNo(lotNo);
			if (object!=null) {
				valueObject.put(lotNo, object);
				cacheService.set("lotserver_currentBatchCode", valueObject);
				cacheService.set("lotserver_currentBatchCode_"+lotNo, object);
			}
		}
		
		//logger.info("新期jms end "+"lotNo="+lotNo+";batchCode="+batchCode+";agencyNo="+agencyNo+";startTime="+startTime+";endTime="+endTime);
	}
	
}
