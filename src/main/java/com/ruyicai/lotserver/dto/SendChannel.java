package com.ruyicai.lotserver.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

@RooJson
@RooJavaBean
public class SendChannel {

	private Long id;

	/** 是否有效 0:不发送,1:发送 */
	private Integer needToSend;

}
