<%@ page contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<base href="<%=basePath%>">
<title>HttpSender</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=request.getContextPath()%>/css/battle-1.css"
	rel="stylesheet" type="text/css" />
<link href="<%=request.getContextPath()%>/css/battle-2.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Cache-Control" content="no-cache" />

<link type="text/css"
	href="<%=request.getContextPath()%>/css/themes/base/ui.all.css"
	rel="stylesheet" />
<script src="<%=request.getContextPath()%>/js/jquery-1.3.2.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ui/ui.core.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ui/ui.draggable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ui/ui.resizable.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/ui/ui.dialog.js"></script>
</head>
<body bgColor=#000000 leftMargin=0 topMargin=0 marginwidth="0"
	marginheight="0">

	<form action="<%=request.getContextPath()%>/result.jsp" id="theform"
		target="rightFrame">
		<h3 align="center">Ruyicai Request Sender 3.0</h3>
		<table width="1200" border="0" cellpadding="0" cellspacing="0"
			align="center" bordercolor="#ffffff">
			<tr height="30px">
				<td colspan="1" class="rankingHeader" width="200px">地址</td>
				<td width="1000px"><input
					value="http://127.0.0.1:8088/lotserver/RuyicaiServlet"
					style="width: 890px;" type="text" id="urlAddress" name="urlAddress" />
				</td>
			</tr>

			<tr>
				<td align="center" width="50%" colspan=""><select
					id="commandselect" onchange="chengeCommand()">
						<option value="1">软件升级</option>
						<option value="2">登录</option>
						<option value="3">注册</option>
						<option value="4">投注/追号</option>
						<option value="5">赠送</option>
						<option value="6">发起合买</option>
						<option value="7">参与合买</option>
						<option value="8">投注查询</option>
						<option value="9">追号查询</option>
						<option value="10">中奖查询</option>
						<option value="11">合买大厅查询</option>
						<option value="12">参与合买查询</option>
						<option value="13">合买详情查询</option>
						<option value="14">赠送查询</option>
						<option value="15">被赠送查询</option>
						<option value="16">余额查询</option>
						<option value="17">账户明细查询</option>
						<option value="18">DNA绑定查询</option>
						<option value="19">DNA充值</option>
						<option value="20">手机卡充值</option>
						<option value="21">手机银行充值</option>
						<option value="22">游戏点卡充值</option>
						<option value="23">支付宝充值</option>
						<option value="24">银联在线支付</option>
						<option value="25">支付宝安全支付</option>
						<option value="26">银行充值</option>
						<option value="27">提现</option>
						<option value="28">提现记录查询</option>
						<option value="29">取消提现</option>
						<option value="30">取消追号</option>
						<option value="31">合买撤单</option>
						<option value="32">合买撤资</option>
						<option value="33">用户中心</option>
						<option value="34">修改密码</option>
						<option value="35">找回密码</option>
						<option value="36">设置昵称</option>
						<option value="37">绑定身份证</option>
						<option value="38">绑定手机获取验证码</option>
						<option value="39">绑定手机</option>
						<option value="40">解绑手机</option>
						<option value="41">取消自动登录</option>
						<option value="42">积分明细</option>
						<option value="43">消息设置</option>
						<option value="44">消息设置查询</option>
						<option value="45">用户反馈</option>
						<option value="46">用户反馈查询</option>
						<option value="47">开奖公告</option>
						<option value="48">开奖公告(走势图)</option>
						<option value="49">历史开奖</option>
						<option value="50">开奖公告明细</option>
						<option value="51">遗漏值查询</option>
						<option value="52">合买参与人查询</option>
						<option value="53">足彩预售期号查询</option>
						<option value="54">领取彩票验证码</option>
						<option value="55">领取赠送彩票</option>
						<option value="56">积分兑换彩金</option>
						<option value="57">中奖排行</option>
						<option value="58">竞彩对阵查询</option>
						<option value="59">竞彩对阵查询(日期)</option>
						<option value="60">获取以后的期号</option>
						<option value="61">追号详情查询</option>
						<option value="62">Top新闻</option>
						<option value="63">新闻标题</option>
						<option value="64">新闻内容</option>
						<option value="65">活动标题</option>
						<option value="66">活动内容</option>
						<option value="67">帮助中心标题</option>
						<option value="68">帮助中心内容</option>
						<option value="69">专家荐号</option>
						<option value="70">增加积分</option>
						<option value="71">版本介绍查询</option>
						<option value="72">玩法介绍查询</option>
						<option value="73">异常信息提交</option>
						<option value="74">收益率追号计算</option>
						<option value="75">竞彩赛果查询</option>
						<option value="76">积分规则</option>
						<option value="77">查询键的内容</option>
						<option value="78">竞彩足球数据分析</option>
						<option value="79">竞彩足球即时比分列表</option>
						<option value="80">竞彩足球即时比分明细</option>
						<option value="81">当前期号查询</option>
						<option value="82">代理充值</option>
						<option value="83">购买大厅</option>
						<option value="84">联合登录</option>
						<option value="85">拉卡拉充值</option>
						<option value="86">赠送短信查询</option>
						<option value="87">兑换彩金需积分数</option>
						<option value="88">保存订单</option>
						<option value="89">创建自动跟单</option>
						<option value="90">查询定制跟单</option>
						<option value="91">取消自动跟单</option>
						<option value="92">查询合买发起人信息</option>
						<option value="93">更新自动跟单</option>
						<option value="94">遗漏值列表</option>
						<option value="95">竞彩篮球数据分析</option>
						<option value="96">竞彩篮球即时比分列表</option>
						<option value="97">竞彩篮球即时比分明细</option>
						<option value="98">站内信列表</option>
						<option value="99">足彩对阵查询</option>
						<option value="100">足彩数据分析</option>
						<option value="101">足彩即时比分列表</option>
						<option value="102">足彩即时比分明细</option>
						<option value="103">站内信更新已读</option>
						<option value="104">留言更新已读</option>
						<option value="105">留言未读数量</option>
						<option value="106">新投注列表查询</option>
						<option value="107">注码解析查询</option>
						<option value="108">新中奖列表查询</option>
						<option value="109">新参与合买列表查询</option>
						<option value="110">新合买详情查询</option>
						<option value="111">iPhone保存token</option>
				</select> <input style="width: 100px;" type="button" onclick="sendRequest()"
					value="发送请求" /> <span style="width: 200px;"></span></td>
			</tr>
		</table>
		<table width="1200" border="0" cellpadding="0" cellspacing="0"
			align="center" bordercolor="#ffffff">
			<tr height="30px">
				<td width="50%" class="rankingHeader">发送的报文内容</td>
				<td width="50%" class="rankingHeader">接收的报文内容</td>
			</tr>
			<tr>
				<td width="50%"><textarea name="content" id="content" rows="28"
						style="width: 100%; scrollbar-Base-Color: 282724; scrollbar-Arrow-Color: 666666; scrollbar-3dLight-Color: 161616; scrollbar-DarkShadow-Color: black; scrollbar-Highlight-Color: 666666; scrollbar-Shadow-Color: 383838; color: #00FF33">{"command":"softwareupdate"}</textarea>
				</td>
				<td width="50%"><textarea name="recieved" id="recieved"
						rows="28"
						style="width: 100%; scrollbar-Base-Color: 282724; scrollbar-Arrow-Color: 666666; scrollbar-3dLight-Color: 161616; scrollbar-DarkShadow-Color: black; scrollbar-Highlight-Color: 666666; scrollbar-Shadow-Color: 383838; color: #00FF33"></textarea>
				</td>
			</tr>
		</table>
		<input type="hidden" id="operation" name="operation" />
	</form>
	<div id="addCommandDialog" title="添加命令" style="display: none;">
		<table>
			<tr>
				<td colspan="2">添加前请首先选择产品版本号</td>
			</tr>
			<tr>
				<td><strong>命令名称</strong></td>
				<td><input type="text" id="commandName"
					style="background: white;" /></td>
			</tr>
			<tr>
				<td><strong>提交内容</strong></td>
				<td><textarea id="myCommandContent" cols="50" rows="6"
						style="background: white;"></textarea></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
					<button onclick="ajaxRequestToAddCommand()">提交</button></td>
			</tr>
		</table>
	</div>
	<div id="manage" style="display: none">
		<table id="manageTable" title="命令管理">
			<tr>
				<td>命令名称</td>
				<td>操作</td>
			</tr>
		</table>
	</div>
</body>
<script>
	  function chengeCommand() {
	      var command = document.getElementById("commandselect");
	      if(command.value == "1") {
	          document.getElementById("content").innerHTML = "{\"command\":\"softwareupdate\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"randomNumber\":\"bfb50322-eb27-479b-a1c0-47e6877c41c7\"}";
	      } else if (command.value == "2") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"login\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"password\":\"123456\",\r\n\"isAutoLogin\":\"0\"}";
	      } else if (command.value == "3") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"register\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"18701690748\",\r\n\"password\":\"123456\",\r\n\"name\":\"r\",\r\n\"certid\":\"130123198709151873\"}";
	      } else if (command.value == "4") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"bet\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bet_code\":\"0001121315212233~12^\",\r\n\"batchnum\":\"1\",\r\n\"batchcode\":\"2011566\",\r\n\"lotno\":\"F47104\",\r\n\"lotmulti\":\"1\",\r\n\"amount\":\"200\",\r\n\"prizeend\":\"1\",\r\n\"subscribeInfo\":\"\",\r\n\"oneAmount\":\"200\"}";
	      } else if (command.value == "5") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"gift\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bet_code\":\"0001121315212233~12^\",\r\n\"batchcode\":\"2011566\",\r\n\"lotno\":\"F47104\",\r\n\"lotmulti\":\"1\",\r\n\"amount\":\"200\",\r\n\"to_mobile_code\":\"15210394691\",\r\n\"oneAmount\":\"200\",\r\n\"blessing\":\"你好\"}";
	      } else if (command.value == "6") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"startcase\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bet_code\":\"0001121315212233~12^\",\r\n\"batchcode\":\"2011743\",\r\n\"lotno\":\"F47104\",\r\n\"lotmulti\":\"1\",\r\n\"amount\":\"200\",\r\n\"oneAmount\":\"200\",\r\n\"safeAmt\":\"100\",\r\n\"buyAmt\":\"100\",\r\n\"commisionRation\":\"0\",\r\n\"visibility\":\"0\",\r\n\"minAmt\":\"100\",\r\n\"description\":\"你好\"}";
	      } else if (command.value == "7") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"betcase\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"caseid\":\"C00000000032347\",\r\n\"amount\":\"200\"}";
	      } else if (command.value == "8") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"bet\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "9") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"track\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "10") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"win\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "11") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"querycaselot\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"batchcode\":\"2011598\",\r\n\"orderBy\":\"totalAmt\",\r\n\"orderDir\":\"DESC\"}";
	      } else if (command.value == "12") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"caselot\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "13") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"querycaselotdetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"caseid\":\"C00000000026094\",\r\n\"userno\":\"\"}";
	      } else if (command.value == "14") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"gift\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "15") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"gifted\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "16") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"balance\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "17") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"accountdetail\",\r\n\"type\":\"new\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"transactiontype\":\"0\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "18") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"dna\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "19") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"01\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardno\":\"123456789\",\r\n\"cardtype\":\"0101\",\r\n\"name\":\"test\",\r\n\"certid\":\"111111111111111111\",\r\n\"bankaddress\":\"test\",\r\n\"addressname\":\"test\",\r\n\"iswhite\":\"false\"}";
	      } else if (command.value == "20") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"02\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"cardtype\":\"0203\",\r\n\"amount\":\"100\",\r\n\"cardno\":\"12343243434343\",\r\n\"cardpwd\":\"456456324543\"}";
	      } else if (command.value == "21") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"03\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardno\":\"123456789\",\r\n\"cardtype\":\"0102\"}";
	      } else if (command.value == "22") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"04\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardno\":\"123456789\",\r\n\"cardtype\":\"0201\",\r\n\"cardpwd\":\"123456\"}";
	      } else if (command.value == "23") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"05\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardtype\":\"0300\",\r\n\"bankAccount\":\"3\"}";
	      } else if (command.value == "24") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"06\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardtype\":\"0900\"}";
	      } else if (command.value == "25") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"07\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardtype\":\"0300\"}";
	      } else if (command.value == "26") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"08\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "27") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"getCash\",\r\n\"cashtype\":\"cash\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bankcardno\":\"11111111111\",\r\n\"name\":\"翼支付测试\",\r\n\"bankname\":\"招商银行\",\r\n\"amount\":\"200\",\r\n\"type\":\"1\"}";
	      } else if (command.value == "28") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"getCash\",\r\n\"cashtype\":\"cashRecord\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "29") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"getCash\",\r\n\"cashtype\":\"cancelCash\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"cashdetailid\":\"00000006\"}";
	      } else if (command.value == "30") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"cancelTrack\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"tsubscribeNo\":\"0000000000000081\"}";
	      } else if (command.value == "31") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"cancelCaselot\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"caseid\":\"C00000000026094\"}";
	      } else if (command.value == "32") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"cancelCaselotbuy\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"caseid\":\"C00000000026094\"}";
	      } else if (command.value == "33") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"userCenter\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "34") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updatePass\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"old_pass\":\"123456\",\r\n\"new_pass\":\"667600\"}";
	      } else if (command.value == "35") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"retrievePassword\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"bindPhoneNum\":\"13520582339\"}";
	      } else if (command.value == "36") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"updateNickName\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"nickName\":\"你好\"}";
	      } else if (command.value == "37") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"certid\":\"123456789012345\",\r\n\"name\":\"你好\"}";
	      } else if (command.value == "38") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"bindPhoneSecurityCode\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"bindPhoneNum\":\"13520582339\",\r\n\"userno\":\"00000001\",\r\n\"phonenum\":\"15335586558\"}";
	      } else if (command.value == "39") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"bindPhone\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bindPhoneNum\":\"13520582339\",\r\n\"securityCode\":\"123456\"}";
	      } else if (command.value == "40") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"removeBindPhone\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "41") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"cancelAutoLogin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "42") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"scoreDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "43") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"message\",\r\n\"type\":\"messageSetting\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"info\":\"\"}";
	      } else if (command.value == "44") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"message\",\r\n\"type\":\"queryMessageSetting\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "45") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"feedback\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"content\":\"如意彩客户端真的很好用\",\r\n\"userno\":\"00000001\",\r\n\"contactWay\":\"ruyicai@ruyicai.com\"}";
	      } else if (command.value == "46") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"feedback\",\r\n\"type\":\"feedBack\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "47") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"winInfo\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "48") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"lotteryinfomation\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "49") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"winInfoList\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "50") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"winInfoDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"batchcode\":\"2012023\"}";
	      } else if (command.value == "51") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"missValue\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"sellway\":\"F47104MV_X\"}";
	      } else if (command.value == "52") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"caseLotBuys\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"caseid\":\"C00000000026094\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "53") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"zcIssue\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"T01003\"}";
	      } else if (command.value == "54") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"receivePresentSecurityCode\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"presentId\":\"BJ000000000000030\"}";
	      } else if (command.value == "55") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"receivePresent\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"presentId\":\"BJ000000000000030\",\r\n\"securityCode\":\"123456\"}";
	      } else if (command.value == "56") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"transScore2Money\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"score\":\"500\"}";
	      } else if (command.value == "57") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"prizeRank\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "58") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"jcDuiZhen\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"jingcaiType\":\"1\",\r\n\"jingcaiValueType\":\"1\"}";
	      } else if (command.value == "59") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"jcDuiZhenLimit\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"jingcaiType\":\"1\",\r\n\"jingcaiValueType\":\"1\"}";
	      } else if (command.value == "60") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"afterIssue\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"batchnum\":\"10\"}";
	      } else if (command.value == "61") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"trackDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"tsubscribeNo\":\"0000000000262449\"}";
	      } else if (command.value == "62") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"topNews\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "63") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"title\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"1\",\r\n\"lotno\":\"\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "64") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"content\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"newsId\":\"18\"}";
	      } else if (command.value == "65") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"activityTitle\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "66") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"activityContent\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"activityId\":\"1\"}";
	      } else if (command.value == "67") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"helpCenterTitle\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"1\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "68") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"helpCenterContent\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"1\"}";
	      } else if (command.value == "69") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"expertCode\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"1\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "70") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"updateUserInfo\",\r\n\"type\":\"addScore\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"scoreType\":\"9\",\r\n\"description\":\"资讯分享\"}";
	      } else if (command.value == "71") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"versionIntroduce\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "72") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"playIntroduce\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\"}";
	      } else if (command.value == "73") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"feedback\",\r\n\"type\":\"exceptionSubmit\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"content\":\"NullPointerExcetion\"}";
	      } else if (command.value == "74") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"yield\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"bet_code\":\"103@*06070809^\",\r\n\"batchnum\":\"10\",\r\n\"batchcode\":\"2012060715\",\r\n\"lotno\":\"T01012\",\r\n\"lotmulti\":\"1\",\r\n\"betNum\":\"4\",\r\n\"wholeYield\":\"10\",\r\n\"beforeBatchNum\":\"\",\r\n\"beforeYield\":\"\",\r\n\"afterYield\":\"\"}";
	      } else if (command.value == "75") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"jcResult\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"jingcaiType\":\"1\",\r\n\"date\":\"20120418\"}";
	      } else if (command.value == "76") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"scoreRule\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "77") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"information\",\r\n\"newsType\":\"messageContent\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"keyStr\":\"dnaChargeDescription\"}";
	      } else if (command.value == "78") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"dataAnalysis\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"1_20120806_1_001\"}";
	      } else if (command.value == "79") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"immediateScore\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"0\",\r\n\"date\":\"\"}";
	      } else if (command.value == "80") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"immediateScoreDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"1_20120806_1_001\"}";
	      } else if (command.value == "81") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"QueryLot\",\r\n\"type\":\"highFrequency\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"T01007\"}";
	      } else if (command.value == "82") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"09\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"to_mobile_code\":\"15210394691\",\r\n\"amount\":\"100\",\r\n\"password\":\"123456\"}";
	      } else if (command.value == "83") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"AllQuery\",\r\n\"type\":\"buyCenter\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\"}";
	      } else if (command.value == "84") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"login\",\r\n\"requestType\":\"unionLogin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"source\":\"qq\",\r\n\"openId\":\"abcabc\",\r\n\"nickName\":\"祝福\"}";
	      } else if (command.value == "85") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"recharge\",\r\n\"rechargetype\":\"10\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"amount\":\"100\",\r\n\"cardtype\":\"0910\"}";
	      } else if (command.value == "86") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"giftmessage\",\r\n\"requestType\":\"new\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"pageindex\":\"1\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "87") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"score\",\r\n\"requestType\":\"transMoneyNeedScores\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "88") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"betLot\",\r\n\"bettype\":\"saveorder\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"phonenum\":\"15335586558\",\r\n\"userno\":\"00000001\",\r\n\"bet_code\":\"0001121315212233~12^\",\r\n\"batchcode\":\"2011566\",\r\n\"lotno\":\"F47104\",\r\n\"lotmulti\":\"1\",\r\n\"amount\":\"200\",\r\n\"oneAmount\":\"200\",\r\n\"rechargetype\":\"05\",\r\n\"cardtype\":\"0300\",\r\n\"bankAccount\":\"3\"}";
	      } else if (command.value == "89") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"autoJoin\",\r\n\"requestType\":\"createAutoJoin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"starterUserNo\":\"00000002\",\r\n\"lotno\":\"F47104\",\r\n\"times\":\"1\",\r\n\"joinAmt\":\"200\",\r\n\"percent\":\"10\",\r\n\"maxAmt\":\"500\",\r\n\"safeAmt\":\"200\",\r\n\"joinType\":\"0\",\r\n\"forceJoin\":\"1\"}";
	      } else if (command.value == "90") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"autoJoin\",\r\n\"requestType\":\"selectAutoJoin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "91") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"autoJoin\",\r\n\"requestType\":\"cancelAutoJoin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"87095\"}";
	      } else if (command.value == "92") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"autoJoin\",\r\n\"requestType\":\"selectCaseLotStarterInfo\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"starterUserNo\":\"00000001\",\r\n\"lotno\":\"F47104\"}";
	      } else if (command.value == "93") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"autoJoin\",\r\n\"requestType\":\"updateAutoJoin\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"87095\",\r\n\"joinAmt\":\"200\",\r\n\"percent\":\"10\",\r\n\"maxAmt\":\"500\",\r\n\"safeAmt\":\"200\",\r\n\"joinType\":\"0\",\r\n\"forceJoin\":\"1\"}";
	      } else if (command.value == "94") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"missValue\",\r\n\"requestType\":\"list\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"F47104\",\r\n\"sellway\":\"F47104MV_X\",\r\n\"batchnum\":\"10\"}";
	      } else if (command.value == "95") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"dataAnalysisJcl\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"0_20121210_1_306\"}";
	      } else if (command.value == "96") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"immediateScoreJcl\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"0\",\r\n\"date\":\"\"}";
	      } else if (command.value == "97") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"jingCai\",\r\n\"requestType\":\"immediateScoreDetailJcl\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"0_20121212_3_301\"}";
	      } else if (command.value == "98") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"letter\",\r\n\"requestType\":\"list\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "99") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"zuCai\",\r\n\"requestType\":\"duiZhen\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"lotno\":\"T01003\",\r\n\"batchcode\":\"2012112\"}";
	      } else if (command.value == "100") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"zuCai\",\r\n\"requestType\":\"dataAnalysis\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"T01003_2012178_1\"}";
	      } else if (command.value == "101") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"zuCai\",\r\n\"requestType\":\"immediateScore\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"type\":\"0\",\r\n\"lotno\":\"T01003\",\r\n\"batchcode\":\"2012178\"}";
	      } else if (command.value == "102") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"zuCai\",\r\n\"requestType\":\"immediateScoreDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"event\":\"T01003_2012178_1\"}";
	      } else if (command.value == "103") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"letter\",\r\n\"requestType\":\"updateReadState\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"abc123456789\"}";
	      } else if (command.value == "104") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"feedback\",\r\n\"type\":\"updateReadState\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"312\"}";
	      } else if (command.value == "105") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"letter\",\r\n\"requestType\":\"notReadCount\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\"}";
	      } else if (command.value == "106") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"select\",\r\n\"requestType\":\"betList\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "107") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"select\",\r\n\"requestType\":\"betCodeAnalysis\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"TE2013022500436006\"}";
	      } else if (command.value == "108") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"select\",\r\n\"requestType\":\"winList\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "109") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"select\",\r\n\"requestType\":\"caseLotBuyList\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"userno\":\"00000001\",\r\n\"pageindex\":\"0\",\r\n\"maxresult\":\"10\"}";
	      } else if (command.value == "110") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"select\",\r\n\"requestType\":\"caseLotDetail\",\r\n\"platform\":\"android\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"HTC Desire\",\r\n\"coopid\":\"001\",\r\n\"id\":\"C00000000026094\",\r\n\"userno\":\"\"}";
	      } else if (command.value == "111") {
	    	  document.getElementById("content").innerHTML = "{\"command\":\"message\",\r\n\"requestType\":\"saveToken\",\r\n\"platform\":\"iPhone\",\r\n\"imei\":\"3434343434534534\",\r\n\"imsi\":\"460023123123123\",\r\n\"softwareversion\":\"2.3\",\r\n\"phoneSIM\":\"13520586666\",\r\n\"machineid\":\"pad\",\r\n\"coopid\":\"001\",\r\n\"type\":\"91\",\r\n\"token\":\"abcdsefeefddfegkjjliij\"}";
	      }
	  }    
	  chengeCommand();   
  	  function gosubmit(operation){
		  if(document.getElementById("address").options[document.getElementById("address").selectedIndex].value=="null"){
		  		SetCookie("coustomURL",$("#urlAddress").val());
		  }
  	  	  document.getElementById("theform").action = '<%=request.getContextPath()%>/'+operation;
  	  	  document.getElementById("theform").submit();
  	  	  
  	  }
  	  
  	  function init(){
  	  	  ajaxExecute(  '<%=request.getContextPath()%>/getData?command=busi','document.getElementById("busiid").parentNode.innerHTML = msg;refreshVersion();');
  	  	  var imsi = getCookie("IMSI");
  	  	  var imei = getCookie("IMEI");
  	  	  if(imsi!=null && imsi!="null" && imsi!=""){
  	  		document.getElementById("imsi").value = imsi;
  	  	  }
  	  	  if(imei!=null && imei!="null" && imsi!=""){
  	  		document.getElementById("imei").value = imei;
  	  	  }
  	  		SetCookie("IMSI",document.getElementById("imsi").value);
  	  		SetCookie("IMEI",document.getElementById("imei").value);
  	  		document.getElementById("urlAddress").value = getCookie("urlAddress");
		  }
		  
  	  function ajaxExecute(_url,successfun){
		$.ajax({
		  type: "POST",
		  url: _url,
		  data: "",
		  success: function(msg){
			 eval(successfun);
		  }
		});
	}
	function ajaxRequestToAddCommand(){
		var version = document.getElementById("version").options[document.getElementById("version").selectedIndex].value;
		var commandName = document.getElementById("commandName").value;
		var commandContent = document.getElementById("myCommandContent").value;
		$.post("<%=request.getContextPath()%>/getData",
		{command:"addCommand",versionID:version,commandName:commandName,content:commandContent},
		   function(msg){
				if(msg.indexOf("成功")!=-1){
					refreshAddandCommand();
					alert(msg);
					 $("#addCommandDialog").dialog("close");
				 }
				 else{
				 	alert(msg);
				 }
		  }
		);
	}
	function refreshVersion(){
		ajaxExecute('<%=request.getContextPath()%>/getData?command=version&busiid='+document.getElementById("busiid").value,'document.getElementById("version").parentNode.innerHTML = msg');
	}
	
	function refreshAddandCommand(){
		ajaxExecute('<%=request.getContextPath()%>/getData?command=address&version='+document.getElementById("version").value,'document.getElementById("address").parentNode.innerHTML = msg;setUrlAddress();');
		ajaxExecute('<%=request.getContextPath()%>/getData?command=command&version='+document.getElementById("version").value,'document.getElementById("command").parentNode.innerHTML = msg');
	}
	
	function setcommand(){
		ajaxExecute('<%=request.getContextPath()%>/getData?command=commandDetail&commandid='+document.getElementById("command").value,'document.getElementById("content").value = msg');
		<%--ajaxExecute('<%=request.getContextPath()%>/getData?command=commandnotes&commandid='+document.getElementById("command").value,'document.getElementById("commandnotes").innerHTML = msg');--%>
	}
	$(document).ready(function(){
		 //init();
	});
	
	function setUrlAddress(){
		if(document.getElementById("address").options[document.getElementById("address").selectedIndex].value!="null"){			
			document.getElementById("urlAddress").value = document.getElementById("address").options[document.getElementById("address").selectedIndex].value;			
		} else{
			if(getCookie("coustomURL")!=null && getCookie("coustomURL")!="")
			{
				document.getElementById("urlAddress").value = getCookie("coustomURL");
			}
		}
	}
	
	function sendRequest(){
		var content = document.getElementById("content").value;
		var urlAddress =document.getElementById("urlAddress").value;
		
		$.post("<%=request.getContextPath()%>/test/sendRequest",
			{content:content,urlAddress:urlAddress},
			function(msg) {
				document.getElementById("recieved").value = msg;
			}
		);
	}
	
	function SetCookie(name,value)//两个参数，一个是cookie的名子，一个是值
	{
	    var Days = 30; //此 cookie 将被保存 30 天
	    var exp  = new Date();    //new Date("December 31, 9998");
	    exp.setTime(exp.getTime() + Days*24*60*60*1000);
	    document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
	}
	function getCookie(name)//取cookies函数        
	{
	    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
	     if(arr != null) return unescape(arr[2]); return null;
		
	}
	function onClickAddCommand(){
		 $("#addCommandDialog").dialog({modal:true,width:600,height:512});
		 $("#addCommandDialog").dialog("open");
	}
	
	function onClickManageCommand(){
		var version = document.getElementById("version").options[document.getElementById("version").selectedIndex].value;
		var tableContent;
		$.post("<%=request.getContextPath()%>/getData",
		{command:"getAllCommands",version:version},
		   function(msg,textStatus){
		   if(textStatus=="success"){
		   		var length = 0;
				for( var b in document.getElementById("manageTable").rows){
					if(b>=0){
							length++;
					}
				}
				for(var i=0;i<length-1;i++){
					document.getElementById("manageTable").deleteRow(1);
				}
				
				var jsonObject = eval(msg);
				for(var a in jsonObject.msg){
					var x=document.getElementById("manageTable").insertRow(1);
					 var y=x.insertCell(0);
  					 var z=x.insertCell(1);
					  y.innerHTML=jsonObject.msg[a].result;
  					  z.innerHTML="<a href='javascript:return null' onclick='deleteCommand("+jsonObject.msg[a].id+")'>删除</a>";
				}
				 $("#manage").dialog({modal:true,width:400,height:512});
				 $("#manage").dialog("open");
		  }
		  else{
		  		alert("无法获取相关信息，请稍候再试！");
		  }
		  }
		  
		);
	}
	
	function deleteCommand(id){
		$.post("<%=request.getContextPath()%>/getData",
		{command:"deleteCommand",commandID:id},
		   function(msg,textStatus){
		   if(textStatus=="success"){
				alert(msg);
				refreshAddandCommand();
				$("#manage").dialog("close");
			}
			else
			{
				alert("无法获取相关信息，请稍候再试！");	
			}
				
		  }
		);
	}
</script>
</html>
