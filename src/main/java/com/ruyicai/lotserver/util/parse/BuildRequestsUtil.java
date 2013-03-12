package com.ruyicai.lotserver.util.parse;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.dto.BetRequest;
import com.ruyicai.lotserver.dto.SubscribeRequest;
import com.ruyicai.lotserver.protocol.ClientInfo;
import com.ruyicai.lotserver.util.common.Tools;
import com.ruyicai.lotserver.util.lot.LotTypeUtil;

/**
 * 构造投注请求串
 * @author Administrator
 *
 */
public class BuildRequestsUtil {

	/**
	 * 构造彩票请求（投注、赠送、发起合买使用）
	 * @param clientInfo
	 * @return
	 */
	public static List<BetRequest> buildBetRequests(ClientInfo clientInfo) {
		List<BetRequest> betRequests = new ArrayList<BetRequest>(); //彩票信息
		String betCode = clientInfo.getBetCode(); //注码
		String isSellWays = clientInfo.getIsSellWays(); //是否多玩法
		
		if (!Tools.isEmpty(isSellWays)&&isSellWays.trim().equals("1")) { //多玩法
			//0001051315182125~02^_1_200_200!0001051315182125~02^_1_200_200 注码_倍数_单注的金额_总金额
			if (!Tools.isEmpty(betCode)) { //判断注码是否为空
				String[] singleCodeStrings = betCode.split("!");
				for (String singleCodeString : singleCodeStrings) {
					String[] strings = singleCodeString.split("_");
					BetRequest betRequest = new BetRequest();
					betRequest.setBetcode(strings[0]); //注码
					betRequest.setAmt(new BigDecimal(Integer.parseInt(strings[3]))); //金额
					betRequests.add(betRequest);
				}
			}
		} else { //单玩法
			String lotmulti = clientInfo.getLotmulti(); //倍数
			String lotNo = clientInfo.getLotNo(); //彩种编号
			if (lotNo!=null&&(lotNo.equals(LotType.QLC.lotNo())||lotNo.equals(LotType.SSQ.lotNo()))) { //双色球、七乐彩
				String[] split = betCode.split("\\^");
				for (String code : split) {
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/(Integer.parseInt(clientInfo.getLotmulti())*split.length)));
					if (!Tools.isEmpty(lotmulti)&&Integer.parseInt(lotmulti)>=1&&Integer.parseInt(lotmulti)<=99) { //倍数1~99
						String newString  = code.substring(0, 2)+"01"+code.substring(4)+"^";
						betRequest.setBetcode(newString);
					} else { //倍数超过99倍,注码中的倍数统一传01倍
						betRequest.setBetcode(code+"^");
					}
					betRequests.add(betRequest);
				}
			} else if (lotNo!=null&&lotNo.equals(LotType.DDD.lotNo())) { //福彩3D
				if (betCode.substring(0, 2).equals("20")) { //直选投注 注码格式为20010105^0106^0107,所以要特殊处理
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/Integer.parseInt(clientInfo.getLotmulti())));
					if (!Tools.isEmpty(lotmulti)&&Integer.parseInt(lotmulti)>=1&&Integer.parseInt(lotmulti)<=99) { //倍数1~99
						String newString  = betCode.substring(0, 2)+"01"+betCode.substring(4);
						betRequest.setBetcode(newString);
					} else { //倍数超过99倍,注码中的倍数统一传01倍
						betRequest.setBetcode(betCode);
					}
					betRequests.add(betRequest);
				} else if (betCode.substring(0, 2).equals("12")) { //组6和值(如果注码小于10客户端传过来的注码是1位前面没有加0，所以在此处理)
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/Integer.parseInt(clientInfo.getLotmulti())));
					if (betCode.substring(4, betCode.indexOf("^")).length()==1) {
						String newString  = betCode.substring(0, 2)+"01"+"0"+betCode.substring(4);
						betRequest.setBetcode(newString);
					} else {
						if (!Tools.isEmpty(lotmulti)&&Integer.parseInt(lotmulti)>=1&&Integer.parseInt(lotmulti)<=99) { //倍数1~99
							String newString  = betCode.substring(0, 2)+"01"+betCode.substring(4);
							betRequest.setBetcode(newString);
						} else { //倍数超过99倍,注码中的倍数统一传01倍
							betRequest.setBetcode(betCode);
						}
					}
					betRequests.add(betRequest);
				} else if (betCode.substring(0, 2).equals("10")) { //直选和值 (如果和值为10客户端传过来的注码多加了0，所以在此处理)
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/Integer.parseInt(clientInfo.getLotmulti())));
					if (betCode.substring(4, betCode.indexOf("^")).length()==3) {
						String newString  = betCode.substring(0, 2)+"01"+betCode.substring(5);
						betRequest.setBetcode(newString);
					} else {
						if (!Tools.isEmpty(lotmulti)&&Integer.parseInt(lotmulti)>=1&&Integer.parseInt(lotmulti)<=99) { //倍数1~99
							String newString  = betCode.substring(0, 2)+"01"+betCode.substring(4);
							betRequest.setBetcode(newString);
						} else {
							betRequest.setBetcode(betCode);
						}
					}
					betRequests.add(betRequest);
				} else {
					String[] split = betCode.split("\\^");
					for (String code : split) {
						BetRequest betRequest = new BetRequest();
						betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/(Integer.parseInt(clientInfo.getLotmulti())*split.length)));
						if (!Tools.isEmpty(lotmulti)&&Integer.parseInt(lotmulti)>=1&&Integer.parseInt(lotmulti)<=99) { //倍数1~99
							String newString  = code.substring(0, 2)+"01"+code.substring(4)+"^";
							betRequest.setBetcode(newString);
						} else { //倍数超过99倍,注码中的倍数统一传01倍
							betRequest.setBetcode(code+"^");
						}
						betRequests.add(betRequest);
					}
				}
			} else if (lotNo!=null&&(LotTypeUtil.isJingCaiZQ(lotNo)||LotTypeUtil.isJingCaiLQ(lotNo))) { //竞彩
				BetRequest betRequest = new BetRequest();
				betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/Integer.parseInt(clientInfo.getLotmulti()))); //金额
				betRequest.setBetcode(betCode); //注码
				betRequests.add(betRequest);
			} else if (lotNo!=null&&(lotNo.equals(LotType.SSC.lotNo())||lotNo.equals(LotType.DLT.lotNo()))) { //时时彩,大乐透
				String[] split = betCode.split(";");
				for (String code : split) {
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/(Integer.parseInt(clientInfo.getLotmulti())*split.length)));
					betRequest.setBetcode(code);
					betRequests.add(betRequest);
				}
			} else if (lotNo!=null&&(lotNo.equals(LotType.OO_YDJ.lotNo())||lotNo.equals(LotType.TT_F.lotNo()))) { //十一运夺金,22选5
				String[] split = betCode.split("\\^");
				for (String code : split) {
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/(Integer.parseInt(clientInfo.getLotmulti())*split.length)));
					betRequest.setBetcode(code+"^");
					betRequests.add(betRequest);
				}
			} else {
				String[] split = betCode.split("\\^");
				for (String code : split) {
					BetRequest betRequest = new BetRequest();
					betRequest.setAmt(new BigDecimal(Integer.parseInt(clientInfo.getAmount())/(Integer.parseInt(clientInfo.getLotmulti())*split.length)));
					betRequest.setBetcode(code);
					betRequests.add(betRequest);
				}
			}
		}
		return betRequests;
	}
	
	/**
	 * 构造追号请求
	 * @param subscribeRequests
	 * @param subscribeInfo
	 * @param totalAmt
	 * @throws UnsupportedEncodingException
	 */
	public static BigDecimal buildSubscribeRequests(List<SubscribeRequest> subscribeRequests, String subscribeInfo) throws UnsupportedEncodingException {
		BigDecimal totalAmt = BigDecimal.ZERO;
		String[] subscribes = subscribeInfo.split("!");
		for (String subscribe : subscribes) {
			String[] info = subscribe.split(",");
			SubscribeRequest subscribeRequest = new SubscribeRequest();
			subscribeRequest.setBatchcode(info[0]);
			subscribeRequest.setAmt(new BigDecimal(info[1]));
			totalAmt = totalAmt.add(new BigDecimal(info[1]));
			subscribeRequest.setLotmulti(new BigDecimal(info[2]));
			if (info!=null&&info.length>3) { //收益率追号
				subscribeRequest.setDesc(URLEncoder.encode(info[3], "UTF-8"));
			}
			subscribeRequests.add(subscribeRequest);
		}
		return totalAmt;
	}
	
}
