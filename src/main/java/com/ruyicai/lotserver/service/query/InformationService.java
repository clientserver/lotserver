package com.ruyicai.lotserver.service.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.domain.Activity;
import com.ruyicai.lotserver.domain.ExpertCode;
import com.ruyicai.lotserver.domain.HelpCenter;
import com.ruyicai.lotserver.domain.LotTypeInfo;
import com.ruyicai.lotserver.domain.News;
import com.ruyicai.lotserver.domain.TversionInfo;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.ActivityCenterUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Page;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;

/**
 * 资讯相关的Service
 * @author Administrator
 *
 */
@Service
public class InformationService  {
	
	private Logger logger = Logger.getLogger(InformationService.class);
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ActivityCenterUtil activityCenterUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Produce(uri = "jmsLotserver:topic:recordNewsClick", context = "lotserverCamelContext")
	private ProducerTemplate recordNewsClickTemplate;
	
	/**
	 * 旧版本的资讯查询
	 * @param clientInfo
	 * @return
	 */
	public String manage(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			logger.info("该接口已废弃  旧版本的资讯查询(manage),platform:"+clientInfo.getPlatform()+";softwareVersion:"+clientInfo.getSoftwareVersion()
					+";imei:"+clientInfo.getImei()+";userNo:"+clientInfo.getUserno()+";userName:"+clientInfo.getPhonenum());
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.vol_typeid_fk=? and");
			params.add(clientInfo.getType());
			
			builder.append(" o.state='1' ");
			
			Page<News> page = new Page<News>();
			page.setPageIndex(0);
			page.setMaxResult(10);
			News.findList(builder.toString(), "order by o.updatetime desc", params, page);
			List<News> list = page.getList();
			
			responseJson.put("type", "1");
			responseJson.put("curpage", "1");
			responseJson.put("totalpage", "10");
			if (list!=null&&list.size()>0) {
				for (News news : list) {
					JSONObject object = new JSONObject();
					object.put("title", news.getVol_title());
					object.put("content", news.getVol_content());
					resultArray.add(object);
				}
			}
		} catch (Exception e) {
			logger.error("旧版本的资讯查询发生异常", e);
		}
		responseJson.put("news", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获取资讯标题列表
	 * @param clientInfo
	 * @return
	 */
	public String getNewsTitle(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "80";
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.vol_typeid_fk=? and");
			params.add(clientInfo.getType());
			
			String lotNo = clientInfo.getLotNo(); //彩种
			if (!Tools.isEmpty(lotNo)) {
				builder.append(" o.lotno=? and");
				params.add(lotNo);
			}
			
			builder.append(" o.state='1' ");
			
			Page<News> page = new Page<News>();
			page.setPageIndex(Integer.parseInt(pageIndex)-1);
			page.setMaxResult(Integer.parseInt(maxResult));
			News.findList(builder.toString(), "order by o.updatetime desc", params, page);
			List<News> list = page.getList();
			if (list!=null&&list.size()>0) {
				totalPage = page.getTotalPage()+"";
				for (News news : list) {
					JSONObject object = new JSONObject();
					object.put("newsId", news.getId());
					object.put("title", news.getVol_title());
					resultArray.add(object);
				}
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取资讯标题列表发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("news", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获取活动标题列表
	 * @param clientInfo
	 * @return
	 */
	public String getActivityTitle(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)||pageIndex.equals("null")) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)||maxResult.equals("null")) {
				maxResult = "15";
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			//根据渠道号获得产品编号
			String coopId = clientInfo.getCoopId(); //渠道号
			String productNo = commonUtil.getProductNoByCoopId(coopId);
			
			builder.append(" o.productno=? ");
			params.add(productNo);
			
			Page<Activity> page = new Page<Activity>();
			page.setPageIndex(Integer.parseInt(pageIndex)-1<0?0:Integer.parseInt(pageIndex)-1); //iphone3.2版本第一页pageIndex=0,第二页pageIndex=2
			page.setMaxResult(Integer.parseInt(maxResult));
			Activity.findList(builder.toString(), "order by o.isend asc, o.updatetime desc", params, page);
			List<Activity> list = page.getList();
			if (list!=null&&list.size()>0) {
				totalPage = page.getTotalPage()+"";
				for (Activity activity : list) {
					JSONObject object = new JSONObject();
					object.put("activityId", activity.getId()); //活动id
					object.put("title", activity.getTitle()); //活动标题
					object.put("introduce", activity.getIntroduce()); //活动介绍
					object.put("activityTime", activity.getActivitytime()); //活动时间
					object.put("isEnd", activity.getIsend()); //活动是否结束
					resultArray.add(object);
				}
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取活动标题列表发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 专家荐号查询
	 * @param clientInfo
	 * @return
	 */
	public String getExpertCode(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "10";
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.type=? ");
			params.add(Integer.parseInt(clientInfo.getType()));
			
			Page<ExpertCode> page = new Page<ExpertCode>();
			page.setPageIndex(Integer.parseInt(pageIndex)-1);
			page.setMaxResult(Integer.parseInt(maxResult));
			ExpertCode.findList(builder.toString(), "order by o.id desc", params, page);
			List<ExpertCode> list = page.getList();
			if (list!=null&&list.size()>0) {
				totalPage = page.getTotalPage()+"";
				for (ExpertCode expertCode : list) {
					JSONObject object = new JSONObject();
					object.put("title", expertCode.getTitle()); //标题
					object.put("messageCode", expertCode.getMessagecode()); //短信码
					object.put("toPhone", expertCode.getTophone()); //发送到的号码
					object.put("content", expertCode.getContent()); //内容
					object.put("alertMessage", expertCode.getAlertmessage()); //弹出信息
					object.put("buttonText", expertCode.getButtontext()); //按钮文字
					resultArray.add(object);
				}
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("专家荐号查询发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 根据newsId获得资讯内容
	 * @param clientInfo
	 * @return
	 */
	public String getNewsContent(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String newsId = clientInfo.getNewsId();
			News news = News.findNews(Integer.parseInt(newsId));
			if (news!=null) {
				String newsType = news.getVol_typeid_fk(); //新闻类型
				
				responseJson.put("title", news.getVol_title());
				responseJson.put("content", news.getVol_content());
				responseJson.put("updateTime", sdf.format(news.getUpdatetime()));
				responseJson.put("url", "http://wap.ruyicai.com/w/news/wapNews.jspx?id="+newsId+"&type="+newsType);
				
				//记录新闻点击
				recordNewsClickJMS(clientInfo.getImei(), news.getId(), news.getProductno(), newsType);
			} else {
				responseJson.put("title", "");
				responseJson.put("content", "");
			}
		} catch (Exception e) {
			responseJson.put("title", "");
			responseJson.put("content", "");
			logger.error("根据newsId获得资讯内容发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 查询Top新闻的内容
	 * @param clientInfo
	 * @return
	 */
	public String getTopNewsContent(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		//根据渠道号获得产品编号
		String coopId = clientInfo.getCoopId(); //渠道号
		String productNo = commonUtil.getProductNoByCoopId(coopId);
		
		List<News> list = commonService.queryTopNews(productNo, clientInfo);
		if (list!=null && list.size()>0) {
			News news = list.get(0);
			responseJson.put("title", news.getVol_title());
			responseJson.put("content", news.getVol_content());
			
			//记录新闻点击
			recordNewsClickJMS(clientInfo.getImei(), news.getId(), news.getProductno(), news.getVol_typeid_fk());
		} else {
			responseJson.put("title", "");
			responseJson.put("content", "");
		}
		return responseJson.toString();
	}
	
	/**
	 * 记录新闻点击的JMS
	 * @param imei
	 * @param newsId
	 * @param newsType
	 */
	public void recordNewsClickJMS(String imei, Integer newsId, String productNo, String newsType) {
		Map<String, Object> header = new HashMap<String, Object>();
		header.put("imei", imei);
		header.put("newsId", newsId);
		header.put("newsType", newsType);
		header.put("productNo", productNo);
		
		logger.info("recordNewsClickTemplate start, headers:" + header);
		recordNewsClickTemplate.sendBodyAndHeaders(null, header);
		//logger.info("recordNewsClickTemplate end, headers:" + header);
	}
	
	/**
	 * 根据activityId获得活动内容
	 * @param clientInfo
	 * @return
	 */
	public String getActivityContent(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			Activity activity = Activity.findActivity(Integer.parseInt(clientInfo.getActivityId()));
			if (activity!=null) {
				responseJson.put("title", activity.getTitle());
				responseJson.put("content", activity.getContent());
				responseJson.put("url", ""); //91做投票活动送彩金时使用,现在已不再使用
				
				Integer activityId = activity.getId(); //活动编号
				//记录活动中心点击
				activityCenterUtil.recordActivityClickJMS(clientInfo.getImei(), activityId, activity.getProductno());
			} else {
				responseJson.put("title", "");
				responseJson.put("content", "");
			}
		} catch (Exception e) {
			responseJson.put("title", "");
			responseJson.put("content", "");
			logger.error("根据activityId获得活动内容发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 获得积分规则
	 * @param clientInfo
	 * @return
	 */
	public String getScoreRule(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		
		try {
			responseJson.put("content", commonUtil.getMessageContentByKeyStr("scoreRule"));
		} catch (Exception e) {
			responseJson.put("content", "");
			logger.error("获得积分规则发生异常", e);
		}
		
		return responseJson.toString();
	}
	
	/**
	 * 根据键获得内容
	 * @param clientInfo
	 * @return
	 */
	public String getMessageContentByKeyStr(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		
		try {
			responseJson.put("content", commonUtil.getMessageContentByKeyStr(clientInfo.getKeyStr()));
		} catch (Exception e) {
			responseJson.put("content", "");
			logger.error("根据键获得内容发生异常", e);
		}
		
		return responseJson.toString();
	}
	
	/**
	 * 获得帮助中心标题列表
	 * @param clientInfo
	 * @return
	 */
	public String getHelpCenterTitle(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "1";
			}
			String maxResult = clientInfo.getMaxresult(); //每页显示多少条
			if (Tools.isEmpty(maxResult)) {
				maxResult = "30";
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.type=? and");
			params.add(clientInfo.getType());
			
			builder.append(" o.platform=?");
			params.add(clientInfo.getPlatform());
			
			Page<HelpCenter> page = new Page<HelpCenter>();
			page.setPageIndex(Integer.parseInt(pageIndex)-1);
			page.setMaxResult(Integer.parseInt(maxResult));
			HelpCenter.findList(builder.toString(), "order by o.id asc", params, page);
			List<HelpCenter> list = page.getList();
			if (list!=null&&list.size()>0) {
				totalPage = page.getTotalPage()+"";
				for (HelpCenter helpCenter : list) {
					JSONObject object = new JSONObject();
					object.put("id", helpCenter.getId());
					object.put("title", helpCenter.getTitle());
					resultArray.add(object);
				}
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获得帮助中心标题列表发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 获得帮助中心内容
	 * @param clientInfo
	 * @return
	 */
	public String getHelpCenterContent(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			HelpCenter helpCenter = HelpCenter.findHelpCenter(Integer.parseInt(clientInfo.getId()));
			if (helpCenter!=null) {
				responseJson.put("title", helpCenter.getTitle());
				responseJson.put("content", helpCenter.getContent());
			} else {
				responseJson.put("title", "");
				responseJson.put("content", "");
			}
		} catch (Exception e) {
			responseJson.put("title", "");
			responseJson.put("content", "");
			logger.error("根据newsId获得资讯内容发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 获取版本介绍
	 * @param clientInfo
	 * @return
	 */
	public String getVersionIntroduce(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String platform = clientInfo.getPlatform(); //平台
			String coopId = clientInfo.getCoopId(); //渠道号
			String softwareVersion = clientInfo.getSoftwareVersion(); //版本号
			if (softwareVersion.length()>5) {
				softwareVersion = softwareVersion.substring(0, 5);
			}
			//根据渠道号获得产品编号
			String productNo = commonUtil.getProductNoByCoopId(coopId);
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.productno=? and");
			params.add(productNo);
			
			builder.append(" o.platform=? and");
			params.add(platform);
			
			builder.append(" o.version=? ");
			params.add(softwareVersion); //判断前3位
			
			List<TversionInfo> list = TversionInfo.getList(builder.toString(), "", params);
			if (list!=null&&list.size()>0) {
				TversionInfo tversionInfo = list.get(0);
				
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
				responseJson.put("introduce", tversionInfo.getVersionintroduce());
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取版本介绍发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 获取玩法介绍
	 * @param clientInfo
	 * @return
	 */
	public String getPlayIntroduce(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种编号
			if (Tools.isEmpty(lotNo)) { //彩种编号为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			if (LotTypeUtil.isZuCai(lotNo)) { //足彩
				lotNo = "ZC";
			} else if (LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩篮球
				lotNo = "JC_L";
			} else if (LotTypeUtil.isJingCaiZQ(lotNo)) { //竞彩足球
				lotNo = "JC_Z";
			}
			
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.lotno=? ");
			params.add(lotNo);
			
			List<LotTypeInfo> list = LotTypeInfo.getList(builder.toString(), "", params);
			if (list!=null&&list.size()>0) {
				LotTypeInfo lotTypeInfo = list.get(0);
				
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "查询成功");
				responseJson.put("title", lotTypeInfo.getTitle());
				responseJson.put("introduce", lotTypeInfo.getIntroduce());
			} else {
				responseJson.put(Constants.error_code, "0047");
				responseJson.put(Constants.message, "无记录");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("获取玩法介绍发生异常", e);
		}
		return responseJson.toString();
	}
	
}
