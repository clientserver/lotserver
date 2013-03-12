package com.ruyicai.lotserver.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.CoopId;
import com.ruyicai.lotserver.consts.Platform;
import com.ruyicai.lotserver.domain.Coop;
import com.ruyicai.lotserver.domain.Message;
import com.ruyicai.lotserver.domain.UserInf;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.service.common.CommonService;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;
import com.ruyicai.lotserver.util.parse.JingCaiHhBetCodeParseUtil;
import com.ruyicai.lotserver.util.parse.JingCaiBetCodeParseUtil;
import com.ruyicai.lotserver.util.parse.OrderInfoHtmlParseUtil;
import com.ruyicai.lotserver.util.parse.OrderInfoParseUtil;
import com.ruyicai.lotserver.util.parse.BetCodeParseUtil;
import com.ruyicai.lotserver.util.parse.ZuCaiBetCodeParseUtil;

/**
 * 公共类
 * @author Administrator
 *
 */
@Service
public class CommonUtil {
	
	private Logger logger = Logger.getLogger(CommonUtil.class);

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private LotteryService lotteryCommonService;
	
	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	@Autowired
	private JingCaiBetCodeParseUtil jingCaiBetCodeParseUtil;
	
	@Autowired
	private JingCaiHhBetCodeParseUtil jcHhBetCodeParseUtil;
	
	@Autowired
	private ZuCaiBetCodeParseUtil zuCaiBetCodeParseUtil;
	
	/**
	 * 验证密码是否正确
	 * @param userNo
	 * @param password
	 * @return
	 */
	public boolean verifyPassword(String userNo, String password) {
		boolean isRight = false;
		String result = lotteryCommonService.queryUsersByUserNo(userNo);
		if (!Tools.isEmpty(result)) {
			JSONObject fromObject = JSONObject.fromObject(result);
			if (fromObject!=null&&!Tools.isEmpty(fromObject.getString("errorCode"))
					&&fromObject.getString("errorCode").equals("0")) {
				JSONObject valueObject = fromObject.getJSONObject("value");
				String dbPassword = valueObject.getString("password");
				if (dbPassword.equals(Tools.EncoderByMd5(password))) {
					isRight = true;
				}
			}
		}
		return isRight;
	}
	
	/**
	 * 获得解析后的注码
	 * @param displayTlots
	 * @param orderId
	 * @param lotNo
	 * @param multiple
	 * @param orderInfo
	 * @param betcode
	 * @return
	 */
	public String getParseCode(String displayTlots, String lotNo, String orderInfo, String betCode, 
			JSONObject tlotsValueObject, boolean isHtml, String winCode) {
		StringBuffer sBuffer = new StringBuffer();
		if (!Tools.isEmpty(displayTlots)&&displayTlots.equals("false")) { //合买注码保密
			sBuffer.append("保密");
		} else { //公开
			String lineBreak = "\n"; //换行符
			if (isHtml) { //html格式
				lineBreak = "<br/>";
			}
			JSONArray parseBetCodes = new JSONArray();
			if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
				parseBetCodes = jingCaiBetCodeParseUtil.parseBetCode(lotNo, orderInfo, tlotsValueObject, isHtml);
			} else { //其他彩种
				if (!Tools.isEmpty(orderInfo)&&!orderInfo.equals("null")) { //优先解析orderInfo
					if (isHtml&&!Tools.isEmpty(winCode)&&!winCode.equals("null")) { //注码需返回html样式,开奖号码不为空
						parseBetCodes = OrderInfoHtmlParseUtil.parseOrderInfo(lotNo, orderInfo, winCode);
					} else {
						parseBetCodes = OrderInfoParseUtil.parseOrderInfo(lotNo, orderInfo);
					}
				} else { //解析betcode
					parseBetCodes = BetCodeParseUtil.parseBetCode(lotNo, betCode);
				}
			}
			for (int j = 0; j < parseBetCodes.size(); j++) {
				JSONObject parseBetCodeJson = parseBetCodes.getJSONObject(j);
				sBuffer.append("玩法:"+parseBetCodeJson.getString("play")).append(lineBreak)
				.append(parseBetCodeJson.getString("betCode")).append(lineBreak);
			}
		}
		return sBuffer.toString();
	}
	
	/**
	 * 获得解析后的注码(新接口使用)
	 * @param displayTlots
	 * @param lotNo
	 * @param orderInfo
	 * @param betCode
	 * @param tlotsValueObject
	 * @param isHtml
	 * @param winCode
	 * @return
	 */
	public String getBetCodeAnalysis(String displayTlots, String lotNo, String orderInfo, String betCode, 
			JSONObject tlotsValueObject, boolean isHtml, String winCode) {
		StringBuffer sBuffer = new StringBuffer();
		if (!Tools.isEmpty(displayTlots)&&displayTlots.equals("false")) { //合买注码保密
			sBuffer.append("保密");
		} else { //公开
			String lineBreak = "\n"; //换行符
			if (isHtml) { //html格式
				lineBreak = "<br/>";
			}
			JSONArray parseBetCodes = new JSONArray();
			//不是竞彩和足彩
			if (!LotTypeUtil.isJingCaiZQ(lotNo)&&!LotTypeUtil.isJingCaiLQ(lotNo)&&!LotTypeUtil.isJingCaiHH(lotNo)
					&&!LotTypeUtil.isZuCai(lotNo)) { 
				if (!Tools.isEmpty(orderInfo)&&!orderInfo.equals("null")) { //优先解析orderInfo
					if (isHtml&&!Tools.isEmpty(winCode)&&!winCode.equals("null")) { //注码需返回html样式,开奖号码不为空
						parseBetCodes = OrderInfoHtmlParseUtil.parseOrderInfo(lotNo, orderInfo, winCode);
					} else {
						parseBetCodes = OrderInfoParseUtil.parseOrderInfo(lotNo, orderInfo);
					}
				}
			}
			for (int j = 0; j < parseBetCodes.size(); j++) {
				JSONObject parseBetCodeJson = parseBetCodes.getJSONObject(j);
				sBuffer.append("玩法:"+parseBetCodeJson.getString("play")).append(lineBreak)
				.append(parseBetCodeJson.getString("betCode")).append(lineBreak);
			}
		}
		return sBuffer.toString();
	}
	
	/**
	 * 获得注码解析的Json
	 * @param displayTlots
	 * @param orderId
	 * @param lotNo
	 * @param orderInfo
	 * @param isOutTicket
	 * @return
	 */
	public JSONObject getParseCodeJson(String displayTlots, String visibility, String lotNo, String batchCode, 
			String orderInfo, String winCode, JSONObject tlotsValueObject) {
		JSONObject betCodeJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		if (!Tools.isEmpty(displayTlots)&&displayTlots.equals("false")) { //注码保密
			betCodeJson.put("display", "false");
		} else { //注码公开
			betCodeJson.put("display", "true");
			if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竟彩
				//显示重复的赛事
				resultArray = jingCaiBetCodeParseUtil.getParseBetCodeArray(lotNo, orderInfo, tlotsValueObject);
			} else if (LotTypeUtil.isZuCai(lotNo)) { //足彩
				resultArray = zuCaiBetCodeParseUtil.getParseBetCodeArray(lotNo, batchCode, orderInfo, winCode);
			}
		}
		betCodeJson.put("visibility", visibility);
		betCodeJson.put("result", resultArray);
		return betCodeJson;
	}
	
	/**
	 * 获得注码解析(表格形式)
	 * @param displayTlots
	 * @param visibility
	 * @param lotNo
	 * @param batchCode
	 * @param orderInfo
	 * @param winCode
	 * @param tlotsValueObject
	 * @param isNewMethod
	 * @return
	 */
	public JSONObject getParseCodeTable(String displayTlots, String visibility, String lotNo, String batchCode, 
			String orderInfo, String winCode, JSONObject tlotsValueObject) {
		JSONObject betCodeJson = new JSONObject();
		JSONArray resultArray = new JSONArray();
		if (!Tools.isEmpty(displayTlots)&&displayTlots.equals("false")) { //注码保密
			betCodeJson.put("display", "false");
		} else { //注码公开
			betCodeJson.put("display", "true");
			if (LotTypeUtil.isJingCaiHH(lotNo)) { //竞彩混合过关
				//重复的赛事信息合并
				resultArray = jcHhBetCodeParseUtil.getParseBetCodeList(tlotsValueObject);
			} else if (LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩
				//重复的赛事信息合并
				resultArray = jingCaiBetCodeParseUtil.getParseBetCodeList(lotNo, orderInfo, tlotsValueObject);
			} else if (LotTypeUtil.isZuCai(lotNo)) { //足彩
				resultArray = zuCaiBetCodeParseUtil.getParseBetCodeArray(lotNo, batchCode, orderInfo, winCode);
			}
		}
		betCodeJson.put("visibility", visibility);
		betCodeJson.put("result", resultArray);
		return betCodeJson;
	}
	
	/**
	 * 获得当前期号信息
	 * @param allValueObject
	 * @param lotNo
	 * @return
	 */
	public JSONObject getCurrentBatchCodeValueObject(JSONObject allValueObject, String lotNo) {
		JSONObject lotNoValueObject = null;
		if (allValueObject!=null&&allValueObject.has(lotNo)) {
			lotNoValueObject = allValueObject.getJSONObject(lotNo);
		} else {
			lotNoValueObject = cacheCommonUtil.getCurrentBatchCodeValueObjectByLotNo(lotNo);
		}
		return lotNoValueObject;
	}
	
	/**
	 * 根据彩种获取当前期号
	 * @param lotNo
	 * @return
	 */
	public String getCurrentBatchCodeByLotNo(String lotNo) {
		String batchCode = "";
		if (!Tools.isEmpty(lotNo)) {
			JSONObject valueObject = commonService.getCurrentBatchCodeByLotNo(lotNo);
			if (valueObject!=null) {
				JSONObject idObject = valueObject.getJSONObject("id");
				batchCode = idObject.getString("batchcode");
			}
		}
		return batchCode;
	}
	
	/**
	 * 期号过期后获取当前期号
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public String getCurrentBatchCodeAfterBatchCodeExpired(String lotNo, String batchCode) {
		String currentBatchCode = "";
		if (!Tools.isEmpty(lotNo)&&!Tools.isEmpty(batchCode)) {
			String result = lotteryCommonService.getAfterIssue(lotNo, batchCode, "1");
			if (!Tools.isEmpty(result)&&!result.equals("null")) {
				JSONArray valueArray = JSONArray.fromObject(result);
				if (valueArray!=null&&valueArray.size()==2) {
					JSONObject valueObject = valueArray.getJSONObject(1);
					if (valueObject!=null&&!valueObject.toString().equals("null")) {
						JSONObject idObject = valueObject.getJSONObject("id");
						currentBatchCode = idObject.getString("batchcode"); //期号
					}
				}
			}
		}
		return currentBatchCode;
	}
	
	/**
	 * 获得上期开奖号码信息
	 * @param allValueObject
	 * @param lotNo
	 * @param num
	 * @return
	 */
	public JSONArray getWinInfoValueArray(JSONObject allValueObject, String lotNo, int num) {
		JSONArray valueArray = null;
		if (allValueObject!=null&&allValueObject.has(lotNo)) {
			valueArray = allValueObject.getJSONArray(lotNo);
		} else {
			valueArray = cacheCommonUtil.getWinInfoValueArrayByLotNoAndNum(lotNo, num);
		}
		return valueArray;
	}
	
	/**
	 * 记录用户信息
	 * @param imei
	 * @param imsi
	 * @param machineId
	 * @param platform
	 * @param softwareVersion
	 * @param coopId
	 * @param isEmulator
	 * @param phoneSIM
	 * @param mac
	 */
	public static void addUserInf(String imei, String imsi, String machineId, String platform, String softwareVersion, 
			String coopId, String isEmulator, String phoneSIM, String mac) {
		UserInf userInf = new UserInf();
		userInf.setImei(imei);
		userInf.setImsi(imsi);
		userInf.setCreatetime(new Date());
		userInf.setMachine(machineId);
		userInf.setLastnetconnecttime(new Date());
		userInf.setPlatfrom(platform);
		userInf.setSoftwareversion(softwareVersion);
		userInf.setChannel(coopId);
		userInf.setIsemular(isEmulator);
		userInf.setPhoneSIM(phoneSIM);
		userInf.setMac(mac);
		userInf.persist();
	}
	
	/**
	 * 获得新的渠道号
	 * iPhone力美和点乐广告墙统计购彩、充值时使用
	 * @param imei
	 * @param platform
	 * @param coopId
	 * @return
	 */
	public static String getNewCoopId(String imei, String platform, String coopId) {
		String newCoopId = coopId;
		//iPhone平台
		if (platform!=null&&platform.equals(Platform.iPhone.value())) { 
			StringBuilder builder = new StringBuilder(" where");
			List<Object> params = new ArrayList<Object>();
			
			builder.append(" o.imei=? and");
			params.add(imei);
			
			builder.append(" o.platfrom=?");
			params.add(Platform.iPhone.value());
			List<UserInf> list = UserInf.getList(builder.toString(), "", params);
			if (list!=null&&list.size()>0) {
				UserInf userInf = list.get(0);
				String channel = userInf.getChannel(); //数据库中的渠道号
				if (coopId!=null&&coopId.equals(CoopId.appStore_new.value())) { //积分墙按激活数推广
					if (channel!=null&&(channel.equals(CoopId.limei.value())
							||channel.equals(CoopId.dianjoy.value())
							||channel.equals(CoopId.appStore_old.value())
							||channel.equals(CoopId.appStore_new.value()))) { //力美和点乐广告墙和appStore
						newCoopId = channel;
					}
				} else if (coopId!=null&&coopId.equals(CoopId.appStore_guanFang.value())) { //积分墙按注册数推广
					if (channel!=null&&(channel.equals(CoopId.limei_zhuCe.value())
							||channel.equals(CoopId.dianRu_zhuCe.value())
							||channel.equals(CoopId.duoMeng_zhuCe.value()))) { //力美、点入、多盟积分墙
						newCoopId = channel;
					}
				}
			}
		}
		return newCoopId;
	}
	
	/**
	 * 根据渠道号获得产品编号
	 * @param coopId
	 * @return
	 */
	public String getProductNoByCoopId(String coopId) {
		String productNo = "1"; //如意彩
		try {
			if (!Tools.isEmpty(coopId)) {
				StringBuilder builder = new StringBuilder(" where");
				List<Object> params = new ArrayList<Object>();
				
				builder.append(" o.coopid=?");
				params.add(Integer.parseInt(coopId));
				List<Coop> list = Coop.getList(builder.toString(), "", params);
				if (list!=null&&list.size()>0) {
					Coop coop = list.get(0);
					productNo = coop.getProductno();
				}
			}
		} catch (Exception e) {
			logger.error("根据渠道号获得产品编号发生异常", e);
		}
		return productNo;
	}
	
	/**
	 * 根据键获取消息内容
	 * @param keyStr
	 * @return
	 */
	public String getMessageContentByKeyStr(String keyStr) {
		String content = "";
		
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.keystr=?");
		params.add(keyStr);
		List<Message> list = Message.getList(builder.toString(), "", params);
		if (list!=null&&list.size()>0) {
			Message message = list.get(0);
			content = message.getContent();
		}
		
		return content;
	}
	
}
