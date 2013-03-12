package com.ruyicai.lotserver.consts.jc;

public enum SpfEnum {

	win("3", "胜"),
	standoff("1", "平"),
	loss("0", "负");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private SpfEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}	
	
}
