package com.ruyicai.lotserver.util.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUtil {

	@Value("${lotteryUrl}")
	private String lotteryUrl;
	public String getLotteryUrl() {
		return lotteryUrl;
	}
	
	@Value("${chargeCenterUrl}")
	private String chargeCenterUrl;
	public String getChargeCenterUrl() {
		return chargeCenterUrl;
	}
	
	@Value("${agencyCenterUrl}")
	private String agencyCenterUrl;
	public String getAgencyCenterUrl() {
		return agencyCenterUrl;
	}

	@Value("${msgCenterUrl}")
	private String msgCenterUrl;
	public String getMsgCenterUrl() {
		return msgCenterUrl;
	}
	
	@Value("${rankingUrl}")
	private String rankingUrl;
	public String getRankingUrl() {
		return rankingUrl;
	}
	
	@Value("${scoreCenterUrl}")
	private String scoreCenterUrl;
	public String getScoreCenterUrl() {
		return scoreCenterUrl;
	}
	
	@Value("${messageUrl}")
	private String messageUrl;
	public String getMessageUrl() {
		return messageUrl;
	}
	
	@Value("${prizeDataUrl}")
	private String prizeDataUrl;
	public String getPrizeDataUrl() {
		return prizeDataUrl;
	}
	
	@Value("${dataAnalysisUrl}")
	private String dataAnalysisUrl;
	public String getDataAnalysisUrl() {
		return dataAnalysisUrl;
	}
	
	@Value("${iphone_isWapPage_userno}")
	private String iphone_isWapPage_userno;
	public String getIphone_isWapPage_userno() {
		return iphone_isWapPage_userno;
	}
	
	@Value("${limei.notifyUrl}")
	private String limei_notifyUrl;
	public String getLimei_notifyUrl() {
		return limei_notifyUrl;
	}

	@Value("${dianjoy.salt}")
	private String dianjoy_salt;
	public String getDianjoy_salt() {
		return dianjoy_salt;
	}

	@Value("${dianjoy.notifyUrl}")
	private String dianjoy_notifyUrl;
	public String getDianjoy_notifyUrl() {
		return dianjoy_notifyUrl;
	}
	
	@Value("${dianru.notifyUrl}")
	private String dianru_notifyUrl;
	public String getDianru_notifyUrl() {
		return dianru_notifyUrl;
	}
	
	@Value("${domob.notifyUrl}")
	private String domob_notifyUrl;
	public String getDomob_notifyUrl() {
		return domob_notifyUrl;
	}
	
}
