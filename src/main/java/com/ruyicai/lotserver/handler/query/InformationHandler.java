package com.ruyicai.lotserver.handler.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.protocol.LotserverInterfaceHandler;
import com.ruyicai.lotserver.service.query.InformationService;

/**
 * 资讯相关请求
 * @author Administrator
 *
 */
@Service("information")
public class InformationHandler implements LotserverInterfaceHandler {
	
	@Autowired
	private InformationService informationService;
	
	public String execute(ClientInfo clientInfo) {
		String responseString = "";
		
		String newsType = clientInfo.getNewsType(); //请求类型
		if(newsType!=null&&newsType.equals("title")) { //查询新闻标题
			responseString = informationService.getNewsTitle(clientInfo);
		} else if (newsType!=null&&newsType.equals("content")) { //查询新闻的内容
			responseString = informationService.getNewsContent(clientInfo);
		} else if (newsType!=null&&newsType.equals("topNews")) { //Top新闻内容
			responseString = informationService.getTopNewsContent(clientInfo);
		} else if (newsType!=null&&newsType.equals("activityTitle")) { //查询活动标题
			responseString = informationService.getActivityTitle(clientInfo);
		} else if (newsType!=null&&newsType.equals("activityContent")) { //查询活动内容
			responseString = informationService.getActivityContent(clientInfo);
		} else if (newsType!=null&&newsType.equals("helpCenterTitle")) { //帮助中心标题
			responseString = informationService.getHelpCenterTitle(clientInfo);
		} else if (newsType!=null&&newsType.equals("helpCenterContent")) { //帮助中心内容
			responseString = informationService.getHelpCenterContent(clientInfo);
		} else if (newsType!=null&&newsType.equals("expertCode")) { //专家荐号查询
			responseString = informationService.getExpertCode(clientInfo);
		} else if (newsType!=null&&newsType.equals("scoreRule")) { //积分规则
			responseString = informationService.getScoreRule(clientInfo);
		} else if (newsType!=null&&newsType.equals("messageContent")) { //根据键获得内容
			responseString = informationService.getMessageContentByKeyStr(clientInfo);
		}  else if (newsType!=null&&newsType.equals("versionIntroduce")) { //版本介绍
			responseString = informationService.getVersionIntroduce(clientInfo);
		} else if (newsType!=null&&newsType.equals("playIntroduce")) { //玩法介绍
			responseString = informationService.getPlayIntroduce(clientInfo);
		} else { //旧的新闻资讯查询
			responseString = informationService.manage(clientInfo);
		}
		
		return responseString;
	}
	
}
