package com.ruyicai.lotserver.jms;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoutesConfiguration {
	
	private Logger logger = LoggerFactory.getLogger(RoutesConfiguration.class);

	@Resource(name="lotteryCamelContext")
	private CamelContext camelContext;
	
	@Resource(name="lotserverCamelContext")
	private CamelContext lotserverCamelContext;
	
	@PostConstruct
	public void init() throws Exception{
		logger.info("init camel routes");
		camelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				deadLetterChannel("jms:queue:dead").maximumRedeliveries(-1)
				.redeliveryDelay(3000);
				from("jms:queue:VirtualTopicConsumers.lotserver.drawLottery").to("bean:winInfoListener?method=updateWinInfoCache").routeId("开奖通知");
				from("jms:queue:VirtualTopicConsumers.lotserver.new-issue").to("bean:currentBatchCodeListener?method=updateCurrentBatchCodeCache").routeId("新期通知");
				from("jms:queue:VirtualTopicConsumers.lotserver.drawLotterySaveinfo").to("bean:winInfoDetailListener?method=updateWinInfoDetailCache").routeId("开奖详情通知");
				from("jms:queue:trycodeQueue").to("bean:tryCodeUpdateListener?method=update").routeId("试机号更新通知");
				from("jms:queue:VirtualTopicConsumers.lotserver.jingcairesult-topic").to("bean:jingCaiMatchesResultListener?method=update").routeId("竟彩赛果更新通知");
				from("jms:queue:VirtualTopicConsumers.lotserver.releaseIssusProducer").to("bean:zuCaiMatchInfoListener?method=update").routeId("足彩赛事更新通知");
			}
		});
		
		logger.info("init lotserverCamel routes");
		lotserverCamelContext.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				deadLetterChannel("jms:queue:dead").maximumRedeliveries(-1)
				.redeliveryDelay(3000);
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.recordUserInfo").to("bean:recordUserInfoListener?method=recordUserInfo").routeId("记录用户信息通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.registerUpdateUserInfo").to("bean:registerUpdateUserInfoListener?method=updateUserInfo").routeId("注册后更新用户信息通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.registerAgency").to("bean:registerAgencyListener?method=process").routeId("注册代理通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.perfectUserInfoAddScore").to("bean:perfectUserInfoAddScoreListener?method=addScore").routeId("完善用户信息送积分通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.loginSuccessAddScore").to("bean:loginSuccessAddScoreListener?method=addScore").routeId("登录成功送积分通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.iphoneSaveToken").to("bean:iphoneSaveTokenListener?method=save").routeId("登录时iphone保存token通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.recordNewsClick").to("bean:recordNewsClickListener?method=save").routeId("记录新闻点击通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.recordActivityClick").to("bean:recordActivityClickListener?method=save").routeId("记录活动中心点击通知");
				from("jmsLotserver:queue:VirtualTopicConsumers.lotserver.recordWeiboShare").to("bean:recordWeiboShareListener?method=save").routeId("记录微博分享通知");
			}
		});
	}
}
