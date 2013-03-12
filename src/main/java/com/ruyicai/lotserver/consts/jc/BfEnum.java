package com.ruyicai.lotserver.consts.jc;

public enum BfEnum {

	//主胜
	one_zero("10", "1:0"),
	two_zero("20", "2:0"),
	two_one("21", "2:1"),
	three_zero("30", "3:0"),
	three_one("31", "3:1"),
	three_two("32", "3:2"),
	four_zero("40", "4:0"),
	four_one("41", "4:1"),
	four_two("42", "4:2"),
	five_zero("50", "5:0"),
	five_one("51", "5:1"),
	five_two("52", "5:2"),
	win_others("90", "胜其他"),
	
	//主负
	zero_one("01", "0:1"),
	zero_two("02", "0:2"),
	one_two("12", "1:2"),
	zero_three("03", "0:3"),
	one_three("13", "1:3"),
	two_three("23", "2:3"),
	zero_four("04", "0:4"),
	one_four("14", "1:4"),
	two_four("24", "2:4"),
	zero_five("05", "0:5"),
	one_five("15", "1:5"),
	two_five("25", "2:5"),
	loss_others("09", "负其他"),
	
	//平
	zero_zero("00", "0:0"),
	one_one("11", "1:1"),
	two_two("22", "2:2"),
	three_three("33", "3:3"),
	standoff_others("99", "平其他");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private BfEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
