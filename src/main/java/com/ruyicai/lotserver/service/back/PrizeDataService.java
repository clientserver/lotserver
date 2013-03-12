package com.ruyicai.lotserver.service.back;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;

/**
 * 遗漏值的接口
 * @author Administrator
 *
 */
@Service
public class PrizeDataService {
	
	@Autowired
	private PropertiesUtil propertiesUtil;

	/**
	 * 获取遗漏值
	 * 
	 * @param userNo
	 * @param scoreType
	 * @return
	 */
	public String getMissValue(String lotNo, String batchCode, String key) {
		String url = propertiesUtil.getPrizeDataUrl()+"select/missvalue2nd?lotno="+lotNo
						+"&batchcode="+ batchCode+"&key="+key;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("获取遗漏值返回:" + result + ",url:" + url);
		return result;
	}
	
}
