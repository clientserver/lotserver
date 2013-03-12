package com.ruyicai.lotserver.util.lot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.dto.BetRequest;
import com.ruyicai.lotserver.dto.OrderRequest;
import com.ruyicai.lotserver.dto.SubscribeRequest;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.service.back.LotteryService;
import com.ruyicai.lotserver.util.CommonUtil;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.parse.BuildRequestsUtil;

/**
 * 投注公共类
 * @author Administrator
 *
 */
@Service
public class BetUtil {
	
	@Autowired
	LotteryService lotteryCommonService;
	
	@Autowired
	private CommonUtil commonUtil;
	
	/**
	 * 构造投注请求
	 * @param clientInfo
	 * @return
	 */
	public OrderRequest buildBetOrderRequest(String userNo, ClientInfo clientInfo) {
		String lotNo = clientInfo.getLotNo(); //彩种编号
		String amount = clientInfo.getAmount(); //单期金额(注数*单注金额*倍数)
		String lotMulti = clientInfo.getLotmulti(); //倍数
		String subscribeInfo = clientInfo.getSubscribeInfo(); //追号信息
		String batchNum = clientInfo.getBatchnum(); //追号期数
		
		//每注的金额，默认是2元，大乐透追加为3元每注
		String newOneAmount = getNewOneAmount(clientInfo);
		//获取期号
		String batchCode = clientInfo.getBatchCode(); //客户端传过来的期号
		String isBetAfterIssue = clientInfo.getIsBetAfterIssue(); //是否可以投当前期以后的期(追号可以不从当前期追,1:是)
		String newBatchCode = getNewBatchCode(lotNo, batchCode, isBetAfterIssue, subscribeInfo);
		//中奖后是否停止追号
		BigDecimal isPrizeEnd = BigDecimal.ZERO;
		String prizeEnd = clientInfo.getPrizeend();
		if (prizeEnd!=null&&prizeEnd.equals("1")) { //中奖后停止追号
			isPrizeEnd = BigDecimal.ONE;
		}
		//彩票信息
		List<BetRequest> betRequests = BuildRequestsUtil.buildBetRequests(clientInfo);
		//追号信息
		BigDecimal betType = new BigDecimal(2); //投注类型为购彩
		int batchNumInt = 1; //期数
		if (!Tools.isEmpty(batchNum)) { 
			batchNumInt = Integer.parseInt(batchNum);
		}
		BigDecimal totalAmt = new BigDecimal(amount).multiply(new BigDecimal(batchNumInt)); //总金额=单期金额*追号期数
		List<SubscribeRequest> subscribeRequests = new ArrayList<SubscribeRequest>(); //追号SubscribeRequest
		if (batchNumInt>1) { //追号
			betType = new BigDecimal(0); //投注类型为追号
			if (Tools.isEmpty(subscribeInfo)) { //如果为空即每期追的倍数相同
				String afterIssue = lotteryCommonService.getAfterIssue(lotNo, newBatchCode, (batchNumInt-1)+"");
				if (!Tools.isEmpty(afterIssue)&&!afterIssue.equals("null")) {
					JSONArray afterIssueArray = JSONArray.fromObject(afterIssue);
					for (int i = 0; i < afterIssueArray.size(); i++) {
						JSONObject afterIssueObject = afterIssueArray.getJSONObject(i);
						JSONObject idObject = afterIssueObject.getJSONObject("id");
						
						SubscribeRequest subscribeRequest = new SubscribeRequest();
						subscribeRequest.setAmt(new BigDecimal(amount));
						subscribeRequest.setBatchcode(idObject.getString("batchcode"));
						subscribeRequest.setLotmulti(new BigDecimal(lotMulti));
						subscribeRequests.add(subscribeRequest);
					}
				}
			} else { //如果不为空即每期追号的倍数不同
				String[] subscribes = subscribeInfo.split("!");
				totalAmt = BigDecimal.ZERO;
				for (String subscribe : subscribes) {
					String[] info = subscribe.split(",");
					SubscribeRequest subscribeRequest = new SubscribeRequest();
					subscribeRequest.setBatchcode(info[0]);
					subscribeRequest.setAmt(new BigDecimal(info[1]));
					totalAmt = totalAmt.add(new BigDecimal(info[1]));
					subscribeRequest.setLotmulti(new BigDecimal(info[2]));
					if (info!=null&&info.length>3) { //收益率追号
						subscribeRequest.setDesc(info[3]);
					}
					subscribeRequests.add(subscribeRequest);
				}
			}
		}
		
		//OrderRequest
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setBuyuserno(userNo); //用户编号
		orderRequest.setUserno(userNo);
		orderRequest.setLotno(lotNo); //彩种
		orderRequest.setBatchcode(newBatchCode); //期号
		orderRequest.setLotmulti(new BigDecimal(lotMulti)); //倍数
		orderRequest.setAmt(totalAmt); //总金额
		orderRequest.setOneamount(new BigDecimal(newOneAmount)); //单注金额
		orderRequest.setPaytype(BigDecimal.ONE); //追号时所有期全部扣款
		orderRequest.setBettype(betType); //投注类型(购彩或追号)
		orderRequest.setPrizeend(isPrizeEnd); //中奖后是否停止追号
		orderRequest.setBetRequests(betRequests); //彩票信息
		orderRequest.setSubscribeRequests(subscribeRequests); //追号信息
		orderRequest.setSubchannel("00092493"); //用户系统
		orderRequest.setDesc(clientInfo.getDescription()); //描述信息(收益率追号时使用,如:全程收益率10%)
		//orderRequest.setChannel(clientInfo.getCoopId()); //渠道编号
		String newCoopId = CommonUtil.getNewCoopId(clientInfo.getImei(), clientInfo.getPlatform(), clientInfo.getCoopId()); //渠道号
		orderRequest.setChannel(newCoopId);
		
		return orderRequest;
	}

	/**
	 * 得到新的期号(投注、赠送、发起合买使用)
	 * @param lotNo
	 * @param batchCode
	 * @return
	 */
	public String getNewBatchCode(String lotNo, String batchCode, String isBetAfterIssue, String subscribeInfo) {
		String newBatchCode = "";
		if (!LotTypeUtil.isJingCaiZQ(lotNo)&&!LotTypeUtil.isJingCaiLQ(lotNo)) { //竞彩没有期号
			if (!Tools.isEmpty(subscribeInfo)) { //收益率追号和追号时每期倍数不同
				String[] subscribes = subscribeInfo.split("!");
				if (subscribes!=null&&subscribes.length>0) {
					String[] info = subscribes[0].split(",");
					newBatchCode = info[0];
				}
			} else {
				if (!Tools.isEmpty(batchCode)) { //客户端传过来的期号不为空
					newBatchCode = batchCode;
				} else { //客户端传过来的期号为空
					//newBatchCode = lotteryCommonService.getCurrentBatchCodeByLotNo(lotNo);
					newBatchCode = commonUtil.getCurrentBatchCodeByLotNo(lotNo);
				}
			}
		}
		return newBatchCode;
	}
	
	/**
	 * 获得单注金额
	 * @param oneAmount 单注金额
	 * @param isSuperAddition 是否追加
	 * @param lotNo 彩种
	 * @param betCode 注码
	 * @param amount 总金额
	 * @param lotmulti 倍数
	 * @return
	 */
	public static String getNewOneAmount(ClientInfo clientInfo) {
		String newOneAmount = "200";
		String oneAmount = clientInfo.getOneAmount();
		String isSuperAddition = clientInfo.getIsSuperaddition();
		String lotNo = clientInfo.getLotNo();
		String betCode = clientInfo.getBetCode();
		String amount = clientInfo.getAmount();
		String lotmulti = clientInfo.getLotmulti();
		String isSellWays = clientInfo.getIsSellWays();
		//解决iphone时时彩投注oneAmount传300的bug
		if (lotNo!=null&&lotNo.equals(LotType.DLT.lotNo())) { //大乐透
			if (!Tools.isEmpty(oneAmount)) { //如果客户端传过来的oneAmount不为空就直接用
				newOneAmount = oneAmount;
			} else if (!Tools.isEmpty(isSuperAddition)&&isSuperAddition.equals("0")) { //如果isSuperAddition不为空,并且为"0"代表追加
				newOneAmount = "300";
			} else if (DaletouUtil.isSuperaddition(betCode, amount, lotmulti, isSellWays)) { //根据注码金额判断是否是追加
				newOneAmount = "300";
			}
		}
		return newOneAmount;
	}
	
	/**
	 * 判断是否是组合的玩法(获取遗漏值时使用)
	 * @param sellway
	 * @return
	 */
	public static boolean isZuHeSellway(String sellway) {
		boolean isZuHeSellway = false;
		if (sellway!=null&&(sellway.equals("T01007MV_2D")||sellway.equals("T01007MV_2Z")
				||sellway.equals("T01007MV_3D")||sellway.equals("T01010MV_R5ZH")
				||sellway.equals("T01010MV_R7ZH")||sellway.equals("T01010MV_R8ZH")
				||sellway.equals("T01010MV_Q3ZH")||sellway.equals("T01012MV_Q3ZH")
				||sellway.equals("T01012MV_R5ZH")||sellway.equals("T01012MV_R7ZH")
				||sellway.equals("T01012MV_R8ZH")||sellway.equals("T01014MV_R5ZH")
				||sellway.equals("T01014MV_R7ZH")||sellway.equals("T01014MV_R8ZH")
				||sellway.equals("T01014MV_Q3ZH"))) { //组合
			isZuHeSellway = true;
		}
		return isZuHeSellway;
	}
	
}
