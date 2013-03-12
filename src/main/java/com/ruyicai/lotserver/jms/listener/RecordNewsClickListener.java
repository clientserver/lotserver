package com.ruyicai.lotserver.jms.listener;

import java.util.Date;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.domain.NewsClickRecord;

/**
 *  记录新闻点击的jms
 * @author Administrator
 *
 */
@Service
public class RecordNewsClickListener {

	private Logger logger = Logger.getLogger(RecordNewsClickListener.class);
	
	public void save(@Header("imei") String imei, @Header("newsId") Integer newsId, @Header("productNo") String productNo,
			@Header("newsType") String newsType) {
		logger.info("记录新闻点击的jms start "+"imei="+imei+";newsId="+newsId);
		
		try {
			NewsClickRecord newsClickRecord = new NewsClickRecord();
			newsClickRecord.setImei(imei);
			newsClickRecord.setNewsId(newsId);
			newsClickRecord.setNewsType(newsType);
			newsClickRecord.setProductno(productNo);
			newsClickRecord.setCreateTime(new Date());
			newsClickRecord.persist();
		} catch (Exception e) {
			logger.error("记录新闻点击的jms发生异常", e);
		}
		
		//logger.info("记录新闻点击的jms end "+"imei="+imei+";newsId="+newsId);
	}
	
}
