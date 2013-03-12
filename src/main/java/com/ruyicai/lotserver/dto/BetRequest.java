package com.ruyicai.lotserver.dto;

import java.math.BigDecimal;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 存放注码的DTO
 * @author Administrator
 *
 */
@RooJson
@RooJavaBean
public class BetRequest {

	private String betcode;

	private BigDecimal amt;

}
