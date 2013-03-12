package com.ruyicai.lotserver.consts;

public enum CoopId {

	appStore_old("838", "appStore旧"),
	appStore_new("887", "appStore新"),
	appStore_guanFang("889", "appStore官方版"),
	limei("886", "力美"),
	limei_zhuCe("936", "力美注册"),
	dianRu_zhuCe("937", "点入注册"),
	duoMeng_zhuCe("938", "多盟注册"),
	dianjoy("888", "点乐"),
	androidMarket("151", "android market"),
	lePhone("100", "乐phone"),
	leShiTV("751", "乐视TV"),
	eRenEBen("656", "e人e本"),
	youYang("784", "优扬");
	
	
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private CoopId(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}
	
}
