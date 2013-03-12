package com.ruyicai.lotserver.consts.jc;

public enum JqsEnum {

	zero("0", "0个"),
	one("1", "1个"),
	two("2", "2个"),
	three("3", "3个"),
	four("4", "4个"),
	five("5", "5个"),
	six("6", "6个"),
	seven("7", "7个");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private JqsEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}	
	
}
