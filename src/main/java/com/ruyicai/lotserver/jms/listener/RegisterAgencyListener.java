package com.ruyicai.lotserver.jms.listener;

import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.service.back.AgencyCenterService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 注册代理的jms
 * @author Administrator
 *
 */
@Service
public class RegisterAgencyListener {

	private Logger logger = Logger.getLogger(RegisterAgencyListener.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private AgencyCenterService agencyCenterService;
	
	public void process(@Header("userNo") String userNo, @Header("recommender") String recommender, @Header("agencyNo") String agencyNo) {
		logger.info("注册代理jms start "+"userNo="+userNo+";recommender="+recommender+";agencyNo="+agencyNo);
		
		//优先使用用户输入的上级用户,即recommender
		String parentUserNo = commonService.getUserNoByPhoneNum(recommender);
		if (Tools.isEmpty(parentUserNo)&&!Tools.isEmpty(agencyNo)) {
			parentUserNo = agencyNo;
		}
		if (!Tools.isEmpty(parentUserNo)) {
			agencyCenterService.createUserAgency(userNo, parentUserNo);
		}
		
		/*
		//根据用户名查询上级代理用户的userNo
		String parentUserno = commonService.getUserNoByPhoneNum(recommender);
		if (!Tools.isEmpty(parentUserno)) {
			lotteryCommonService.createUserAgency(userNo, parentUserno);
		}*/
		
		//logger.info("注册代理jms end "+"userNo="+userNo+";recommender="+recommender);
	}
	
}
