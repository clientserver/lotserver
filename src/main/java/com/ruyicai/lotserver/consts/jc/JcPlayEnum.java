package com.ruyicai.lotserver.consts.jc;

public enum JcPlayEnum {

	danGuan("500", "单关"),
	guoGuan502("502", "过关2串1"),
	guoGuan503("503", "过关3串1"),
	guoGuan504("504", "过关4串1"),
	guoGuan505("505", "过关5串1"),
	guoGuan506("506", "过关6串1"),
	guoGuan507("507", "过关7串1"),
	guoGuan508("508", "过关8串1"),
	guoGuan509("509", "过关2串3"),
	guoGuan510("510", "过关3串6"),
	guoGuan511("511", "过关3串7"),
	guoGuan512("512", "过关4串10"),
	guoGuan513("513", "过关4串14"),
	guoGuan514("514", "过关4串15"),
	guoGuan515("515", "过关5串15"),
	guoGuan516("516", "过关5串25"),
	guoGuan517("517", "过关5串30"),
	guoGuan518("518", "过关5串31"),
	guoGuan519("519", "过关6串21"),
	guoGuan520("520", "过关6串41"),
	guoGuan521("521", "过关6串56"),
	guoGuan522("522", "过关6串62"),
	guoGuan523("523", "过关6串63"),
	guoGuan524("524", "过关7串127"),
	guoGuan525("525", "过关8串255"),
	guoGuan526("526", "过关3串3"),
	guoGuan527("527", "过关3串4"),
	guoGuan528("528", "过关4串6"),
	guoGuan529("529", "过关4串11"),
	guoGuan530("530", "过关5串10"),
	guoGuan531("531", "过关5串20"),
	guoGuan532("532", "过关5串26"),
	guoGuan533("533", "过关6串15"),
	guoGuan534("534", "过关6串35"),
	guoGuan535("535", "过关6串50"),
	guoGuan536("536", "过关6串57"),
	guoGuan537("537", "过关7串120"),
	guoGuan538("538", "过关8串247"),
	guoGuan539("539", "过关4串4"),
	guoGuan540("540", "过关4串5"),
	guoGuan541("541", "过关5串16"),
	guoGuan542("542", "过关6串20"),
	guoGuan543("543", "过关6串42"),
	guoGuan544("544", "过关5串5"),
	guoGuan545("545", "过关5串6"),
	guoGuan546("546", "过关6串22"),
	guoGuan547("547", "过关7串35"),
	guoGuan548("548", "过关8串70"),
	guoGuan549("549", "过关6串6"),
	guoGuan550("550", "过关6串7"),
	guoGuan551("551", "过关7串21"),
	guoGuan552("552", "过关8串56"),
	guoGuan553("553", "过关7串7"),
	guoGuan554("554", "过关7串8"),
	guoGuan555("555", "过关8串28"),
	guoGuan556("556", "过关8串8"),
	guoGuan557("557", "过关8串9");
	
	private String value;
	
	private String memo;
	
	public String value() {
		return value;
	}
	
	public String memo() {
		return memo;
	}
	
	private JcPlayEnum(String value, String memo) {
		this.value = value;
		this.memo = memo;
	}	
	
}
