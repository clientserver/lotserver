package com.ruyicai.lotserver.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.Constants;
import com.ruyicai.lotserver.consts.ErrorCode;
import com.ruyicai.lotserver.dto.BetRequest;
import com.ruyicai.lotserver.dto.CaseLotRequest;
import com.ruyicai.lotserver.dto.OrderRequest;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.BetUtil;
import com.ruyicai.lotserver.util.lot.DaletouUtil;
import com.ruyicai.lotserver.util.lot.YieldUtil;
import com.ruyicai.lotserver.util.parse.BuildRequestsUtil;
import com.ruyicai.lotserver.util.parse.LotsTypeParseUtil;

/**
 * 投注相关的Service
 * @author Administrator
 *
 */
@Service
public class BetLotService {
	
	private Logger logger = Logger.getLogger(BetLotService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private BetUtil betUtil;
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
    private RechargeService rechargeService; 
	
	/**
	 * 订单投注
	 * @param clientInfo
	 * @return
	 */
	public String bet(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			String lotMulti = clientInfo.getLotmulti(); //倍数
			String betCode = clientInfo.getBetCode(); //注码
			String batchCode = clientInfo.getBatchCode(); //期号
			
			String userNo = commonService.getNewUserNo(clientInfo); //用户编号
			//如果userNo或倍数或注码为空,返回参数错误
			if (Tools.isEmpty(userNo)||Tools.isEmpty(betCode)
					||Tools.isEmpty(lotMulti)||lotMulti.equals("null")) { 
				return Tools.paramError(clientInfo.getImei());
			}
			//判断是否是大乐透12选2追加
			if (DaletouUtil.isDaLeTou12_2ZhuiJia(clientInfo)) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "12选2不支持追加");
				return responseJson.toString();
			}
			OrderRequest orderRequest = betUtil.buildBetOrderRequest(userNo, clientInfo); //构造OrderRequest
			
			String url = propertiesUtil.getLotteryUrl()+"bet/tobetOrder?";
			String result = HttpUtil.sendRequestByPost(url, "body="+URLEncoder.encode(JSONObject.fromObject(orderRequest).toString(), "UTF-8"), true);
			logger.info("投注返回:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if(errorCode!=null&&errorCode.equals("0")) { //投注成功
					responseJson.put(Constants.error_code, ErrorCode.success.value());
					responseJson.put(Constants.message, "投注已受理");
				} else if(errorCode!=null&&(errorCode.equals("20100710")
						||errorCode.equals("20100701")||errorCode.equals("20100702"))) { //余额不足
					responseJson.put(Constants.error_code, ErrorCode.balanceNotEnough.value());
					responseJson.put(Constants.message, "余额不足");
				} else if(errorCode!=null&&errorCode.equals("20100706")) {//该期已过期
					responseJson.put(Constants.error_code, ErrorCode.batchCodeExpired.value());
					responseJson.put(Constants.message, "该期已过期");
					
					String currentBatchCode = commonUtil.getCurrentBatchCodeAfterBatchCodeExpired(lotNo, batchCode);
					responseJson.put(Constants.batchCode, currentBatchCode);
				} else if(errorCode!=null&&errorCode.equals("20100705")) {//该期不存在
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "该期不存在");
				} else if(errorCode!=null&&errorCode.equals("200026")) {//场次已过期
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "场次已过期");
				} else if(errorCode!=null&&errorCode.equals("200028")) {//该彩种暂停销售
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "该彩种暂停销售");
				} else if(errorCode!=null&&errorCode.equals("200030")) {//不支持此玩法
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "不支持此玩法");
				} else {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "投注失败");
				}
			} else {
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "投注失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("投注发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 保存订单
	 * @param clientInfo
	 * @return
	 */
	public String saveOrder(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String lotMulti = clientInfo.getLotmulti(); //倍数
			String betCode = clientInfo.getBetCode(); //注码
			String continueBetType = "1"; //投注方式(1:投注)
			
			String userNo = commonService.getNewUserNo(clientInfo); //用户编号
			//如果userNo或倍数或注码为空,返回参数错误
			if (Tools.isEmpty(userNo)||Tools.isEmpty(betCode)
					||Tools.isEmpty(lotMulti)||lotMulti.equals("null")) { 
				return Tools.paramError(clientInfo.getImei());
			}
			//判断是否是大乐透12选2追加
			if (DaletouUtil.isDaLeTou12_2ZhuiJia(clientInfo)) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "12选2不支持追加");
				return responseJson.toString();
			}
			OrderRequest orderRequest = betUtil.buildBetOrderRequest(userNo, clientInfo); //构造OrderRequest
			
			String url = propertiesUtil.getLotteryUrl()+"bet/saveorderonly?";
			String result = HttpUtil.sendRequestByPost(url, "body="+URLEncoder.encode(JSONObject.fromObject(orderRequest).toString(), "UTF-8"), true);
			logger.info("保存订单返回:"+result+",userNo:"+userNo);
			if (Tools.isEmpty(result)) { // 如果返回空,参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if(errorCode!=null&&errorCode.equals("0")) { //投注成功
					JSONObject valueObject = fromObject.getJSONObject("value");
					String orderId = valueObject.getString("id"); //订单号
					
					String rechargeType = clientInfo.getRechargetype(); //充值类型
					if(rechargeType!=null&&rechargeType.equals("05")){//支付宝充值
						return rechargeService.zfbCharge(clientInfo, continueBetType, orderId);
					} else if (rechargeType!=null&&rechargeType.equals("07")) { //支付宝安全支付
						return rechargeService.zfbSecurityCharge(clientInfo, continueBetType, orderId);
					} else {
						return Tools.paramError(clientInfo.getImei());
					}
				} else {
					responseJson.put(Constants.error_code, ErrorCode.fail.value());
					responseJson.put(Constants.message, "提交失败");
				}
			} else {
				responseJson.put(Constants.error_code, ErrorCode.fail.value());
				responseJson.put(Constants.message, "提交失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, ErrorCode.fail.value());
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("保存订单发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 发起合买
	 * @param clientInfo
	 * @return
	 */
	public String startCaseLot(ClientInfo clientInfo){
		JSONObject responseJson = new JSONObject();
		try {
			String lotNo = clientInfo.getLotNo(); //彩种编号
			String amount = clientInfo.getAmount(); //单期金额(注数*单注金额*倍数)
			String lotMulti = clientInfo.getLotmulti(); //倍数
			String betCode = clientInfo.getBetCode(); //注码
			String safeAmt = clientInfo.getSafeAmt(); //保底金额
			String buyAmt = clientInfo.getBuyAmt(); //购买金额
			String commisionRatio = clientInfo.getCommisionRatio(); //提成比例
			String visibility = clientInfo.getVisibility(); //是否可见(0:不可见;1:可见)
			String minAmt = clientInfo.getMinAmt(); //最低认购金额
			
			String userNo = commonService.getNewUserNo(clientInfo);
			//如果userNo或倍数或注码为空,返回参数错误
			if (Tools.isEmpty(userNo)||Tools.isEmpty(betCode)
					||Tools.isEmpty(lotMulti)||lotMulti.equals("null")) { 
				return Tools.paramError(clientInfo.getImei());
			}
			//判断是否是大乐透12选2追加
			if (DaletouUtil.isDaLeTou12_2ZhuiJia(clientInfo)) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "12选2不支持追加");
				return responseJson.toString();
			}
			//每注的金额，默认是2元，大乐透追加为3元每注
			String newOneAmount = BetUtil.getNewOneAmount(clientInfo);
			//获取期号
			String batchCode = clientInfo.getBatchCode(); //客户端传过来的期号
			String newBatchCode = betUtil.getNewBatchCode(lotNo, batchCode, "", "");
			//彩票类型(0-单式上传，1-复式，2-胆拖，3-多方案)
			String lotsType = LotsTypeParseUtil.parse(lotNo, betCode, clientInfo.getIsSellWays());
			//彩票信息
			List<BetRequest> betRequests = BuildRequestsUtil.buildBetRequests(clientInfo);
			//合买信息
			CaseLotRequest caseLotRequest = new CaseLotRequest(); //合买信息
			caseLotRequest.setSafeAmt(Long.parseLong(Tools.isEmpty(safeAmt)?"0":safeAmt)); //保底金额
			caseLotRequest.setBuyAmt(Long.parseLong(Tools.isEmpty(buyAmt)?"0":buyAmt)); //购买金额
			caseLotRequest.setTotalAmt(Long.parseLong(amount)); //总金额
			caseLotRequest.setCommisionRatio(Integer.parseInt(Tools.isEmpty(commisionRatio)?"0":commisionRatio)); //提成比例
			caseLotRequest.setTitle(""); //标题
			caseLotRequest.setDesc(clientInfo.getDescription()); //描述
			caseLotRequest.setVisibility(Integer.parseInt(Tools.isEmpty(visibility)?"0":visibility)); //是否可见
			caseLotRequest.setMinAmt(Long.parseLong(Tools.isEmpty(minAmt)?"0":minAmt)); //最低认购金额
			caseLotRequest.setStarter(userNo); //发起人
			caseLotRequest.setCaselotinfo("");
			
			//OrderRequest
			OrderRequest orderRequest = new OrderRequest();
			orderRequest.setBuyuserno(userNo); //用户编号
			orderRequest.setLotno(lotNo); //彩种
			orderRequest.setBatchcode(newBatchCode); //期号
			orderRequest.setLotmulti(new BigDecimal(lotMulti)); //倍数
			orderRequest.setAmt(new BigDecimal(amount)); //总金额
			orderRequest.setOneamount(new BigDecimal(newOneAmount)); //单注金额
			orderRequest.setBettype(new BigDecimal(3)); //投注类型为合买
			orderRequest.setLotsType(new BigDecimal(lotsType)); //彩票类型
			orderRequest.setBetRequests(betRequests); //彩票信息
			orderRequest.setCaseLotRequest(caseLotRequest); //合买信息
			orderRequest.setSubchannel("00092493"); //用户系统
			//orderRequest.setChannel(clientInfo.getCoopId()); //渠道编号
			String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
			orderRequest.setChannel(newCoopId);
			
			String url = propertiesUtil.getLotteryUrl()+"caselot/caselotOrder";
			String result = HttpUtil.sendRequestByPost(url, "body="+URLEncoder.encode(JSONObject.fromObject(orderRequest).toString(), "UTF-8"), true);
			logger.info("发起合买返回:"+result+",userNo:"+userNo);
			if(Tools.isEmpty(result)) { //返回为空，参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) { //成功
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "发起合买成功");	
				} else if (errorCode!=null&&(errorCode.equals("20100710")
						||errorCode.equals("20100701")||errorCode.equals("20100702"))) { //余额不足
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "余额不足");
				} else if (errorCode!=null&&errorCode.equals("20100706")) { //该期已过期
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "该期已过期");
				} else if (errorCode!=null&&errorCode.equals("500017")) { //合买总金额小于要求
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "发起合买最低金额为6元");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "发起合买失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "发起合买失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("发起合买发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 参与合买
	 * @param clientInfo
	 * @return
	 */
	public String betCaseLot(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			//如果userNo为空,返回参数错误
			if (Tools.isEmpty(userNo)) { 
				return Tools.paramError(clientInfo.getImei());
			}
			//保底金额
			String safeAmt = clientInfo.getSafeAmt();
			if(Tools.isEmpty(safeAmt)) {
				safeAmt = "0";
			}
			String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno="+userNo);
			paramStr.append("&amt="+clientInfo.getAmount());
			paramStr.append("&caseId="+clientInfo.getCaseid());
			paramStr.append("&safeAmt="+safeAmt);
			//paramStr.append("&channel="+clientInfo.getCoopId()); //渠道号
			paramStr.append("&channel="+newCoopId); //渠道号
			
			String url = propertiesUtil.getLotteryUrl()+"caselot/betCaselot";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("参与合买返回:"+result+",userNo:"+userNo);
			if(Tools.isEmpty(result)) { //返回为空，参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "参与合买成功");
				} else if (errorCode!=null&&errorCode.equals("500001")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "合买方案不存在");
				} else if (errorCode!=null&&errorCode.equals("500002")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "合买方案已满");
				} else if (errorCode!=null&&errorCode.equals("500004")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "保底金额错误");
				} else if (errorCode!=null&&(errorCode.equals("20100710")
						||errorCode.equals("20100701")||errorCode.equals("20100702"))) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "余额不足");
				} else if(errorCode!=null&&errorCode.equals("20100706")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "该期已过期");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "参与合买失败");
				}
			} else {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "参与合买失败");
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("参与合买发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 合买撤单
	 * @param clientInfo
	 * @return
	 */
	public String cancelCaselot(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno="+userNo);
			paramStr.append("&caseId="+clientInfo.getCaseid());
			
			String url = propertiesUtil.getLotteryUrl()+"caselot/cancelCaselot";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("合买撤单返回:"+result+",paramStr:"+paramStr.toString());
			if(Tools.isEmpty(result)) { //返回为空，参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "撤单成功");	
				} else if (errorCode!=null&&errorCode.equals("500007")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "合买进度大于50%");	
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "撤单失败");
				}
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("合买撤单发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 合买撤资
	 * @param clientInfo
	 * @return
	 */
	public String cancelCaselotbuy(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			String userNo = commonService.getNewUserNo(clientInfo);
			if (Tools.isEmpty(userNo)) { // 如果userNo为空,参数错误
				return Tools.paramError(clientInfo.getImei());
			} 
			
			StringBuffer paramStr = new StringBuffer();
			paramStr.append("userno="+userNo);
			paramStr.append("&caseId="+clientInfo.getCaseid());
			
			String url = propertiesUtil.getLotteryUrl()+"caselot/cancelCaselotbuy";
			String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
			logger.info("合买撤资返回:"+result+",paramStr:"+paramStr.toString());
			if(Tools.isEmpty(result)) { //返回为空，参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null) {
				String errorCode = fromObject.getString("errorCode");
				if (errorCode!=null&&errorCode.equals("0")) {
					responseJson.put(Constants.error_code, "0000");
					responseJson.put(Constants.message, "撤资成功");	
				} else if (errorCode!=null&&errorCode.equals("500009")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "发起人不能撤资");
				} else if (errorCode!=null&&errorCode.equals("500010")) {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "合买进度大于20%");
				} else {
					responseJson.put(Constants.error_code, "9999");
					responseJson.put(Constants.message, "撤资失败");
				}
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("合买撤资发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 计算收益率
	 * @param clientInfo
	 * @return
	 */
	public String yield(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		try {
			DecimalFormat df = new DecimalFormat("0.00"); //格式化小数点
			
			String betCode = clientInfo.getBetCode(); //注码
			if (Tools.isEmpty(betCode)) { // 如果betCode为空,返回参数错误
				return Tools.paramError(clientInfo.getImei());
			}
			
			String lotMulti = clientInfo.getLotmulti(); //倍数
			if (Tools.isEmpty(lotMulti)||Integer.parseInt(lotMulti)>9999) { //倍数为空或倍数超过9999
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "倍数不合法 ");	
				return responseJson.toString();
			}
			
			String batchNum = clientInfo.getBatchnum(); //追号期数
			String betNum = clientInfo.getBetNum(); //注数
			String lotNo = clientInfo.getLotNo(); //彩种编号
			String wholeYield = clientInfo.getWholeYield(); //全程收益率
			String beforeBatchNum = clientInfo.getBeforeBatchNum(); //前几期
			String beforeYield = clientInfo.getBeforeYield(); //前程收益率
			String afterYield = clientInfo.getAfterYield(); //后程收益率
			String batchCode = clientInfo.getBatchCode(); //期号
			

			String[] lotMultiArray = new String[Integer.parseInt(batchNum)];// 保存每期应投入的倍数
			String[] accumulatedInputArray = new String[Integer.parseInt(batchNum)];// 保存每期累计的本金
			String[] batchCodeArray = new String[Integer.parseInt(batchNum)];// 保存每期累计的本金
			
			Integer accumulatedInput = (Integer.parseInt(betNum)*Integer.parseInt(lotMulti)*2);
			lotMultiArray[0] = lotMulti;
			accumulatedInputArray[0] = accumulatedInput+"";
			Integer prix = YieldUtil.getPrix(lotNo, betCode);// 获取该玩法的奖金

			//全程收益率
			if (!Tools.isEmpty(wholeYield)) {
				for (int i = 1; i < Integer.parseInt(batchNum); i++) {
					String num = YieldUtil.calculateYield(lotMulti, betNum, accumulatedInputArray[i-1], wholeYield, prix, lotMultiArray, accumulatedInputArray, i);
					if (!Tools.isEmpty(num)) {
						responseJson.put(Constants.error_code, "9999");
						responseJson.put(Constants.message, "您好,因为倍投方案上限限制9999倍,你的方案不适合投入"+batchNum+"期倍投追号!第"+num+"期的倍数已达到上限倍数!");
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			} else { //前后程收益率
				for (int i = 1; i < Integer.parseInt(beforeBatchNum); i++) { // 前程收益率
					String num = YieldUtil.calculateYield(lotMulti, betNum, accumulatedInputArray[i-1], beforeYield, prix, lotMultiArray, accumulatedInputArray, i);
					if (!Tools.isEmpty(num)) {
						responseJson.put(Constants.error_code, "9999");
						responseJson.put(Constants.message, "您好,因为倍投方案上限限制9999倍,你的方案不适合投入"+batchNum+"期倍投追号!第"+num+"期的倍数已达到上限倍数!");
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
				for (int i = Integer.parseInt(beforeBatchNum); i < Integer.parseInt(batchNum); i++) { // 后程收益率
					String num = YieldUtil.calculateYield(lotMulti, betNum, accumulatedInputArray[i-1], afterYield, prix, lotMultiArray, accumulatedInputArray, i);
					if (!Tools.isEmpty(num)) {
						responseJson.put(Constants.error_code, "9999");
						responseJson.put(Constants.message, "您好,因为倍投方案上限限制9999倍,你的方案不适合投入"+batchNum+"期倍投追号!第"+num+"期的倍数已达到上限倍数!");
						responseJson.put("result", resultArray);
						return responseJson.toString();
					}
				}
			}
			
			//获取以后的期号
			String afterIssue = lotteryCommonService.getAfterIssue(lotNo, batchCode, (Integer.parseInt(batchNum)-1)+"");
			if (!Tools.isEmpty(afterIssue)&&!afterIssue.equals("null")) {
				JSONArray afterIssueArray = JSONArray.fromObject(afterIssue);
				for (int i = 0; i < afterIssueArray.size(); i++) {
					JSONObject afterIssueObject = afterIssueArray.getJSONObject(i);
					JSONObject idObject = afterIssueObject.getJSONObject("id");
					batchCodeArray[i] = idObject.getString("batchcode");
				}
			}
			
			for (int i = 0; i < Integer.parseInt(batchNum); i++) {
				Float shouyilvF = (Float.parseFloat(lotMultiArray[i])*Float.parseFloat(prix+"")-Float.parseFloat(accumulatedInputArray[i]))*100/Float.parseFloat(accumulatedInputArray[i]);
				JSONObject object = new JSONObject();
				object.put("batchCode", batchCodeArray[i]); //期号
				object.put("lotMulti", lotMultiArray[i]); //倍数
				object.put("currentIssueInput", (Integer.parseInt(lotMultiArray[i])*Integer.parseInt(betNum)*2)+"00"); //当期投入
				object.put("currentIssueYield", (Integer.parseInt(lotMultiArray[i])*prix-(Integer.parseInt(lotMultiArray[i])*Integer.parseInt(betNum)*2))+"00"); //当期收益
				object.put("accumulatedInput", accumulatedInputArray[i]+"00"); //累计投入
				object.put("accumulatedYield", (Integer.parseInt(lotMultiArray[i])*prix-Integer.parseInt(accumulatedInputArray[i]))+"00"); //累计收益
				object.put("yieldRate", df.format(shouyilvF)+"%"); //收益率
				resultArray.add(object);
			}
			
			responseJson.put(Constants.error_code, "0000");
			responseJson.put(Constants.message, "获取成功");
		} catch (RuntimeException e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");	
			logger.error("计算收益率发生异常", e);
		}
		responseJson.put("result", resultArray);
		return responseJson.toString();
	}
	
	/**
	 * 赠送
	 * @param clientInfo
	 * @return
	 */
	public String gift(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		
		String success = ""; //赠送成功的手机号
		String fail = ""; //赠送失败的手机号
		try {
			String lotNo = clientInfo.getLotNo(); //彩种
			String amount = clientInfo.getAmount(); //单期金额(注数*单注金额*倍数)
			String lotMulti = clientInfo.getLotmulti(); //倍数
			String betCode = clientInfo.getBetCode(); //注码
			
			String userNo = commonService.getNewUserNo(clientInfo);
			//如果userNo或倍数或注码为空,返回参数错误
			if (Tools.isEmpty(userNo)||Tools.isEmpty(betCode)
					||Tools.isEmpty(lotMulti)||lotMulti.equals("null")) { 
				return Tools.paramError(clientInfo.getImei());
			}
			
			//判断是否是大乐透12选2追加
			if (DaletouUtil.isDaLeTou12_2ZhuiJia(clientInfo)) {
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "12选2不支持追加");
				return responseJson.toString();
			}
			//验证被赠送人手机号的合法性
			String to_moblie_code = clientInfo.getTo_mobile_code(); //被赠送人手机号
			Pattern pattern = Pattern.compile("((13[0-9]|15[0-9]|18[0-9])\\d{8})(,(13[0-9]|15[0-9]|18[0-9])\\d{8})*");
			Matcher matcher = pattern.matcher(to_moblie_code);
			if (Tools.isEmpty(to_moblie_code)||!matcher.matches()) { //验证手机号码的合法性
				JSONObject giftResult = new JSONObject(); //赠送结果
				giftResult.put("success", success);
				giftResult.put("fail", to_moblie_code);
				responseJson.put(Constants.error_code, "9999");
				responseJson.put(Constants.message, "赠送号码不合法");
				responseJson.put(Constants.gift_result, giftResult);
				return responseJson.toString();
			}
			//每注的金额，默认是2元，大乐透追加为3元每注
			String newOneAmount = BetUtil.getNewOneAmount(clientInfo);
			
			boolean hasDNACharge = false; //是否有DNA充值
			String[] toMobleArray = to_moblie_code.split(","); 
			for (int i=0; i<toMobleArray.length; i++) {
				String toMobileId = toMobleArray[i].trim();
				if (Tools.isEmpty(toMobileId)||toMobileId.length()!=11) {
					continue;
				}
				//获取期号
				String batchCode = clientInfo.getBatchCode(); //客户端传过来的期号
				String newBatchCode = betUtil.getNewBatchCode(lotNo, batchCode, "", ""); 
				//彩票信息
				List<BetRequest> betRequests = BuildRequestsUtil.buildBetRequests(clientInfo); 
				//OrderRequest
				OrderRequest orderRequest = new OrderRequest();
				orderRequest.setBuyuserno(userNo); //用户编号
				orderRequest.setReciverMobile(toMobileId); //被赠送人手机号码
				orderRequest.setLotno(lotNo); //彩种
				orderRequest.setBatchcode(newBatchCode); //期号
				orderRequest.setLotmulti(new BigDecimal(lotMulti)); //倍数
				orderRequest.setAmt(new BigDecimal(amount)); //总金额
				orderRequest.setOneamount(new BigDecimal(newOneAmount)); //单注金额
				orderRequest.setBetRequests(betRequests); //彩票信息
				//orderRequest.setBettype(new BigDecimal(5)); //控制后台是否发送短信(4:后台发短信；5：后台不发短信)
				orderRequest.setBettype(new BigDecimal(4)); //控制后台是否发送短信(4:后台发短信；5：后台不发短信)
				orderRequest.setBlessing(clientInfo.getBlessing()); //赠送寄语
				orderRequest.setSubchannel("00092493"); //用户系统
				//orderRequest.setChannel(clientInfo.getCoopId()); //渠道编号
				String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
				orderRequest.setChannel(newCoopId);
				
				String url = propertiesUtil.getLotteryUrl()+"present/savepresent?";
				String result = HttpUtil.sendRequestByPost(url, "body="+JSONObject.fromObject(orderRequest), true);
				logger.info("赠送返回:"+result+",userNo:"+userNo+",to_moblie_code:"+to_moblie_code);
				JSONObject fromObject = JSONObject.fromObject(result);
				if (fromObject!=null) {
					String errorCode = fromObject.getString("errorCode");
					if (errorCode!=null&&errorCode.equals("0")) { //赠送成功
						success += toMobleArray[i]+","; 
					} else if (errorCode!=null&&errorCode.equals("810007")) { //由于您使用了银联语音充值,为保障您银行卡的资金安全,此账户不能做赠送
						hasDNACharge = true;
						fail += toMobleArray[i]+",";
					} else {
						fail += toMobleArray[i]+",";
					}
				} else {
					fail += toMobleArray[i]+",";
				}
			}
			if (success.length()>=11) {
				responseJson.put(Constants.error_code, "0000");
				responseJson.put(Constants.message, "赠送成功");
			} else {
				responseJson.put(Constants.error_code, "9999");
				if (hasDNACharge) {
					responseJson.put(Constants.message, "由于您使用了银联语音充值,为保障您银行卡的资金安全,此账户不能做赠送");
				} else {
					responseJson.put(Constants.message, "赠送失败");
				}
			}
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("赠送发生异常", e);
		}
		JSONObject resultJson = new JSONObject(); //赠送结果
		resultJson.put("success", success);
		resultJson.put("fail", fail);
		responseJson.put(Constants.gift_result, resultJson);
		
		return responseJson.toString();
	}
	
	/**
	 * 领取赠送彩票
	 * @param clientInfo
	 * @return
	 */
	public String receivePresent(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "赠送彩票已调整为免领取,可直接查看您收到的彩票信息");
		} catch (Exception e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");
			logger.error("领取赠送彩票发生异常", e);
		}
		return responseJson.toString();
	}
	
	/**
	 * 领取赠送彩票取得验证码
	 * @param clientInfo
	 * @return
	 */
	public String receivePresentSecurityCode(ClientInfo clientInfo) {
		JSONObject responseJson = new JSONObject();
		try {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "赠送彩票已调整为免领取,可直接查看您收到的彩票信息");	
		} catch (RuntimeException e) {
			responseJson.put(Constants.error_code, "9999");
			responseJson.put(Constants.message, "系统繁忙");	
			logger.error("领取赠送彩票取得验证码发生异常", e);
		}
		return responseJson.toString();
	}
	
}
