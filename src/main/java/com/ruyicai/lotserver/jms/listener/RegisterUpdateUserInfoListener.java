package com.ruyicai.lotserver.jms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Platform;
import com.ruyicai.lotserver.domain.AdvertiseInfo;
import com.ruyicai.lotserver.domain.UserInf;
import com.ruyicai.lotserver.util.AdvertiseUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 *  注册后更新用户信息的jms
 * @author Administrator
 *
 */
@Service
public class RegisterUpdateUserInfoListener {

	private Logger logger = Logger.getLogger(RegisterUpdateUserInfoListener.class);
	
	@Autowired
	private AdvertiseUtil advertiseUtil;
	
	public void updateUserInfo(@Header("imei") String imei, @Header("phoneNum") String phoneNum, 
			@Header("userNo") String userNo, @Header("platform") String platform, @Header("mac") String mac) {
		logger.info("注册后更新用户信息jms start "+"imei="+imei+";phoneNum="+phoneNum+";userNo="+userNo
				+";platform="+platform+";mac="+mac);
		try {
			//通知第三方积分墙
			if (platform!=null&&platform.equals(Platform.iPhone.value())) { //iPhone
				AdvertiseInfo advertiseInfo = advertiseUtil.getAdvertiseInfo(mac);
				advertiseUtil.notifyThirdParty(advertiseInfo); //通知第三方
			}
			//更新用户信息
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.imei=?");
			params.add(imei);
			
			List<UserInf> list = UserInf.getList(builder.toString(), "", params);
			if (list!=null&&list.size()>0) {
				UserInf userInf = list.get(0);
				userInf.setMobilenum(phoneNum);
				if (!Tools.isEmpty(userNo)) { //用户编号不为空就更新
					userInf.setUserno(userNo);
				}
				userInf.setRegtime(new Date());
				userInf.merge();
			} else {
				logger.error("注册后更新用户信息时用户表记录为空,Imei="+imei);
			}
		} catch (Exception e) {
			logger.error("注册后更新用户信息时发生异常imei="+imei+";phoneNum="+phoneNum+";userNo="+userNo, e);
		}
		//logger.info("注册后更新用户信息jms end "+"imei="+imei+";phoneNum="+phoneNum+";userNo="+userNo);
	}
	
}
