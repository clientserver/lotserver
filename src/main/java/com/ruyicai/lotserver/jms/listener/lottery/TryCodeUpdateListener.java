package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.common.CommonService;

/**
 *  试机号更新的jms
 * @author Administrator
 *
 */
@Service
public class TryCodeUpdateListener {

	private Logger logger = Logger.getLogger(TryCodeUpdateListener.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	CacheService cacheService;
	
	public void update(@Header("lotno") String lotNo, @Header("batchcode") String batchCode) {
		logger.info("试机号更新jms start "+"lotNo="+lotNo+";batchCode="+batchCode);
		
		//更新最新试机号
		JSONObject latestTryCodeValueObject = commonService.getLatestTryCodeValueObjectByLotNo(lotNo);
		if (latestTryCodeValueObject!=null) {
			cacheService.set("lotserver_latestTryCode_"+lotNo, latestTryCodeValueObject);
		}
		
		//根据彩种和期号更新试机号
		JSONObject tryCodeValueObject = commonService.getTryCodeValueObjectByLotNo(lotNo, batchCode);
		if (tryCodeValueObject!=null) {
			cacheService.set("lotserver_tryCode_"+lotNo+"_"+batchCode, tryCodeValueObject);
		}
		
		//logger.info("试机号更新jms end "+"lotNo="+lotNo+";batchCode="+batchCode);
	}
	
}
