package com.ruyicai.lotserver.jms.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.camel.Header;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ruyicai.lotserver.consts.Platform;
import com.ruyicai.lotserver.domain.AdvertiseInfo;
import com.ruyicai.lotserver.domain.UserInf;
import com.ruyicai.lotserver.util.AdvertiseUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 *  记录用户信息的jms
 * @author Administrator
 *
 */
@Service
public class RecordUserInfoListener {

	private Logger logger = Logger.getLogger(RecordUserInfoListener.class);
	
	@Autowired
	private AdvertiseUtil advertiseUtil;
	
	@Transactional
	public void recordUserInfo(@Header("imei") String imei, @Header("imsi") String imsi, @Header("platform") String platform, 
			@Header("machineId") String machineId, @Header("isEmulator") String isEmulator, @Header("softwareVersion") String softwareVersion,
			@Header("coopId") String coopId, @Header("phoneSIM") String phoneSIM, @Header("mac") String mac) {
		logger.info("记录用户信息jms start "+"imei="+imei);
		try {
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.imei=?");
			params.add(imei);
			List<UserInf> list = UserInf.getList(builder.toString(), "", params);
			if (list==null||list.size()==0) { //无此用户数据记录,是个新来的用户,创建新用户
				//AdvertiseInfo advertiseInfo = null; //获得广告信息对象
				if (platform!=null&&platform.equals(Platform.iPhone.value())) { //iPhone
					AdvertiseInfo advertiseInfo = advertiseUtil.getAdvertiseInfo(mac);
					coopId = advertiseUtil.getAdvertiseCoopId(advertiseInfo, coopId);
				}
				CommonUtil.addUserInf(imei, imsi, machineId, platform, softwareVersion, coopId, isEmulator, phoneSIM, mac); //插入记录
				/*if (platform!=null&&platform.equals(Platform.iPhone.value())) { //iPhone
					advertiseUtil.notifyThirdParty(advertiseInfo); //通知第三方
				}*/
			} else { //是老用户了
				UserInf userInf = list.get(0);
				if(Tools.isEmpty(userInf.getImsi())) { //原用户的imsi为空
					if(Tools.isEmpty(imsi)) { //但是这次imsi不是空了,说明用户用上手机卡了
						userInf.setImsi(imsi);
					}
				}
				if(!userInf.getSoftwareversion().equals(softwareVersion)) { //版本号变化了需要更新版本
					userInf.setSoftwareversion(softwareVersion);
				}
				if(!Tools.isEmpty(isEmulator)) { //是否使用模拟器
					userInf.setIsemular(isEmulator);
				}
				String dbPhoneSIM = userInf.getPhoneSIM(); //数据库中的SIM手机号
				//如果数据库中的SIM手机号为空或者有变化则更新
				if (!Tools.isEmpty(phoneSIM)&&(Tools.isEmpty(dbPhoneSIM)||!dbPhoneSIM.equals(phoneSIM))) { 
					userInf.setPhoneSIM(phoneSIM);
				}
				String dbMac = userInf.getMac(); //数据库中的网卡地址
				//如果数据库中的mac为空或者有变化则更新
				if (!Tools.isEmpty(mac)&&(Tools.isEmpty(dbMac)||!dbMac.equalsIgnoreCase(mac))) { 
					userInf.setMac(mac);
				}
				userInf.setLastnetconnecttime(new Date()); //更新最后联网日期
				userInf.merge();
			}
		} catch (Exception e) {
			logger.error("记录用户信息的jms发生异常imei="+imei, e);
		}
		//logger.info("记录用户信息jms end "+"imei="+imei);
	}
	
}
