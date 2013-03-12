package com.ruyicai.lotserver.consts.jc;

/**
 * 竞彩篮球胜分差常量类
 * @author Administrator
 *
 */
public enum SfcEnum {

	//主胜
	homeWin1_5("01", "主胜1-5分"),
	homeWin6_10("02", "主胜6-10分"),
	homeWin11_15("03", "主胜11-15分"),
	homeWin16_20("04", "主胜16-20分"),
	homeWin21_25("05", "主胜21-25分"),
	homeWin26("06", "主胜26+分"),
	
	//客胜
	guestWin1_5("11", "客胜1-5分"),
	guestWin6_10("12", "客胜6-10分"),
	guestWin11_15("13", "客胜11-15分"),
	guestWin16_20("14", "客胜16-20分"),
	guestWin21_25("15", "客胜21-25分"),
	guestWin26("16", "客胜26+分");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private SfcEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}	
	
}
