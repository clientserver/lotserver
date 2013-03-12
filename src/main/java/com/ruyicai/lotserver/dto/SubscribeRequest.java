package com.ruyicai.lotserver.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 存放追号的DTO
 * @author Administrator
 *
 */
@RooJson
@RooJavaBean
public class SubscribeRequest {

	private BigDecimal lotmulti;
	
	private BigDecimal amt;
	
	private String batchcode;
	
	private Date endtime;
	
	private String desc;
	
	private BigDecimal lotsType;

}
