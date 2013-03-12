package com.ruyicai.lotserver.jms.listener;

import java.util.Date;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.domain.ActivityClickRecord;

/**
 *  记录活动中心点击的jms
 * @author Administrator
 *
 */
@Service
public class RecordActivityClickListener {

	private Logger logger = Logger.getLogger(RecordActivityClickListener.class);
	
	public void save(@Header("imei") String imei, @Header("activityId") Integer activityId, @Header("productNo") String productNo) {
		logger.info("记录活动中心点击的jms start "+"imei="+imei+";activityId="+activityId);
		
		try {
			ActivityClickRecord activityClickRecord = new ActivityClickRecord();
			activityClickRecord.setImei(imei);
			activityClickRecord.setActivityId(activityId);
			activityClickRecord.setProductno(productNo);
			activityClickRecord.setCreateTime(new Date());
			activityClickRecord.persist();
		} catch (Exception e) {
			logger.error("记录活动中心点击的jms发生异常", e);
		}
		
		//logger.info("记录活动中心点击的jms end "+"imei="+imei+";activityId="+activityId);
	}
	
}
