package com.ruyicai.lotserver.consts;

public enum Platform {

	android("android", "安卓"),
	iPhone("iPhone", "苹果"),
	S60_V5("S60_V5", "塞班");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private Platform(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
