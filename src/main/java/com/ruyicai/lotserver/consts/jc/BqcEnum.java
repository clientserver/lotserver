package com.ruyicai.lotserver.consts.jc;

public enum BqcEnum {

	lose_lose("00", "负负"),
	lose_standoff("01", "负平"),
	lose_win("03", "负胜"),
	standoff_standoff("11", "平平"),
	standoff_lose("10", "平负"),
	standoff_win("13", "平胜"),
	win_win("33", "胜胜"),
	win_lose("30", "胜负"),
	win_standoff("31", "胜平");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private BqcEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
