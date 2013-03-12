package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 监听开奖详情的jms
 * @author Administrator
 *
 */
@Service
public class WinInfoDetailListener {

	private Logger logger = Logger.getLogger(WinInfoDetailListener.class);
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	CommonService commonService;
	
	public void updateWinInfoDetailCache(@Header("lotno") String lotNo, @Header("batchcode") String batchCode,
			@Header("info") String info) {
		logger.info("开奖详情jms start "+"lotNo="+lotNo+";batchcode="+batchCode+";info="+info);
		
		JSONObject valueObject = commonService.getWinInfoDetailValueObject(lotNo, batchCode);
		if (valueObject!=null) {
			if (!Tools.isEmpty(info)&&!info.equals("null")) { //奖级信息不为空
				valueObject.put("info", info); //防止接口返回的info为空
			}
			cacheService.set("lotserver_winInfoDetail_"+lotNo+"_"+batchCode, valueObject);
			
			logger.info("开奖详情jms lotNo="+lotNo+",valueObject="+valueObject);
			String key = "lotserver_winInfoDetail_"+lotNo+"_"+batchCode;
			logger.info("开奖详情jms lotNo="+lotNo+",key="+key+",cache="+cacheService.get(key));
		}
		
		//logger.info("开奖详情jms end "+"lotNo="+lotNo+";batchcode="+batchCode);
	}
	
}
