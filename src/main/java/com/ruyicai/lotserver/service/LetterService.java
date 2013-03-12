package com.ruyicai.lotserver.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.MsgCenterService;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 站内信
 * @author Administrator
 *
 */
@Service
public class LetterService {

	private Logger logger = Logger.getLogger(LetterService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private MsgCenterService msgCenterService;
	
	/**
	 * 列表
	 * @param clientInfo
	 * @return
	 */
	public String findLetterList(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		
		String totalPage = "0"; //总页数
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			if (Tools.isEmpty(userNo)) { //返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			String pageIndex = clientInfo.getPageindex(); //当前页数
			if (Tools.isEmpty(pageIndex)) {
				pageIndex = "0";
			}
			String maxresult = clientInfo.getMaxresult(); //每页显示的条数
			if (Tools.isEmpty(maxresult)) {
				maxresult = "10";
			}
			
			StringBuffer paramStr = new StringBuffer();
			JSONObject requestObject = new JSONObject(); //查询条件
			requestObject.put("EQS_toUserno", userNo);
			paramStr.append("condition=" + requestObject.toString());
			paramStr.append("&startLine=" + Integer.parseInt(pageIndex)*Integer.parseInt(maxresult)); //开始行数
			paramStr.append("&endLine=" + maxresult); //取多少条记录
			
			String url = propertiesUtil.getMsgCenterUrl() + "letter/findLetterByPage";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("查询站内信列表返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					JSONObject valueObject = fromObject.getJSONObject("value");
					totalPage = valueObject.getString("totalPage");
					JSONArray list = valueObject.getJSONArray("list");
					if (list!=null&&list.size()>0) {
						for (int i = 0; i < list.size(); i++) {
							JSONObject listObject = list.getJSONObject(i);
							//站内信
							JSONObject letterObject = listObject.getJSONObject("letter");
							String id = letterObject.getString("id"); //站内信编号
							String title = letterObject.getString("title"); //标题
							String content = letterObject.getString("content"); //内容
							String readState = letterObject.getString("hasRead"); //已读状态(0:未读;1:已读)
							
							JSONObject object = new JSONObject();
							object.put("id", id); //站内信编号
							object.put("title", title); //标题
							object.put("content", content); //内容
							object.put("readState", readState); //已读状态
							resultArray.add(object);
						}
						responseJson.put(Constants.error_code, ErrorCode.success.value());
						responseJson.put(Constants.message, "查询成功");
						responseJson.put("result", resultArray);
						responseJson.put("totalPage", totalPage);
						return responseJson.toString();
					}
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.notHaveRecord.value());
			responseJson.put(Constants.message, "无记录");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("查询站内信列表发生异常", e);
		}
		responseJson.put("totalPage", totalPage);
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 更新已读状态
	 * @param clientInfo
	 * @return
	 */
	public String updateReadState(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String id = clientInfo.getId(); //站内信编号
			if (Tools.isEmpty(id)) { //返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("id=" + id);
			
			String url = propertiesUtil.getMsgCenterUrl() + "letter/update2Read";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			//logger.info("站内信更新已读状态返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "更新成功");
					return responseJson.toString();
				}
			}
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "更新失败");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("站内信更新已读状态发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 查询未读数量
	 * @param clientInfo
	 * @return
	 */
	public String findNotReadCount(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		String notReadFeedbackCount = "0"; //留言未读数量
		String notReadLetterCount = "0"; //站内信未读数量
		try {
			String userNo = clientInfo.getUserno(); //用户编号
			//查询留言的为未读数量
			String result1 = msgCenterService.getNotReadFeedbackCount(userNo);
			if (!Tools.isEmpty(result1)) {
				JSONObject fromObject = JSONObject.fromObject(result1);
				if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
						&&fromObject.getString("errorCode").equals("0")) {
					String valueString = fromObject.getString("value");
					if (!Tools.isEmpty(valueString)) {
						notReadFeedbackCount = valueString;
					}
				}
			}
			//查询站内信的未读数量
			String result2 = msgCenterService.getNotReadLetterCount(userNo);
			if (!Tools.isEmpty(result2)) {
				JSONObject fromObject = JSONObject.fromObject(result2);
				if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
						&&fromObject.getString("errorCode").equals("0")) {
					String valueString = fromObject.getString("value");
					if (!Tools.isEmpty(valueString)) {
						notReadLetterCount = valueString;
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询站内信和留言未读数量发生异常", e);
		}
		responseJson.put("notReadFeedbackCount", notReadFeedbackCount);
		responseJson.put("notReadLetterCount", notReadLetterCount);
		return responseJson.toString();
	}
	
}
