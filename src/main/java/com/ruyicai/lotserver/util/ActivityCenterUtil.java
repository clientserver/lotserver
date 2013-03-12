package com.ruyicai.lotserver.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.lotserver.domain.Activity;

/**
 * 活动中心公共类
 * @author Administrator
 *
 */
@Service
public class ActivityCenterUtil {

	private Logger logger = Logger.getLogger(ActivityCenterUtil.class);
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Produce(uri = "jmsLotserver:topic:recordActivityClick", context = "lotserverCamelContext")
	private ProducerTemplate recordActivityClickTemplate;
	
	/**
	 * 查询活动中心中正在进行中的活动数量
	 * @param coopId
	 * @return
	 */
	public Integer getInProgressActivityCount(String coopId) {
		Integer num = 0;
		try {
			//根据渠道号获得产品编号
			String productNo = commonUtil.getProductNoByCoopId(coopId);
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.productno=? and");
			params.add(productNo);
			
			builder.append(" o.isend='0' ");
			num = Activity.findCount(builder.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return num;
	}
	
	/**
	 * 记录活动中心点击的JMS
	 * @param imei
	 * @param newsId
	 * @param newsType
	 */
	public void recordActivityClickJMS(String imei, Integer activityId, String productNo) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("imei", imei);
		header.put("activityId", activityId);
		header.put("productNo", productNo);
		
		logger.info("recordActivityClickTemplate start, headers:" + header);
		recordActivityClickTemplate.sendBodyAndHeaders(null, header);
		//logger.info("recordActivityClickTemplate end, headers:" + header);
	}
	
}
