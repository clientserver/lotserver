package com.ruyicai.lotserver.timer;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.service.back.RankingService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 获取中奖排行的定时任务
 * @author Administrator
 *
 */
@Service
public class PrizeBankTimer {
	
	private Logger logger = Logger.getLogger(PrizeBankTimer.class);
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private RankingService rankingService;
	
	public void process() {
		logger.info("中奖排行的定时任务 start");
		
		//周排行
		String weekResult = rankingService.queryPrizeRankWeek();
		if (!Tools.isEmpty(weekResult)) {
			JSONObject weekJSon = JSONObject.fromObject(weekResult);
			if (weekJSon!= null&&!Tools.isEmpty(weekJSon.getString("errorCode"))
					&&weekJSon.getString("errorCode").equals("0")) {
				String valueString = weekJSon.getString("value");
				JSONArray valueArray = JSONArray.fromObject(valueString);
				cacheService.set("lotserver_prizeBank_week", valueArray);
			}
		}
		
		//月排行
		String monthResult = rankingService.queryPrizeRankMonth();
		if (!Tools.isEmpty(monthResult)) {
			JSONObject monthJSon = JSONObject.fromObject(monthResult);
			if (monthJSon!=null&&!Tools.isEmpty(monthJSon.getString("errorCode"))
					&&monthJSon.getString("errorCode").equals("0")) {
				String valueString = monthJSon.getString("value");
				JSONArray valueArray = JSONArray.fromObject(valueString);
				cacheService.set("lotserver_prizeBank_month", valueArray);
			}
		}
		
		//总排行
		String totalResult = rankingService.queryPrizeRankTotal();
		if (!Tools.isEmpty(totalResult)) {
			JSONObject totalJSon = JSONObject.fromObject(totalResult);
			if (totalJSon!=null&&!Tools.isEmpty(totalJSon.getString("errorCode"))
					&&totalJSon.getString("errorCode").equals("0")) {
				String valueString = totalJSon.getString("value");
				JSONArray valueArray = JSONArray.fromObject(valueString);
				cacheService.set("lotserver_prizeBank_total", valueArray);
			}
		}
		
		logger.info("中奖排行的定时任务 end");
	}

}
