package com.ruyicai.lotserver.service.query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 遗漏值相关查询
 * @author Administrator
 *
 */
@Service
public class MissValueQueryService {

	private Logger logger = Logger.getLogger(MissValueQueryService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 获取遗漏值列表
	 * @param clientInfo
	 * @return
	 */
	public String getList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			String sellway = clientInfo.getSellway(); //玩法
			String batchNum = clientInfo.getBatchnum(); //期数
			
			String url = propertiesUtil.getPrizeDataUrl()+"select/latestCache?lotno="+lotNo
							+"&key="+sellway+"&count="+batchNum;
			String result = HttpUtil.sendRequestByGet(url, true);
			//logger.info("获取遗漏值列表返回:" + result + ",url:" + url);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONArray list = fromObject.getJSONArray("value");
					if (list!=null&&list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							JSONObject missValueObject = list.getJSONObject(i);
							String value = missValueObject.getString("value"); //遗漏值
							
							JSONObject idObject = missValueObject.getJSONObject("id");
							String batchCode = idObject.getString("batchcode"); //期号
							
							JSONObject object = new JSONObject();
							object.put("batchCode", batchCode); //期号
							object.put("value", value); //遗漏值
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询竞彩即时比分发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
}
