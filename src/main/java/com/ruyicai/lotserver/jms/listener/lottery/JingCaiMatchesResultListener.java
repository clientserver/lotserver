package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONObject;

import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.common.CommonService;

/**
 * 竟彩赛果更新的jms
 * @author Administrator
 *
 */
@Service
public class JingCaiMatchesResultListener {

	private Logger logger = Logger.getLogger(JingCaiMatchesResultListener.class);
	
	@Autowired
	CacheService cacheService;
	
	@Autowired
	private CommonService commonService;
	
	public void update(@Header("EVENT") String event) {
		logger.info("竟彩赛果更新jms start "+"event="+event);
		
		String jingCaiType = ""; //竟彩类型
		String day = ""; //日期
		String weekId = ""; //星期
		String teamId = ""; //场次
		if (event!=null) {
			String[] events = event.split("_");
			if (events.length==4) {
				jingCaiType = events[0];
				day = events[1];
				weekId = events[2];
				teamId = events[3];
			}
		}
		JSONObject valueObject = null;
		if (jingCaiType.equals("1")) { //竟彩足球
			valueObject = commonService.getJingCaiMatchesValueObject("J00001", day, weekId, teamId);
		} else if (jingCaiType.equals("0")) { //竟彩篮球
			valueObject = commonService.getJingCaiMatchesValueObject("J00005", day, weekId, teamId);
		}
		if (valueObject!=null) {
			cacheService.set("lotserver_jingCaiMatches_"+jingCaiType+"_"+day+"_"+weekId+"_"+teamId, valueObject);
		}
		
		//logger.info("竟彩赛果更新jms end "+"event="+event);
	}
	
}
