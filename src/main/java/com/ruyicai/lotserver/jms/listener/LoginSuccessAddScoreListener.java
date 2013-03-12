package com.ruyicai.lotserver.jms.listener;

import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.service.back.ScoreCenterService;

/**
 *  登录成功送积分的jms
 * @author Administrator
 *
 */
@Service
public class LoginSuccessAddScoreListener {

	private Logger logger = Logger.getLogger(LoginSuccessAddScoreListener.class);
	
	@Autowired
	private ScoreCenterService scoreCenterService;
	
	public void addScore(@Header("userNo") String userNo) {
		logger.info("登录成功送积分jms start "+"userNo="+userNo);
		
		try {
			scoreCenterService.addTuserinfoScore(userNo, 8, "用户登录");
		} catch (Exception e) {
			logger.error("登录成功送积分发生异常", e);
		}
		
		//logger.info("登录成功送积分jms end "+"userNo="+userNo);
	}
	
}
