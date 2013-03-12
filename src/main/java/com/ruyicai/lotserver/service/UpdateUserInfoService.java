package com.ruyicai.lotserver.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.domain.SecurityCode;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.back.MsgCenterService;
import com.ruyicai.lotserver.service.back.ScoreCenterService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.ChannelUtil;
import com.ruyicai.lotserver.util.VerifyUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 用户信息的Service
 * @author Administrator
 *
 */
@Service
public class UpdateUserInfoService {
	
    private Logger logger = Logger.getLogger(UpdateUserInfoService.class);
    
    @Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryService;
	
	@Autowired
	private MsgCenterService msgCenterService;
	
	@Autowired
	private ScoreCenterService scoreCenterService;
    
    /**
     * 绑定手机号码
     * @param clientInfo
     * @return
     */
	public String bindPhone(ClientInfo clientInfo){
    	JSONObject responseJson = new JSONObject();
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		
    		String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
    		
    		String bindPhoneNum = clientInfo.getBindPhoneNum(); //要绑定的手机
    		String randomId = clientInfo.getSecurityCode(); //验证码
    		if (Tools.isEmpty(bindPhoneNum)||Tools.isEmpty(randomId)) { //手机号或验证码为空
    			return Tools.paramError(clientInfo.getImei());
    		}
    		
    		List<SecurityCode> list = getSecurityCodeList(bindPhoneNum, "bindPhone", sdf.format(new Date()));
    		if (list!=null && list.size()>0) {
    			SecurityCode securityCode = list.get(0);
    			String code = securityCode.getSecuritycode(); //数据库中的验证码
    			if (!Tools.isEmpty(randomId)&&!Tools.isEmpty(code)&&randomId.equals(code)) { //验证码正确
    				
            		StringBuffer paramStr = new StringBuffer();
        			paramStr.append("userno="+userNo);
        			paramStr.append("&mobileid="+bindPhoneNum);
        			
        			String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/modify";
        			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
        			logger.info("绑定手机号码的返回:"+result+",paramStr:"+paramStr.toString());
        			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
        				return Tools.paramError(clientInfo.getImei());
        			} 
        			JSONObject fromObject = JSONObject.fromObject(result);
        			if (fromObject!=null) {
        				String errorCode = fromObject.getString("errorCode");
        				if (errorCode!=null&&errorCode.equals("0")) {
        					responseJson.put(Constants.error_code, "0000");
        					responseJson.put(Constants.message, "绑定成功");
        					
        					commonService.perfectUserInfoAddScoreJMS(userNo); //判断用户信息是否完整,如果完整则送积分
        				} else {
        					responseJson.put(Constants.error_code, "9999");
        					responseJson.put(Constants.message, "绑定失败");
        				}
        			} else {
        				responseJson.put(Constants.error_code, "9999");
    					responseJson.put(Constants.message, "绑定失败");
        			}
        		} else {
        			responseJson.put(Constants.error_code, "9999");
        			responseJson.put(Constants.message, "验证码错误");
        		}
    		} else {
    			responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "绑定失败");
    		}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("绑定手机号码发生异常", e);
		} 
		return responseJson.toString(); 
    }
    
    /**
     * 解除绑定手机号码
     * @param clientInfo
     * @return
     */
    public String removeBindPhone(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
    		String userNo = commonService.getNewUserNo(clientInfo);
    		if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		} 
    		
    		StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno="+userNo);
			paramStr.append("&mobileid="+" ");
			
			String url = propertiesUtil.getLotteryUrl() + "tuserinfoes/modify";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("解除绑定手机号码的返回:"+result+",paramStr:"+paramStr.toString());
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "解绑成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "解绑失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "解绑失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("解除绑定手机号码发生异常", e);
		} 
		return responseJson.toString(); 
    }
    
    /**
     * 找回密码
     * @param clientInfo
     * @return
     */
	public String retrievePassword(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		//判断是否超过次数限制
    		List<SecurityCode> list = getSecurityCodeList(clientInfo.getBindPhoneNum(), "retrieve_password", sdf.format(new Date()));
    		if (list!=null && list.size()>0) {
    			SecurityCode securityCode = list.get(0);
    			if (securityCode.getCount()>=3) {
    				responseJson.put(Constants.error_code, "9999");
    				responseJson.put(Constants.message, "超过次数限制");
        			return responseJson.toString(); 
    			}
    		}
    		String result = lotteryService.queryUsersByUserName(clientInfo.getPhonenum());
    		if (Tools.isEmpty(result)) // 如果返回空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		JSONObject fromObject = JSONObject.fromObject(result);
    		if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
    				&&fromObject.getString("errorCode").equals("0")) {
    			JSONObject valueJsonObject = fromObject.getJSONObject("value");
    			String userno = valueJsonObject.getString("userno");
    			String mobileId = valueJsonObject.getString("mobileid");
    			if (Tools.isEmpty(mobileId)||mobileId.trim().equals("null")) { //绑定的手机号码为空
    				responseJson.put(Constants.error_code, "9999");
    				responseJson.put(Constants.message, "绑定号码为空");
					return responseJson.toString();
    			}
    			if (mobileId.equals(clientInfo.getBindPhoneNum())) { //绑定的手机号码正确
    	    		String randomNum = Tools.generateRandomNum(6); //随机数
    				String resetPwdResult = lotteryService.resetPassword(userno, randomNum);
					if (resetPwdResult.equals("0")) { //密码重置成功
						//更新Vol_SecurityCode表
						createOrUpdateSecurityCode(list, clientInfo.getBindPhoneNum(), "retrieve_password", "");
						logger.info("找回密码,Phonenum="+clientInfo.getPhonenum()+",新密码:"+randomNum+",BindPhoneNum:"+clientInfo.getBindPhoneNum());
						String sendMessage = ""; //短信发送返回结果
						String coopId = clientInfo.getCoopId(); //渠道号
						if (ChannelUtil.is91Channel(coopId)) { //91彩票
							String content = "尊敬的91彩票用户，您的91彩票账户新密码为："+randomNum+"，请您登录后及时修改密码。";
							sendMessage = msgCenterService.sendMessage(clientInfo.getBindPhoneNum(), content, "menWangOldSMSServiceProvider"); //发送短信
						} else { //如意彩
							String content = "尊敬的如意彩用户，您的如意彩账户新密码为："+randomNum+"，请您登录后及时修改密码。";
							sendMessage = msgCenterService.sendMessage(clientInfo.getBindPhoneNum(), content, ""); //发送短信
						}
						if (sendMessage.equals("0")) {
							responseJson.put(Constants.error_code, "0000");
							responseJson.put(Constants.message, "找回成功");
							return responseJson.toString();
						}
					}
    			}
    		}
		} catch (Exception e) {
			logger.error("找回密码发生异常", e);
		} 
		responseJson.put(Constants.error_code, "9999");
		responseJson.put(Constants.message, "找回失败");
		return responseJson.toString(); 
    }
    
    /**
     * 绑定身份证号
     * @param clientInfo
     * @return
     */
    public String bindCertId(ClientInfo clientInfo){
    	JSONObject responseJson = new JSONObject();
    	try {
    		String userNo = commonService.getNewUserNo(clientInfo);
    		if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		} 
    		String certId = clientInfo.getCertid();// 身份证号
    		if (Tools.isEmpty(certId)||!VerifyUtil.isCertId(certId)) { //身份证验证格式是否正确
    			responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "身份证格式错误");
				return responseJson.toString();
			}
    		String name = clientInfo.getName().trim();// 姓名
    		if (!Tools.isEmpty(name)&&name.length()>16) { //姓名如果不为空验证长度
    			responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "姓名超过长度限制");
				return responseJson.toString();
    		}
    		String errorCode = lotteryService.modifyUserinfo(userNo, certId.trim(), name);
			if (errorCode!=null&&errorCode.equals("0")) {
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "绑定成功");
				
				commonService.perfectUserInfoAddScoreJMS(userNo); //判断用户信息是否完整,如果完整则送积分
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "绑定失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("绑定身份证号发生异常", e);
		} 
		return responseJson.toString(); 
    }
    
    /**
     * 修改昵称
     * @param clientInfo
     * @return
     */
    public String updateNickName(ClientInfo clientInfo){
    	JSONObject responseJson = new JSONObject();
    	try {
    		String userNo = commonService.getNewUserNo(clientInfo); //用户编号
    		if (Tools.isEmpty(userNo)) { //如果userNo为空,参数错误
    			return Tools.paramError(clientInfo.getImei());
    		}
    		String nickName = clientInfo.getNickName(); //昵称
    		if (!VerifyUtil.verifyNickName(nickName)) { //验证昵称的合法性
    			responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "昵称长度应为4-16个字符");
				return responseJson.toString();
    		}
    		String errorCode = lotteryService.updateNickName(userNo, clientInfo.getNickName());
			if (errorCode!=null&&errorCode.equals("0")) {
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "修改成功");
			} else if (errorCode!=null&&errorCode.equals("100105")) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "昵称重复");
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "修改失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("修改昵称发生异常", e);
		} 
		return responseJson.toString(); 
    }
    
    /**
     * 绑定手机号时发送验证码
     * @param clientInfo
     * @return
     */
	public String sendCodeForBindPhone(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    		String bindPhoneNum = clientInfo.getBindPhoneNum();
    		//判断绑定手机号码的合法性
    		if (!VerifyUtil.isMobile(bindPhoneNum)) {
    			responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "绑定号码不合法");
    			return responseJson.toString(); 
    		}
    		//判断是否超过次数限制
    		List<SecurityCode> list = getSecurityCodeList(bindPhoneNum, "bindPhone", sdf.format(new Date()));
    		if (list!=null && list.size()>0) {
    			SecurityCode securityCode = list.get(0);
    			if (securityCode.getCount()>=3) {
    				responseJson.put(Constants.error_code, "9999");
    				responseJson.put(Constants.message, "超过次数限制");
        			return responseJson.toString(); 
    			}
    		}
    		
    		String randomNum = Tools.generateRandomNum(6); //随机数
			logger.info("绑定手机号时发送验证码,bindPhoneNum="+bindPhoneNum+",randomNum="+randomNum);
			String content = "尊敬的用户，您本次绑定手机所使用的验证码是"+randomNum+"（验证码30分钟后失效）。祝您幸运赢大奖！本条信息免费";
			String sendMessage = ""; //短信发送返回结果
			String coopId = clientInfo.getCoopId(); //渠道号
			if (ChannelUtil.is91Channel(coopId)) { //91彩票
				sendMessage = msgCenterService.sendMessage(bindPhoneNum, content, "menWangOldSMSServiceProvider"); //发送短信
			} else { //如意彩
				sendMessage = msgCenterService.sendMessage(bindPhoneNum, content, ""); //发送短信
			}
			if (sendMessage.equals("0")) {
				createOrUpdateSecurityCode(list, bindPhoneNum, "bindPhone", randomNum);
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "发送成功");
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "发送失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("绑定手机号时发送验证码发生异常", e);
		} 
		return responseJson.toString(); 
    }
    
    /**
     * 插入或更新验证码表
     * @param list
     * @param mobileid
     * @param type
     */
	public void createOrUpdateSecurityCode(List<SecurityCode> list, String mobileid, String type, String code) {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	if (list==null || list.size()==0) {
    		SecurityCode securityCode = new SecurityCode();
    		securityCode.setCount(1);
    		securityCode.setMobileid(mobileid);
			securityCode.setType(type);
			securityCode.setSecuritycode(code);
			securityCode.setCreatetime(sdf.format(new Date()));
			securityCode.persist();
    	} else {
    		SecurityCode securityCode = list.get(0);
    		Integer count = securityCode.getCount();
    		securityCode.setCount(count+1);
    		securityCode.setSecuritycode(code);
    		securityCode.merge();
    	}
    }
    
    /**
     * 用户中心信息查询
     * @param clientInfo
     * @return
     */
    public String userCenter(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	String nickName = ""; //昵称
    	String mobileId = ""; //手机号码
    	String userName = ""; //用户名
    	String certId = ""; //身份证号
    	BigDecimal bet_balance = BigDecimal.ZERO; //可投注金额
    	String score = ""; //积分
    	String agencyChargeRight = "0"; //是否有代理充值的权限(0:无;1:有)
    	String notReadLetterCount = "0"; //未读站内信的数量
    	
    	String userNo = clientInfo.getUserno(); //用户编号
    	 //根据用户编号查询用户信息
    	String result1 = lotteryService.queryUsersByUserNo(userNo);
    	if (!Tools.isEmpty(result1)) {
			JSONObject fromObject1 = JSONObject.fromObject(result1);
			if (fromObject1!=null&&!Tools.isEmpty(fromObject1.getString("errorCode"))
					&&fromObject1.getString("errorCode").equals("0")) {
				JSONObject valueObject = fromObject1.getJSONObject("value");
				if (valueObject!=null) {
					mobileId = valueObject.getString("mobileid");
					if (Tools.isEmpty(mobileId)||mobileId.trim().equals("null")) {
						mobileId = "";
					}
					nickName = valueObject.getString("nickname");
					if (Tools.isEmpty(nickName)||nickName.trim().equals("null")) {
						nickName = "";
					}
					certId = valueObject.getString("certid");
					if (Tools.isEmpty(certId)||certId.trim().equals("null")
							||certId.trim().equals("111111111111111111")) { //如果身份证为18个1，则未绑定
						certId = "";
					}
					userName = valueObject.getString("userName");
					if (Tools.isEmpty(userName)||userName.trim().equals("null")) {
						userName = "";
					}
				}
			}
		}
    	//查询余额
    	String result2 = lotteryService.queryUserBalance(userNo); 
    	if (!Tools.isEmpty(result2)) {
    		JSONObject fromObject2 = JSONObject.fromObject(result2);
    		if(fromObject2!=null&&!Tools.isEmpty(fromObject2.getString("errorCode"))
    				&&fromObject2.getString("errorCode").equals("0")){
    			JSONObject valueObject = fromObject2.getJSONObject("value");
    			if (valueObject!=null) {
    				String balance = valueObject.getString("balance"); //总余额
        			String freezebalance = valueObject.getString("freezebalance"); //冻结金额
        			bet_balance = new BigDecimal(balance).subtract(new BigDecimal(freezebalance)); //投注金额=账户余额减去冻结金额
    			}
    		}
    	}
    	//用户积分
    	String result3 = scoreCenterService.findScoreByUserno(userNo);
    	if (!Tools.isEmpty(result3)) {
    		JSONObject fromObject3 = JSONObject.fromObject(result3);
    		if (fromObject3!=null&&!Tools.isEmpty(fromObject3.getString("errorCode"))
    				&&fromObject3.getString("errorCode").equals("0")) {
    			JSONObject valueObject = fromObject3.getJSONObject("value");
    			if (valueObject!=null) {
    				score = valueObject.getString("score");
    				if (Tools.isEmpty(score)||score.trim().equals("null")) {
    					score = "";
					}
    			}
    		}
    	}
    	
    	//查询用户是否有代理充值的权限
    	String result4 = lotteryService.getAgencyChargeRight(userNo);
    	if (!Tools.isEmpty(result4)) {
    		JSONObject fromObject4 = JSONObject.fromObject(result4);
    		if (fromObject4!=null&&!Tools.isEmpty(fromObject4.getString("errorCode"))
    				&&fromObject4.getString("errorCode").equals("0")) {
    			JSONObject valueObject = fromObject4.getJSONObject("value");
    			if (valueObject!=null) {
    				String state = valueObject.getString("state");
    				if (state!=null&&state.equals("1")) { //有权限
    					agencyChargeRight = "1";
    				}
    			}
    		}
    	}
    	
    	//查询未读站内信的数量
    	String result5 = msgCenterService.getNotReadLetterCount(userNo);
    	if (!Tools.isEmpty(result5)) {
    		JSONObject fromObject5 = JSONObject.fromObject(result5);
    		if (fromObject5!=null&&!Tools.isEmpty(fromObject5.getString("errorCode"))
    				&&fromObject5.getString("errorCode").equals("0")) {
    			String valueString = fromObject5.getString("value");
    			if (!Tools.isEmpty(valueString)) {
    				notReadLetterCount = valueString;
    			}
    		}
    	}
    	
    	responseJson.put("nickName", nickName); //昵称
    	responseJson.put("mobileId", mobileId); //手机号
    	responseJson.put(Constants.userName, userName); //用户名
    	responseJson.put("certId", certId); //身份证
    	responseJson.put(Constants.betBalance, (bet_balance.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP))+"元");  //可投注金额
    	responseJson.put("score", score); //积分
    	responseJson.put("agencyChargeRight", agencyChargeRight); //代理充值的权限
    	responseJson.put("notReadLetterCount", notReadLetterCount); //未读站内信的数量
    	
    	return responseJson.toString();
    }
    
    /**
     * 取消自动登录
     * @param clientInfo
     * @return
     */
    public String cancelAutoLogin(ClientInfo clientInfo) {
    	JSONObject responseJson = new JSONObject();
    	try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			String result = lotteryService.deleteTuserloginfo(userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "取消成功");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "取消失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "取消失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("取消自动登录发生异常", e);
		}
    	return responseJson.toString();
    }
    
    /**
     * 取得验证码列表
     * @param mobileId
     * @param type
     * @param createtime
     * @return
     */
    public List<SecurityCode> getSecurityCodeList(String mobileId, String type, String createtime) {
    	StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.mobileid=? and");
		params.add(mobileId);
		
		builder.append(" o.type=? and");
		params.add(type);
		
		builder.append(" o.createtime=?");
		params.add(createtime);
		List<SecurityCode> list = SecurityCode.getList(builder.toString(), "", params);
		return list;
    }
    
}
