package com.ruyicai.lotserver.util.common;

import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * 字符串工具类
 * @author Administrator
 *
 */
public class StringUtil {

	/**
	 * 验证参数是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String string) {
		if (StringUtils.isEmpty(string)) {
			return true;
		}
		if (string.trim().equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 去掉字符串结尾的字符
	 * @param string
	 * @param endCharacter
	 * @return
	 */
	public static String removeEndCharacter(String string, String endCharacter) {
		if (!isEmpty(string)&&string.endsWith(endCharacter)) {
			string = string.substring(0, string.length()-endCharacter.length());
		}
		return string;
	}
	
	/**
	 * 将字符串数组用符合连接
	 * @param list
	 * @param character
	 * @return
	 */
	public static String joinStringArrayWithCharacter(List<String> list, String character) {
		StringBuilder builder = new StringBuilder();
		if (list!=null&&list.size()>0) {
			for (String string : list) {
				builder.append(string).append(character);
			}
		}
		return removeEndCharacter(builder.toString(), character);
	}
	
}
