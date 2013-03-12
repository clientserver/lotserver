<%@ page pageEncoding="UTF-8" %>
<%@page import="net.sf.json.*, com.ruyicai.lotserver.util.common.*, com.ruyicai.lotserver.util.parse.*, com.ruyicai.lotserver.consts.*" %>
<html>
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title>投注确认</title>
</head>
<body style="font-size: 200%">
	<%
		String jsonString = request.getParameter("jsonString");
			String deCrypt = ToolsAesCrypt.Decrypt(jsonString, Constants.key); //解密
			
			JSONObject requestObject = JSONObject.fromObject(deCrypt);
			String amount = requestObject.getString("amount");
			Integer amountInt = Integer.parseInt(amount);
			String bet_code = requestObject.getString("bet_code");
			String lotNo = requestObject.getString("lotno");
			String lotMulti = requestObject.getString("lotmulti");
			String batchNum = requestObject.getString("batchnum");
			String isSellWays = requestObject.getString("isSellWays");
			//解析注码
			JSONArray parseBetCodes = IphoneBetCodeParseUtil.parseBetCode(lotNo, bet_code, isSellWays);
			JSONObject parseBetCode = parseBetCodes.getJSONObject(0);
			StringBuffer sBuffer = new StringBuffer();
			for(int i=0; i<parseBetCodes.size(); i++) {
		JSONObject parseBetCodeJson = parseBetCodes.getJSONObject(i);
		sBuffer.append(parseBetCodeJson.getString("betCode")).append("<br/>");
			}
	%>
	<div>
		<div>
			<div>首页&gt;&gt;投注确认</div>
		</div>
		<br/>
		彩种:<%=parseBetCode.getString("lotName") %><br/>
		倍数:<%=lotMulti %><br/>
		投注金额:<%=amountInt/100 %>元<br/>
		追号期数:<%=batchNum %><br/>
		注码内容:<br/>
		<%=sBuffer.toString() %><br/>
		
		<form action="<%=request.getContextPath()%>/iphoneWapBet" method="post">
			<input type="hidden" name="jsonString" value="<%=jsonString %>"></input>
			<input type="submit" value="确认" style="font-size: 150%"></input>
		</form>
	
	<div>
		提示:您已经进入WAP页面,确认购买之后双击主屏幕按钮("Home"键)，切换到"如意彩"。<br/>
		如有疑问请致电客服热线:<br/>
		客服热线:4006651000<br/>
		客服QQ:1427872305<br/>
	</div>
	</div>
</body>
</html>