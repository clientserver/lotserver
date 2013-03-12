package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.common.CommonService;

/**
 * 监听开奖公告的jms
 * @author Administrator
 *
 */
@Service
public class WinInfoListener {

	private Logger logger = Logger.getLogger(WinInfoListener.class);
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	private CommonService commonService;
	
	public void updateWinInfoCache(@Header("lotno") String lotNo, @Header("batchcode") String batchcode,
			@Header("wincode") String wincode, @Header("winbasecode") String winbasecode, 
			@Header("winspecialcode") String winspecialcode) {
		logger.info("开奖公告jms start "+"lotNo="+lotNo+";batchcode="+batchcode+";wincode="+wincode+";winbasecode="+winbasecode+";winspecialcode="+winspecialcode);
		
		//1期
		Integer num1 = 1;
		JSONObject valueObject1 = cacheService.get("lotserver_winInfo_"+num1);
		if (valueObject1==null) {
			valueObject1 = commonService.getAllWinInfoByNum(num1);
			cacheService.set("lotserver_winInfo_"+num1, valueObject1);
		} else {
			JSONArray valueArray = commonService.getWinInfoByLotNoAndNum(lotNo, num1);
			if (valueArray!=null&&valueArray.size()>0) {
				valueObject1.put(lotNo, valueArray);
				cacheService.set("lotserver_winInfo_"+num1, valueObject1);
				cacheService.set("lotserver_winInfo_"+lotNo+"_"+num1, valueArray);
			}
		}
		
		//5期
		Integer num2 = 5;
		JSONObject valueObject2 = cacheService.get("lotserver_winInfo_"+num2);
		if (valueObject2==null) {
			valueObject2 = commonService.getAllWinInfoByNum(num2);
			cacheService.set("lotserver_winInfo_"+num2, valueObject2);
		} else {
			JSONArray valueArray = commonService.getWinInfoByLotNoAndNum(lotNo, num2);
			if (valueArray!=null&&valueArray.size()>0) {
				valueObject2.put(lotNo, valueArray);
				cacheService.set("lotserver_winInfo_"+num2, valueObject2);
				cacheService.set("lotserver_winInfo_"+lotNo+"_"+num2, valueArray);
			}
		}
		
		//24期
		Integer num3 = 24;
		JSONObject valueObject3 = cacheService.get("lotserver_winInfo_"+num3);
		if (valueObject3==null) {
			valueObject3 = commonService.getAllWinInfoByNum(num3);
			cacheService.set("lotserver_winInfo_"+num3, valueObject3);
		} else {
			JSONArray valueArray = commonService.getWinInfoByLotNoAndNum(lotNo, num3);
			if (valueArray!=null&&valueArray.size()>0) {
				valueObject3.put(lotNo, valueArray);
				cacheService.set("lotserver_winInfo_"+num3, valueObject3);
				cacheService.set("lotserver_winInfo_"+lotNo+"_"+num3, valueArray);
			}
		}
		
		//logger.info("开奖公告jms end "+"lotNo="+lotNo+";batchcode="+batchcode+";wincode="+wincode+";winbasecode="+winbasecode+";winspecialcode="+winspecialcode);
	}
	
}
