package com.ruyicai.lotserver.protocol;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;

@RooJson
@RooJavaBean
public class ClientInfo {

	// 基本字段
	private String command = ""; // 请求命令
	private String imei = ""; // 手机标识
	private String imsi = ""; // 手机标识
	private String softwareVersion = ""; // 版本号
	private String coopId = ""; // 渠道号
	private String machineId = ""; // 机型
	private String phonenum = ""; // 用户名
	private String platform = ""; // 平台(android|iPhone|塞班)
	private String isCompress = ""; // 是否压缩,1为压缩
	private String isemulator = ""; // 是否是模拟器
	private String phoneSIM = ""; // SIM卡手机号
	private String mac = ""; // 网卡地址
	
	//类型
	private String requestType = ""; // 请求类型
	private String transactionType = ""; // 交易类型
	private String type = "";// 查询类型

	//投注、追号、赠送、合买公共参数
	private String lotNo = ""; // 彩种编号
	private String batchCode = ""; // 期号
	private String batchnum = ""; // 追号期数
	private String betCode = ""; // 注码
	private String amount = ""; // 投注金额
	private String betType = "";// 投注类型,投注或者赠彩
	private String lotmulti = "";// 倍数
	private String sellway = "";// 购彩方式
	private String isSellWays = ""; // 投注时是否多玩法
	private String isBetAfterIssue = ""; // 是否可以投当前期以后的期(追号可以不从当前期追,1:是)
	private String isSuperaddition = "";// 默认为不追加,只有大乐透追加.如果追加默认为0
	
	//赠送
	private String to_mobile_code = "";// 被赠送人手机号
	private String blessing = ""; //赠送寄语
	
	//追号
	private String prizeend = ""; // 中奖后停止追号
	private String betNum = ""; // 注数
	private String wholeYield = ""; // 全程收益率
	private String beforeBatchNum = ""; // 前几期
	private String beforeYield = ""; // 前程收益率
	private String afterYield = ""; // 后程收益率
	private String subscribeInfo = ""; // 追号信息(用于每期追号的倍数不同)格式:期号,金额,倍数,收益率!期号,金额,倍数,收益率

	// 合买
	private String caseid = "";// 合买的订单id
	private String orderBy = "";// 按什么排序
	private String orderDir = "";// 排序方式
	private String safeAmt = "";// 保底金额
	private String oneAmount = "";// 单注金额
	private String buyAmt = "";// 购买金额
	private String commisionRatio = "";// 提成比例
	private String visibility = "";// 是否可见
	private String minAmt = "";// 最低认购金额
	private String description = "";// 描述
	
	//自动跟单
	private String starterUserNo = ""; //发起人用户编号
	private String times = ""; //跟单次数
	private String joinAmt = ""; //跟单金额
	private String percent = ""; //跟单百分比
	private String maxAmt = ""; //百分比跟单最大金额
	private String joinType = ""; //跟单类型(0:金额跟单,1:百分比跟单)
	private String forceJoin = ""; //是否强制跟单(1:强制跟单,0:不强制跟单)

	// 查询
	private String startDate = ""; // 账户信息查询开始时间
	private String endDate = ""; // 账户信息查询结束时间
	private String startLine = "";// 开始条数
	private String stopLine = "";// 结束条数
	private String maxresult = "";// 每页显示条数
	private String pageindex = "";// 页数，第几页
	private String tsubscribeNo = ""; // 追号查询时的追号订单
	private String sessionid = ""; // sessionid
	private String sysSessionid = ""; // sysSessionid

	// 充值、提现
	private String cashtype = "";// 提现的交易类型
	private String cashdetailid = "";// 提现记录id
	private String bankcardno = "";// 银行卡号
	private String bankname = "";// 银行名称
	private String araeaname = "";// 地址
	private String cardno = "";// 卡号
	private String cardtype = "";// 卡类型
	private String rechargetype = "";// 充值类型
	private String cardpwd = "";// 卡密码
	private String bankaddress = "";// 开卡地址
	private String addressname = "";// 户口所在地
	private String iswhite = "";// 是否为白名单用户
	private String bankAccount = ""; // 支付宝类型(1：支付宝web支付2：支付宝语音支付3、支付宝wap浏览器支付4、支付宝wap非浏览器支付5、支付宝手机安全支付)

	//用户信息
	private String userno = ""; // 用户编号
	private String password = ""; // 密码
	private String certid = "";// 身份证号
	private String name = "";// 真实姓名
	private String nickName = ""; // 昵称
	
	// 用户中心
	private String score = ""; // 积分
	private String scoreType = ""; // 积分类型
	private String content = ""; // 用户反馈内容
	private String contactWay = ""; // 联系方式
	private String bindPhoneNum = ""; // 绑定的手机号码
	private String securityCode = ""; // 验证码
	
	//修改密码
	private String oldPass = ""; // 旧密码
	private String newPass = ""; // 新密码
	
	//登录
	private String isAutoLogin = ""; // 是否自动登录
	private String randomNumber = ""; // 自动登录随机数
	private String source = ""; //来源(联合登录)
	private String openId = ""; //第三方用户标识(联合登录)
	
	//注册
	private String recommender = ""; //推荐人的用户名
	private String agencyNo = ""; //默认的代理编号

	// 新闻资讯
	private String newsType = ""; // 资讯查询的类型
	private String newsId = ""; // 资讯编号
	private String activityId = ""; // 活动编号
	private String id = ""; //编号
	private String keyStr = ""; //编号
	
	//消息设置
	public String token = ""; //iPhone手机标识
	public String info = ""; //信息

	//竞彩
	private String jingcaiType = ""; // 竞彩类型 (0 篮彩 1足彩)
	private String jingcaiValueType = ""; // 竞彩 ( 0 单关 1 多关 )
	private String event = ""; //赛事信息
	private String date = ""; // 日期(查询竞彩赛果用)
	
}
