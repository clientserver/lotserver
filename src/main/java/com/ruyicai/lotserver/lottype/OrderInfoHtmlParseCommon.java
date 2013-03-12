package com.ruyicai.lotserver.lottype;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import com.ruyicai.lotserver.util.LotteryAlgorithmUtil;

/**
 * 解析OrderInfo(html)公共类
 * @author Administrator
 *
 */
public class OrderInfoHtmlParseCommon {

	/**
	 * 得到解析后的注码(中奖规则不需要按位置)
	 * @param codeString
	 * @param winCodeList
	 * @return
	 */
	public static String getNotPositionalBetCode(String codeString, List<String> winCodeList, int length) {
		StringBuilder builder = new StringBuilder();
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(codeString, length);
		for (String c : codeList) {
			if (winCodeList.contains(c)) { //已中奖
				builder.append("<font color=\"red\">"+c+"</font>").append(",");
			} else {
				builder.append(c).append(",");
			}
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/**
	 * 得到和值解析后的注码
	 * @param codeList
	 * @param winCodeList
	 * @return
	 */
	public static String getHeZhiBetCode(List<String> codeList, List<String> winCodeList) {
		StringBuilder builder = new StringBuilder();
		String total = ""; //开奖号码的和
		for (String string2 : winCodeList) {
			if (total.equals("")) {
				total = string2;
			} else {
				total = (Integer.parseInt(total)+Integer.parseInt(string2))+"";
			}
		}
		for (String c : codeList) {
			if (!total.equals("")&&Integer.parseInt(total)==Integer.parseInt(c)) { //中奖
				builder.append("<font color=\"red\">"+c+"</font>");
			} else {
				builder.append(c);
			}
			builder.append(",");
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/**
	 * 得到单式解析后的注码(中奖规则需要按位置)
	 * 排列五或七星彩使用
	 * @param codeString
	 * @param winCode
	 * @return
	 */
	public static String getPositionalBetCodeDs(String codeString, String winCode) {
		StringBuilder builder = new StringBuilder();
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1);
		for (int i = 0; i < codeList.size(); i++) {
			String c = codeList.get(i);
			String win = winCode.substring(i, i+1);
			if (c.equals(win)) { //中奖
				builder.append("<font color=\"red\">"+c+"</font>").append(",");
			} else {
				builder.append(c).append(",");
			}
		}
		if (builder.toString().endsWith(",")) {
			builder = builder.delete(builder.length()-1, builder.length());
		}
		return builder.toString();
	}
	
	/**
	 * 得到复式解析后的注码(中奖规则需要按位置)
	 * 排列五或七星彩或时时彩使用
	 * @param codeString
	 * @param win
	 * @return
	 */
	public static String getPositionalBetCodeFs(String codeString, String win) {
		StringBuilder builder = new StringBuilder();
		List<String> codeList = LotteryAlgorithmUtil.getStringArrayFromString(codeString, 1);
		for (String c : codeList) {
			if (c.equals(win)) { //中奖
				builder.append("<font color=\"red\">"+c+"</font>");
			} else {
				builder.append(c);
			}
		}
		return builder.toString();
	}
	
	/**
	 * 得到时时彩大小单双的中奖号码列表
	 * @param winCode
	 * @return
	 */
	public static List<String> getSscDxDsWinCodeList(String winCode) {
		List<String> list = new ArrayList<String>();
		
		String[] xiaoArray = new String[]{"0", "1", "2", "3", "4"}; //小号
		String[] daArray = new String[]{"5", "6", "7", "8", "9"}; //大号
		String[] danArray = new String[]{"1", "3", "5", "7", "9"}; //单号
		String[] shuangArray = new String[]{"0", "2", "4", "6", "8"}; //双号
		if (ArrayUtils.contains(xiaoArray, winCode)) {
			list.add("1");
		}
		if (ArrayUtils.contains(daArray, winCode)) {
			list.add("2");
		}
		if (ArrayUtils.contains(danArray, winCode)) {
			list.add("5");
		}
		if (ArrayUtils.contains(shuangArray, winCode)) {
			list.add("4");
		}
		return list;
	}
	
	/**
	 * 得到直选前二解析后的注码
	 * @param qianCode
	 * @param houCode
	 * @param winCode
	 * @return
	 */
	public static String getZhiXuanQianErBetCode(String qianCode, String houCode, String winCode) {
		String bet_code = "";
		List<String> qianWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 2), 2);
		String qian = getNotPositionalBetCode(qianCode, qianWinCodeList, 2);
		
		List<String> houWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 4), 2);
		String hou = getNotPositionalBetCode(houCode, houWinCodeList, 2);
		bet_code = qian+"|"+hou;
		return bet_code;
	}
	
	/**
	 * 得到直选前三解析后的注码
	 * @param qianCode
	 * @param zhongCode
	 * @param houCode
	 * @param winCode
	 * @return
	 */
	public static String getZhiXuanQianSanBetCode(String qianCode, String zhongCode, String houCode, String winCode) {
		String bet_code = "";
		List<String> qianWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(0, 2), 2);
		String qian = getNotPositionalBetCode(qianCode, qianWinCodeList, 2);
		
		List<String> zhongWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(2, 4), 2);
		String zhong = getNotPositionalBetCode(zhongCode, zhongWinCodeList, 2);
		
		List<String> houWinCodeList = LotteryAlgorithmUtil.getStringArrayFromString(winCode.substring(4, 6), 2);
		String hou = getNotPositionalBetCode(houCode, houWinCodeList, 2);
		bet_code = qian+"|"+zhong+"|"+hou;
		return bet_code;
	}
	
}
