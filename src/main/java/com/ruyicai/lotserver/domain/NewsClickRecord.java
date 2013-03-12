package com.ruyicai.lotserver.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.roo.addon.entity.RooEntity;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJson
@RooEntity(versionField="", table="newsclickrecord", persistenceUnit="persistenceUnit", transactionManager="transactionManager")
public class NewsClickRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	private Integer id;
	
	@Column(name = "imei")
	private String imei;
	
	@Column(name = "newsId")
	private Integer newsId;
	
	@Column(name = "productno")
	private String productno;
	
	@Column(name = "newsType")
	private String newsType;
	
	@Column(name = "createTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
}
