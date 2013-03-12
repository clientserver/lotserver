package com.ruyicai.lotserver.util.lot;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

import com.ruyicai.lotserver.util.CacheCommonUtil;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.VerifyUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 合买公共类
 * @author Administrator
 *
 */
@Service
public class CaseLotUtil {
	
	@Autowired
	private CommonUtil commonUtil;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;

	/**
	 * 合买显示昵称如果是手机号码则只显示后三位
	 * @param mobileId
	 * @return
	 */
	public static String getNickname(String mobileId) {
		if (VerifyUtil.isMobile(mobileId)) { //是手机号码
			String nickname = "";
			String hou = mobileId.substring(mobileId.length()-4);
			String qian = mobileId.substring(0, 3);
			nickname = qian+"****"+hou;
			return nickname;
		}
		return mobileId;
	}

	
	/**
	 * 计算认购的进度
	 * @param caselot
	 * @return 百分比整数
	 */
	public static BigDecimal computePlan(long buyAmtByStarter, long buyAmtByFollower, long totalAmt) {
		// 认购金额进度
		BigDecimal subscribeRate = (new BigDecimal(buyAmtByStarter)
				.add(new BigDecimal(buyAmtByFollower))).multiply(
				new BigDecimal(100)).divide(new BigDecimal(totalAmt), 0,
				BigDecimal.ROUND_HALF_UP);
		return subscribeRate;
	}
	
	/**
	 * 计算保底进度
	 * @param safeAmt
	 * @param totalAmt
	 * @return
	 */
	public static BigDecimal computeSafeRate(long safeAmt, long totalAmt) {
		BigDecimal safeRate = new BigDecimal(safeAmt).multiply(
				new BigDecimal(100)).divide(new BigDecimal(totalAmt), 0,
				BigDecimal.ROUND_HALF_UP);
		return safeRate;
	}
	
	/**
	 * 获得合买发起人的名字
	 * @param starterObject
	 * @return
	 */
	public static String getCaseLotStarterName(JSONObject starterObject) {
		String starterName = ""; //发起人名字
		String nickName = starterObject.getString("nickname"); //昵称
		String userName = starterObject.getString("userName"); //用户名
		
		if (Tools.isEmpty(nickName)||nickName.trim().equals("null")) {
			starterName = CaseLotUtil.getNickname(userName);
		} else {
			starterName = nickName;
		}
		return starterName;
	}
	
	/**
	 * 获取合买战绩
	 * @param achievementObject
	 * @return
	 */
	public static JSONObject getCaseLotDisplayIcon(JSONObject achievementObject) {
		JSONObject displayIcon = (achievementObject!=null&&achievementObject.has("displayIcon"))
									?achievementObject.getJSONObject("displayIcon"):new JSONObject();
		return displayIcon;
	}
	
	/**
	 * 解析方案内容
	 * @param displayTlots
	 * @param caseLotObject
	 * @param torderObject
	 * @return
	 */
	public JSONObject getCaseLotContent(String displayTlots, JSONObject caseLotObject, JSONObject torderObject) {
		JSONObject returnJson = new JSONObject();
		String content = ""; //解析后的注码
		String contentHtml = ""; //解析后的注码(html)
		String lotNo = caseLotObject.getString("lotno"); //彩种
		if (!Tools.isEmpty(displayTlots)&&displayTlots.trim().equals("true")) { //显示注码详情
			String orderId = torderObject.getString("id"); //订单编号
			String betCode = torderObject.getString("betcode"); //注码
			String orderInfo = torderObject.getString("orderinfo"); //orderInfo
			String orderState = torderObject.getString("orderstate"); //订单状态
			String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
			//解析注码
			boolean isOutTicket = (orderState!=null&&orderState.equals("1")) ? true : false; //订单是否已出票
			JSONObject tlotsValueObject = null; //订单对应的票
			if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
				tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, isOutTicket);
			}
			content = commonUtil.getParseCode(displayTlots, lotNo, orderInfo, betCode, tlotsValueObject, false, "");
			contentHtml = commonUtil.getParseCode(displayTlots, lotNo, orderInfo, betCode, tlotsValueObject, true, winBaseCode);
		} else { //不显示注码详情
			String visibility = caseLotObject.getString("visibility"); //公开状态
			if (visibility.equals("0")) {
				content = "对所有人立即公开";
			} else if (visibility.equals("1")) {
				content = "保密";
			} else if (visibility.equals("2")) {
				content = "对所有人截止后公开";
			} else if (visibility.equals("3")) {
				content = "对跟单者立即公开";
			} else if (visibility.equals("4")) {
				content = "对跟单者截止后公开";
			}
			contentHtml = content;
		}
		returnJson.put("content", content);
		returnJson.put("contentHtml", contentHtml);
		return returnJson;
	}
	
	/**
	 * 获取合买方案的注码解析html
	 * @param displayTlots
	 * @param caseLotObject
	 * @param torderObject
	 * @return
	 */
	public String getCaseLotBetCodeHtml(String displayTlots, JSONObject caseLotObject, JSONObject torderObject) {
		String contentHtml = ""; //解析后的注码(html)
		String lotNo = caseLotObject.getString("lotno"); //彩种
		if (!Tools.isEmpty(displayTlots)&&displayTlots.trim().equals("true")) { //显示注码详情
			String orderId = torderObject.getString("id"); //订单编号
			String betCode = torderObject.getString("betcode"); //注码
			String orderInfo = torderObject.getString("orderinfo"); //orderInfo
			String orderState = torderObject.getString("orderstate"); //订单状态
			String winBaseCode = torderObject.getString("winbasecode"); //开奖号码
			//解析注码
			boolean isOutTicket = (orderState!=null&&orderState.equals("1")) ? true : false; //订单是否已出票
			JSONObject tlotsValueObject = null; //订单对应的票
			if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
				tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, isOutTicket);
			}
			contentHtml = commonUtil.getBetCodeAnalysis(displayTlots, lotNo, orderInfo, betCode, tlotsValueObject, true, winBaseCode);
		} else { //不显示注码详情
			String visibility = caseLotObject.getString("visibility"); //公开状态
			if (visibility.equals("0")) {
				contentHtml = "对所有人立即公开";
			} else if (visibility.equals("1")) {
				contentHtml = "保密";
			} else if (visibility.equals("2")) {
				contentHtml = "对所有人截止后公开";
			} else if (visibility.equals("3")) {
				contentHtml = "对跟单者立即公开";
			} else if (visibility.equals("4")) {
				contentHtml = "对跟单者截止后公开";
			}
		}
		return contentHtml;
	}
	
	/**
	 * 解析方案内容Json
	 * @param displayTlots
	 * @param orderId
	 * @param lotNo
	 * @param orderInfo
	 * @param orderState
	 * @return
	 */
	public JSONObject getCaseLotContentJson(String displayTlots, String visibility, String orderId, String lotNo, 
			String batchCode, String orderInfo, String winCode, String orderState) {
		JSONObject returnJson = new JSONObject();
		boolean isOutTicket = (orderState!=null&&orderState.equals("1")) ? true : false; //订单是否已出票
		JSONObject tlotsValueObject = null; //订单对应的票
		if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
			tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, isOutTicket);
		}
		returnJson = commonUtil.getParseCodeJson(displayTlots, visibility, lotNo, batchCode, orderInfo, 
				winCode, tlotsValueObject);
		return returnJson;
	}
	
	/**
	 * 解析方案内容(表格形式)
	 * @param displayTlots
	 * @param visibility
	 * @param orderId
	 * @param lotNo
	 * @param batchCode
	 * @param orderInfo
	 * @param winCode
	 * @param orderState
	 * @return
	 */
	public JSONObject getCaseLotContentTable(String displayTlots, String visibility, String orderId, String lotNo, 
			String batchCode, String orderInfo, String winCode, String orderState) {
		JSONObject returnJson = new JSONObject();
		boolean isOutTicket = (orderState!=null&&orderState.equals("1")) ? true : false; //订单是否已出票
		JSONObject tlotsValueObject = null; //订单对应的票
		if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
			tlotsValueObject = cacheCommonUtil.getTlotsByOrderIdValueObject(orderId, isOutTicket);
		}
		returnJson = commonUtil.getParseCodeTable(displayTlots, visibility, lotNo, batchCode, orderInfo, 
				winCode, tlotsValueObject);
		return returnJson;
	}
	
	/**
	 * 活动合买截止时间
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public String getCaseEndTime(String lotNo, String batchCode) {
		String endTime = "";
		JSONObject batchCodeInfoObject = cacheCommonUtil.getBatchCodeInfoValueObject(lotNo, batchCode);
		if (batchCodeInfoObject!=null&&batchCodeInfoObject.has("hemaiendtime")) {
			endTime = batchCodeInfoObject.getString("hemaiendtime");
		}
		return endTime;
	}
	
	/**
	 * 是否可以撤单
	 * 合买方案当前状态为认购中并且当前认购+保底进度未达到50%，发起人可以撤单
	 * @param userNo
	 * @param starterUserNo
	 * @param displayState
	 * @param totalRate
	 * @return
	 */
	public String isCancelCaselot(String userNo, String starterUserNo, String displayState, BigDecimal totalRate) {
		String cancelCaselot = "false";
		if (!Tools.isEmpty(userNo)&&userNo.equals(starterUserNo)&&displayState.equals("1")
				&&totalRate.compareTo(new BigDecimal(50))==-1) { 
			cancelCaselot = "true";
		}
		return cancelCaselot;
	}
	
	/**
	 * 是否置顶
	 * @param sortState
	 * @return
	 */
	public static String isTop(String sortState) {
		String isTop = "true";
		if (!Tools.isEmpty(sortState)&&sortState.equals("0")) {
			isTop = "false";
		} 
		return isTop;
	}
	
	/**
	 * 是否可以撤资
	 * 合买状态为认购中;合买方案当前认购+保底进度未达到20%,参与人可以撤资;发起人不能撤资
	 * @param displayState
	 * @param userNo
	 * @param buyUserNo
	 * @param starterUserNo
	 * @param flag
	 * @param totalRate
	 * @return
	 */
	public static String isCancelCaselotbuy(String displayState, String userNo, String buyUserNo, String starterUserNo, 
			String flag, BigDecimal totalRate) {
		String cancelCaselotbuy = "false";
		if ((displayState.equals("1")||displayState.equals("2"))&&!Tools.isEmpty(userNo)&&userNo.equals(buyUserNo)
				&&!Tools.isEmpty(starterUserNo)&&!starterUserNo.equals(userNo)
				&&flag.equals("1")&&totalRate.compareTo(new BigDecimal(20))==-1) { 
			cancelCaselotbuy = "true";
		}
		return cancelCaselotbuy;
	}
	
}
