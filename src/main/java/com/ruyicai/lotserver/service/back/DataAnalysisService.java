package com.ruyicai.lotserver.service.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;

/**
 * 数据分析的接口
 * @author Administrator
 *
 */
@Service
public class DataAnalysisService {

	//private static Logger logger = Logger.getLogger(DataAnalysisService.class);

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 获取足彩对阵
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public String getZuCaiDuiZhen(String lotNo, String batchCode) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("lotno="+lotNo); //彩种编号
		paramStr.append("&batchcode="+batchCode ); //期号
		
		String url = propertiesUtil.getDataAnalysisUrl() + "selectZc/getDuiZhen";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("获取足彩对阵返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 获取足彩某场比赛的信息
	 * @param lotNo
	 * @param batchCode
	 * @param teamId
	 * @return
	 */
	public String getZuCaiMatches(String lotNo, String batchCode, String teamId) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("lotno=" + lotNo);
		paramStr.append("&batchcode=" + batchCode);
		paramStr.append("&teamid=" + teamId);

		String url = propertiesUtil.getDataAnalysisUrl() + "selectZc/getMatchInfo";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("获取足彩某场比赛的信息的返回:" + result + ",paramStr:" + paramStr.toString());
		return result;
	}
	
}
