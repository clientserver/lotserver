package com.ruyicai.lotserver.util;

import java.util.ArrayList;
import java.util.List;
import com.ruyicai.lotserver.domain.Coop;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 渠道号判断
 * @author Administrator
 *
 */
public class ChannelUtil {

	/**
	 * 判断是否91渠道
	 * @param coopid
	 * @return
	 */
	public static boolean is91Channel(String coopId) {
		boolean is91Channel = false;
		if (!Tools.isEmpty(coopId)) {
			is91Channel = isChannel(coopId, "2");
		}
		return is91Channel;
	}
	
	/**
	 * 判断是否是苏宁的渠道
	 * @param coopId
	 * @return
	 */
	public static boolean isSuNingChannel(String coopId) {
		boolean isSuNingChannel = false;
		if (!Tools.isEmpty(coopId)) {
			isSuNingChannel = isChannel(coopId, "4");
		}
		return isSuNingChannel;
	}
	
	/**
	 * 根据产品编号获取渠道列表
	 * @param productNo
	 * @return
	 */
	public static List<Coop> getCoopListByProductNo(String productNo) {
		StringBuilder builder = new StringBuilder(" where");
		List<Object> params = new ArrayList<Object>();
		
		builder.append(" o.productno=?");
		params.add(productNo);
		List<Coop> list = Coop.getList(builder.toString(), "", params);
		return list;
	}
	
	/**
	 * 判断渠道号是否属于某产品
	 * @param coopId
	 * @param productNo
	 * @return
	 */
	public static boolean isChannel(String coopId, String productNo) {
		boolean isChannel = false;
		List<Coop> list = getCoopListByProductNo(productNo);
		if (list!=null&&list.size()>0) {
			for (Coop coop : list) {
				Integer coopIdInt = coop.getCoopid();
				if (coopId.equals(coopIdInt.toString())) {
					isChannel = true;
					break;
				}
			}
		}
		return isChannel;
	}
	
}
