package com.ruyicai.lotserver.service.back;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.util.common.DateParseFormatUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * lottery后台的接口
 * @author Administrator
 *
 */
@Service
public class LotteryService {

	private static Logger logger = Logger.getLogger(LotteryService.class);

	@Autowired
	private PropertiesUtil propertiesUtil;
	
	/**
	 * 通过用户名进行注册
	 * 
	 * @param userName
	 * @param password
	 * @param channel
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String register(String phoneNum, String password, String certId, String name, 
			String channel, String imei) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userName=").append(phoneNum);
		paramStr.append("&password=").append(URLEncoder.encode(password, "UTF-8")); // 编码防止密码里有+等字符
		if (!Tools.isEmpty(certId)) { // 是否绑定身份证号
			paramStr.append("&certid=").append(certId);
		}
		if (!Tools.isEmpty(name)) { // 是否填写真实姓名
			paramStr.append("&name=").append(URLEncoder.encode(name, "UTF-8"));
		}
		if (!Tools.isEmpty(channel)) { //渠道号
			paramStr.append("&channel=").append(channel);
		}
		paramStr.append("&imei=").append(imei); //手机标识
		paramStr.append("&subChannel=").append(Constants.subChannel); //用户系统

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/register";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("注册返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 联合登录用户注册
	 * @param phoneNum
	 * @param password
	 * @param channel
	 * @param imei
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String registerUnionLoginUser(String phoneNum, String password, String source, String openId,
			String channel, String imei) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userName=").append(phoneNum);
		paramStr.append("&password=").append(URLEncoder.encode(password, "UTF-8")); // 编码防止密码里有+等字符
		if (!Tools.isEmpty(channel)) { //渠道号
			paramStr.append("&channel=").append(channel);
		}
		paramStr.append("&imei=").append(imei); //手机标识
		paramStr.append("&subChannel=").append(Constants.subChannel); //用户系统
		
		paramStr.append("&outuserno=").append(openId);
		paramStr.append("&type=").append(source);
		
		String url = propertiesUtil.getLotteryUrl() + "tbiguserinfoes/registerBigUser";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("联合登录用户注册返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 根据手机查询用户是否存在
	 * 
	 * @param mobileid
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	public String queryUsersByMobileid(String mobileid, String subChannel) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("mobileid=" + URLEncoder.encode(mobileid, "UTF-8"));
		if (!Tools.isEmpty(subChannel)) {
			paramStr.append("&subChannel=" + subChannel);
		}

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes?json&find=ByMobileid";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("根据手机查询用户是否存在返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 根据用户名查询用户
	 * 
	 * @param mobileid
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String queryUsersByUserName(String userName) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userName=" + URLEncoder.encode(userName, "UTF-8"));

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes?json&find=ByUserName";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("根据用户名查询用户返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 根据用户编号查询用户是否存在
	 * 
	 * @param mobileid
	 * @return
	 * @throws IOException
	 */
	public String queryUsersByUserNo(String userNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes?json&find=ByUserno";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("根据用户编号查询用户是否存在返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 查询用户余额
	 * 
	 * @param userNo
	 * @return
	 */
	public String queryUserBalance(String userNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);

		String url = propertiesUtil.getLotteryUrl() + "select/getAccount";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("查询余额返回:" + result + ",paramStr:" + paramStr.toString());
		return result;
	}

	/**
	 * 查询DNA绑定
	 * 
	 * @param userNo
	 * @return
	 */
	public String queryDNABind(String userNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);

		String url = propertiesUtil.getLotteryUrl() + "taccounts/getDNABinding";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("查询DNA绑定返回:" + result + ",paramStr:" + paramStr.toString());
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param mobileId
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String resetPassword(String userno, String password) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userno);
		paramStr.append("&password=" + URLEncoder.encode(password, "UTF-8"));

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/resetPassword";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("修改密码的返回:" + result + ",paramStr:" + paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject != null && fromObject.has("errorCode")) {
				return fromObject.getString("errorCode");
			}
		}
		return "";
	}

	/**
	 * 修改用户信息
	 * 
	 * @param mobileId
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String modifyUserinfo(String userNo, String certId, String name) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);
		paramStr.append("&certid=" + certId);
		if (!Tools.isEmpty(name)) {
			paramStr.append("&name=" + URLEncoder.encode(name, "UTF-8"));
		}

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/modify";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("修改用户信息的返回:"+result+",paramStr:"	+paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject != null && fromObject.has("errorCode")) {
				return fromObject.getString("errorCode");
			}
		}
		return "";
	}

	/**
	 * 修改昵称
	 * 
	 * @param userno
	 * @param certId
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String updateNickName(String userNo, String nickName) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);
		paramStr.append("&nickname=" + URLEncoder.encode(nickName, "UTF-8"));

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/modify";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("修改昵称的返回:" + result + ",paramStr:" + paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject != null && fromObject.has("errorCode")) {
				return fromObject.getString("errorCode");
			}
		}
		return "";
	}

	/**
	 * 获得batchcode期号以后的num期
	 * 
	 * @param lotno
	 * @param batchcode
	 * @param num
	 * @return
	 */
	public String getAfterIssue(String lotno, String batchcode, String num) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("lotno=" + lotno);
		paramStr.append("&batchcode=" + batchcode);
		paramStr.append("&num=" + num);

		String url = propertiesUtil.getLotteryUrl() + "select/getAfterIssue";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("getAfterIssue的返回:"+result+",paramStr:"+paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
					&&fromObject.getString("errorCode").equals("0")) {
				return fromObject.getString("value");
			}
		}
		return "";
	}

	/**
	 * 获取某场比赛的信息(供竞彩使用)
	 * 
	 * @param lotno
	 * @param day
	 * @param weekid
	 * @param teamid
	 * @return
	 */
	public String getJingcaimatches(String lotNo, String day, String weekId, String teamId) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("lotno=" + lotNo);
		paramStr.append("&day=" + day);
		paramStr.append("&weekid=" + weekId);
		paramStr.append("&teamid=" + teamId);

		String url = propertiesUtil.getLotteryUrl() + "select/getjingcaimatches";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("获取竞彩某场比赛的信息的返回:" + result + ",paramStr:" + paramStr.toString());
		return result;
	}

	/**
	 * 获取竞彩赔率
	 * 
	 * @param clientInfo
	 * @return
	 */
	public String queryJingCaiPeiLv(String type, String valueType) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("type=" + type);
		paramStr.append("&valueType=" + valueType);

		String url = propertiesUtil.getLotteryUrl() + "select/findjincaipeilu";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("获取竞彩赔率返回:" + result + ",paramStr:" + paramStr.toString());
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
					&&fromObject.getString("errorCode").equals("0")) {
				return fromObject.getString("value");
			}
		}
		return "";
	}

	/**
	 * 获取足彩预售期期号
	 * 
	 * @param lotNo
	 * @return
	 */
	public String getZCIssue(String lotNo) {
		String url = propertiesUtil.getLotteryUrl() + "select/getZCIssue?lotno=" + lotNo;
		String result = HttpUtil.sendRequestByGet(url, true);
		logger.info("获取足彩预售期期号返回:" + result + ",url:" + url);
		return result;
	}

	/**
	 * 获取开奖公告
	 * 
	 * @param lotNo
	 * @param issueNum
	 * @return
	 */
	public String getWinfolist(String lotNo, Integer issueNum) {
		String url = propertiesUtil.getLotteryUrl()+"select/getWinfolist"+"?lotno="+lotNo
						+ "&issuenum="+issueNum;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("获取开奖公告返回:"+result+",url:"+url);
		return result;
	}
	
	/**
	 * 设置自动登录
	 * 
	 * @param userNo
	 * @param accessType
	 * @return
	 */
	public String addTuserloginfo(String userNo, String accessType) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);
		paramStr.append("&accesstype=" + accessType);

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/addTuserloginfo";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("设置自动登录返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 判断是否自动登录
	 * 
	 * @param random
	 * @return
	 */
	public String getTuserloginfo(String random) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("random=" + random);

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/getTuserloginfo";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("判断是否自动登录返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 取消自动登录
	 * 
	 * @param userno
	 * @return
	 */
	public String deleteTuserloginfo(String userno) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userno);

		String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/deleteTuserloginfo";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("取消自动登录返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}

	/**
	 * 获取当前期号
	 * 
	 * @param lotNo
	 * @return
	 */
	public String getIssue(String lotNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("lotno=" + lotNo);

		String url = propertiesUtil.getLotteryUrl() + "select/getIssue";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("getIssue当前期号返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 根据orderId获取Tlots
	 * @param orderId
	 * @return
	 */
	public String getToltsByOrderId(String orderId) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("orderid=" + orderId);
		paramStr.append("&beginTime=" + "20100808");
		paramStr.append("&endTime=" + DateParseFormatUtil.getTodayDate());
		paramStr.append("&startLine=" + "0");
		paramStr.append("&endLine=" + "10000");

		String url = propertiesUtil.getLotteryUrl() + "select/getTlot";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("根据orderId获取Tlots返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 根据orderId获取Tlots(拆票)
	 * 通过订单ID查询该订单的票，如果数据库不存在Tlot,则模拟拆票(合买竞彩注码解析使用)
	 * @param orderId
	 * @return
	 */
	public String getTlotsByOrderidWrapper(String orderId) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("orderid=" + orderId);

		String url = propertiesUtil.getLotteryUrl() + "select/getTlotsByOrderidWrapper";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("根据orderId获取Tlots(拆票)返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 追号详情查询
	 * @param flowno
	 * @return
	 */
	public String getTorderByScribeno(String flowno) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("flowno=" + flowno);
		paramStr.append("&startLine="+"0");
		paramStr.append("&endLine="+"153");

		String url = propertiesUtil.getLotteryUrl() + "select/getTorderByScribeno?";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("追号详情查询返回:" + result + ",paramStr:" + paramStr);
		return result;
	}
	
	/**
	 * 根据彩种和期号查询试机号
	 * @param lotNo
	 * @param batchcode
	 * @return
	 */
	public String getTryCodeByLotnoAndBatchcode(String lotNo, String batchcode) {
		String url = propertiesUtil.getLotteryUrl()+"select/getTryCode?lotno="+lotNo+"&batchcode="+batchcode;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("根据彩种和期号查询试机号返回:"+result);
		return result;
	}
	
	/**
	 * 根据彩种获取最新的试机号
	 * @param lotNo
	 * @return
	 */
	public String getLatestTryCodeByLotNo(String lotNo) {
		String url = propertiesUtil.getLotteryUrl()+"select/getLatestTryCode?lotno="+lotNo;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("根据彩种获取最新的试机号返回:"+result);
		return result;
	}
	
	/**
	 * 合买详情查询
	 * @param caselotId
	 * @param userNo
	 * @return
	 */
	public String selectCaseLotDetail(String caselotId, String userNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("caselotid="+caselotId); //方案编号
		if (!Tools.isEmpty(userNo)) {
			paramStr.append("&userno="+userNo); //用户编号
		}
		
		String url = propertiesUtil.getLotteryUrl() + "select/selectCaseLotDetail";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("合买详情查询返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 获取开奖公告明细
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public String getWinInfoDetailByLotNoAndBatchCode(String lotNo, String batchCode) {
		String url = propertiesUtil.getLotteryUrl()+"select/getTwininfo"+"?lotno="+lotNo+"&issue="+batchCode;
		String result = HttpUtil.sendRequestByGet(url, true);
		logger.info("获取开奖公告明细返回:"+result+",lotNo:"+lotNo+",batchCode:"+batchCode);
		return result;
	}
	
	/**
	 * 查询用户是否有代理充值的权限
	 * @param userNo
	 * @return
	 */
	public String getAgencyChargeRight(String userNo) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno="+userNo);
		
		String url = propertiesUtil.getLotteryUrl() + "taccounts/getTtransfer";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		//logger.info("查询用户是否有代理充值的权限返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 查询联合登录用户
	 * @param type
	 * @param token
	 * @return
	 */
	public String getUnionLoginUser(String type, String openId) {
		String url = propertiesUtil.getLotteryUrl()+"tbiguserinfoes?json&find=BigUser&type="+type+"&outuserno="+openId;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询联合登录用户返回:" + result);
		return result;
	}
	
	/**
	 * 查询合买战绩
	 * @param paramStr
	 * @return
	 */
	public String getTuserachievements(StringBuffer paramStr) {
		String url = propertiesUtil.getLotteryUrl() + "select/selectTuserachievements?";
		String result = HttpUtil.sendRequestByGet(url+paramStr.toString(), true);
		//logger.info("查询合买战绩返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
	/**
	 * 根据彩种和期号获取期信息
	 * @param lotNo
	 * @param batchcode
	 * @return
	 */
	public String getBatchCodeInfo(String lotNo, String batchCode) {
		String url = propertiesUtil.getLotteryUrl()+"select/getTlotctrl?lotno="+lotNo+"&batchcode="+batchCode;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("根据彩种和期号获取期信息返回:"+result);
		return result;
	}
	
	/**
	 * 根据订单号查询订单
	 * @param orderId
	 * @return
	 */
	public String getTorderByOrderId(String orderId) {
		String url = propertiesUtil.getLotteryUrl()+"select/getTorder?orderid="+orderId;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("根据订单号查询订单返回:"+result);
		return result;
	}
	
}
