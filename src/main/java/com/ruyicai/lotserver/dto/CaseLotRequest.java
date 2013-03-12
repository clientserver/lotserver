package com.ruyicai.lotserver.dto;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 存放合买的DTO
 * @author Administrator
 *
 */
@RooJson
@RooJavaBean
public class CaseLotRequest {
	
	private long buyAmt;
	private long safeAmt;
	private long totalAmt;
	private int commisionRatio;
	private String title;
	private String desc;
	private int visibility;
	private long minAmt;
	private String starter;
	private String caselotinfo;
	
}
