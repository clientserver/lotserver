package com.ruyicai.lotserver.service.query;

import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.domain.GiftMessage;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.Page;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 赠送短信
 * 
 * @author Administrator
 * 
 */
@Service
public class GiftMessageService {

	private Logger logger = Logger.getLogger(GiftMessageService.class);

	/**
	 * 新赠送短信查询
	 * @param clientInfo
	 * @return
	 */
	public String getGiftMessageNew(ClientInfo clientInfo) {
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
			
			builder.append(" o.state=? ");
			params.add("1");
			
			Page<GiftMessage> page = new Page<GiftMessage>();
			page.setPageIndex(Integer.parseInt(pageIndex)-1);
			page.setMaxResult(Integer.parseInt(maxResult));
			GiftMessage.findList(builder.toString(), "order by o.createtime desc", params, page);
			List<GiftMessage> list = page.getList();
			if (list!=null&&list.size()>0) {
				totalPage = page.getTotalPage()+"";
				for (GiftMessage giftMessage : list) {
					JSONObject object = new JSONObject();
					object.put("content", giftMessage.getContent());
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
			logger.error("新赠送短信查询发生异常", e);
		}
		
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 旧赠送短信查询
	 * 
	 * @return
	 */
	public String getGiftMessage() {
		JSONObject responseJson = new JSONObject();

		StringBuffer sBuffer = new StringBuffer();
		try {
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();

			builder.append(" o.state=? ");
			params.add("1");
			
			List<GiftMessage> list = GiftMessage.getList(builder.toString(), "order by o.createtime desc", params);
			responseJson.put("error_code", "0000");
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					GiftMessage giftMessage = list.get(i);
					if (i == list.size() - 1) {
						sBuffer.append(giftMessage.getContent());
					} else {
						sBuffer.append(giftMessage.getContent() + "|");
					}
				}
			}
		} catch (Exception e) {
			logger.error("旧赠送短信查询发生异常", e);
		}
		responseJson.put("message", sBuffer.toString());
		return responseJson.toString();
	}

}
