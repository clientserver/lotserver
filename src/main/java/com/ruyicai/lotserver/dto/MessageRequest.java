package com.ruyicai.lotserver.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 存放留言的DTO
 * @author Administrator
 *
 */
@RooJson
@RooJavaBean
public class MessageRequest {

	private String userno;

	private String imsi;

	private String content;
	
	private String detail;

}
