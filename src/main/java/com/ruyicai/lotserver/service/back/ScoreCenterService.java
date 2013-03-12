package com.ruyicai.lotserver.service.back;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruyicai.lotserver.util.common.HttpUtil;
import com.ruyicai.lotserver.util.common.PropertiesUtil;
import com.ruyicai.lotserver.util.common.Tools;

/**
 * 积分的接口
 * @author Administrator
 *
 */
@Service
public class ScoreCenterService {
	
	private static Logger logger = Logger.getLogger(ScoreCenterService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;

	/**
	 * 查询用户积分
	 * 
	 * @param userNo
	 * @return
	 */
	public String findScoreByUserno(String userNo) {
		String url = propertiesUtil.getScoreCenterUrl()+"findScoreByUserno?userno="+userNo;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询用户积分返回:" + result + ",url:" + url);
		return result;
	}
	
	/**
	 * 增加用户积分
	 * 
	 * @param userNo
	 * @param scoreType
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String addTuserinfoScore(String userNo, Integer scoreType, String memo) throws UnsupportedEncodingException {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=" + userNo);
		paramStr.append("&scoreType=" + scoreType);
		if (!Tools.isEmpty(memo)) {
			paramStr.append("&memo=" + URLEncoder.encode(memo, "UTF-8"));
		}

		String url = propertiesUtil.getScoreCenterUrl() + "addTuserinfoScore";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("增加用户积分返回:" + result + ",paramStr:" + paramStr);
		return result;
	}
	
	/**
	 * 查询用户积分详细
	 * 
	 * @param userNo
	 * @param pageIndex
	 * @param maxResult
	 * @return
	 */
	public String findScoreDetailByUserno(String userNo, String pageIndex, String maxResult) {
		String url = propertiesUtil.getScoreCenterUrl()+"findScoreDetailByUserno?userno="+userNo
						+ "&startLine="+Integer.parseInt(pageIndex)*Integer.parseInt(maxResult)
						+ "&endLine=" + maxResult;
		String result = HttpUtil.sendRequestByGet(url, true);
		//logger.info("查询用户积分详细返回:" + result + ",url:" + url);
		return result;
	}
	
	/**
	 * 积分兑换彩金
	 * 
	 * @param userNo
	 * @param score
	 * @return
	 */
	public String transScore2Money(String userNo, String score) {
		StringBuffer paramStr = new StringBuffer();
		paramStr.append("userno=").append(userNo);
		paramStr.append("&score=").append(score);

		String url = propertiesUtil.getScoreCenterUrl() + "transScore2Money";
		String result = HttpUtil.sendRequestByPost(url, paramStr.toString(), true);
		logger.info("积分兑换彩金返回:"+result+",paramStr:"+paramStr.toString());
		return result;
	}
	
}
