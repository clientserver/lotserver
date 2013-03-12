package com.ruyicai.lotserver.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruyicai.lotserver.consts.CoopId;
import com.ruyicai.lotserver.domain.AdvertiseInfo;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.StringUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 广告公共类
 * @author Administrator
 *
 */
@Service
public class AdvertiseUtil {

	private Logger logger = Logger.getLogger(AdvertiseUtil.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 获得广告信息对象
	 * @param mac
	 * @param coopId
	 * @return
	 */
	public AdvertiseInfo getAdvertiseInfo(String mac) {
		AdvertiseInfo advertiseInfo = null;
		
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.mac=? and");
		params.add(mac);
		
		builder.append(" o.state=? ");
		params.add("1");
		
		List<AdvertiseInfo> list = AdvertiseInfo.getList(builder.toString(), "order by o.createtime desc", params);
		if (list!=null&&list.size()>0) {
			advertiseInfo = list.get(0);
		}
		
		return advertiseInfo;
	}
	
	/**
	 * 获得广告推广的渠道号
	 * @param advertiseInfo
	 * @param coopId
	 * @return
	 */
	public String getAdvertiseCoopId(AdvertiseInfo advertiseInfo, String coopId) {
		String advertiseCoopId = coopId;
		//只有从889过来的才判断是否更改渠道号
		if (advertiseInfo!=null&&coopId.equals(CoopId.appStore_guanFang.value())) {
			String source = advertiseInfo.getSource(); //渠道
			if (source!=null&&source.equals("limei")) { //力美
				advertiseCoopId = CoopId.limei_zhuCe.value();
			} else if (source!=null&&source.equals("diarnu")) { //点入
				advertiseCoopId = CoopId.dianRu_zhuCe.value();
			} else if (source!=null&&source.equals("domob")) { //多盟
				advertiseCoopId = CoopId.duoMeng_zhuCe.value();
			}
		}
		return advertiseCoopId;
	}
	
	/**
	 * 广告通知第三方
	 * @param coopId
	 */
	public void notifyThirdParty(AdvertiseInfo advertiseInfo) {
		try {
			if (advertiseInfo!=null) {
				String source = advertiseInfo.getSource(); //渠道
				if (source!=null&&source.equals("limei")) { //力美
					limeiNotify(advertiseInfo);
				} else if (source!=null&&source.equals("diarnu")) { //点入
					dianruNotify(advertiseInfo);
				} else if (source!=null&&source.equals("domob")) { //多盟
					domobNotify(advertiseInfo);
				}
			}
		} catch (Exception e) {
			logger.error("广告通知第三方发生异常", e);
		}
	}
	
	/**
	 * 力美通知
	 * @param advertiseInfo
	 */
	public void limeiNotify(AdvertiseInfo advertiseInfo) {
		String adMac = advertiseInfo.getMac(); //广告传过来的mac地址
		String appId = advertiseInfo.getAppid(); //app标识
		//转换mac(不加密,不带分隔符,大写)
		adMac = adMac.replaceAll(":", "").toUpperCase();
		
		String result = "";
		int requestCount = 1;
		while (StringUtil.isEmpty(result) && requestCount<4) {
			String url = propertiesUtil.getLimei_notifyUrl()+"?appId="+appId+"&udid="+adMac+"&returnFormat=1";
			result = HttpUtil.sendRequestByGet(url, true);
			logger.info("广告通知力美返回:"+result+";mac="+adMac+";requestCount="+requestCount);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				boolean success = fromObject.getBoolean("success");
				if (success) { //成功
					updateAdvertiseInfoState(advertiseInfo); //更新AdvertiseInfo表的状态
				}
			} else {
				requestCount++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 点乐通知
	 * @param advertiseInfo
	 * @param imei
	 */
	public void dianjoyNotify(AdvertiseInfo advertiseInfo) {
		String adMac = advertiseInfo.getMac(); //广告传过来的mac地址
		String advertiseId = advertiseInfo.getAdvertiseid(); //广告id
		
		String salt = propertiesUtil.getDianjoy_salt(); //表示两个服务器之间约定的一个0-32 位的字符串
		String token = Tools.md5(salt+advertiseId+adMac); //加密串
		
		String result = "";
		int requestCount = 1;
		while (StringUtil.isEmpty(result) && requestCount<4) {
			String url = propertiesUtil.getDianjoy_notifyUrl()+"?device_id="+adMac+"&ad_id="+advertiseId+"&token="+token;
			result = HttpUtil.sendRequestByGet(url, true);
			logger.info("广告通知点乐返回:"+result+";mac="+adMac+";requestCount="+requestCount);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				if (fromObject!=null) {
					String status = fromObject.getString("status");
					if (status!=null&&status.equals("1")) { //成功
						updateAdvertiseInfoState(advertiseInfo); //更新AdvertiseInfo表的状态
					}
				}
			} else {
				requestCount++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 点入通知
	 * @param advertiseInfo
	 */
	public void dianruNotify(AdvertiseInfo advertiseInfo) {
		String drkey = advertiseInfo.getDrkey(); 
		
		String result = "";
		int requestCount = 1;
		while (StringUtil.isEmpty(result) && requestCount<4) {
			String url = propertiesUtil.getDianru_notifyUrl()+"?drkey="+drkey+"&isvalidactive=true";
			result = HttpUtil.sendRequestByGet(url, true);
			logger.info("广告通知点入返回:"+result+";drkey="+drkey+";requestCount="+requestCount);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				String status = fromObject.getString("status");
				if (status!=null&&status.equals("1")) { //成功
					updateAdvertiseInfoState(advertiseInfo); //更新AdvertiseInfo表的状态
				}
			} else {
				requestCount++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 多盟通知
	 * @param advertiseInfo
	 */
	public void domobNotify(AdvertiseInfo advertiseInfo) {
		String appId = advertiseInfo.getAppid();
		String mac = advertiseInfo.getMac();
		
		String result = "";
		int requestCount = 1;
		while (StringUtil.isEmpty(result) && requestCount<4) {
			String url = propertiesUtil.getDomob_notifyUrl()+"?appId="+appId+"&udid="+mac+"&returnFormat=1";
			result = HttpUtil.sendRequestByGet(url, true);
			logger.info("广告通知多盟返回:"+result+";mac="+mac+";requestCount="+requestCount);
			if (!Tools.isEmpty(result)) {
				JSONObject fromObject = JSONObject.fromObject(result);
				boolean success = fromObject.getBoolean("success");
				if (success) { //成功
					updateAdvertiseInfoState(advertiseInfo); //更新AdvertiseInfo表的状态
				}
			} else {
				requestCount++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 更新AdvertiseInfo的状态
	 * @param advertiseInfo
	 */
	public void updateAdvertiseInfoState(AdvertiseInfo advertiseInfo) {
		advertiseInfo.setState("0");
		advertiseInfo.setUpdatetime(new Date());
		advertiseInfo.merge();
	}
	
}
