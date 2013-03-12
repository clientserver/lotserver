package com.ruyicai.lotserver.consts;

public enum LotType {

	//福彩
	QLC("F47102", "七乐彩"),	//七乐彩
	DDD("F47103", "福彩3D"),	//福彩3D
	SSQ("F47104", "双色球"),	//双色球
	//体彩
	DLT("T01001", "大乐透"),	//大乐透
	PLS("T01002", "排列三"),	//排列三
	PLW("T01011", "排列五"),	//排列五
	QXC("T01009", "七星彩"),	//七星彩
	TT_F("T01013", "22选5"),	//22选5
	//高频彩
	SSC("T01007", "时时彩"),	//时时彩
	OO_F("T01010", "江西11选5"),	//江西11选5
	OO_YDJ("T01012", "11运夺金"),	//11运夺金
	GDOO_F("T01014", "广东11选5"),	//广东11选5
	GDH_T("T01015", "广东快乐十分"),	//广东快乐十分
	//足彩
	ZC_SFC("T01003", "胜负彩"),	//足彩胜负彩
	ZC_RX9("T01004", "任选9"),	//足彩任选9
	ZC_JQC("T01005", "进球彩"),	//足彩进球彩
	ZC_BQC("T01006", "6场半"),	//足彩半全场
	//竞彩足球
	JCZ_SPF("J00001", "竞彩足球胜平负"),	//竞彩足球胜平负
	JCZ_BF("J00002", "竞彩足球比分"),	//竞彩足球比分
	JCZ_JQS("J00003", "竞彩足球总进球数"),	//竞彩足球总进球数
	JCZ_BQC("J00004", "竞彩足球半全场"),	//竞彩足球半全场
	//竞彩篮球
	JCL_SF("J00005", "竞彩篮球胜负"),	//竞彩篮球胜负
	JCL_RFSF("J00006", "竞彩篮球让分胜负"),	//竞彩篮球让分胜负
	JCL_SFC("J00007", "竞彩篮球胜分差"),	//竞彩篮球胜分差
	JCL_DXF("J00008", "竞彩篮球大小分"),	//竞彩篮球大小分
	//竞彩混合过关
	JCHH_ZQ("J00011", "竞彩混合足球"), //竞彩混合足球
	JCHH_LQ("J00012", "竞彩混合篮球"); //竞彩混合篮球
	
	private String lotNo;
	
	private String lotName;

	public String lotNo() {
		return lotNo;
	}
	
	public String lotName() {
		return lotName;
	}

	private LotType(String lotNo, String lotName) {
		this.lotNo = lotNo;
		this.lotName = lotName;
	}
	
}
