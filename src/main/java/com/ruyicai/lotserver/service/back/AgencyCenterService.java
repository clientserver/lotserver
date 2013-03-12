package com.ruyicai.lotserver.service.back;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;

/**
 * 代理的接口
 * @author Administrator
 *
 */
@Service
public class AgencyCenterService {
	
	private static Logger logger = Logger.getLogger(AgencyCenterService.class);

	@Autowired
	PropertiesUtil propertiesUtil;

	/**
	 * 创建代理用户
	 * @param userNo
	 * @param recommender
	 * @return
	 */
	public String createUserAgency(String userNo, String parentUserno) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);
		paramStr.append("&parentUserno=" + parentUserno);
		
		String url = propertiesUtil.getAgencyCenterUrl() + "createUserAgency";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("创建代理用户返回:"+result+",userNo:"+userNo+";parentUserno:"+parentUserno);
		return result;
	}
	
}
