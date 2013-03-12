package com.ruyicai.lotserver.util;

import com.ruyicai.lotserver.consts.LotType;
import com.ruyicai.lotserver.consts.ScoreType;

/**
 * 描述信息公共类
 * @author Administrator
 *
 */
public class MemoUtil {

	/**
	 * 根据lotNo获得lotName
	 * @param lotNo
	 * @return
	 */
	public static String getLotNameByLotNo(String lotNo) {
		String lotName = "未知";
		if (lotNo!=null) {
			LotType[] values = LotType.values();
			for (LotType lotType : values) {
				if (lotNo.equals(lotType.lotNo())) {
					lotName = lotType.lotName();
					break;
				}
			}
		}
		return lotName;
	}
	
	/**
	 * 获得竟彩足球即时比分状态描述
	 * @param matchState
	 * @return
	 */
	public static String getJczImmediateScoreStateMemo(String matchState) {
		//（0:未开）未开,（1:上半场,2:中场,3:下半场,-11:待定,-12:腰斩,-13:中断,-14:推迟）比赛中,(-1:完场，-10取消) 完场
		String stateMemo = ""; //状态描述
		if (matchState!=null&&matchState.equals("0")) {
			stateMemo = "未开赛";
		} else if (matchState!=null&&(matchState.equals("-1")||matchState.equals("-10"))) {
			stateMemo = "已完场";
		} else {
			stateMemo = "进行中";
		}
		return stateMemo;
	}
	
	/**
	 * 获得竟彩篮球即时比分状态描述
	 * @param matchState
	 * @return
	 */
	public static String getJclImmediateScoreStateMemo(String matchState) {
		//(0:未开) 未开,(1:一节,2:二节,3:三节,4:四节,-2:待定,-3:中断,-5:推迟) 比赛中,(-1:完场,-4:取消) 完场
		String stateMemo = ""; //状态描述
		if (matchState!=null&&matchState.equals("0")) {
			stateMemo = "未开赛";
		} else if (matchState!=null&&(matchState.equals("-1")||matchState.equals("-4"))) {
			stateMemo = "已完场";
		} else {
			stateMemo = "进行中";
		}
		return stateMemo;
	}
	
	/**
	 * 获得订单状态描述
	 * @param orderState
	 * @return
	 */
	public static String getOrderStateMemo(String orderState) {
		String orderStateMemo = "";
		if (orderState!=null&&orderState.equals("-1")) {
			orderStateMemo = "失败";
		} else if (orderState!=null&&orderState.equals("0")) {
			orderStateMemo = "等待处理";
		} else if (orderState!=null&&orderState.equals("1")) {
			orderStateMemo = "已出票";
		} else if (orderState!=null&&orderState.equals("2")) {
			orderStateMemo = "空订单";
		} else if (orderState!=null&&orderState.equals("3")) {
			orderStateMemo = "撤销";
		} else if (orderState!=null&&orderState.equals("4")) {
			orderStateMemo = "流单";
		}
		return orderStateMemo;
	}
	
	/**
	 * 根据积分类型获取积分来源
	 * @param scoreType
	 * @return
	 */
	public static String getScoreSourceByScoreType(String type) {
		String scoreSource = "未知";
		if (type!=null) {
			ScoreType[] values = ScoreType.values();
			for (ScoreType scoreType : values) {
				if (type.equals(scoreType.value())) {
					scoreSource = scoreType.memo();
					break;
				}
			}
		}
		return scoreSource;
	}
	
	/**
	 * 获取时时彩大小单双的描述
	 * @param code
	 * @return
	 */
	public static String getSscDXDSMemo(String code) {
		String memo = "";
		if (code!=null) {
			if (code.equals("2")) {
				memo = "大";
			} else if (code.equals("1")) {
				memo = "小";
			} else if (code.equals("5")) {
				memo = "单";
			} else if (code.equals("4")) {
				memo = "双";
			}
		}
		return memo;
	}
	
	/**
	 * 获取广东十一选五的玩法
	 * @param smallType
	 * @return
	 */
	public static String getT01014Play(String smallType) {
		String play = "";
		if (smallType!=null&&smallType.equals("R1")) {
			play = "任选一";
		} else if (smallType!=null&&smallType.equals("R2")) {
			play = "任选二";
		} else if (smallType!=null&&smallType.equals("R3")) {
			play = "任选三";
		} else if (smallType!=null&&smallType.equals("R4")) {
			play = "任选四";
		} else if (smallType!=null&&smallType.equals("R5")) {
			play = "任选五";
		} else if (smallType!=null&&smallType.equals("R6")) {
			play = "任选六";
		} else if (smallType!=null&&smallType.equals("R7")) {
			play = "任选七";
		} else if (smallType!=null&&smallType.equals("R8")) {
			play = "任选八";
		} else if (smallType!=null&&smallType.equals("Q2")) {
			play = "直选前二";
		} else if (smallType!=null&&smallType.equals("Q3")) {
			play = "直选前三";
		} else if (smallType!=null&&smallType.equals("Z2")) {
			play = "组选前二";
		} else if (smallType!=null&&smallType.equals("Z3")) {
			play = "组选前三";
		}
		return play;
	}
	
	/**
	 * 获取广东快乐十分的玩法
	 * @param smallType
	 * @return
	 */
	public static String getT01015Play(String smallType) {
		String play = "";
		if (smallType!=null&&smallType.equals("S1")) {
			play = "选一数投";
		} else if (smallType!=null&&smallType.equals("H1")) {
			play = "选一红投";
		} else if (smallType!=null&&smallType.equals("R2")) {
			play = "任选二";
		} else if (smallType!=null&&smallType.equals("R3")) {
			play = "任选三";
		} else if (smallType!=null&&smallType.equals("R4")) {
			play = "任选四";
		} else if (smallType!=null&&smallType.equals("R5")) {
			play = "任选五";
		} else if (smallType!=null&&smallType.equals("Q2")) {
			play = "选二连直";
		} else if (smallType!=null&&smallType.equals("Q3")) {
			play = "直选前三";
		} else if (smallType!=null&&smallType.equals("Z2")) {
			play = "选二连组";
		} else if (smallType!=null&&smallType.equals("Z3")) {
			play = "组选前三";
		}
		return play;
	}
	
}
