package com.ruyicai.lotserver.consts;

public enum ScoreType {

	transScore("-1", "兑换积分"),
	perfectUserInfo("1", "注册并完善信息"),
	bet("2", "普通投注"),
	track("3", "追号"),
	startCase("4", "发起合买"),
	betCase("5", "参与合买"),
	charge("6", "充值"),
	feedBack("7", "留言建议"),
	login("8", "用户登录"),
	weiboShare("9", "微博分享"),
	presentScore("99", "赠送积分");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private ScoreType(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
