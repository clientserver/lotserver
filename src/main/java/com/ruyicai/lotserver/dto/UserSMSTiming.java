package com.ruyicai.lotserver.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 消息提醒设置
 * @author Administrator
 *
 */
@RooJson
@RooJavaBean
public class UserSMSTiming {

	public Integer id;
	
	/** 是否有效 0:不发送,1:发送 */
	private Integer needToSend;

}
