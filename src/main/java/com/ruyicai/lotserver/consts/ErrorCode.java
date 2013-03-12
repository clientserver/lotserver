package com.ruyicai.lotserver.consts;

public enum ErrorCode {
	
	success("0000", "成功"),
	fail("9999", "失败"),
	notHaveRecord("0047", "无记录"),
	balanceNotEnough("0406", "余额不足"),
	batchCodeExpired("1001", "期号已过期");

	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private ErrorCode(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
