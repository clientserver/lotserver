package com.ruyicai.lotserver.jms.listener;

import java.util.Date;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.domain.WeiboShareRecord;

/**
 *  记录微博分享的jms
 * @author Administrator
 *
 */
@Service
public class RecordWeiboShareListener {

	private Logger logger = Logger.getLogger(RecordWeiboShareListener.class);
	
	public void save(@Header("imei") String imei, @Header("type") String type) {
		logger.info("记录微博分享的jms start "+"imei="+imei+";type="+type);
		
		try {
			WeiboShareRecord weiboShareRecord = new WeiboShareRecord();
			weiboShareRecord.setImei(imei);
			weiboShareRecord.setCreatetime(new Date());
			weiboShareRecord.setType(type);
			weiboShareRecord.persist();
		} catch (Exception e) {
			logger.error("记录微博分享的jms发生异常", e);
		}
		
		//logger.info("记录微博分享的jms end "+"imei="+imei+";type="+type);
	}
	
}
