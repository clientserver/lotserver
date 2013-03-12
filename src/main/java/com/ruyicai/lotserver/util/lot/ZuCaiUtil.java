package com.ruyicai.lotserver.util.lot;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.util.CacheCommonUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 足彩公共类
 * @author Administrator
 *
 */
@Service
public class ZuCaiUtil {

	@Autowired
	private CacheCommonUtil cacheCommonUtil;
	
	/**
	 * 获得赛事信息
	 * @param lotNo
	 * @param batchCode
	 * @param teamId
	 * @return
	 */
	public JSONObject getMatchInfo(String lotNo, String batchCode, String teamId) {
		JSONObject resultObject = new JSONObject();
		String homeTeam = ""; //主队
		String guestTeam = ""; //客队
		JSONObject valueObject = cacheCommonUtil.getZuCaiMatchInfoValueObject(lotNo, batchCode, teamId);
		if (valueObject!=null) {
			homeTeam = valueObject.getString("homeTeam");
			guestTeam = valueObject.getString("guestTeam");
		}
		resultObject.put("homeTeam", homeTeam);
		resultObject.put("guestTeam", guestTeam);
		return resultObject;
	}
	
	/**
	 * 判断注码是否是单式
	 * @param code
	 * @return
	 */
	public static boolean isDanShiBetCode(String code) {
		boolean isDanShi = true;
		if (code.indexOf(",") > -1) {
			String[] split2 = code.split(",");
			for (int i = 0; i < split2.length; i++) {
				if (split2[i].length() > 1) {
					isDanShi = false;
					break;
				}
			}
		}
		return isDanShi;
	}
	
	/**
	 * 排序注码数组(胆拖使用)
	 * @param resultArray
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void sortBetCodeArray(JSONArray resultArray) {
		if (resultArray!=null&&resultArray.size()>0) {
			Collections.sort(resultArray, new Comparator(){
				public int compare(Object o1, Object o2) {
					JSONObject object1 = (JSONObject)o1;
					String teamId1 = object1.getString("teamId");
					JSONObject object2 = (JSONObject)o2;
					String teamId2 = object2.getString("teamId");
					return Integer.parseInt(teamId1)-Integer.parseInt(teamId2);
				}
			});
		}
	}
	
	/**
	 * 根据Map得到注码数组
	 * @param tempMap
	 * @return
	 */
	public static JSONArray getCodeArrayByMap(Map<String, Map<String, String>> tempMap) {
		JSONArray resultArray = new JSONArray();
		for(Entry<String, Map<String, String>> entry : tempMap.entrySet()) {
			JSONObject object = new JSONObject();
			String teamId = entry.getKey();
			Map<String, String> value = entry.getValue();
			object.put("teamId", teamId);
			for(Entry<String, String> entry2 : value.entrySet()) {
				object.put(entry2.getKey(), entry2.getValue());
			}
			resultArray.add(object);
		}
		//根据teamId进行排序
		ZuCaiUtil.sortBetCodeArray(resultArray);
		return resultArray;
	}
	
	/**
	 * 获得进球彩或半全场的场次
	 * @param i
	 * @return
	 */
	public static String getJqcOrBqcTeamId(boolean isEven, int i) {
		String teamId = "";
		if (isEven) { //是偶数
			teamId = (i/2+1)+"";
		} else { //是奇数
			teamId = ((i-1)/2+1)+"";
		}
		return teamId;
	}
	
}
