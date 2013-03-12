package com.ruyicai.lotserver.dto;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

/**
 * 存放订单的DTO
 * 
 * @author Administrator
 * 
 */
@RooJson
@RooJavaBean
public class OrderRequest {

	String userno;
	String buyuserno;
	String lotno;
	String batchcode;
	BigDecimal lotmulti;
	BigDecimal amt;
	BigDecimal oneamount;
	BigDecimal bettype;
	BigDecimal paytype;
	BigDecimal lotsType; // 订单类型 0-单式上传，1-复式，2-胆拖，3-多方案
	BigDecimal prizeend; // 中奖后停止追号
	String Blessing; // 赠送寄语
	String reciverMobile; // 赠送彩票接收人手机号码
	List<BetRequest> betRequests;
	List<SubscribeRequest> subscribeRequests;
	CaseLotRequest caseLotRequest;
	String subchannel;
	String channel;
	String desc;

}
