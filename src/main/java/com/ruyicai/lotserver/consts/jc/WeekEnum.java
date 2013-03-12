package com.ruyicai.lotserver.consts.jc;

public enum WeekEnum {

	monday("1", "星期一"),
	tuesday("2", "星期二"),
	wednesday("3", "星期三"),
	thursday("4", "星期四"),
	friday("5", "星期五"),
	saturday("6", "星期六"),
	sunday("7", "星期日");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private WeekEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}	
	
}
