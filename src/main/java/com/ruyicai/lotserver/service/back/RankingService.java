package com.ruyicai.lotserver.service.back;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;

/**
 * 中奖排行的接口
 * @author Administrator
 *
 */
@Service
public class RankingService {

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 中奖排行-周排行
	 * @return
	 */
	public String queryPrizeRankWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String url = propertiesUtil.getRankingUrl()+"selectUserRankingHistory?time="+sdf.format(new Date())+"&type=2";
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("中奖排行-周排行后台返回:" + result + ",url:" + url);
		return result;
	}
	
	/**
	 * 中奖排行-月排行
	 * @return
	 */
	public String queryPrizeRankMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String url = propertiesUtil.getRankingUrl()+"selectUserRankingHistory?time="+sdf.format(new Date())+"&type=3";
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("中奖排行-月排行后台返回:" + result + ",url:" + url);
		return result;
	}
	
	/**
	 * 中奖排行-总排行
	 * @return
	 */
	public String queryPrizeRankTotal() {
		String url = propertiesUtil.getRankingUrl()+"selectUserRankingHistory?time="+"all"+"&type=5";
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("中奖排行-总排行后台返回:" + result + ",url:" + url);
		return result;
	}
	
}
