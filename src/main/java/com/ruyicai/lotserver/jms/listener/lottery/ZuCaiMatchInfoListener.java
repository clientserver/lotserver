package com.ruyicai.lotserver.jms.listener.lottery;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.cache.CacheService;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.service.back.DataAnalysisService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 足彩赛事信息更新的jms
 * @author Administrator
 *
 */
@Service
public class ZuCaiMatchInfoListener {
	
	private Logger logger = Logger.getLogger(ZuCaiMatchInfoListener.class);
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private DataAnalysisService dataAnalysisService;

	public void update(@Header("LOTNO") String lotNo, @Header("BATCHCODE") String batchCode) {
		logger.info("足彩赛事信息更新的jms start "+"lotNo="+lotNo+";batchCode="+batchCode);
		
		String result = dataAnalysisService.getZuCaiDuiZhen(lotNo, batchCode);
		logger.info("足彩赛事信息更新的jms返回结果:"+result);
		if (Tools.isEmpty(result)) {
			return;
		}
		JSONObject fromObject = JSONObject.fromObject(result);
		if (fromObject!=null) {
			String errorCode = fromObject.getString("errorCode");
			if (errorCode!=null&&errorCode.equals("0")) {
				JSONArray list = fromObject.getJSONArray("value");
				if (list==null||list.isEmpty()) {
					return;
				}
				if (lotNo!=null&&(lotNo.equals(LotType.ZC_SFC.lotNo())||lotNo.equals(LotType.ZC_RX9.lotNo()))
						&&list.size()!=14) { //胜负彩、任九场没有14场比赛则返回
					return;
				}
				if (lotNo!=null&&lotNo.equals(LotType.ZC_JQC.lotNo())&&list.size()!=4) { //进球彩没有4场比赛则返回
					return;
				}
				if (lotNo!=null&&lotNo.equals(LotType.ZC_BQC.lotNo())&&list.size()!=6) { //半全场没有6场比赛则返回
					return;
				}
				for (int i = 0; i < list.size(); i++) {
					JSONObject valueObject = list.getJSONObject(i);
					String teamId = valueObject.getString("teamId"); //场次编号
					cacheService.set("lotserver_zuCaiMatches_"+lotNo+"_"+batchCode+"_"+teamId, valueObject);
				}
			}
		}
	}
	
}
