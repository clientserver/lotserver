package com.ruyicai.lotserver.jms.listener;

import net.sf.json.JSONObject;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.back.ScoreCenterService;
import com.ruyicai.lotserver.util.common.Tools;

/**
 *  完善用户信息送积分的jms
 * @author Administrator
 *
 */
@Service
public class PerfectUserInfoAddScoreListener {

	private Logger logger = Logger.getLogger(PerfectUserInfoAddScoreListener.class);
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private ScoreCenterService scoreCenterService;
	
	public void addScore(@Header("userNo") String userNo) {
		logger.info("完善用户信息送积分jms start "+"userNo="+userNo);
		
		try {
			String result = lotteryService.queryUsersByUserNo(userNo);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
						&&fromObject.getString("errorCode").equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					String mobileId = valueObject.getString("mobileid");
					String userName = valueObject.getString("userName");
					String certId = valueObject.getString("certid");
					String name = valueObject.getString("name");
					if (!Tools.isEmpty(mobileId)&&!mobileId.equals("null")
							&&!Tools.isEmpty(userName)&&!userName.equals("null")
							&&!Tools.isEmpty(certId)&&!certId.equals("null")
							&&!Tools.isEmpty(name)&&!name.equals("null")) {
						//信息完整,调用赠送积分的方法
						scoreCenterService.addTuserinfoScore(userNo, 1, "注册并完善信息");
					}
				}
			}
		} catch (Exception e) {
			logger.error("完善用户信息送积分发生异常", e);
		}
		
		//logger.info("完善用户信息送积分jms end "+"userNo="+userNo);
	}
	
}
