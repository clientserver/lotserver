package com.ruyicai.lotserver.util.parse;

import javax.servlet.http.HttpServletRequest;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.protocol.ClientInfo;
import net.sf.json.JSONObject;

/**
 * 解析请求参数
 * @author Administrator
 *
 */
public class RequestParamUtil {

	/**
	 * 解析参数
	 * @param request
	 * @param requestObject
	 * @param clientInfo
	 */
	public static void parseRequestParam(HttpServletRequest request, JSONObject requestObject, ClientInfo clientInfo) {
		//基本参数
		clientInfo.setCommand(requestObject.getString(Constants.command)); //请求命令
		clientInfo.setSysSessionid(request.getSession().getId()); //SessionId
		
		if (requestObject.has(Constants.imei)) { //手机标识
			clientInfo.setImei(requestObject.getString(Constants.imei));
		}
		if (requestObject.has(Constants.imsi)) { //SIM卡标识
			clientInfo.setImsi(requestObject.getString(Constants.imsi));
		}
		if (requestObject.has(Constants.softwareVersion)) {
			clientInfo.setSoftwareVersion(requestObject.getString(Constants.softwareVersion)); //版本号
		}
		if (requestObject.has(Constants.machineId)) {
			clientInfo.setMachineId(requestObject.getString(Constants.machineId)); //机型
		}
		if (requestObject.has(Constants.coopId)) {
			clientInfo.setCoopId(requestObject.getString(Constants.coopId)); //渠道号
			//为了配合力美的推广,等iPhone3.5版本上线后即可删除
			/*String coopId = requestObject.getString(Constants.coopId);
			if (coopId!=null&&coopId.equals(CoopIdConstants.appStore_oldCoopId)) {
				coopId = CoopIdConstants.appStore_newCoopId;
			}
			clientInfo.setCoopId(coopId);*/
		}
		if (requestObject.has(Constants.platform)) {
			clientInfo.setPlatform(requestObject.getString(Constants.platform)); //平台
		}
		if (requestObject.has(Constants.isCompress)) { //是否压缩
			clientInfo.setIsCompress(requestObject.getString(Constants.isCompress));
		}
		if (requestObject.has(Constants.isemulator)) { //是否是模拟器联网
			clientInfo.setIsemulator(requestObject.getString(Constants.isemulator));
		}
		if (requestObject.has(Constants.phoneSIM)) { //SIM卡手机号
			clientInfo.setPhoneSIM(requestObject.getString(Constants.phoneSIM));
		}
		if (requestObject.has(Constants.mac)) { //网卡地址
			clientInfo.setMac(requestObject.getString(Constants.mac));
		}
		
		//类型
		if (requestObject.has(Constants.requestType)) { //请求类型
			clientInfo.setRequestType(requestObject.getString(Constants.requestType));
		}
		if (requestObject.has(Constants.type)) { //查询类型
			clientInfo.setType(requestObject.getString(Constants.type));
		}
		if (requestObject.has(Constants.transactionType)) { //交易类型
			clientInfo.setTransactionType(requestObject.getString(Constants.transactionType));
		}
		if (requestObject.has(Constants.rechargeType)) { //充值类型
			clientInfo.setRechargetype(requestObject.getString(Constants.rechargeType));
		}
		if (requestObject.has(Constants.cashType)) { //提现类型
			clientInfo.setCashtype(requestObject.getString(Constants.cashType));
		}
		
		//投注、追号、赠送、合买的公告参数
		if (requestObject.has(Constants.lotNo)) { //彩种编号
			clientInfo.setLotNo(requestObject.getString(Constants.lotNo));
		}
		if (requestObject.has(Constants.betCode)) { //注码
			clientInfo.setBetCode(requestObject.getString(Constants.betCode));
		}
		if (requestObject.has(Constants.batchCode)) { //期号
			clientInfo.setBatchCode(requestObject.getString(Constants.batchCode));
		}
		if (requestObject.has(Constants.lotMulti)) { //倍数
			clientInfo.setLotmulti(requestObject.getString(Constants.lotMulti));
		}
		if (requestObject.has(Constants.amount)) { //总金额
			clientInfo.setAmount(requestObject.getString(Constants.amount));
		}
		if (requestObject.has(Constants.batchNum)) { //追号期数
			clientInfo.setBatchnum(requestObject.getString(Constants.batchNum));
		}
		if (requestObject.has(Constants.sellway)) { //玩法
			clientInfo.setSellway(requestObject.getString(Constants.sellway));
		}
		if (requestObject.has(Constants.betType)) { //投注类型
			clientInfo.setBetType(requestObject.getString(Constants.betType));
		}
		if (requestObject.has(Constants.isSellWays)) { //投注时是否多玩法
			clientInfo.setIsSellWays(requestObject.getString(Constants.isSellWays));
		}
		if (requestObject.has(Constants.isBetAfterIssue)) { //是否可以投当前期以后的期(追号可以不从当前期追,1:是)
			clientInfo.setIsBetAfterIssue(requestObject.getString(Constants.isBetAfterIssue));
		}
		if (requestObject.has(Constants.isSuperaddition)) { //是否追加
			clientInfo.setIsSuperaddition(requestObject.getString(Constants.isSuperaddition));
		}
		
		//追号
		if (requestObject.has(Constants.prizeEnd)) { //中奖后停止追号
			clientInfo.setPrizeend(requestObject.getString(Constants.prizeEnd));
		}
		if (requestObject.has(Constants.betNum)) { //注数
			clientInfo.setBetNum(requestObject.getString(Constants.betNum));
		}
		if (requestObject.has(Constants.wholeYield)) { //全程收益率
			clientInfo.setWholeYield(requestObject.getString(Constants.wholeYield));
		}
		if (requestObject.has(Constants.beforeBatchNum)) { //前几期
			clientInfo.setBeforeBatchNum(requestObject.getString(Constants.beforeBatchNum));
		}
		if (requestObject.has(Constants.beforeYield)) { //前程收益率
			clientInfo.setBeforeYield(requestObject.getString(Constants.beforeYield));
		}
		if (requestObject.has(Constants.afterYield)) { //后程收益率
			clientInfo.setAfterYield(requestObject.getString(Constants.afterYield));
		}
		if (requestObject.has(Constants.subscribeInfo)) { //追号信息(用于每期追号的倍数不同)格式:期号,金额,倍数,收益率!期号,金额,倍数,收益率
			clientInfo.setSubscribeInfo(requestObject.getString(Constants.subscribeInfo));
		}
		
		//赠送
		if (requestObject.has(Constants.to_mobile_code)) { //赠送人的手机号码
			clientInfo.setTo_mobile_code(requestObject.getString(Constants.to_mobile_code));
		}
		if (requestObject.has(Constants.blessing)) { //赠送寄语
			clientInfo.setBlessing(requestObject.getString(Constants.blessing));
		}
		
		//查询
		if (requestObject.has(Constants.cashDetailId)) { //提现ID
			clientInfo.setCashdetailid(requestObject.getString(Constants.cashDetailId));
		}
		if (requestObject.has(Constants.tsubscribeNo)) { //追号订单
			clientInfo.setTsubscribeNo(requestObject.getString(Constants.tsubscribeNo));
		}
		if (requestObject.has(Constants.startDate)) { //开始时间
			clientInfo.setStartDate(requestObject.getString(Constants.startDate));
		}
		if (requestObject.has(Constants.endDate)) { //结束时间
			clientInfo.setEndDate(requestObject.getString(Constants.endDate));
		}
		if (requestObject.has(Constants.maxResult)) { //每页显示多少条
			clientInfo.setMaxresult(requestObject.getString(Constants.maxResult));
		}
		if (requestObject.has(Constants.startLine)) { //起始行
			clientInfo.setStartLine(requestObject.getString(Constants.startLine));
		}
		if (requestObject.has(Constants.stopLine)) { //结束行
			clientInfo.setStopLine(requestObject.getString(Constants.stopLine));
		}
		if (requestObject.has(Constants.pageIndex)) { //第几页
			clientInfo.setPageindex(requestObject.getString(Constants.pageIndex));
		}
		if (requestObject.has(Constants.sessionId)) { //sessionID
			clientInfo.setSessionid(requestObject.getString(Constants.sessionId));
		}
		
		//合买
		if (requestObject.has(Constants.caseId)) { //方案编号
			clientInfo.setCaseid(requestObject.getString(Constants.caseId));
		}
		if (requestObject.has(Constants.orderBy)) { //按什么进行排序
			clientInfo.setOrderBy(requestObject.getString(Constants.orderBy));
		}
		if (requestObject.has(Constants.orderDir)) { //排序方式
			clientInfo.setOrderDir(requestObject.getString(Constants.orderDir));
		}
		if (requestObject.has(Constants.safeAmt)) { //保底金额
			clientInfo.setSafeAmt(requestObject.getString(Constants.safeAmt));
		}
		if (requestObject.has(Constants.oneAmount)) { //单注金额
			clientInfo.setOneAmount(requestObject.getString(Constants.oneAmount));
		}
		if (requestObject.has(Constants.buyAmt)) { //购买金额
			clientInfo.setBuyAmt(requestObject.getString(Constants.buyAmt));
		}
		if (requestObject.has(Constants.commisionRation)) { //提成比例
			clientInfo.setCommisionRatio(requestObject.getString(Constants.commisionRation));
		}
		if (requestObject.has(Constants.visibility)) { //方案是否可见
			clientInfo.setVisibility(requestObject.getString(Constants.visibility));
		}
		if (requestObject.has(Constants.minAmt)) { //最低认购金额
			clientInfo.setMinAmt(requestObject.getString(Constants.minAmt));
		}
		if (requestObject.has(Constants.description)) { //描述
			clientInfo.setDescription(requestObject.getString(Constants.description));
		}
		
		//自动跟单
		if (requestObject.has(Constants.starterUserNo)) { //跟单次数
			clientInfo.setStarterUserNo(requestObject.getString(Constants.starterUserNo));
		}
		if (requestObject.has(Constants.times)) { //跟单次数
			clientInfo.setTimes(requestObject.getString(Constants.times));
		}
		if (requestObject.has(Constants.joinAmt)) { //跟单金额
			clientInfo.setJoinAmt(requestObject.getString(Constants.joinAmt));
		}
		if (requestObject.has(Constants.percent)) { //跟单百分比
			clientInfo.setPercent(requestObject.getString(Constants.percent));
		}
		if (requestObject.has(Constants.maxAmt)) { //百分比跟单最大金额
			clientInfo.setMaxAmt(requestObject.getString(Constants.maxAmt));
		}
		if (requestObject.has(Constants.joinType)) { //跟单类型
			clientInfo.setJoinType(requestObject.getString(Constants.joinType));
		}
		if (requestObject.has(Constants.forceJoin)) { //是否强制跟单
			clientInfo.setForceJoin(requestObject.getString(Constants.forceJoin));
		}
		
		//充值、提现
		if (requestObject.has(Constants.cardNo)) { //卡号
			clientInfo.setCardno(requestObject.getString(Constants.cardNo));
		}
		if (requestObject.has(Constants.cardPwd)) { //密码
			clientInfo.setCardpwd(requestObject.getString(Constants.cardPwd));
		}
		if (requestObject.has(Constants.cardType)) { //卡类型
			clientInfo.setCardtype(requestObject.getString(Constants.cardType));
		}
		if (requestObject.has(Constants.bankAddress)) { //DNA 银行地址
			clientInfo.setBankaddress(requestObject.getString(Constants.bankAddress));
		}
		if (requestObject.has(Constants.addressName)) { //DNA 家庭住址
			clientInfo.setAddressname(requestObject.getString(Constants.addressName));
		}
		if (requestObject.has(Constants.phonenum)) { //DNA 手机号码
			clientInfo.setPhonenum(requestObject.getString(Constants.phonenum));
		}
		if (requestObject.has(Constants.certId)) { //DNA 身份证号
			clientInfo.setCertid(requestObject.getString(Constants.certId));
		}
		if (requestObject.has(Constants.iswhite)) { //DNA 是否是白名单
			clientInfo.setIswhite(requestObject.getString(Constants.iswhite));
		}
		if (requestObject.has(Constants.bankName)) { //银行名称
			clientInfo.setBankname(requestObject.getString(Constants.bankName));
		}
		if (requestObject.has(Constants.bankAccount)) {
			clientInfo.setBankAccount(requestObject.getString(Constants.bankAccount)); //支付宝类型
		}
		if (requestObject.has(Constants.areaName)) { //地址
			clientInfo.setAraeaname(requestObject.getString(Constants.areaName));
		}
		if (requestObject.has(Constants.bankCardNo)) { //银行卡号
			clientInfo.setBankcardno(requestObject.getString(Constants.bankCardNo));
		}
		
		//新闻资讯
		if (requestObject.has(Constants.newsType)) { //资讯查询类型
			clientInfo.setNewsType(requestObject.getString(Constants.newsType));
		}
		if (requestObject.has(Constants.newsId)) { //资讯编号
			clientInfo.setNewsId(requestObject.getString(Constants.newsId));
		}
		if (requestObject.has(Constants.activityId)) { //活动编号
			clientInfo.setActivityId(requestObject.getString(Constants.activityId));
		}
		if (requestObject.has(Constants.id)) { //编号
			clientInfo.setId(requestObject.getString(Constants.id));
		}
		if (requestObject.has(Constants.keyStr)) { //键
			clientInfo.setKeyStr(requestObject.getString(Constants.keyStr));
		}
		
		//用户信息
		if (requestObject.has(Constants.userNo)) { //用户编号
			clientInfo.setUserno(requestObject.getString(Constants.userNo));
		}
		if (requestObject.has(Constants.nickName)) { //昵称
			clientInfo.setNickName(requestObject.getString(Constants.nickName));
		}
		if (requestObject.has(Constants.password)) { //密码
			clientInfo.setPassword(requestObject.getString(Constants.password));
		}
		if (requestObject.has(Constants.name)) { //名字
			clientInfo.setName(requestObject.getString(Constants.name));
		}
		
		//用户中心
		if (requestObject.has(Constants.score)) { //积分
			clientInfo.setScore(requestObject.getString(Constants.score));
		}
		if (requestObject.has(Constants.scoreType)) { //积分类型
			clientInfo.setScoreType(requestObject.getString(Constants.scoreType));
		}
		if (requestObject.has(Constants.bindPhoneNum)) { //绑定的手机号码
			clientInfo.setBindPhoneNum(requestObject.getString(Constants.bindPhoneNum));
		}
		if (requestObject.has(Constants.securityCode)) { //验证码
			clientInfo.setSecurityCode(requestObject.getString(Constants.securityCode));
		}
		if (requestObject.has(Constants.content)) { //反馈内容
			clientInfo.setContent(requestObject.getString(Constants.content));
		}
		
		//修改密码
		if (requestObject.has(Constants.old_pass)) { //旧密码
			clientInfo.setOldPass(requestObject.getString(Constants.old_pass));
		}
		if (requestObject.has(Constants.new_pass)) { //新密码
			clientInfo.setNewPass(requestObject.getString(Constants.new_pass));
		}
		
		//登录
		if (requestObject.has(Constants.isAutoLogin)) { //是否自动登录
			clientInfo.setIsAutoLogin(requestObject.getString(Constants.isAutoLogin));
		}
		if (requestObject.has(Constants.randomNumber)) { //自动登录的随机数
			clientInfo.setRandomNumber(requestObject.getString(Constants.randomNumber));
		}
		if (requestObject.has(Constants.source)) { //来源(联合登录)
			clientInfo.setSource(requestObject.getString(Constants.source));
		}
		if (requestObject.has(Constants.openId)) { //第三方用户标识(联合登录)
			clientInfo.setOpenId(requestObject.getString(Constants.openId));
		}
		
		//注册
		if (requestObject.has(Constants.recommender)) { //推荐人的用户名
			clientInfo.setRecommender(requestObject.getString(Constants.recommender));
		}
		if (requestObject.has(Constants.agencyNo)) { //默认的代理编号
			clientInfo.setAgencyNo(requestObject.getString(Constants.agencyNo));
		}
		
		//消息设置
		if (requestObject.has(Constants.token)) { //iPhone手机标识
			clientInfo.setToken(requestObject.getString(Constants.token));
		}
		if (requestObject.has(Constants.info)) { //信息
			clientInfo.setInfo(requestObject.getString(Constants.info));
		}
		
		//竞彩
		if (requestObject.has(Constants.jingcaiType)) { //竞彩类型
			clientInfo.setJingcaiType(requestObject.getString(Constants.jingcaiType));
		}
		if (requestObject.has(Constants.jingcaiValueType)) { //竞彩
			clientInfo.setJingcaiValueType(requestObject.getString(Constants.jingcaiValueType));
		}
		if (requestObject.has(Constants.event)) { //赛事信息
			clientInfo.setEvent(requestObject.getString(Constants.event));
		}
		if (requestObject.has(Constants.date)) { //日期
			clientInfo.setDate(requestObject.getString(Constants.date));
		}
	}
	
}
