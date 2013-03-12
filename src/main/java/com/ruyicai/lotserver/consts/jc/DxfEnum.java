package com.ruyicai.lotserver.consts.jc;

public enum DxfEnum {

	big("1", "大"),
	small("2", "小");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private DxfEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
