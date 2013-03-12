package com.ruyicai.lotserver.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class LotteryAlgorithmUtil {

	private static Logger logger = Logger.getLogger(LotteryAlgorithmUtil.class);

	/**
	 * 将注码转成数组
	 * @param code 注码
	 * @param num (1位还是2位)
	 * @return
	 */
	public static List<String> getStringArrayFromString(String code, Integer num) {
		List<String> list = new ArrayList<String>();
		try {
			int l = code.length();
			int h = l / num;
			int n = 0;
			for (int i = 0; i < h; i++) {
				String ss = code.substring(n, n + num);
				n = n + num;
				list.add(ss);
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
		return list;
	}
	
	/**
	 * 将注码数组转换成带character的字符串
	 * 
	 * @param stringArray
	 * @return
	 */
	/*public static String joinStringArrayWithCharacter(List<String> list, String character) {
		String resultStr = "";
		if (list == null || list.size() == 0) {
			return "";
		} else {
			for (int i = 0; i < list.size(); i++) {
				resultStr += list.get(i) + character;
			}
			if (resultStr.endsWith(character)) {
				resultStr = resultStr.substring(0, resultStr.length() - 1);
			}
			return resultStr;
		}
	}*/

	/**
	 * 将3D注码中的"0"去掉
	 * 
	 * @param str
	 * @return
	 */
	public static String removeZero3D(String str) {
		StringBuffer sBuffer = new StringBuffer();
		int j = 1;
		for (int i = 0; i < str.length() / 2; i++) {
			sBuffer.append(str.substring(j, j + 1));
			j += 2;
		}
		return sBuffer.toString();
	}
	
	/**
	 * 去掉倍数前的0
	 * @param multiple
	 * @return
	 */
	public static String removeZeroMutiple(String multiple) {
		if (multiple.length()==2 && multiple.startsWith("0")) {
			return multiple.substring(1);
		}
		return multiple;
	}
	
	/**
	 * 组合公式
	 * 
	 * @param m
	 * @param n
	 * @return
	 */
	public static long zuhe(int m, int n) {
		long t_a = 0L;
		long total = 1L;
		int temp = n;
		for (int i = 0; i < m; i++) {
			total = total * temp;
			temp--;
		}
		t_a = total / jiec(m);
		return t_a;
	}

	/**
	 * 求阶乘
	 * 
	 * @param a
	 * @return
	 */
	public static long jiec(int a) {
		long t_a = 1L;
		for (long i = 1; i <= a; i++) {
			t_a = t_a * i;
		}
		return t_a;
	}

	/**
	 * 每两个字符加一个空格
	 * 
	 * @param str
	 * @return
	 */
	public static String joinStringWithBlank(String str) {
		StringBuffer sBuffer = new StringBuffer();
		if (str != null && !str.trim().equals("") && str.length() % 2 == 0) {
			int len = str.length() / 2;
			for (int i = 0; i < len; i++) {
				if (i == len) {
					sBuffer.append(str.subSequence(i * 2, i * 2 + 2));
				} else {
					sBuffer.append(str.subSequence(i * 2, i * 2 + 2)).append(" ");
				}
			}
		}
		return sBuffer.toString();
	}

}
