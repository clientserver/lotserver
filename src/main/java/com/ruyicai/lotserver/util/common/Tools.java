package com.ruyicai.lotserver.util.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.Deflater;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;
import com.ruyicai.lotserver.consts.Constants;

/**
 * 工具类
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("restriction")
public class Tools {

	private static Logger logger = Logger.getLogger(Tools.class);

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
	 * 参数错误
	 * 
	 * @return
	 */
	public static String paramError(String imei) {
		logger.info("参数错误imei:" + imei);
		JSONObject responseJson = new JSONObject();
		responseJson.put(Constants.error_code, "9999");
		responseJson.put("errorCode", "9999");
		responseJson.put(Constants.message, "参数错误");
		return responseJson.toString();
	}

	/**
	 * MD5加密（内部系统使用）
	 * 
	 * @param str
	 * @return
	 */
	public static String EncoderByMd5(String str) {
		String newstr = ""; // 加密后的字符串
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			logger.error("error", e);
		}
		return newstr;
	}

	/**
	 * MD5加密（外部合作使用）
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		String result = "";
		try {
			byte[] btInput = str.getBytes();
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(btInput);
			byte[] md = mdInst.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < md.length; i++) {
				int val = ((int) md[i]) & 0xff;
				if (val < 16) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(val));
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("error", e);
		}
		return result;
	}

	/**
	 * 生成随机数
	 * 
	 * @param num
	 *            位数
	 */
	public static String generateRandomNum(int num) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < num; i++) {
			Random random = new Random();
			int nextInt = random.nextInt(10);
			sBuffer.append(nextInt);
		}
		return sBuffer.toString();
	}

	/**
	 * 替换特殊符号
	 * 
	 * @param string
	 * @return
	 */
	public static String replaceSpecialCharacter(String string) {
		if (string != null) {
			string = string.trim().replaceAll(",", "").replaceAll("\n", "")
					.replaceAll(">", "");
		}
		return string;
	}

	/**
	 * 压缩数据
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];
		Deflater compresser = new Deflater();
		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			logger.error("error", e);
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				logger.error("error", e);
			}
		}
		compresser.end();
		return output;
	}

	/**
	 * base64编码
	 * 
	 * @param src
	 * @param charset
	 * @return
	 */
	public static String base64Encode(byte[] src, String charset) {
		Base64 base64 = new Base64();
		try {
			return new String(base64.encode(src), charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("error", e);
			return null;
		}
	}

	/**
	 * base64解码
	 * 
	 * @param src
	 * @param charset
	 * @return
	 */
	public static byte[] base64Decode(String src, String charset) {
		Base64 base64 = new Base64();
		try {
			return base64.decode(src.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			logger.error("error", e);
			return null;
		}
	}
	
	/**
	 * 按key排序Map
	 * 
	 * @param map
	 * @param reverse
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map sortMapByKey(Map map, final boolean reverse) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (reverse) {
					return -((Comparable) ((Map.Entry) (o1)).getKey())
							.compareTo(((Map.Entry) (o2)).getKey());
				}
				return ((Comparable) ((Map.Entry) (o1)).getKey())
						.compareTo(((Map.Entry) (o2)).getKey());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 按value排序Map
	 * 
	 * @param map
	 * @param reverse
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map sortMapByValue(Map map, final boolean reverse) {
		List list = new LinkedList(map.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (reverse) {
					return -((Comparable) ((Map.Entry) (o1)).getValue())
							.compareTo(((Map.Entry) (o2)).getValue());
				}
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		Map result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 将数据格式成两位小数
	 * 
	 * @param data
	 * @return
	 */
	public static String formatTwoDecimal(String data) {
		DecimalFormat df = new DecimalFormat("0.00");
		if (Tools.isEmpty(data)||data.equals("null")) {
			return "";
		}
		return df.format(Double.parseDouble(data));
	}
	
	/**
	 * 是否是偶数
	 * @param num
	 * @return
	 */
	public static boolean isEvenNumber(int num) {
		boolean isEven = false;
		if (num%2==0) { //是偶数
			isEven = true;
		}
		return isEven;
	}
	
}