package com.ruyicai.lotserver.consts;

/**
 * 常量类
 * @author Administrator
 *
 */
public class Constants {
	
	//公共请求信息
	public final static String command = "command"; //请求命令
	public final static String imei = "imei"; //手机标识
	public final static String imsi = "imsi"; //手机标识
	public final static String softwareVersion = "softwareversion"; //版本号
	public final static String coopId = "coopid"; //渠道号
	public final static String machineId = "machineid"; //机型
	public final static String platform = "platform"; //平台(android|iPhone|塞班)
	public final static String isemulator =  "isemulator"; //是否是模拟器
	public final static String isCompress = "isCompress"; //是否压缩
	public final static String phoneSIM = "phoneSIM"; //SIM卡手机号
	public final static String mac = "mac"; //网卡地址
	
	//常量
	public final static String accessType = "C"; //接入方式
	public final static String subChannel = "00092493"; //用户系统
	
	//类型
	public final static String requestType = "requestType"; //请求类型
	public final static String cashType = "cashtype"; //提现相关的类型
	public final static String type = "type"; //查询类型
	public final static String transactionType = "transactiontype"; //交易类型
	
	//投注、追号、赠送、合买公共参数
	public final static String betType = "bettype"; //投注类型
	public final static String batchCode = "batchcode"; //期号
	public final static String batchNum = "batchnum"; //追号期数
	public final static String betCode = "bet_code"; //注码
	public final static String lotNo = "lotno"; //彩种编号
	public final static String lotMulti = "lotmulti"; //倍数
	public final static String sellway = "sellway"; //玩法
	public final static String amount = "amount"; //金额
	public final static String isSuperaddition = "issuperaddition";//是否追加
	public final static String isSellWays = "isSellWays"; //投注时是否多玩法
	public final static String isBetAfterIssue = "isBetAfterIssue"; //是否可以投当前期以后的期(追号可以不从当前期追,1:是)
	
	//赠送
	public final static String to_mobile_code = "to_mobile_code"; //被赠送手机号码
	public final static String gift_result = "gift_result"; //赠送结果
	public final static String blessing = "blessing";//赠送寄语
	
	//追号
	public final static String prizeEnd = "prizeend"; //中奖后停止追号
	public final static String betNum = "betNum"; // 注数
	public final static String wholeYield = "wholeYield"; // 全程收益率
	public final static String beforeBatchNum = "beforeBatchNum"; // 前几期
	public final static String beforeYield = "beforeYield"; // 前程收益率
	public final static String afterYield = "afterYield"; // 后程收益率
	public final static String subscribeInfo = "subscribeInfo"; //追号信息(用于每期追号的倍数不同)格式:期号,金额,倍数,收益率!期号,金额,倍数,收益率
	
	//合买
	public final static String caseId = "caseid"; //方案编号
	public final static String orderBy = "orderBy"; //按什么排序
	public final static String orderDir = "orderDir"; //排序方式
	public final static String safeAmt = "safeAmt"; //保底金额
	public final static String oneAmount = "oneAmount"; //单注金额
	public final static String buyAmt = "buyAmt"; //购买金额
	public final static String commisionRation = "commisionRation"; //提成比例
	public final static String visibility = "visibility"; //是否可见
	public final static String minAmt = "minAmt"; //最低认购金额
	public final static String description = "description"; //描述
	
	//自动跟单
	public final static String starterUserNo = "starterUserNo"; //发起人用户编号
	public final static String times = "times"; //跟单次数
	public final static String joinAmt = "joinAmt"; //跟单金额
	public final static String percent = "percent"; //跟单百分比
	public final static String maxAmt = "maxAmt"; //百分比跟单最大金额
	public final static String joinType = "joinType"; //跟单类型(0:金额跟单,1:百分比跟单)
	public final static String forceJoin = "forceJoin"; //是否强制跟单(1:强制跟单,0:不强制跟单)
	
	//用户信息
	public final static String phonenum = "phonenum"; //用户名
	public final static String password = "password"; //密码
	public final static String name = "name"; //真实姓名
	public final static String nickName = "nickName"; //昵称
	public final static String userName = "userName"; //用户姓名
	public final static String certId = "certid"; //身份证号
	public final static String mobileId = "mobileid"; //手机号码
	public final static String userNo = "userno"; //用户编号
	
	//修改密码
	public final static String old_pass = "old_pass"; //原密码
	public final static String new_pass = "new_pass"; //新密码
	
	//登录
	public final static String isAutoLogin = "isAutoLogin"; //是否自动登录
	public final static String randomNumber = "randomNumber"; //自动登录随机数
	public final static String source = "source"; //来源(联合登录)
	public final static String openId = "openId"; //第三方用户标识(联合登录)
	
	//充值
	public final static String rechargeType = "rechargetype"; //充值类型
	public final static String cardNo = "cardno"; //卡号
	public final static String cardPwd = "cardpwd"; //密码
	public final static String cardType = "cardtype"; //卡类型
	public final static String iswhite = "iswhite"; //是否是白名单
	public final static String bankAccount = "bankAccount"; //支付宝类型(1：支付宝web支付2：支付宝语音支付3、支付宝wap浏览器支付4、支付宝wap非浏览器支付5、支付宝手机安全支付)
	
	//提现
	public final static String cashDetailId = "cashdetailid"; //提现记录id
	public final static String stat = "stat"; //提现记录状态(旧)
	public final static String state = "state"; //提现记录状态(新)
	public final static String drawType = "5"; //提现交易类型
	public final static String drawId = "FP0001"; //费率方案标示
	public final static String drawName = "draw1"; //提现费率名称
	public final static String bankName = "bankname"; //开户行
	public final static String clientBankId = "CLIENT001"; //客户端提现bankid标识
	public final static String allBankName = "allbankname"; //所有银行名称
	public final static String cashTime = "cashTime"; //提现时间
	public final static String rejectReason = "rejectReason"; //提现失败原因
	public final static String allBankName_content = "中国工商银行,中国农业银行,中国建设银行,中国民生银行,招商银行,中国邮政储蓄银行,交通银行,兴业银行,中信银行,中国光大银行,广东发展银行,上海浦东发展银行,深圳发展银行,杭州银行"; //所有银行名称
	
	//DNA绑定信息
	public final static String bindState = "bindstate";//绑定状态
	public final static String bankCardNo = "bankcardno";//银行卡号
	public final static String addressName = "addressname";//身份证户口所在地
	public final static String bindDate = "binddate";//绑定时间
	public final static String areaName = "areaname";//身份证所在地
	public final static String bankAddress = "bankaddress";//开户行所在地
	
	//余额
	public final static String balance = "balance"; //余额
	public final static String drawBalance = "drawbalance"; //可提现余额
	public final static String freezeBalance = "freezebalance"; //冻结金额
	public final static String betBalance = "bet_balance"; //可投注余额
	
	//查询
	public final static String date = "date"; //日期
	public final static String pageIndex = "pageindex"; //第几页
	public final static String maxResult = "maxresult"; //每页显示多少条
	public final static String tsubscribeNo = "tsubscribeNo"; //追号订单
	public final static String startTime = "starttime"; //开始时间
	public final static String endTime = "endtime"; //结束时间
	public final static String sysCurrentTime = "syscurrenttime"; //系统当前时间
	public final static String timeRemaining = "time_remaining"; //当前期剩余时间
	public final static String startDate = "startdate"; //开始日期
	public final static String endDate = "enddate"; //结束日期
	public final static String startLine = "startLine"; //起始行
	public final static String stopLine = "stopLine"; //结束行
	public final static String sessionId = "sessionid"; //SessionId
	public final static String transation_id = "transation_id"; //交易ID
	public final static String stateMemo = "stateMemo"; //状态描述
	
	//注册
	public final static String recommender = "recommender"; //推荐人的用户名
	public final static String agencyNo = "agencyNo"; //默认的代理编号
	
	//用户中心
	public final static String score = "score"; //积分
	public final static String scoreType = "scoreType"; //积分类型
	public final static String bindPhoneNum = "bindPhoneNum"; //绑定的手机号
	public final static String securityCode = "securityCode"; //验证码
	public final static String content = "content"; //反馈内容
	public final static String contactWay = "contactWay"; //联系方式
	
	//彩票资讯
	public final static String newsType = "newsType"; //资讯类型
	public final static String newsId = "newsId"; //资讯编号
	public final static String activityId = "activityId"; //活动编号
	public final static String id = "id"; //编号
	public final static String keyStr = "keyStr"; //键
	
	//消息设置
	public final static String token = "token"; //iPhone手机标识
	public final static String info = "info"; //信息
	
	//竞彩
	public final static String jingcaiType = "jingcaiType"; //竞彩类型
	public final static String jingcaiValueType = "jingcaiValueType"; //竞彩( 0 单关 1 多关 )
	public final static String event = "event"; //赛事信息
	
	//返回
	public final static String error_code = "error_code"; //错误码
	public final static String message = "message"; //错误信息
	
	//其他
	public final static String key = "<>hj12@#$$%^~~ff"; //加密串
	public final static String mobile_pattern = "(13[0-9]|15[0-9]|18[0-9]|147)\\d{8}"; //手机号码的正则
	
}
